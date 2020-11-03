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
    private Date createAt;
    private Date updateAt;
    private String identifier;
    private String passwordHash;
    private Boolean verified;
    private List<Role> roles = new ArrayList<>();
    private String lastLogin;

}
