package org.easy.cloud.config;

import com.deocean.common.exception.DeoceanException;
import com.deocean.common.exception.SecureException;
import com.deocean.common.web.R;
import com.deocean.common.web.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ControllerAdviceConfiguration {


    @ResponseBody
    @ExceptionHandler(value = DeoceanException.class)
    public R<String> deoceanException(Exception ex){
        log.error(ex.getMessage(),ex);
        return R.fail(((DeoceanException) ex).getCode(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = SecureException.class)
    public R<String> secureException(Exception ex){
        log.error(ex.getMessage(),ex);
        return R.fail(((DeoceanException) ex).getCode(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> exception(Exception ex){
        log.error(ex.getMessage(),ex);

        if(ex instanceof DeoceanException){
            return R.fail(((DeoceanException) ex).getCode(), ex.getMessage());
        }else {
            if(StringUtils.isEmpty(ex.getCause().getMessage())){
                return R.fail(ResultCode.INTERNAL_SERVER_ERROR, ResultCode.INTERNAL_SERVER_ERROR.getMessage());
            }else {
                return R.fail(ResultCode.INTERNAL_SERVER_ERROR, ex.getCause().getMessage());
            }
        }
    }
}
