package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.AuthenticationMethodEntity;
import co.jueyi.geekshop.entity.RoleEntity;
import co.jueyi.geekshop.entity.UserEntity;
import co.jueyi.geekshop.entity.UserRoleJoinEntity;
import co.jueyi.geekshop.exception.*;
import co.jueyi.geekshop.mapper.AuthenticationMethodEntityMapper;
import co.jueyi.geekshop.mapper.RoleEntityMapper;
import co.jueyi.geekshop.mapper.UserEntityMapper;
import co.jueyi.geekshop.mapper.UserRoleJoinEntityMapper;
import co.jueyi.geekshop.service.helper.ServiceHelper;
import co.jueyi.geekshop.service.helper.VerificationTokenGenerator;
import co.jueyi.geekshop.types.role.Role;
import co.jueyi.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityMapper userEntityMapper;
    private final RoleEntityMapper roleEntityMapper;
    private final UserRoleJoinEntityMapper userRoleJoinEntityMapper;
    private final ConfigService configService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final VerificationTokenGenerator verificationTokenGenerator;
    private final AuthenticationMethodEntityMapper authenticationMethodEntityMapper;

    public AuthenticationMethodEntity getNativeAuthMethodEntityByUserId(Long userId) {
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
        return nativeAuthMethod;
    }

    public User findUserById(Long id) {
        UserEntity userEntity = this.userEntityMapper.selectById(id);
        if (userEntity == null) return null;
        return BeanMapper.map(userEntity, User.class);
    }

    public User findUserWithRolesById(Long userId) {
        User user = this.findUserById(userId);
        if (user == null) return null;
        List<Role> roles = this.findRolesByUserId(userId);
        user.setRoles(roles);
        return user;
    }

    public User findUserByIdentifier(String identifier) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getIdentifier, identifier);
        UserEntity userEntity = this.userEntityMapper.selectOne(queryWrapper);
        if (userEntity == null) return null;
        return BeanMapper.map(userEntity, User.class);
    }

    public User findUserByEmailAddress(String emailAddress) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getIdentifier, emailAddress).isNull(UserEntity::getDeletedAt);
        UserEntity userEntity = this.userEntityMapper.selectOne(queryWrapper);
        if (userEntity == null) return null;
        return BeanMapper.map(userEntity, User.class);
    }

    public User findUserWithRoleByIdentifier(String identifier) {
        User user = this.findUserByIdentifier(identifier);
        if (user == null) return null;
        List<Role> roles = this.findRolesByUserId(user.getId());
        user.setRoles(roles);
        return user;
    }

    public List<Role> findRolesByUserId(Long userId) {
        QueryWrapper<UserRoleJoinEntity> userRoleJoinEntityQueryWrapper = new QueryWrapper<>();
        userRoleJoinEntityQueryWrapper.lambda().eq(UserRoleJoinEntity::getUserId, userId);
        List<UserRoleJoinEntity> userRoleJoinEntities =
                this.userRoleJoinEntityMapper.selectList(userRoleJoinEntityQueryWrapper);
        if (CollectionUtils.isEmpty(userRoleJoinEntities)) return new ArrayList<>();

        List<Long> roleIds =
                userRoleJoinEntities.stream().map(UserRoleJoinEntity::getRoleId).collect(Collectors.toList());
        QueryWrapper<RoleEntity> roleEntityQueryWrapper = new QueryWrapper<>();
        roleEntityQueryWrapper.lambda().in(RoleEntity::getId, roleIds);
        List<RoleEntity> roleEntities = this.roleEntityMapper.selectList(roleEntityQueryWrapper);
        return roleEntities.stream().map(roleEntity -> BeanMapper.map(roleEntity, Role.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public User createCustomerUser(String identifier, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdentifier(identifier);
        this.userEntityMapper.insert(userEntity); // 需要生成userId

        userEntity = addNativeAuthenticationMethod(userEntity, identifier, password);

        this.userEntityMapper.updateById(userEntity); // verified字段有更新

        RoleEntity customerRoleEntity = this.roleService.getCustomerRole();
        UserRoleJoinEntity userRoleJoinEntity = new UserRoleJoinEntity();
        userRoleJoinEntity.setUserId(userEntity.getId());
        userRoleJoinEntity.setRoleId(customerRoleEntity.getId());
        this.userRoleJoinEntityMapper.insert(userRoleJoinEntity);

        return BeanMapper.map(userEntity, User.class);
    }

    public UserEntity addNativeAuthenticationMethod(UserEntity userEntity, String identifier, String password) {
        AuthenticationMethodEntity authenticationMethodEntity = new AuthenticationMethodEntity();
        authenticationMethodEntity.setExternal(false); // native
        if (this.configService.getAuthOptions().isRequireVerification()) {
            authenticationMethodEntity.setVerificationToken(
                    this.verificationTokenGenerator.generateVerificationToken());
            userEntity.setVerified(false);
        } else {
            userEntity.setVerified(true);
        }
        if (password != null) {
            authenticationMethodEntity.setPasswordHash(this.passwordEncoder.encode(password));
        } else {
            authenticationMethodEntity.setPasswordHash("");
        }
        authenticationMethodEntity.setIdentifier(identifier);
        authenticationMethodEntity.setUserId(userEntity.getId());
        this.authenticationMethodEntityMapper.insert(authenticationMethodEntity);
        return userEntity;
    }

    @Transactional
    public User createAdminUser(String identifier, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdentifier(identifier);
        userEntity.setVerified(true);
        this.userEntityMapper.insert(userEntity); // 需要生成userId
        AuthenticationMethodEntity authenticationMethodEntity = new AuthenticationMethodEntity();
        authenticationMethodEntity.setExternal(false); // native
        authenticationMethodEntity.setIdentifier(identifier);
        authenticationMethodEntity.setPasswordHash(this.passwordEncoder.encode(password));
        authenticationMethodEntity.setUserId(userEntity.getId());
        this.authenticationMethodEntityMapper.insert(authenticationMethodEntity);
        return BeanMapper.map(userEntity, User.class);
    }

    public void softDelete(Long userId) {
        UserEntity userEntity = ServiceHelper.getEntityOrThrow(this.userEntityMapper, userId);
        userEntity.setDeletedAt(new Date());
        this.userEntityMapper.updateById(userEntity);
    }

    @Transactional
    public User setVerificationToken(Long userId) {
        UserEntity userEntity = ServiceHelper.getEntityOrThrow(this.userEntityMapper, userId);
        AuthenticationMethodEntity nativeAuthMethodEntity = getNativeAuthMethodEntityByUserId(userId);
        nativeAuthMethodEntity.setVerificationToken(this.verificationTokenGenerator.generateVerificationToken());
        this.authenticationMethodEntityMapper.updateById(nativeAuthMethodEntity);
        userEntity.setVerified(false);
        this.userEntityMapper.updateById(userEntity);
        return BeanMapper.map(userEntity, User.class);
    }

    @Transactional
    public User verifyUserByToken(String verificationToken, String password) {
        QueryWrapper<AuthenticationMethodEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthenticationMethodEntity::getVerificationToken, verificationToken);
        AuthenticationMethodEntity authMethodEntity =
                this.authenticationMethodEntityMapper.selectOne(queryWrapper);
        if (authMethodEntity == null) return null;

        if (!this.verificationTokenGenerator.verifyVerificationToken(verificationToken)) {
            throw new VerificationTokenException(ErrorCode.EXPIRED_VERIFICATION_TOKEN);
        }

        AuthenticationMethodEntity nativeAuthMethod =
                this.getNativeAuthMethodEntityByUserId(authMethodEntity.getUserId());
        if (StringUtils.isEmpty(password)) {
            if (StringUtils.isEmpty(nativeAuthMethod.getPasswordHash())) {
                throw new UserInputException("A password must be provided as it was not set during registration");
            }
        } else {
            if (!StringUtils.isEmpty(nativeAuthMethod.getPasswordHash())) {
                throw new UserInputException("A password has already been set during registration");
            }
            nativeAuthMethod.setPasswordHash(this.passwordEncoder.encode(password));
        }
        nativeAuthMethod.setVerificationToken(null);
        this.authenticationMethodEntityMapper.updateById(nativeAuthMethod);

        UserEntity userEntity = this.userEntityMapper.selectById(authMethodEntity.getUserId());
        userEntity.setVerified(true);
        this.userEntityMapper.updateById(userEntity);

        return BeanMapper.map(userEntity, User.class);
    }

    public User setPasswordResetToken(String emailAddress) {
        User user = this.findUserByEmailAddress(emailAddress);
        if (user == null) return null;

        AuthenticationMethodEntity nativeAuthMethod =
                this.getNativeAuthMethodEntityByUserId(user.getId());
        nativeAuthMethod.setPasswordRestToken(this.verificationTokenGenerator.generateVerificationToken());
        this.authenticationMethodEntityMapper.updateById(nativeAuthMethod);
        return user;
    }

    public User resetPasswordByToken(String passwordResetToken, String password) {
        QueryWrapper<AuthenticationMethodEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthenticationMethodEntity::getPasswordRestToken, passwordResetToken);
        AuthenticationMethodEntity authMethodEntity =
                this.authenticationMethodEntityMapper.selectOne(queryWrapper);
        if (authMethodEntity == null) return null;

        if (!this.verificationTokenGenerator.verifyVerificationToken(passwordResetToken)) {
            throw new PasswordResetTokenExpiredException();
        }

        AuthenticationMethodEntity nativeAuthMethod =
                this.getNativeAuthMethodEntityByUserId(authMethodEntity.getUserId());
        nativeAuthMethod.setPasswordHash(this.passwordEncoder.encode(password));
        nativeAuthMethod.setPasswordRestToken(null);

        this.authenticationMethodEntityMapper.updateById(nativeAuthMethod);

        return this.findUserById(authMethodEntity.getUserId());
    }

    public Pair<User, String> changeIdentifierByToken(String token) {
        QueryWrapper<AuthenticationMethodEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthenticationMethodEntity::getIdentifierChangeToken, token);
        AuthenticationMethodEntity authMethodEntity =
                this.authenticationMethodEntityMapper.selectOne(queryWrapper);
        if (authMethodEntity == null) throw new IdentifierChangeTokenException();

        if (!this.verificationTokenGenerator.verifyVerificationToken(token)) {
            throw new IdentifierChangeTokenExpiredException();
        }

        AuthenticationMethodEntity nativeAuthMethod =
                this.getNativeAuthMethodEntityByUserId(authMethodEntity.getUserId());
        String pendingIdentifier = nativeAuthMethod.getPendingIdentifier();
        if (StringUtils.isEmpty(pendingIdentifier)) {
            throw new InternalServerError("Pending identifier is missing");
        }

        UserEntity userEntity = this.userEntityMapper.selectById(authMethodEntity.getUserId());
        final String oldIdentifier = userEntity.getIdentifier();
        userEntity.setIdentifier(pendingIdentifier);
        nativeAuthMethod.setIdentifier(pendingIdentifier);
        nativeAuthMethod.setIdentifierChangeToken(null);
        nativeAuthMethod.setPendingIdentifier(null);

        this.authenticationMethodEntityMapper.updateById(nativeAuthMethod);
        this.userEntityMapper.updateById(userEntity);

        User user = BeanMapper.map(userEntity, User.class);
        return Pair.of(user, oldIdentifier);
    }

    public boolean updatePassword(Long userId, String currentPassword, String newPassword) {
        // 确保用户存在
        ServiceHelper.getEntityOrThrow(this.userEntityMapper, userId);
        AuthenticationMethodEntity nativeAuthMethod =
                this.getNativeAuthMethodEntityByUserId(userId);
        boolean matches = this.passwordEncoder.matches(currentPassword, nativeAuthMethod.getPasswordHash());
        if (!matches) {
            throw new UnauthorizedException();
        }
        nativeAuthMethod.setPasswordHash(this.passwordEncoder.encode(newPassword));
        this.authenticationMethodEntityMapper.updateById(nativeAuthMethod);
        return true;
    }

    public void setIdentifierChangeToken(Long userId) {
        AuthenticationMethodEntity nativeAuthMethod =
                this.getNativeAuthMethodEntityByUserId(userId);
        nativeAuthMethod.setIdentifierChangeToken(this.verificationTokenGenerator.generateVerificationToken());
        this.authenticationMethodEntityMapper.updateById(nativeAuthMethod);
    }

}
