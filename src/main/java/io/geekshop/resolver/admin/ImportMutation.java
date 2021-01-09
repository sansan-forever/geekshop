/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.custom.security.Allow;
import io.geekshop.data_import.importer.Importer;
import io.geekshop.exception.InternalServerError;
import io.geekshop.types.ImportInfo;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ImportMutation implements GraphQLMutationResolver {
    private final Importer importer;

    @Allow(Permission.SuperAdmin)
    public ImportInfo importProducts(Part csvFile, DataFetchingEnvironment dfe) {
        try {
            InputStream input = csvFile.getInputStream();
            return this.importer.parseAndImport(input);
        } catch (IOException ex) {
            log.error("IO exception during products import", ex);
            throw new InternalServerError("IO exception during products import");
        }
    }
}
