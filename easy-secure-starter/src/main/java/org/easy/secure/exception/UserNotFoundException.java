package org.easy.secure.exception;


import org.easy.tool.exception.CustomException;
import org.easy.tool.web.ResultCode;

/**
 * @author xy
 */
public class UserNotFoundException extends CustomException {


    public UserNotFoundException(ResultCode code, String message) {
        super(code, message);
    }

    public UserNotFoundException(ResultCode code) {
        super(code);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
