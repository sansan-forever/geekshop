package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateAddressInput {
    public String fullName;
    public String company;
    public String streetLine1;
    public String streetLine2;
    public String city;
    public String province;
    public String postalCode;
    public String phoneNumber;
    public Boolean defaultShippingAddress;
    public Boolean defaultBillingAddress;
}
