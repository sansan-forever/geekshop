package co.jueyi.geekshop.types.customer;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AddNoteToCustomerInput {
    public Long id;
    public String node;
    public Boolean isPublic;
}
