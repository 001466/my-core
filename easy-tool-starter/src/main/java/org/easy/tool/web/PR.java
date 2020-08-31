package org.easy.tool.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.easy.tool.constant.SystemConstant;

@ApiModel(value = "PageResult", description = "PageResult")
public class PR<T> extends R<T>{


    @ApiModelProperty(value = "总数",required=false,position=0,hidden=false)
    long total;
    @ApiModelProperty(value = "页码",required=false,position=0,hidden=false)
    int page;
    @ApiModelProperty(value = "页数",required=false,position=0,hidden=false)
    int size;



    public PR() {
        super();
    }

    public PR(Boolean success, Integer code, String message, T data) {
        super(success,code,message,data);
    }

    public PR(Boolean success, Integer code, String message) {
        super(success,code,message);
    }

    public PR(String message, T data) {
        super(message,data);
    }

    public PR(String message) {
        super(message);
    }
    




    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }


    public PR<T> setTotal(long total) {
        this.total = total;return this;
    }

    public PR<T> setPage(int page) {
        this.page = page;return this;
    }

    public PR<T> setSize(int size) {
        this.size = size;return this;
    }

    @Override
    public PR setSuccess(boolean success) {
        this.success = success;return this;
    }

    @Override
    public PR setCode(Integer code) {
        this.code = code;return this;
    }

    @Override
    public PR setMessage(String message) {
        this.message = message;return this;
    }

    @Override
    public PR setData(T data) {
        this.data = data;return this;
    }



    public static <T> PR<T> success() {
        return new PR<>(true, ResultCode.SUCCESS.getCode(), ResultCode.FAILURE.getMessage());
    }

    public static <T> PR<T> success(String msg) {
        return new PR<>(true, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> PR<T> success(T data) {
        return new PR<>(true, ResultCode.SUCCESS.getCode(), ResultCode.FAILURE.getMessage(), data);
    }

    public static <T> PR<T> success(String msg, T data) {
        return new PR<>(true, ResultCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> PR<T> fail() {
        return new PR<>(false, ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage());
    }

    public static <T> PR<T> fail(ResultCode code) {
        return new PR<>(false, code == null ? null : code.getCode(), code.getMessage());
    }

    public static <T> PR<T> fail(String msg) {
        return new PR<>(false, ResultCode.FAILURE.getCode(), msg);
    }

    public static <T> PR<T> fail(ResultCode code, String msg) {
        return new PR<>(false, code == null ? null : code.getCode(), msg);
    }
}
