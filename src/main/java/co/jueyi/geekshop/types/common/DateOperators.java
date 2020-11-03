package co.jueyi.geekshop.types.common;

import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class DateOperators {
    public Date eq;
    public Date before;
    public Date after;
    public DateRange between;
}
