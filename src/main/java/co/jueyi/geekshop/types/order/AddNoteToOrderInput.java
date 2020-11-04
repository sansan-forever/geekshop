package co.jueyi.geekshop.types.order;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AddNoteToOrderInput {
    public Long id;
    public String note;
    public Boolean isPublic;
}
