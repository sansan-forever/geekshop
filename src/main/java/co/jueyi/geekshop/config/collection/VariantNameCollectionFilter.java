/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.collection;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import co.jueyi.geekshop.exception.UserInputException;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class VariantNameCollectionFilter extends CollectionFilter {

    final static String CONFIG_NAME_OPERATOR = "operator";
    final static String CONFIG_NAME_TERM = "term";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String getCode() {
        return "variant-name-filter";
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgs() {
        Map<String, ConfigArgDefinition> args = new HashMap<>();

        ConfigArgDefinition configArgDefinition = new ConfigArgDefinition();
        configArgDefinition.setType("string");
        configArgDefinition.getUi().put("component", "select-form-input");

        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("value", "startsWith");
        arrayNode.add(objectNode);
        objectNode = objectMapper.createObjectNode();
        objectNode.put("value", "endsWith");
        arrayNode.add(objectNode);
        objectNode.put("value", "contains");
        arrayNode.add(objectNode);
        objectNode.put("value", "doesNotContain");
        arrayNode.add(objectNode);
        configArgDefinition.getUi().put("options", arrayNode);

        args.put(CONFIG_NAME_OPERATOR, configArgDefinition);

        configArgDefinition = new ConfigArgDefinition();
        configArgDefinition.setType("string");
        args.put(CONFIG_NAME_TERM, configArgDefinition);

        return args;
    }

    @Override
    public String getDescription() {
        return "Filter by ProductVariant name";
    }

    @Override
    public QueryWrapper<ProductVariantEntity> apply(
            ConfigArgValues configArgValues,
            QueryWrapper<ProductVariantEntity> resultQueryWrapper) {
        String operator = configArgValues.getString(CONFIG_NAME_OPERATOR);
        String term = configArgValues.getString(CONFIG_NAME_TERM);
        if (StringUtils.isEmpty(operator) || StringUtils.isEmpty(term)) {
            throw new UserInputException("Either operator or term is empty");
        };
        switch (operator) {
            case "contains":
                resultQueryWrapper.lambda().like(ProductVariantEntity::getName, term);
                break;
            case "doesNotContain":
                resultQueryWrapper.lambda().notLike(ProductVariantEntity::getName, term);
                break;
            case "startsWith":
                resultQueryWrapper.lambda().likeLeft(ProductVariantEntity::getName, term);
                break;
            case "endsWith":
                resultQueryWrapper.lambda().likeRight(ProductVariantEntity::getName, term);
                break;
            default:
                throw new UserInputException("'" + operator + "' is not a valid operator");
        }
        return resultQueryWrapper;
    }

}
