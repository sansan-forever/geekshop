package co.jueyi.geekshop.exception;

/**
 * This error should be thrown when the user's authentication credentials do not match.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class UnauthorizedException extends AbstractGraphqlException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
