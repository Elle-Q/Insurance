package com.fintech.insurance.micro.api.thirdparty;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.thirdparty.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Description: 上上签合同接口
 * @Author: Yong Li
 * @Date: 2017/11/25 11:07
 */
@RequestMapping(value = "/thirdparty/bestsign")
public interface BestsignServiceAPI {

    /**
     * 为个人在上上签创建一个账户(该账户对应用户的身份证号码)， 并申请数字证书
     *
     * @param userInfoVO
     * @return 上上签的数字证书
     */
    @PostMapping(path = "/create-account")
    FintechResponse<String> createPersonalUserAccount(@Validated @RequestBody BestsignUserInfoVO userInfoVO);

    /**
     * 根据给定的模板数据，以及合同模板，生成预览的合同内容文件存储在上上签文件服务存储中心（保留10天）.
     *
     * @param creationVO
     * @return 1. 合同内容文件编号, 保存10天
     */
    @Deprecated
    @PostMapping(path = "/create-draft-contract")
    FintechResponse<String> createDraftContract(@Validated @RequestBody ContractCreationVO creationVO);

    @PostMapping(path = "/create-draft-contract/file")
    FintechResponse<RemoteFileVO> createDraftContractByFileData(@Validated @RequestBody WordFileVO wordFileVO);

    @GetMapping(path = "/get-download-url")
    FintechResponse<String> getFileDownloadURL(@RequestParam(name = "fileId") String fileId);

    @PostMapping(path = "/sign-contract")
    FintechResponse<RemoteFileVO> signContract(@Validated @RequestBody ContractSignVO signInfo);
}
