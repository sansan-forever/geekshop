/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception;

/**
 * This exception is thrown when the coupon code is associated with a Promotion that has expired.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class CouponCodeExpiredException extends AbstractGraphqlException {
    public CouponCodeExpiredException(String couponCode) {
        super(String.format("Coupon code \"{ %s }\" has expired", couponCode),
                ErrorCode.COUPON_CODE_EXPIRED);
    }
}
