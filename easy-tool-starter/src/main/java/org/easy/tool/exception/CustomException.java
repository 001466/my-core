package org.easy.tool.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.easy.tool.web.ResultCode;

/**
 * @author xy
 */


@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;

    protected ResultCode code;

    public CustomException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ResultCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public CustomException(String message) {
        super(message);
        this.code = ResultCode.USER_NOT_FOUND;
    }
}
