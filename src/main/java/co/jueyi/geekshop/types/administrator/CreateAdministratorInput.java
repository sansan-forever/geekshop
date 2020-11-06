package co.jueyi.geekshop.types.administrator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateAdministratorInput {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private List<Long> roleIds = new ArrayList<>();
}
