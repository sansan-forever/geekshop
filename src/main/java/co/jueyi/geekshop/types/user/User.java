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
    public Date createAt;
    public Date updateAt;
    public String identifier;
    public  String passwordHash;
    public Boolean verified;
    public List<Role> roles = new ArrayList<>();
    public String lastLogin;
}
