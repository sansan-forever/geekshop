package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateCustomerInput {
    private String title;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
}
