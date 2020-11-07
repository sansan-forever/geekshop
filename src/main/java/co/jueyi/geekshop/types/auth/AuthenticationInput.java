package co.jueyi.geekshop.types.auth;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AuthenticationInput {
    private String method;
    private Map<String, String> data = new HashMap<>();
}
