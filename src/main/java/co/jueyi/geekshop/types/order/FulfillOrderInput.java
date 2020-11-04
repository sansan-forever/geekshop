package co.jueyi.geekshop.types.order;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FulfillOrderInput {
    public List<OrderLineInput> lines = new ArrayList<>();
    public String method;
    public String trackingCode;
}
