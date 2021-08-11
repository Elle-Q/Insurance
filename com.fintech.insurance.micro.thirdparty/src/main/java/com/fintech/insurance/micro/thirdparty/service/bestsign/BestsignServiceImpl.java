package com.fintech.insurance.micro.thirdparty.service.bestsign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fintech.insurance.micro.dto.thirdparty.SignLocationVO;
import com.fintech.insurance.micro.thirdparty.service.BestsignService;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.FileType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.IdentityType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.UserType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignBizException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignCertExistingException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignDataException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignUserExistException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.model.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/20 21:02
 */
@Service
public class BestsignServiceImpl implements BestsignService {

    private static final Logger LOG = LoggerFactory.getLogger(BestsignServiceImpl.class);

    /**
     * http客户端对象
     */
    private CloseableHttpClient httpClient = null;

    @Autowired
    private RequestUrlBuilder requestUrlBuilder;

    public BestsignServiceImpl() {
        PoolingHttpClientConnectionManager connManager = null;
        boolean enableSSL = false;
        if (enableSSL) {
            ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
            LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
            connManager = new PoolingHttpClientConnectionManager(registry);
        } else {
            connManager = new PoolingHttpClientConnectionManager();
        }

        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(10);
        //重试规则
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        this.httpClient = HttpClients.custom().setConnectionManager(connManager).setRetryHandler(httpRequestRetryHandler).build();
    }

    @Override
    public void registerUserAccount(UserType userType, String userAccountId, String userName, String mail, String mobile) throws BestsignBizException, BestsignUserExistException {
        if (StringUtils.isBlank(userAccountId) || StringUtils.isBlank(userName) || null == userType || StringUtils.isBlank(userName)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        UserRegisterVO request = new UserRegisterVO();
        request.setAccount(userAccountId);
        request.setName(userName);
        request.setUserType(userType);
        request.setMobile(mobile);
        request.setMail(mail);

        BestsignResponse<String> response = postRequest(request, BestsignUtil.BESTSIGN_API_USER_REGISTER, null);
        if (null != response && !response.isOK()) {
            if (BestsignUtil.EXCEPTION_CODE_USER_EXISTS.equals(response.getCode())) {
                throw new BestsignUserExistException("user " + userAccountId + " exists.");
            }
            throw new BestsignBizException("Failed register user: " + response.getMessage());
        }
    }

    @Override
    public void setPersonalCredential(String userAccountId, IdentityType identityType, String identityNum,
                                      String userName, String contactMail, String contractMobile,
                                      String province, String city, String address) throws BestsignBizException, BestsignCertExistingException {
        if (StringUtils.isBlank(userAccountId) || StringUtils.isBlank(identityNum) || StringUtils.isBlank(userName)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        PersonalCredentialVO request = new PersonalCredentialVO();
        request.setAccount(userAccountId);
        request.setIdentityType(identityType);
        request.setIdentity(identityNum);
        request.setName(userName);
        request.setContactMail(contactMail);
        request.setContactMobile(contractMobile);
        request.setProvince(province);
        request.setCity(city);
        request.setAddress(address);

        BestsignResponse<JSONObject> response = postRequest(request, BestsignUtil.BESTSIGN_API_PERSONAL_CREDENTIAL, null);
        if (null != response && !response.isOK()) {
            if (BestsignUtil.EXCEPTION_CODE_CERT_EXISTS.equals(response.getCode())) {
                throw new BestsignCertExistingException("cert for user:" + userAccountId + " existing.");
            }
            LOG.error("Bestsign Response Error: {}, response data: {}", response.getMessage(),
                    response.getData() == null ? null : response.getData().toJSONString());
            throw new BestsignBizException("Failed set user credential for " + response.getMessage());
        }
    }

    @Override
    public BestsignResponse<PersonalCredentialVO> getPersonalCredential(String userAccountId) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", userAccountId);
        BestsignResponse<PersonalCredentialVO> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_GET_PERSONAL_CREDENTIAL, PersonalCredentialVO.class);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed set user credential" + response.getMessage());
        }
        //把账号设置回去
        response.getData().setAccount(userAccountId);
        return response;
    }

    @Override
    public BestsignResponse<EnterpriseCredentialVO> getEnterpriseCredential(String enterpriseAccountId) throws BestsignBizException {
        if (StringUtils.isBlank(enterpriseAccountId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", enterpriseAccountId);
        BestsignResponse<EnterpriseCredentialVO> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_GET_ENTERPRISE_CREDENTIAL, EnterpriseCredentialVO.class);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed set enterprise credential" + response.getMessage());
        }
        //把账号设置回去
        response.getData().setAccount(enterpriseAccountId);
        return response;
    }

    @Override
    public void createUserSignatureImage(String userAccountId) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", userAccountId);
        BestsignResponse<?> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_SIGNATUREIMAGE_CREATE, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to upload picture for user: %s, for %s", userAccountId, response.getMessage()));
        }
    }

    @Override
    public void uploadUserSignatureImage(String userAccountId, String imageBase64String, String imageName) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId) || StringUtils.isBlank(imageBase64String)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", userAccountId);
        paramMap.put("imageData", imageBase64String);
        paramMap.put("imageName", imageName);
        BestsignResponse<?> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_SIGNATUREIMAGE_UPLOAD, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to upload picture for user: %s, for %s", userAccountId, response.getMessage()));
        }
    }

    @Override
    public String downloadUserSignatureImage(String userAccountId, String imageName) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account", userAccountId);
        paramMap.put("imageName", imageName);
        BestsignResponse<?> response = getRequest(paramMap, BestsignUtil.BESTSIGN_API_SIGNATUREIMAGE_DOWNLOAD, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to upload picture for user: %s, for %s", userAccountId, response.getMessage()));
        }
        return (String)response.getData();
    }

    @Override
    public void setEnterpriseCredential(String userAccountId, String regCode, String orgCode, String taxCode,
                                        String enterpriseName, String legalPersonName, IdentityType identityType,
                                        String legalPersonIdentityNum, String legalPersonMobile, String contactMobile,
                                        String contactMail, String province, String city, String address) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId) || StringUtils.isBlank(regCode) || StringUtils.isBlank(enterpriseName)
                || StringUtils.isBlank(legalPersonName) || null == identityType ||
                StringUtils.isBlank(legalPersonIdentityNum) || StringUtils.isBlank(legalPersonMobile) ||
                StringUtils.isBlank(contactMobile)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        EnterpriseCredentialVO request = new EnterpriseCredentialVO();
        request.setAccount(userAccountId);
        request.setRegCode(regCode);
        request.setOrgCode(orgCode);
        request.setTaxCode(taxCode);
        request.setName(enterpriseName);
        request.setLegalPersonName(legalPersonName);
        request.setLegalPersonIdentityType(identityType);
        request.setLegalPersonIdentity(legalPersonIdentityNum);
        request.setLegalPersonMobile(legalPersonMobile);
        request.setContactMobile(contactMobile);
        request.setContactMail(contactMail);
        request.setProvince(province);
        request.setCity(city);
        request.setAddress(address);

        BestsignResponse<JSONObject> response = postRequest(request, BestsignUtil.BESTSIGN_API_ENTERPRISE_CREDENTIAL, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed set user credential" + response.getMessage());
        }
    }

    @Override
    public BestsignResponse<UserCertVO> applyUserCertification(String userAccountId) throws BestsignBizException, BestsignCertExistingException {
        if (StringUtils.isBlank(userAccountId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", userAccountId);
        BestsignResponse<UserCertVO> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_APPLY_CERTIFICATION, UserCertVO.class);
        if (null != response && !response.isOK()) {
            if (BestsignUtil.EXCEPTION_CODE_CERT_EXISTS.equals(response.getCode())) {
                throw new BestsignCertExistingException("cert for user:" + userAccountId + " existing.");
            }
            throw new BestsignBizException("Failed apply certfication for user:" + userAccountId + " " + response.getMessage());
        }
        return response;
    }

    @Override
    public String getPersonalUserCertification(String userAccountId) throws BestsignBizException {
        return this.getUserCertification(userAccountId, UserType.PERSONAL);
    }

    @Override
    public String getEnterpriseCertification(String enterpriseAccountId) throws BestsignBizException {
        return this.getUserCertification(enterpriseAccountId, UserType.ENTERPRISE);
    }

    private String getUserCertification(String userAccountId, UserType userType) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId) || null == userType) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", userAccountId);
        paramMap.put("certType", UserType.PERSONAL == userType ? BestsignUtil.PERSONAL_CERT_TYPE : BestsignUtil.ENTERPRISE_CERT_TYPE);

        BestsignResponse<JSONObject> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_GET_CERTIFICATION, JSONObject.class);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed apply certfication for user:" + userAccountId + response.getMessage());
        }
        return (String)response.getData().get("certId");
    }

    @Override
    public String uploadFile(String accountId, InputStream fileStream, FileType fileType, String fileSimpleName, Integer filePages) throws IOException, BestsignBizException {
        if (StringUtils.isBlank(accountId) || null == fileStream || null == fileType
                || StringUtils.isBlank(fileSimpleName) || null == filePages) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        UploadFileVO fileVO = null;
        try {
            byte[] bdata = IOUtils.toByteArray(fileStream);

            this.uploadFile(accountId, bdata, fileType, fileSimpleName, filePages);
        } finally {
            if (null != fileStream) {
                fileStream.close();
            }
        }

        return this.uploadFile(fileVO);
    }

    @Override
    public String uploadFile(String accountId, byte[] fileData, FileType fileType, String fileSimpleName, Integer filePages) throws IOException, BestsignBizException {
        if (StringUtils.isBlank(accountId) || null == fileData || null == fileType
                || StringUtils.isBlank(fileSimpleName) || null == filePages) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        UploadFileVO fileVO = new UploadFileVO();
        fileVO.setAccountId(accountId);
        fileVO.setFileData(Base64.encodeBase64String(fileData));
        fileVO.setFileMD5(DigestUtils.md5Hex(fileData));
        fileVO.setFileType(fileType);
        fileVO.setFileSimpleName(fileSimpleName);
        fileVO.setFilePages(filePages);

        return this.uploadFile(fileVO);
    }

    private String uploadFile(UploadFileVO uploadFileVO) throws IOException, BestsignBizException {
        BestsignResponse<JSONObject> response = postRequest(uploadFileVO, BestsignUtil.BESTSIGN_API_UPLOAD_FILE, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed to upload file: " + uploadFileVO.getFileSimpleName() + response.getMessage());
        }
        return (String)response.getData().get("fid");
    }

    @Override
    public String getFileDownloadURL(String accountId, String fileNum) throws BestsignBizException {
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(fileNum)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", accountId);
        paramMap.put("fid", fileNum);

        BestsignResponse<JSONObject> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_FILE_DOWNLOAD_URL, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to get download file url, fid:%s, account:%s for %s", fileNum, accountId, response.getMessage()));
        }
        return (String)response.getData().get("url");
    }

    @Override
    public String convertWordToPDF(String accountId, String sourceFileId) throws BestsignBizException {
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(sourceFileId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", accountId);
        paramMap.put("fid", sourceFileId);
        paramMap.put("ftype", FileType.PDF.getCode());

        BestsignResponse<JSONObject> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_FILE_CONVERTER, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed to convert word file: " + sourceFileId + response.getMessage());
        }
        return (String)response.getData().get("fid");
    }

    @Override
    public String createDraftContract(String accountId, String fileId, Integer expiredDays, String title, String description) throws BestsignBizException {
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(fileId) || StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("account", accountId);
        paramMap.put("fid", fileId);
        paramMap.put("title", title);
        paramMap.put("description", description);
        paramMap.put("expireTime", expiredDays == null ? null : String.valueOf(BestsignUtil.getUnixTimestampSecond(expiredDays)));

        BestsignResponse<JSONObject> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_CONTRACT_DRAFT_CREATE, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException("Failed to creat draft contract for file: " + fileId + response.getMessage());
        }
        return (String)response.getData().get("contractId");
    }

    @Override
    public void addSignersOnContract(String contractId, List<String> userAccounts) throws BestsignBizException {
        if (StringUtils.isBlank(contractId) || null == userAccounts || userAccounts.isEmpty()) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("contractId", contractId);
        paramMap.put("signers", BestsignUtil.toJsonString(userAccounts));

        BestsignResponse<JSONObject> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_CONTRACT_ADD_SIGNER, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to add signer on contract: %s, signers: %s, for: %s", contractId, userAccounts, response.getMessage()));
        }
    }

    @Override
    public void signContract(String userAccountId, String contractId, List<SignLocationVO> signLocations) throws BestsignBizException {
        if (StringUtils.isBlank(userAccountId) || StringUtils.isBlank(contractId) || null == signLocations || signLocations.isEmpty()) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("contractId", contractId);
        paramMap.put("signer", userAccountId);
        paramMap.put("signaturePositions", BestsignUtil.toJsonString(signLocations));

        BestsignResponse<JSONObject> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_CONTRACT_SIGN, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to sign contract: %s, signer: %s, for: %s", contractId, userAccountId, response.getMessage()));
        }
    }

    @Override
    public void finishContract(String contractId) throws BestsignBizException {
        if (StringUtils.isBlank(contractId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("contractId", contractId);

        BestsignResponse<?> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_FINISH_SIGN, null);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to finish contract: %s for: %s", contractId, response.getMessage()));
        }
    }

    @Override
    public DownloadContractVO getContractDownloadURL(String contractId) throws BestsignBizException {
        if (StringUtils.isBlank(contractId)) {
            throw new IllegalArgumentException("parameter can not be null. ");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("contractId", contractId);

        BestsignResponse<DownloadContractVO> response = postRequest(paramMap, BestsignUtil.BESTSIGN_API_CONTRACT_DOWNLOAD_URL, DownloadContractVO.class);
        if (null != response && !response.isOK()) {
            throw new BestsignBizException(String.format("Failed to finish contract: %s for: %s", contractId, response.getMessage()));
        }
        return response.getData();
    }

    private <T> BestsignResponse<T> getRequest(Map<String, Object> reuestParameters, String requestPath, Class<T> returnObjectClazz) {
        CloseableHttpResponse response = null;
        try {
            String requestURL = this.requestUrlBuilder.buildGETReqeustUrl(requestPath, reuestParameters);
            LOG.info("GET request url is:" + requestURL);

            HttpGet getRequest = new HttpGet(requestURL);
            response = this.httpClient.execute(getRequest, HttpClientContext.create());
            if (200 == response.getStatusLine().getStatusCode()) {
                return handleSuccessResponse(response, returnObjectClazz);
            } else {
                return BestsignResponse.createNetworkErrorResponse(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new BestsignDataException(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ioe) {
                throw new BestsignDataException(ioe);
            }
        }
    }

    private <T> BestsignResponse<T> postRequest(Object request, String requestPath, Class<T> returnObjectClazz) {
        CloseableHttpResponse response = null;
        try {
            String postBodyJsonStr = BestsignUtil.toJsonString(request);
            LOG.info("==== POST Body JSON:" + postBodyJsonStr);
            String requestURL = this.requestUrlBuilder.buildPOSTReqeustUrl(requestPath, postBodyJsonStr);
            LOG.info("POST request url is:" + requestURL);

            HttpPost postRequest = new HttpPost(requestURL);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(postBodyJsonStr.getBytes(BestsignUtil.CHARSET_UTF8));
            byteArrayEntity.setContentType(BestsignUtil.RESTFUL_REQUEST_HEADER);
            postRequest.setEntity(byteArrayEntity);

            response = this.httpClient.execute(postRequest, HttpClientContext.create());
            if (200 == response.getStatusLine().getStatusCode()) {
                return handleSuccessResponse(response, returnObjectClazz);
            } else {
                return BestsignResponse.createNetworkErrorResponse(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new BestsignDataException(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ioe) {
                throw new BestsignDataException(ioe);
            }
        }
    }

    private <T> BestsignResponse<T> handleSuccessResponse(CloseableHttpResponse response, Class<T> returnObjectClazz) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity.getContentType().getValue().contains("image/")) { // 处理图片，转化图片为Base64编码的字符串
            byte[] imageBytes = EntityUtils.toByteArray(response.getEntity());
            String imageBase64String = org.springframework.util.Base64Utils.encodeToString(imageBytes);
            return new BestsignResponse(BestsignUtil.BESTSIGN_REQUEST_OK_STATUS, "get image success", imageBase64String);
        } else {
            String result = EntityUtils.toString(entity, BestsignUtil.CHARSET_UTF8);
            EntityUtils.consume(entity);
            LOG.info("user registion result:" + result);

            BestsignResponse<T> rawTypeResponse = JSON.parseObject(result, BestsignResponse.class);
            if (null != returnObjectClazz && (returnObjectClazz != JSONObject.class && null != returnObjectClazz) && null != rawTypeResponse.getData()) {//根据类型参数重新对rawTypeResposne的数据进行序列化
                BestsignResponse<T> bestsignResponse = BestsignResponse.createResponse(rawTypeResponse.getCode(), rawTypeResponse.getMessage());
                bestsignResponse.setData(JSON.parseObject(rawTypeResponse.getData().toString(), returnObjectClazz));
                return bestsignResponse;
            } else {
                return rawTypeResponse;
            }
        }
    }

    public RequestUrlBuilder getRequestUrlBuilder() {
        return this.requestUrlBuilder;
    }

    public void setRequestUrlBuilder(RequestUrlBuilder builder) {
        this.requestUrlBuilder = builder;
    }
}
