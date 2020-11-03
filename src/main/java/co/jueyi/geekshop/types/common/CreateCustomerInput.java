package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateCustomerInput {
    public String title;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;
}
