package co.jueyi.geekshop.types.user;

import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.role.Role;
import lombok.Data;

import java.util.*;

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
    // 内部助手方法，GraphQL不可见
    public List<Permission> getPermissions() {
        Set<Permission> permissionSet = new HashSet<>();
        for (Role role : this.getRoles()) {
            permissionSet.addAll(role.getPermissions());
        }
        return new ArrayList(permissionSet);
    }
}
