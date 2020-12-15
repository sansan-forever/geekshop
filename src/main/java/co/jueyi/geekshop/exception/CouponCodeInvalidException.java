/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception;

/**
 * This exception is thrown when the coupon code is not associated with any active Promotion.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class CouponCodeInvalidException extends AbstractGraphqlException {
    public CouponCodeInvalidException(String couponCode) {
        super(String.format("Coupon code \"{ %s }\" is not valid", couponCode),
                ErrorCode.COUPON_CODE_INVALID);
    }
}
