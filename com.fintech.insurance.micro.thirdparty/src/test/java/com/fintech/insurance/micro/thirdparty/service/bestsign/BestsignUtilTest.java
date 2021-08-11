package com.fintech.insurance.micro.thirdparty.service.bestsign;

import com.fintech.insurance.commons.enums.AdStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/21 11:48
 */
@ActiveProfiles("junit")
public class BestsignUtilTest {

    static class UserRegisterRequest {
        private String name;
        private AdStatus adStatus;
        private Integer age;
        private String course;
        private List<Integer> values;

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public AdStatus getAdStatus() {
            return adStatus;
        }

        public void setAdStatus(AdStatus adStatus) {
            this.adStatus = adStatus;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public List<Integer> getValues() {
            return values;
        }

        public void setValues(List<Integer> values) {
            this.values = values;
        }
    }

    @Test
    public void testToJsonString() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("abc00");
        request.setAdStatus(AdStatus.FINISHED);

        String expected = "{\"adStatus\":\"finished\",\"age\":\"\",\"course\":\"\",\"name\":\"abc00\",\"values\":\"\"}";
        String jsonStr = BestsignUtil.toJsonString(request);
        Assert.assertEquals(expected, jsonStr);
    }

    @Test
    public void testParseJsonString() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("abc00");
        request.setAdStatus(AdStatus.FINISHED);

        String jsonStr = BestsignUtil.toJsonString(request);
        Map<String, Object> resultMap = BestsignUtil.parseJsonString(jsonStr);
        Assert.assertEquals("abc00", resultMap.get("name"));
        Assert.assertEquals("finished", resultMap.get("adStatus"));
        Assert.assertEquals("", resultMap.get("age"));
        Assert.assertEquals("", resultMap.get("course"));
        Assert.assertEquals("", resultMap.get("values"));
    }


}
