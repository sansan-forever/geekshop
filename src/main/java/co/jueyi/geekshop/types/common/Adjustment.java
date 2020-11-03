package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Adjustment {
    public String adjustmentSource;
    public AdjustmentType type;
    public String description;
    public Integer amount;
}
