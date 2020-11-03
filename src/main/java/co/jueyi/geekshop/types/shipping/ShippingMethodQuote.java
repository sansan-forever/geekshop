package co.jueyi.geekshop.types.shipping;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ShippingMethodQuote {
    public Long id;
    public Integer price;
    public String description;
    public Map<String, String> metadata = new HashMap<>();
}
