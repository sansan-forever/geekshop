package co.jueyi.geekshop.types.user;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AuthenticationMethod implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String strategy;
}
