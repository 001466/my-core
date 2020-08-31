package org.easy.cloud.config;


import lombok.extern.slf4j.Slf4j;
import org.easy.tool.exception.CustomException;
import org.easy.tool.exception.SecureException;
import org.easy.tool.web.R;
import org.easy.tool.web.ResultCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ControllerAdviceConfiguration {


    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public R<String> deoceanException(Exception ex){
        log.error(ex.getMessage(),ex);
        return R.fail(((CustomException) ex).getCode(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = SecureException.class)
    public R<String> secureException(Exception ex){
        log.error(ex.getMessage(),ex);
        return R.fail(((CustomException) ex).getCode(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> exception(Exception ex){
        log.error(ex.getMessage(),ex);

        if(ex instanceof CustomException){
            return R.fail(((CustomException) ex).getCode(), ex.getMessage());
        }else {
            if(StringUtils.isEmpty(ex.getCause().getMessage())){
                return R.fail(ResultCode.INTERNAL_SERVER_ERROR, ResultCode.INTERNAL_SERVER_ERROR.getMessage());
            }else {
                return R.fail(ResultCode.INTERNAL_SERVER_ERROR, ex.getCause().getMessage());
            }
        }
    }
}
