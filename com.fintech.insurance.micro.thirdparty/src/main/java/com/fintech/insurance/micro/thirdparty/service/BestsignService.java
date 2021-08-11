package com.fintech.insurance.micro.thirdparty.service;

import com.fintech.insurance.micro.dto.thirdparty.SignLocationVO;
import com.fintech.insurance.micro.thirdparty.service.bestsign.BestsignResponse;
import com.fintech.insurance.micro.thirdparty.service.bestsign.RequestUrlBuilder;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.FileType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.IdentityType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.UserType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignBizException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignCertExistingException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignUserExistException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description: 上上签服务接口
 * @Author: Yong Li
 * @Date: 2017/11/17 11:56
 */
public interface BestsignService {

    void setRequestUrlBuilder(RequestUrlBuilder builder);

    /**
     * 使用用户手机号码注册上上签个人用户， 身份证号码是用户的唯一标识
     * @param userType 用户类型：个人用户 或 企业用户
     * @param userAccountId 账户ID (使用身份证作为用户账号), 不能为空
     * @param userName 用户姓名或企业名称, 最好用真实姓名或企业名称, 不能为空
     * @param mail 用户邮箱，可不填
     * @param mobile 手机号码，可不填
     */
    void registerUserAccount(UserType userType, String userAccountId, String userName, String mail, String mobile) throws BestsignBizException, BestsignUserExistException;

    /**
     * 为个人用户设置证件信息，该证件信息用于申请数字证书。
     *
     * @param userAccountId 在上上签的用户标识, 我们平台统一使用手机号码, 不能为空
     * @param identityType 证件类型,
     * @param identityNum 证件号码 需要和证件上登记的号码一致, 不能为空
     * @param userName 姓名 需要和证件上登记的号码一致, 不能为空
     * @param contactMail 联系邮箱
     * @param contractMobile 联系手机
     * @param province 省份 可以是中文
     * @param city 城市 可以是中文
     * @param address 详细地址 可以是中文
     */
    void setPersonalCredential(String userAccountId, IdentityType identityType, String identityNum, String userName,
            String contactMail, String contractMobile, String province, String city, String address) throws BestsignBizException, BestsignCertExistingException;

    /**
     * 根据指定的个人账户标识获取个人用户信息
     *
     * @param userAccountId 个人账户标识
     * @return
     * @throws BestsignBizException
     */
    BestsignResponse<PersonalCredentialVO> getPersonalCredential(String userAccountId) throws BestsignBizException;

    /**
     * 为某一个企业用户设置证件信息，该证件信息用于申请数字证书。
     *
     * @param userAccountId 企业用户证件信息的帐号, 不能为空
     * @param regCode 工商注册号, 不能为空
     * @param orgCode 组织机构代码
     * @param taxCode 税务登记证号
     * @param enterpriseName 企业名称, 不能为空
     * @param legalPersonName 法人代表姓名, 不能为空
     * @param identityType 法人代表证件号, 不能为空
     * @param legalPersonIdentityNum 法人代表证件类型, 不能为空
     * @param legalPersonMobile 法人代表手机号, 不能为空
     * @param contactMobile 联系手机, 不能为空
     * @param contactMail 联系邮箱
     * @param province 省份 可以是中文
     * @param city 城市 可以是中文
     * @param address 详细地址 可以是中文
     * @throws BestsignBizException
     */
    void setEnterpriseCredential(String userAccountId, String regCode, String orgCode, String taxCode, String enterpriseName,
                                 String legalPersonName, IdentityType identityType, String legalPersonIdentityNum,
                                 String legalPersonMobile, String contactMobile, String contactMail, String province,
                                 String city, String address) throws BestsignBizException;

    /**
     *  根据指定的企业账户标识获取企业用户信息
     *
     * @param enterpriseAccountId 企业账户标识
     * @return
     * @throws BestsignBizException
     */
    BestsignResponse<EnterpriseCredentialVO> getEnterpriseCredential(String enterpriseAccountId) throws BestsignBizException;

    /**
     * 为用户创建默认的签名图片、默认的印章图片
     * @param userAccountId 用户账户
     * @throws BestsignBizException
     */
    void createUserSignatureImage(String userAccountId) throws BestsignBizException;

    /**
     * 为某一用户上传用户签名/印章图片, <b>对于企业用户， 必须设置印章图片, 必须调用该接口或者创建默认签名图片的接口</b>
     *
     * @param userAccountId 用户账户
     * @param imageBase64String 图片经Base64编码后的字符串
     *                          印章图片的尺寸请按照实际尺寸设置（实际尺寸就是PS中按厘米为单位的尺寸），
     *                          比如印章是“5厘米×3.5厘米”大小的，印章图片实际尺寸就设置为“5厘米×3.5厘米”，
     *                          印章图片的dpi建议使用96或120，dpi太低签完后的清晰度不够，dpi太高则图片体积太大
     * @param imageName  传空或default表示更新默认的签名/印章图片
     * @throws BestsignBizException
     */
    void uploadUserSignatureImage(String userAccountId, String imageBase64String, String imageName) throws BestsignBizException;

    /**
     * 下载用户的签名/印章图片数据，便于核对
     *
     * @param userAccountId  用户账户
     * @param imageName 传空或default表示下载默认的签名/印章图片
     * @return 图片的Base64编码字符串
     * @throws BestsignBizException
     */
    String downloadUserSignatureImage(String userAccountId, String imageName) throws BestsignBizException;


    /**
     * 给个人或企业用户申请证书
     *
     * @param userAccountId 用户账户标识
     * @return 证书类型和编号
     * @throws BestsignBizException
     */
    BestsignResponse<UserCertVO> applyUserCertification(String userAccountId) throws BestsignBizException, BestsignCertExistingException;

    /**
     * 获取个人用户证书编号（CFCA类型）
     *
     * @param userAccountId 个人用户账户标识
     * @return
     * @throws BestsignBizException
     */
    String getPersonalUserCertification(String userAccountId) throws BestsignBizException;

    /**
     * 获取企业用户证书编号（ZJCA类型）
     *
     * @param enterpriseAccountId 企业用户账户标识
     * @return
     * @throws BestsignBizException
     */
    String getEnterpriseCertification(String enterpriseAccountId) throws BestsignBizException;

    /**
     * 上传文件(上上签的文件存储服务), 获得文件编号
     *
     * 一般上传的文件包括合同的word版， PDF版
     *
     * @param accountId 执行操作的账户
     * @param fileStream 文件流， 读取完接口会关闭文件流
     * @param fileType 文件类型
     * @param fileSimpleName 文件简单名称,不带路径
     * @param filePages 文件页数
     * @return 文件编号
     */
    String uploadFile(String accountId, InputStream fileStream, FileType fileType, String fileSimpleName, Integer filePages) throws IOException, BestsignBizException;

    /**
     * 上传文件(上上签的文件存储服务), 获得文件编号
     *
     * 一般上传的文件包括合同的word版， PDF版
     *
     * @param accountId 执行操作的账户
     * @param fileData 文件的二进制数组
     * @param fileType 文件类型
     * @param fileSimpleName 文件简单名称,不带路径
     * @param filePages 文件页数
     * @return 文件编号
     */
    String uploadFile(String accountId, byte[] fileData, FileType fileType, String fileSimpleName, Integer filePages) throws IOException, BestsignBizException;

    /**
     * 获取指定账户上传的指定编号的下载链接
     * @param accountId 指定账户
     * @param fileNum 上传文件获得的文件编号
     * @return
     */
    String getFileDownloadURL(String accountId, String fileNum) throws BestsignBizException;

    /**
     * 将存储在上上签的word文档转为PDF格式，文件仍然存储在上上签服务器上， 返回PDF文件的文件编号
     * @param accountId 操作账户
     * @param sourceFileId word文档文件编号
     * @return PDF文件编号
     */
    String convertWordToPDF(String accountId, String sourceFileId) throws BestsignBizException;

    /**
     * 使用已得到的文件编号创建合同，得到合同编号，此合同还未添加签署者，属于草稿性质
     *
     * @param accountId 指定账户(必填)
     * @param fileId 文件编号(必填)
     * @param expiredDays 合同过期天数(如不指定，默认7天)： 合同必须在指定的到期时间之前完成签署，一旦过期则此合同将无法被签署。
     * @param title 合同标题(必填), 可以将自己的业务合同编号、或合同标题放此处
     * @param description 合同描述（非必填）
     * @return 合同编号
     */
    String createDraftContract(String accountId, String fileId, Integer expiredDays, String title, String description) throws BestsignBizException;

    /**
     * 为已创建的草稿合同批量添加签署者，可重复调用
     *
     * @param contractId 合同编号
     * @param userAccounts 用户账户列表
     */
    void addSignersOnContract(String contractId, List<String> userAccounts) throws BestsignBizException;

    /**
     * 用指定的用户账户对指定合同进行签署, 可以设置签注位置
     *
     * @param userAccountId 用户账户， 该用户需要已经设置进入合同的签署者
     * @param contractId 合同编号
     * @param signLocations 签署位置， 可以多个。
     */
    void signContract(String userAccountId, String contractId, List<SignLocationVO> signLocations) throws BestsignBizException;

    /**
     * 完成合同。 合同完成后即可以下载
     *
     * @param contractId 合同编号
     */
    void finishContract(String contractId) throws BestsignBizException;

    /**
     * 当合同已经结束后，使用此接口获取下载合同的链接地址
     *
     * @param contractId 合同编号
     * @return 合同与附页文件的下载url
     */
    DownloadContractVO getContractDownloadURL(String contractId) throws BestsignBizException;
}
