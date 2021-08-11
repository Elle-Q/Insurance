package com.fintech.insurance.service.agg.impl;


import com.fintech.insurance.commons.annotations.ImageUrl;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.interceptor.FinanceDataInterceptor;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.service.agg.ImageUtilService;
import com.fintech.insurance.service.agg.ThirdPartyService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/12/15 17:51
 */
@Service
public class ImageUtilServiceImpl implements ImageUtilService{

    private static final Logger LOG = LoggerFactory.getLogger(ImageUtilService.class);

    @Autowired
    ThirdPartyService thirdPartyService;

    @Override
    public void transferImageUrlOnObject(Object objectData, int fieldDeepth) {
        if (objectData == null || Object.class == objectData.getClass() || fieldDeepth > 5) {// 最多递归5层
            return;
        }
        if (objectData instanceof FintechResponse) {
            Object responseData = ((FintechResponse) objectData).getData();
            if (responseData instanceof Pagination) {
                transferImageUrlOnObject(((Pagination) responseData).getItems(), 0);
            } else {
                transferImageUrlOnObject(responseData, 0);
            }
        }
        if (objectData instanceof Collection) {
            Collection<?> items = (Collection<?>) objectData;
            for (Object item : items) {
                transferImageUrlOnObject(item, fieldDeepth);
            }
        } else if (objectData instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) objectData).entrySet()) {
                if (null != entry.getValue()) {
                    transferImageUrlOnObject(entry.getValue(), fieldDeepth + 1);
                }
            }
        } else if (objectData.getClass().getCanonicalName().startsWith("com.fintech")) {// 只对当前项目定义的VO进行处理
            Field[] fields = FieldUtils.getAllFields(objectData.getClass());
            for (Field field : fields) {
                try {
                    Object fieldValue = FieldUtils.readField(field, objectData, true);
                    if (null != fieldValue) {
                        if (null != field.getAnnotation(ImageUrl.class)) {
                            this.convertForWebShow(objectData, field.getName(), fieldValue);
                        } else {//继续递归
                            transferImageUrlOnObject(fieldValue, fieldDeepth + 1);
                        }
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 更新对象
     * @param objectData    转化的对象
     * @param fieldName     属性名称
     * @param fieldValue    属性值
     * @throws IllegalAccessException
     */
    private void convertForWebShow(Object objectData, String fieldName, Object fieldValue) throws IllegalAccessException {

        ImageVO imageVO = null;//图片url和缩略图urlvo
        List<String> urlList = null;//图片url集合
        List<String> narrowUrlList = null;//缩略图url集合

        if (fieldValue instanceof String) {//当前属性是String类型，即图片的uuid，直接取imageVO
            imageVO = thirdPartyService.getImageVO((String) fieldValue);
        } else if (fieldValue instanceof ArrayList<?>) {//当前属性是String类型，即图片的uuid集合，分别拿到图片url集合和缩略图url集合
            urlList = thirdPartyService.getImageUrl((List<String>) fieldValue);
            narrowUrlList = thirdPartyService.getImageNarrowUrl((List<String>) fieldValue);
        } else if (fieldValue instanceof String[]) {
            urlList = thirdPartyService.getImageUrl(Arrays.asList((String[]) fieldValue));
            narrowUrlList = thirdPartyService.getImageNarrowUrl(Arrays.asList((String[]) fieldValue));
        }

        //字符串拼接（统一后缀）
        String urlValueFeildName = fieldName + "Url";
        String narrowUrlValueFeildName = fieldName + "NarrowUrl";

        Field urlValueFeild = FieldUtils.getDeclaredField(objectData.getClass(), urlValueFeildName, true);
        Field narrowUrlValueFeild = FieldUtils.getDeclaredField(objectData.getClass(), narrowUrlValueFeildName, true);

        if (null != imageVO) {
            FieldUtils.writeField(urlValueFeild, objectData, imageVO.getImageUrl(), true);
            FieldUtils.writeField(narrowUrlValueFeild, objectData, imageVO.getThumbnailUrl(), true);
        }
        if (null != urlList && null != narrowUrlList) {
            if (fieldValue instanceof String[]) {
                String[] stringsUrl = new String[urlList.size()];
                String[] stringsNarrowUrl = new String[urlList.size()];
                for (int index = 0; index < urlList.size(); index++) {
                    stringsUrl[index] = urlList.get(index);
                }

                for (int index = 0; index < narrowUrlList.size(); index++) {
                    stringsNarrowUrl[index] = narrowUrlList.get(index);
                }
                FieldUtils.writeField(urlValueFeild, objectData, stringsUrl, true);
                FieldUtils.writeField(narrowUrlValueFeild, objectData, stringsNarrowUrl, true);
            } else {
                FieldUtils.writeField(urlValueFeild, objectData, urlList, true);
                FieldUtils.writeField(narrowUrlValueFeild, objectData, narrowUrlList, true);
            }
        }
    }
}
