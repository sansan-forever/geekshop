package co.jueyi.geekshop.exception;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class InvalidInputException extends AbstractGraphqlException {
    public InvalidInputException() {
        super(ErrorCode.INVALID_INPUT_PARAMETER);
    }
}
