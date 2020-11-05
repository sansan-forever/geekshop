package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class NumberOperators {
    private Float eq;
    private Float lt;
    private Float lte;
    private Float gt;
    private Float gte;
    private NumberRange between;
}
