package co.jueyi.geekshop.properties;

import lombok.Data;

/**
 * The AuthOptions define how authentication is managed.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AuthOptions {
    /**
     * Disable authentication & permissions checks.
     * NEVER set the to true in production. It exists
     * only to aid certain development tasks.
     *
     * @default false
     */
    private boolean disableAuth = false;
    /**
     * Sets the method by which the session token is delivered and read.
     *
     * * 'cookie': Upon login, a 'Set-Cookie' header will be returned to the client, setting a
     *   cookie containing the session token. A browser-based client (making requests with credentials)
     *   should automatically send the session cookie with each request.
     * * 'bearer': Upon login, the token is returned in the response and should be then stored by the
     *   client app. Each request should include the header 'Authorization: Bearer <token>'.
     *
     * @default 'cookie'
     */
    private TokenMethod tokenMethod = TokenMethod.cookie;
    /**
     * The secret used for signing the session cookies for authenticated users. Only applies when
     * tokenMethod is set to 'cookie'.
     *
     * In production applications, this should not be stored as a string in
     * source control for security reasons, but may be loaded from an external
     * file not under source control, or from an environment variable, for example.
     *
     * @default 'session-secret'
     */
    private String sessionSecret = "session-secret";
    /**
     * Sets the header property which will be used to send the auth token when using the 'bearer' method.
     *
     * @default 'geekshop-auth-token'
     */
    private String authTokenHeaderKey = "geekshop-auth-token";
    /**
     * Session duration, i.e. the time which must elapse from the last authenticated request
     * after which the user must re-authenticate.
     *
     * Expressed as a string describing a time span per
     * [zeit/ms](https://github.com/zeit/ms.js).  Eg: `60`, `'2 days'`, `'10h'`, `'7d'`
     *
     * @default '7d'
     */
    private String sessionDuration = "7d";
    /**
     * Determines whether new User accounts require verification of their email address.
     *
     * @defaut true
     */
    private boolean requireVerification = true;
    /**
     * Sets the length of time that a verification token is valid for, after which the verification token must be refreshed.
     *
     * Expressed as a string describing a time span per
     * [zeit/ms](https://github.com/zeit/ms.js).  Eg: `60`, `'2 days'`, `'10h'`, `'7d'`
     *
     * @default '7d'
     */
    private String verificationTokenDuration = "7d";
}
