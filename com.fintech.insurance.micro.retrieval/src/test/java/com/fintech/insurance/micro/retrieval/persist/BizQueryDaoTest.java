package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.micro.dto.retrieval.UserVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:27
 */
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@Transactional
public class BizQueryDaoTest {

    @Autowired
    private BizQueryDao bizQueryDao;

    @Test
    public void testFindAll() {
        List<UserVO> userVOList = bizQueryDao.findAllUser();
        Assert.assertNotNull(userVOList);
    }
}
