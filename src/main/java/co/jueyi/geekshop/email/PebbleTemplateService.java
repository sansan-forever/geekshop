/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.email;

import co.jueyi.geekshop.exception.email.TemplateException;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class PebbleTemplateService {
    private final PebbleEngine pebbleEngine;

    public String mergeTemplateIntoString(@NonNull String templateName, @NonNull Map<String, Object> model)
            throws TemplateException {
        try {
            PebbleTemplate template = pebbleEngine.getTemplate(templateName);
            Writer writer = new StringWriter();
            template.evaluate(writer, model);
            return writer.toString();
        } catch (Exception ex) {
            throw new TemplateException(ex);
        }
    }
}
