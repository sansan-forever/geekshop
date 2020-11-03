package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class NumberOperators {
    public Float eq;
    public Float lt;
    public Float lte;
    public Float gt;
    public Float gte;
    public NumberRange between;
}
