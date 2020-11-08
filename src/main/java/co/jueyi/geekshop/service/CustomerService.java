package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.common.utils.NormalizeUtil;
import co.jueyi.geekshop.entity.*;
import co.jueyi.geekshop.eventbus.events.AccountRegistrationEvent;
import co.jueyi.geekshop.exception.UserInputException;
import co.jueyi.geekshop.mapper.*;
import co.jueyi.geekshop.service.args.CreateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.common.CreateCustomerInput;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerGroup;
import co.jueyi.geekshop.types.history.HistoryEntryType;
import co.jueyi.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerEntityMapper customerEntityMapper;
    private final AddressEntityMapper addressEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final CustomerGroupEntityMapper customerGroupEntityMapper;
    private final CustomerGroupJoinEntityMapper customerGroupJoinEntityMapper;
    private final UserService userService;
    private final HistoryService historyService;
    private final EventBus eventBus;

    public Customer findOne(Long id) {
        QueryWrapper<CustomerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerEntity::getId, id).isNull(CustomerEntity::getDeletedAt);
        CustomerEntity customerEntity = this.customerEntityMapper.selectOne(queryWrapper);
        return BeanMapper.map(customerEntity, Customer.class);
    }

    public Customer findOneByUserId(Long userId) {
        QueryWrapper<CustomerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerEntity::getUserId, userId).isNull(CustomerEntity::getDeletedAt);
        CustomerEntity customerEntity = this.customerEntityMapper.selectOne(queryWrapper);
        return BeanMapper.map(customerEntity, Customer.class);
    }

    public List<Address> findAddressesByCustomerId(RequestContext ctx, Long customerId) {
        QueryWrapper<AddressEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AddressEntity::getCustomerId, customerId);
        List<AddressEntity> addressEntityList = this.addressEntityMapper.selectList(queryWrapper);
        return addressEntityList.stream()
                .map(addressEntity -> BeanMapper.map(addressEntity, Address.class)).collect(Collectors.toList());
    }

    public List<CustomerGroup> getCustomerGroups(Long customerId) {
        QueryWrapper<CustomerGroupJoinEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerGroupJoinEntity::getCustomerId, customerId);
        List<CustomerGroupJoinEntity> customerGroupJoinEntityList =
                this.customerGroupJoinEntityMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(customerGroupJoinEntityList)) return new ArrayList<>();

        List<Long> groupIds = customerGroupJoinEntityList.stream()
                .map(CustomerGroupJoinEntity::getGroupId).collect(Collectors.toList());

        QueryWrapper<CustomerGroupEntity> customerGroupQueryWrapper = new QueryWrapper<>();
        customerGroupQueryWrapper.lambda().in(CustomerGroupEntity::getId, groupIds);
        List<CustomerGroupEntity> customerGroupEntityList =
                this.customerGroupEntityMapper.selectList(customerGroupQueryWrapper);
        return customerGroupEntityList.stream()
                .map(customerGroupEntity -> BeanMapper.map(customerGroupEntity, CustomerGroup.class))
                .collect(Collectors.toList());
    }

    public Customer create(RequestContext ctx, CreateCustomerInput input, String password) {
        input.setEmailAddress(NormalizeUtil.normalizeEmailAddress(input.getEmailAddress()));
        CustomerEntity newCustomerEntity = BeanMapper.map(input, CustomerEntity.class);

        QueryWrapper<CustomerEntity> customerEntityQueryWrapper = new QueryWrapper<>();
        customerEntityQueryWrapper.lambda().eq(CustomerEntity::getEmailAddress, input.getEmailAddress())
                .isNull(CustomerEntity::getDeletedAt);
        CustomerEntity existingCustomerEntity = this.customerEntityMapper.selectOne(customerEntityQueryWrapper);

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.lambda().eq(UserEntity::getIdentifier, input.getEmailAddress())
                .isNull(UserEntity::getDeletedAt);
        UserEntity existingUserEntity = this.userEntityMapper.selectOne(userEntityQueryWrapper);

        if (existingCustomerEntity != null || existingUserEntity != null) {
            throw new UserInputException("The email address must be unique");
        }

        User user = this.userService.createCustomerUser(input.getEmailAddress(), password);

        if (!StringUtils.isEmpty(password)) {
            String verificationToken = this.userService.getNativeAuthMethodEntityByUserId(user.getId())
                    .getVerificationToken();
            if (!StringUtils.isEmpty(verificationToken)) {
                user = this.userService.verifyUserByToken(verificationToken, null);
            }
        } else {
            this.eventBus.post(new AccountRegistrationEvent(ctx, user));
        }
        this.customerEntityMapper.insert(newCustomerEntity);

        CreateCustomerHistoryEntryArgs createCustomerHistoryEntryArgs = new CreateCustomerHistoryEntryArgs();
        createCustomerHistoryEntryArgs.setCtx(ctx);
        createCustomerHistoryEntryArgs.setCustomerId(newCustomerEntity.getId());
        createCustomerHistoryEntryArgs.setType(HistoryEntryType.CUSTOMER_REGISTERED);
        createCustomerHistoryEntryArgs.setData(ImmutableMap.of("strategy", Constant.NATIVE_AUTH_STRATEGY_NAME));
        this.historyService.createHistoryEntryForCustomer(createCustomerHistoryEntryArgs, false);

        if (BooleanUtils.isTrue(user.getVerified())) {
            createCustomerHistoryEntryArgs = new CreateCustomerHistoryEntryArgs();
            createCustomerHistoryEntryArgs.setCtx(ctx);
            createCustomerHistoryEntryArgs.setCustomerId(newCustomerEntity.getId());
            createCustomerHistoryEntryArgs.setType(HistoryEntryType.CUSTOMER_VERIFIED);
            createCustomerHistoryEntryArgs.setData(ImmutableMap.of("strategy", Constant.NATIVE_AUTH_STRATEGY_NAME));
            this.historyService.createHistoryEntryForCustomer(createCustomerHistoryEntryArgs, false);
        }

        return BeanMapper.map(newCustomerEntity, Customer.class);
    }

}
