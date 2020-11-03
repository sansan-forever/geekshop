package co.jueyi.geekshop.types.stock;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.product.ProductVariant;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
public interface StockMovement extends Node {
    Date getCreatedAt();
    void setCreatedAt(Date createdAt);
    Date getUpdatedAt();
    void setUpdatedAt(Date updatedAt);
    ProductVariant getProductVariant();
    void setProductVariant(ProductVariant productVariant);
    StockMovementType getType();
    void setType(StockMovementType type);
    Integer getQuantity();
    void setQuantity(Integer quantity);
}
