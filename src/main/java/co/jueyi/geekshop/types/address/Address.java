package co.jueyi.geekshop.types.address;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Address implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
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
