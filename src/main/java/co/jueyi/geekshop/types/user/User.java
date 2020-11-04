package co.jueyi.geekshop.types.user;

import co.jueyi.geekshop.types.role.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class User {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String identifier;
    public Boolean verified;
    public List<Role> roles = new ArrayList<>();
    public Date lastLogin;
    public List<AuthenticationMethod> authenticationMethods = new ArrayList<>();
}
