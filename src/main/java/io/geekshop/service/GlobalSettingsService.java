/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.GlobalSettingsEntity;
import io.geekshop.exception.InternalServerError;
import io.geekshop.mapper.GlobalSettingsEntityMapper;
import io.geekshop.types.settings.UpdateGlobalSettingsInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class GlobalSettingsService {
    private final GlobalSettingsEntityMapper globalSettingsEntityMapper;

    @PostConstruct
    void initGlobalSettings() {
        try {
            this.getSettings();
        } catch (Exception ex) {
            GlobalSettingsEntity globalSettingsEntity = new GlobalSettingsEntity();
            this.globalSettingsEntityMapper.insert(globalSettingsEntity);
        }
    }

    public GlobalSettingsEntity getSettings() {
        GlobalSettingsEntity globalSettingsEntity = this.globalSettingsEntityMapper.selectOne(null);
        if (globalSettingsEntity == null) {
            throw new InternalServerError("Global settings not found");
        }
        return globalSettingsEntity;
    }

    public GlobalSettingsEntity updateSettings(UpdateGlobalSettingsInput input) {
        GlobalSettingsEntity globalSettingsEntity = getSettings();
        BeanMapper.patch(input, globalSettingsEntity);
        this.globalSettingsEntityMapper.updateById(globalSettingsEntity);
        return globalSettingsEntity;
    }

}