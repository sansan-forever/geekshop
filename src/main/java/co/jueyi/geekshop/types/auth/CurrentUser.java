package co.jueyi.geekshop.types.auth;

import co.jueyi.geekshop.types.common.Permission;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CurrentUser {
    public Long id;
    public String identifier;
    public List<Permission> permissions = new ArrayList<>();
}
