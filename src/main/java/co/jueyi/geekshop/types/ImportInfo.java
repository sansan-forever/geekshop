package co.jueyi.geekshop.types;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ImportInfo {
    public List<String> errors = new ArrayList<>();
    public Integer processed;
    public Integer imported;
}
