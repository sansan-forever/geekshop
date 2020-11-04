package co.jueyi.geekshop.types.customer;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateCustomerInput {
    public Long id;
    public String title;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;
}
