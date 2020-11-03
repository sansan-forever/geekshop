package co.jueyi.geekshop.types.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ConfigArgDefinition {
    public String name;
    public String type;
    public Boolean list;
    public String label;
    public String description;
    public Map<String, String> ui = new HashMap<>();
}
