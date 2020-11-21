/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.GlobalSettingsEntity;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.mapper.GlobalSettingsEntityMapper;
import co.jueyi.geekshop.types.settings.UpdateGlobalSettingsInput;
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