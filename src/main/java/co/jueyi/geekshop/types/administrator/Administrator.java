package co.jueyi.geekshop.types.administrator;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.user.User;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Administrator implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String firstName;
    public String lastName;
    public String emailAddress;
    public User user;
}
