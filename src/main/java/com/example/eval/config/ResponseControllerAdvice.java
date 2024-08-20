package com.example.eval.config;

import com.example.eval.common.ResponseData;
import com.example.eval.common.ResultEnum;
import com.example.eval.common.ResponseData;
import com.example.eval.common.ResultEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 控制层统一返回数据封装，拦截器实现
 * by 张渝
 * @since 2022-09-30
 */
@RestControllerAdvice(annotations = RestController.class)
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        // 若返回值本来就是ResponseData，就不需要再封装
        return !(returnType.getParameterType().equals(ResponseData.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ResponseData<Object> responseData = new ResponseData<>(ResultEnum.SUCCESS);
        responseData.setData(body);
        // 当返回给前端的数据只有msg时，不要将它封装进data了
        if(returnType.getGenericParameterType().equals(String.class)) {
            responseData.setData(null);
            responseData.setMsg((String) body);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(responseData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return responseData;
    }
}
