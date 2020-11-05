package co.jueyi.geekshop.types.order;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateOrderNoteInput {
    private Long id;
    private String note;
    private Boolean isPublic;
}
