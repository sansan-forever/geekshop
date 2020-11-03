package co.jueyi.geekshop.exception;

import graphql.ErrorClassification;
import graphql.ErrorType;

/**
 * 内部服务器处理异常
 *
 * Created on Nov, 2020 by @author bobo
 */
public class InternalServerError extends AbstractGraphqlException {
    public InternalServerError() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerError(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalServerError(ErrorCode errorCode, String message) {
        super(message, errorCode);
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ExecutionAborted;
    }
}
