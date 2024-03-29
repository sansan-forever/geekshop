/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.shop;

import io.geekshop.common.Constant;
import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.config.session_cache.CachedSession;
import io.geekshop.custom.graphql.CustomGraphQLServletContext;
import io.geekshop.custom.security.Allow;
import io.geekshop.custom.security.SessionTokenHelper;
import io.geekshop.entity.CustomerEntity;
import io.geekshop.entity.UserEntity;
import io.geekshop.exception.ForbiddenException;
import io.geekshop.exception.PasswordResetTokenException;
import io.geekshop.exception.VerificationTokenException;
import io.geekshop.resolver.base.BaseAuthMutation;
import io.geekshop.service.*;
import io.geekshop.service.args.CreateCustomerHistoryEntryArgs;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.types.auth.AuthenticationInput;
import io.geekshop.types.auth.CurrentUser;
import io.geekshop.types.auth.LoginResult;
import io.geekshop.types.common.Permission;
import io.geekshop.types.customer.RegisterCustomerInput;
import io.geekshop.types.history.HistoryEntryType;
import io.geekshop.types.user.User;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@SuppressWarnings("Duplicates")
public class ShopAuthMutation extends BaseAuthMutation implements GraphQLMutationResolver {

    private final CustomerService customerService;
    private final HistoryService historyService;

    protected ShopAuthMutation(
            AuthService authService,
            UserService userService,
            AdministratorService administratorService,
            ConfigService configService,
            CustomerService customerService, HistoryService historyService) {
        super(authService, userService, administratorService, configService);
        this.customerService = customerService;
        this.historyService = historyService;
    }

    /**
     * Authenticates the user using the native authentication strategy.
     * This mutation is an alias for `authenticate({ native: { ... }})`
     */
    @Allow(Permission.Public)
    public LoginResult login(String username, String password, Boolean rememberMe, DataFetchingEnvironment dfe) {
        super.requireNativeAuthStrategy();
        return super.login(username, password, rememberMe, dfe);
    }

    /**
     * Authenticates the user using a named authentication strategy
     */
    @Allow(Permission.Public)
    public LoginResult authenticate(AuthenticationInput input, Boolean rememberMe, DataFetchingEnvironment dfe) {
        return super.authenticateAndCreateSession(input, rememberMe, dfe);
    }

    /**
     * End the current authenticated session
     */
    @Allow(Permission.Public)
    public Boolean logout(DataFetchingEnvironment dfe) {
        return super.logout(dfe);
    }

    /**
     * Register a Customer account with the given credentials. There are three possible registration flows:
     *
     *     _If `authOptions.requireVerification` is set to `true`:_
     *
     *     1. **The Customer is registered _with_ a password**. A verificationToken will be created (and typically emailed to the Customer). That
     *        verificationToken would then be passed to the `verifyCustomerAccount` mutation _without_ a password. The Customer is then
     *        verified and authenticated in one step.
     *     2. **The Customer is registered _without_ a password**. A verificationToken will be created (and typically emailed to the Customer). That
     *        verificationToken would then be passed to the `verifyCustomerAccount` mutation _with_ the chosed password of the Customer. The Customer is then
     *        verified and authenticated in one step.
     *
     *     _If `authOptions.requireVerification` is set to `false`:_
     *
     *     3. The Customer _must_ be registered _with_ a password. No further action is needed - the Customer is able to authenticate immediately.
     */
    @Allow(Permission.Public)
    public Boolean registerCustomerAccount(RegisterCustomerInput input, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.customerService.registerCustomerAccount(ctx, input);
    }

    /**
     *  Verify a Customer email address with the token sent to that address. Only applicable if `authOptions.requireVerification` is set to true.
     *
     *  If the Customer was not registered with a password in the `registerCustomerAccount` mutation, the a password _must_ be
     *  provided here.
     */
    @Allow(Permission.Public)
    public LoginResult verifyCustomerAccount(String token, String password, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerEntity customerEntity = this.customerService.verifiyCustomerEmailAddress(ctx, token, password);

        if (customerEntity == null || customerEntity.getUserId() == null) {
            throw new VerificationTokenException();
        }

        User user = userService.findUserWithRolesById(customerEntity.getUserId());
        CachedSession session =
                this.authService.createAuthenticatedSessionForUser(ctx, user, Constant.NATIVE_AUTH_STRATEGY_NAME);

        CustomGraphQLServletContext customGraphQLServletContext = dfe.getContext();
        HttpServletRequest request = customGraphQLServletContext.getHttpServletRequest();
        HttpServletResponse response = customGraphQLServletContext.getHttpServletResponse();

        SessionTokenHelper.setSessionToken(
                session.getToken(),
                true,
                this.configService.getAuthOptions(),
                request,
                response
        );
        LoginResult loginResult = new LoginResult();
        CurrentUser currentUser = BeanMapper.map(session.getUser(), CurrentUser.class);
        loginResult.setUser(currentUser);
        return loginResult;
    }

    /**
     * Regenerate and send a verification token for a new Customer registration.
     * Only applicable if `authOptions.requireVerification` is set to true.
     */
    @Allow(Permission.Public)
    public Boolean refreshCustomerVerification(String emailAddress, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        this.customerService.refreshVerificationToken(ctx, emailAddress);
        return true;
    }

    /**
     * Requests a password reset email to be sent
     */
    @Allow(Permission.Public)
    public Boolean requestPasswordReset(String emailAddress, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        this.customerService.requestPasswordReset(ctx, emailAddress);
        return true;
    }

    /**
     * Resets a Customer's password based on the provided token
     */
    @Allow(Permission.Public)
    public LoginResult resetPassword(String token, String password, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerEntity customerEntity = this.customerService.resetPassword(ctx, token, password);
        if (customerEntity == null || customerEntity.getUserId() == null) {
            throw new PasswordResetTokenException();
        }
        UserEntity userEntity = userService.findUserEntityById(customerEntity.getUserId());
        return super.login(userEntity.getIdentifier(), password, false, dfe);
    }

    /**
     * Update the password of the active Customer
     */
    @Allow(Permission.Owner)
    public Boolean updateCustomerPassword(String currentPassword, String newPassword, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        boolean result = super.updatePassword(ctx, currentPassword, newPassword);
        if (result && ctx.getActiveUserId() != null) {
            CustomerEntity customerEntity = this.customerService.findOneByUserId(ctx.getActiveUserId());
            if (customerEntity != null) {
                CreateCustomerHistoryEntryArgs args =
                        ServiceHelper.buildCreateCustomerHistoryEntryArgs(
                                ctx, customerEntity.getId(), HistoryEntryType.CUSTOMER_PASSWORD_UPDATED);
                this.historyService.createHistoryEntryForCustomer(args);
            }
        }
        return result;
    }

    /**
     * Request to update the emailAddress of the active Customer. If `authOptions.requireVerification` is enabled
     * (as is the default), then the `identifierChangeToken` will be assigned to the current User and
     * a IdentifierChangeRequestEvent will be raised. This can then be used e.g. by the EmailPlugin to email
     * that verification token to the Customer, which is then used to verify the change of email address.
     */
    @Allow(Permission.Owner)
    public Boolean requestUpdateCustomerEmailAddress(
            String password, String newEmailAddress, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ctx.getActiveUserId() == null) {
            throw new ForbiddenException();
        }
        this.authService.verifyUserPassword(ctx.getActiveUserId(), password);
        return this.customerService.requestUpdateEmailAddress(ctx, ctx.getActiveUserId(), newEmailAddress);
    }

    /**
     * Confirm the update of the emailAddress with the provided token, which has been generated by the
     * `requestUpdateCustomerEmailAddress` mutation.
     */
    @Allow(Permission.Owner)
    public Boolean updateCustomerEmailAddress(String token, DataFetchingEnvironment dfe) {
        this.requireNativeAuthStrategy();
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.customerService.updateEmailAddress(ctx, token);
    }
}
