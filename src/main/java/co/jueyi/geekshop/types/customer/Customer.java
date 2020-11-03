package co.jueyi.geekshop.types.customer;

import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.order.OrderList;
import co.jueyi.geekshop.types.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Customer implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String title;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;
    public List<Address> addresses = new ArrayList<>();
    public OrderList orders;
    public User user;
}
