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
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String identifier;
    private Boolean verified;
    private List<Role> roles = new ArrayList<>();
    private Date lastLogin;
    private List<AuthenticationMethod> authenticationMethods = new ArrayList<>();
}
