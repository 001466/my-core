package org.easy.tool.web;


import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.easy.tool.constant.SystemConstant;
import org.easy.tool.util.SpringUtil;
import org.easy.tool.util.StringUtil;

import java.io.Serializable;

@ApiModel(value = "Result", description = "Result")
@Slf4j
public class R<T>  implements Serializable {


    private static final long serialVersionUID = 1L;

    protected String traceId;
    protected boolean success = true;
    protected Integer code;
    protected String message="无";
    protected Long timestamp;
    protected T data=null;


    public R() {
        this.code=ResultCode.SUCCESS.getCode();this.traceId=getTraceId();
    }

    public R(Boolean success, Integer code, String message, T data) {
        this.success=success;
        this.code=code;
        this.message=message;
        this.data=data;
        this.timestamp=System.currentTimeMillis();
        //this.traceId=getTraceId();
    }

    public R(Boolean success, Integer code, String message) {
        this.success=success;
        this.code=code;
        this.message=message;
        this.timestamp=System.currentTimeMillis();
        //this.data=data;
        //this.traceId=getTraceId();
    }

    public R(String message, T data) {
        this.success=true;
        this.code=ResultCode.SUCCESS.getCode();
        this.message=message;
        this.data=data;
        this.timestamp=System.currentTimeMillis();
        //this.traceId=getTraceId();
    }

    public R(String message) {
        this.success=true;
        this.code=ResultCode.SUCCESS.getCode();
        this.message=message;
        this.timestamp=System.currentTimeMillis();
        //this.data=data;
        //this.traceId=getTraceId();
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public R setSuccess(boolean success) {
        this.success = success;return this;
    }

    public R setCode(Integer code) {
        this.code = code;return this;
    }

    public R setMessage(String message) {
        this.message = message;return this;
    }

    public R setData(T data) {
        this.data = data;return this;
    }



    public boolean isSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }





    public static <T>  R<T> success() {
        return new R<>(true, ResultCode.SUCCESS.getCode(), ResultCode.FAILURE.getMessage());
    }

    public static <T> R<T> success(String msg) {
        return new R<>(true, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> R<T> success(T data) {
        return new R<>(true, ResultCode.SUCCESS.getCode(), ResultCode.FAILURE.getMessage(), data);
    }

    public static <T> R<T> success(String msg, T data) {
        return new R<>(true, ResultCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> R<T> fail() {
        return new R<>(false, ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage());
    }

    public static <T> R<T> fail(ResultCode code) {
        return new R<>(false, code == null ? null : code.getCode(), code.getMessage());
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(false, ResultCode.FAILURE.getCode(), msg);
    }

    public static <T> R<T> fail(ResultCode code, String msg) {
        return new R<>(false, code == null ? null : code.getCode(), msg);
    }


    public String getTraceId(){

        if(!StringUtil.isEmpty(this.traceId)){
            return this.traceId;
        }

        try{
            return SpringUtil.getBean("tracer", brave.Tracer.class).currentSpan().context().traceIdString();
        }catch(Exception e){
            //log.error("生成链路追踪ID失败{}"+e.getMessage());
            return null;
        }

    }

    public R setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }



}
