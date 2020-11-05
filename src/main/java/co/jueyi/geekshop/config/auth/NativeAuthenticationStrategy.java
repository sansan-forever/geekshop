package co.jueyi.geekshop.config.auth;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.AuthenticationMethodEntity;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.exception.UnauthorizedException;
import co.jueyi.geekshop.exception.UserInputException;
import co.jueyi.geekshop.mapper.AuthenticationMethodEntityMapper;
import co.jueyi.geekshop.mapper.UserEntityMapper;
import co.jueyi.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * This strategy implements a username/password credential-based authentication, with the credentials
 * being stored in the GeekShop database. This is the default method of authentication, and it is advised
 * to keep it configured unless there is a specific reason not to.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class NativeAuthenticationStrategy implements AuthenticationStrategy<NativeAuthenticationData> {

    @Autowired
    private UserEntityMapper userEntityMapper;
    @Autowired
    private AuthenticationMethodEntityMapper authenticationMethodEntityMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    static String KEY_USERNAME = "username";
    static String KEY_PASSWORD = "password";

    @Override
    public String getName() {
        return Constant.NATIVE_AUTH_STRATEGY_NAME;
    }

    @Override
    public NativeAuthenticationData convertInputToData(Map<String, String> inputMap) {
        NativeAuthenticationData data = new NativeAuthenticationData();
        data.setUsername(inputMap.get(KEY_USERNAME));
        if (StringUtils.isEmpty(data.getUsername())) {
            throw new UserInputException("username is empty");
        }
        data.setPassword(inputMap.get(KEY_PASSWORD));
        if (StringUtils.isEmpty(data.getPassword())) {
            throw new UserInputException("password is empty");
        }
        return data;
    }

    @Override
    public User authenticate(RequestContext ctx, NativeAuthenticationData data) {
        User user = this.getUserFromIdentifier(data.getUsername());
        boolean passwordMatch = this.verifyUserPassword(user.getId(), data.getPassword());
        if (!passwordMatch) {
            return null;
        }
        return user;
    }

    @Override
    public void onLogOut(User user) {
        // nothing to do
    }

    private User getUserFromIdentifier(String identifier) {
        User user = this.userEntityMapper.findUserWithRoleByIdentifier(identifier);
        if (user == null) {
            throw new UnauthorizedException();
        }
        return user;
    }

    /**
     * Verify the provided password against the one we have for the given user.
     */
    boolean verifyUserPassword(Long userId, String password) {
        QueryWrapper<AuthenticationMethodEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthenticationMethodEntity::getUserId, userId);
        List<AuthenticationMethodEntity> authMethods =
                this.authenticationMethodEntityMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(authMethods)) {
            throw new InternalServerError("User's native authentication methods not loaded");
        }
        AuthenticationMethodEntity nativeAuthMethod =
                authMethods.stream().filter(m -> !m.isExternal())
                .findFirst().orElse(null);
        if (nativeAuthMethod == null) {
            throw new InternalServerError("User's native authentication method not found");
        }

        boolean passwordMatches = this.passwordEncoder.matches(password, nativeAuthMethod.getPasswordHash());

        return passwordMatches;
    }
}
