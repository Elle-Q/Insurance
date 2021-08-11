package com.fintech.insurance.micro.channel.webchat.aspect;

import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.commons.constants.MockConstants;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 控制层拦截器，用于补全mock用户的上下文信息
 */
@Aspect
@Component
public class MockContextAspect {

    private static final Logger logger = LoggerFactory.getLogger(MockContextAspect.class);

    @Autowired
    private SysUserServiceFeign sysUserServiceClient;

    @Pointcut("execution(* com.fintech.insurance.micro.channel.webchat.controller..*.*(..))")
    public void executeService(){}

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("start the handle in mock context aspect");
        Map<String, Object> context = FInsuranceApplicationContext.currentContext();
        if (context != null && context.containsKey(JWTConstant.TOKEN_TYPE) && MockConstants.MOCK_TOKEN_TYPE.equals(context.get(JWTConstant.TOKEN_TYPE))) {
            logger.info("current token type is " + MockConstants.MOCK_TOKEN_TYPE);
            Integer userId = FInsuranceApplicationContext.getCurrentUserId();
            FintechResponse<UserVO> userVOResponse = sysUserServiceClient.getUserById(userId);
            //用户信息不存在
            if (userVOResponse == null || !userVOResponse.isOk() || userVOResponse.getData() == null) {
                logger.info("Cannot find the user information according to user with id " + userId);
                throw new FInsuranceBaseException(101503);
            } else {
                UserVO userVO = userVOResponse.getData();
                context.put(JWTConstant.USER_TYPE, userVO.getUserType());
                context.put(JWTConstant.CHANNEL_CODE, userVO.getChannelCode());
                context.put(JWTConstant.CHANNEL_ADMIN, String.valueOf(userVO.isChannelAdmin() ? 1 : 0));
                context.put(JWTConstant.ORGANIZATION_ID, String.valueOf(userVO.getOrganizationId()));
                FInsuranceApplicationContext.JWT_CONTEXT.set(context);
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
