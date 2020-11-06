package co.jueyi.geekshop.types.role;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RoleListOptions {
    @Min(1)
    private Integer currentPage;
    @Min(1)
    private Integer pageSize;
    private RoleSortParameter sort;
    private RoleFilterParameter filter;
}
