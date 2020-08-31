package org.easy.tool.support.controller;

import org.springframework.lang.Nullable;

import java.util.function.Supplier;

public class TypeDeferredResult<T> extends org.springframework.web.context.request.async.DeferredResult<T>{

    public TypeDeferredResult() {
        super();
    }


    public TypeDeferredResult(Long timeout) {
        super(timeout);
    }


    public TypeDeferredResult(@Nullable Long timeout, final Object timeoutResult) {
        super(timeout,timeoutResult);
    }


    public TypeDeferredResult(@Nullable Long timeout, Supplier<?> timeoutResult) {
        super(timeout,timeoutResult);
    }
    @Override
    @Nullable
    public T getResult() {
        Object object=super.getResult();
        return (T) object;
    }
}
