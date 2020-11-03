package co.jueyi.geekshop.types.payment;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.*;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Payment implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String method;
    public Integer amount;
    public String state;
    public String transactionId;
    public String errorMessage;
    public List<Refund> refunds = new ArrayList<>();
    public Map<String, String> metadata = new HashMap<>();
}
