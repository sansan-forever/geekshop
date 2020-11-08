package co.jueyi.geekshop.types.customer;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RegisterCustomerInput {
    private String emailAddress;
    private String title;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}
