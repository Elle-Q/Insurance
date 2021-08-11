package com.fintech.insurance.micro.thirdparty.service.bestsign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/17 10:45
 */
public class BestsignUtil {

    public static final String RESTFUL_REQUEST_HEADER = "application/json";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String PERSONAL_CERT_TYPE = "CFCA";
    public static final String ENTERPRISE_CERT_TYPE = "ZJCA";

    public static final String BESTSIGN_REQUEST_OK_STATUS = "0";

    public static final String BESTSIGN_API_USER_REGISTER = "/user/reg";
    public static final String BESTSIGN_API_PERSONAL_CREDENTIAL = "/user/setPersonalCredential";
    public static final String BESTSIGN_API_ENTERPRISE_CREDENTIAL = "/user/setEnterpriseCredential";
    public static final String BESTSIGN_API_GET_PERSONAL_CREDENTIAL = "/user/getPersonalCredential";
    public static final String BESTSIGN_API_GET_ENTERPRISE_CREDENTIAL = "/user/getEnterpriseCredential";
    public static final String BESTSIGN_API_APPLY_CERTIFICATION = "/user/applyCert";
    public static final String BESTSIGN_API_GET_CERTIFICATION = "/user/getCert";
    public static final String BESTSIGN_API_UPLOAD_FILE = "/storage/upload";
    public static final String BESTSIGN_API_FILE_DOWNLOAD_URL = "/file/getDownloadURL";
    public static final String BESTSIGN_API_FILE_CONVERTER = "/file/convert";
    public static final String BESTSIGN_API_CONTRACT_DRAFT_CREATE = "/contract/create";
    public static final String BESTSIGN_API_CONTRACT_ADD_SIGNER = "/contract/addSigners";
    public static final String BESTSIGN_API_CONTRACT_SIGN = "/contract/sign/cert";
    public static final String BESTSIGN_API_FINISH_SIGN = "/contract/finish";
    public static final String BESTSIGN_API_CONTRACT_DOWNLOAD_URL = "/contract/getDownloadURLs";
    public static final String BESTSIGN_API_SIGNATUREIMAGE_CREATE = "/signatureImage/user/create";
    public static final String BESTSIGN_API_SIGNATUREIMAGE_UPLOAD = "/signatureImage/user/upload";
    public static final String BESTSIGN_API_SIGNATUREIMAGE_DOWNLOAD = "/signatureImage/user/download";


    public static final String EXCEPTION_CODE_USER_EXISTS = "241208";
    public static final String EXCEPTION_CODE_CERT_EXISTS = "241308";

    private static ValueFilter nullValueFilter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if (null == value) {
                return "";
            }
            return value.toString();
        }
    };

    public static String toJsonString(Object obj) {
        String jsonStr = JSON.toJSONString(obj, nullValueFilter, SerializerFeature.WriteEnumUsingToString, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNonStringValueAsString);
        return jsonStr;
    }

    public static Map<String, Object> parseJsonString(String jsonStr) {
        return (Map<String, Object>)JSON.parse(jsonStr);
    }

    public static String getRtickStr() {
        String unix = Long.toString(System.currentTimeMillis());
        String randomStr = BestsignUtil.rand(1000, 9999) + "";
        String rtick = unix + randomStr;
        return rtick;
        //return "999999";
        //return "9999999999999999";
    }

    public static byte[] rsaSign(byte[] data, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] privateKeyBytes = Base64.decodeBase64(privateKey);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(priKey);
        signature.update(data);

        return signature.sign();
    }

    /**
     * 计算当前时间之后指定天数的unix时间戳（秒级）
     * @param delayDays
     * @return
     */
    public static long getUnixTimestampSecond(int delayDays) {
        return System.currentTimeMillis() / 1000 + delayDays * 86400;
    }

    public static int rand(int min, int max) {
        return (int) ((max - min + 1) * Math.random() + min);
    }

    public static void main(String[] args) {
        /*String jsonStr = "{\n" +
                "\t\t\"contactMobile\": \"13902443573\",\n" +
                "\t\t\"address\": \"\",\n" +
                "\t\t\"province\": \"\",\n" +
                "\t\t\"identityType\": \"0\",\n" +
                "\t\t\"city\": \"\",\n" +
                "\t\t\"identity\": \"43068119820721061X\",\n" +
                "\t\t\"name\": \"测试ABC\",\n" +
                "\t\t\"contactMail\": \"13902443573@177.com\"\n" +
                "\t}";

        System.out.println(jsonStr);

        PersonalCredentialVO vo = JSON.parseObject(jsonStr, PersonalCredentialVO.class);
        System.out.println(vo);*/

        String responseStr = "{\"errno\":0,\"data\":{\"contactMobile\":\"13902443573\",\"address\":\"\",\"province\":\"\",\"identityType\":\"0\",\"city\":\"\",\"identity\":\"43068119820721061X\",\"name\":\"测试ABC\",\"contactMail\":\"13902443573@177.com\"},\"errmsg\":\"\"}";
        JSONObject obj = JSON.parseObject(responseStr, JSONObject.class);
        System.out.println(obj);
    }


}
