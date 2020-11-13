/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.email;

import co.jueyi.geekshop.common.utils.NormalizeUtil;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class FileEmailSender implements EmailSender {
    private final String outputPath;

    public FileEmailSender(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void send(EmailDetails emailDetails) throws Exception {
        String fileName = new Date().toString() + " " + emailDetails.getRecipient() + " " + emailDetails.getSubject();
        fileName = NormalizeUtil.normalizeString(fileName, "-");
        fileName += ".html";

        File file = Paths.get(outputPath, fileName).toFile();
        com.google.common.io.Files.write(emailDetails.getBody().getBytes(), file);
    }

    @PostConstruct
    void init() {
        try {
            Path path = Paths.get(outputPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
