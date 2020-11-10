package co.jueyi.geekshop;

import lombok.Builder;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
@Builder
public class PopulateOptions {
    private Integer productCount;
    private Integer customerCount;
}
