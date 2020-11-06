package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.AdministratorEntity;
import co.jueyi.geekshop.entity.RoleEntity;
import co.jueyi.geekshop.entity.UserEntity;
import co.jueyi.geekshop.entity.UserRoleJoinEntity;
import co.jueyi.geekshop.exception.EntityNotFoundException;
import co.jueyi.geekshop.mapper.AdministratorEntityMapper;
import co.jueyi.geekshop.mapper.UserEntityMapper;
import co.jueyi.geekshop.mapper.UserRoleJoinEntityMapper;
import co.jueyi.geekshop.options.SuperadminCredentials;
import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.administrator.CreateAdministratorInput;
import co.jueyi.geekshop.types.role.Role;
import co.jueyi.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class AdministratorService {
    private final ConfigService configService;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;
    private final AdministratorEntityMapper administratorEntityMapper;
    private final UserRoleJoinEntityMapper userRoleJoinEntityMapper;
    private final UserService userService;
    private final RoleService roleService;

    @PostConstruct
    public void initAdministrators() {
        this.ensureSuperAdminExists();
    }

    public Administrator findOne(Long administratorId) {
        AdministratorEntity administratorEntity = this.administratorEntityMapper.selectById(administratorId);
        if (administratorEntity == null) return null;
        return BeanMapper.map(administratorEntity, Administrator.class);
    }

    @Transactional
    public Administrator create(CreateAdministratorInput input) {
        AdministratorEntity administratorEntity = BeanMapper.map(input, AdministratorEntity.class);
        User user = this.userService.createAdminUser(input.getEmailAddress(), input.getPassword());
        administratorEntity.setUserId(user.getId());
        this.administratorEntityMapper.insert(administratorEntity);
        input.getRoleIds().forEach(roleId -> this.assignRole(administratorEntity.getId(), roleId));
        return BeanMapper.map(administratorEntity, Administrator.class);
    }

    /**
     * Assigns a Role to the Administrator's User entity.
     */
    public Administrator assignRole(Long administratorId, Long roleId) {
        AdministratorEntity administratorEntity = this.administratorEntityMapper.selectById(administratorId);
        if (administratorEntity == null) throw new EntityNotFoundException("Administrator", administratorId);
        Role role = this.roleService.findOne(roleId);
        if (role == null) throw new EntityNotFoundException("Role", roleId);
        QueryWrapper<UserRoleJoinEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleJoinEntity::getRoleId, roleId)
                .eq(UserRoleJoinEntity::getUserId, administratorEntity.getUserId());
        if (this.userRoleJoinEntityMapper.selectCount(queryWrapper) == 0) { // 确保不存在
            UserRoleJoinEntity userRoleJoinEntity = new UserRoleJoinEntity();
            userRoleJoinEntity.setRoleId(roleId);
            userRoleJoinEntity.setUserId(administratorEntity.getUserId());
            this.userRoleJoinEntityMapper.insert(userRoleJoinEntity);
        }
        return BeanMapper.map(administratorEntity, Administrator.class);
    }

    /**
     * There must always exists a SuperAdmin, otherwise full administration via API will
     * no longer be possible.
     */
    private void ensureSuperAdminExists() {
        SuperadminCredentials superadminCredentials =
                this.configService.getAuthOptions().getSuperadminCredentials();

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getIdentifier, superadminCredentials.getIdentifier());
        UserEntity superAdminUserEntity = userEntityMapper.selectOne(queryWrapper);
        if (superAdminUserEntity == null) {
            RoleEntity superAdminRole = this.roleService.getSuperAdminRoleEntity();
            CreateAdministratorInput input = new CreateAdministratorInput();
            input.setEmailAddress(superadminCredentials.getIdentifier());
            input.setPassword(superadminCredentials.getPassword());
            input.setFirstName("Super");
            input.setLastName("Admin");
            input.getRoleIds().add(superAdminRole.getId());
            this.create(input);
        }
    }
}
