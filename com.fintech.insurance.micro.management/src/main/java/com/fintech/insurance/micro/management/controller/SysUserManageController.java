package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.system.PasswordVO;
import com.fintech.insurance.micro.dto.system.RoleVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 15:02
 */
@RestController
@RequestMapping(value = "/management/system/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysUserManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(SysUserManageController.class);

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;
    /**
     * 创建编辑用户
     * @param userVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<Integer> saveUser(@RequestBody UserVO userVO) {
        FintechResponse<Integer> result = sysUserServiceFeign.saveUser(userVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 用户修改密码
     *
     * @param passwordVO
     */
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    void changeUserPassword(@RequestBody PasswordVO passwordVO) {
        FintechResponse<VoidPlaceHolder> result = sysUserServiceFeign.changeUserPassword(passwordVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }

    /**s
     * 管理员重置密码
     *
     * @param passwordVO
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    void resetUserPassword(@RequestBody PasswordVO passwordVO) {
        FintechResponse<VoidPlaceHolder> result = sysUserServiceFeign.resetUserPassword(passwordVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }


    /**
     * 用户列表
     * @param mobile 手机号码
     * @param name  姓名
     * @param isLocked 用户状态
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    FintechResponse<List<UserVO>> listUser(@RequestParam(name = "mobile", defaultValue = "") String mobile,
                                           @RequestParam(name = "name", defaultValue = "") String name,
                                           @RequestParam(name = "isLocked", defaultValue = "") Integer isLocked) {
        FintechResponse<List<UserVO>> result = sysUserServiceFeign.listUser(mobile, name, isLocked);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 冻结用户
     * @param userVO
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    void freezeUser(@Validated({Update.class}) @RequestBody UserVO userVO) {
        FintechResponse<VoidPlaceHolder> result = sysUserServiceFeign.freezeUser(userVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }

    /**
     * 解冻用户
     * @param userVO
     */
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    void unfreezeUser(@Validated({Update.class}) @RequestBody UserVO userVO) {
        FintechResponse<VoidPlaceHolder> result = sysUserServiceFeign.unfreezeUser(userVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }

    /**
     * 角色列表
     */
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    FintechResponse<List<RoleVO>> listRoles() {
        FintechResponse<List<RoleVO>> result = sysUserServiceFeign.listRoles();
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

}
