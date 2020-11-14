/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.common;

/**
 * Created on Nov, 2020 by @author bobo
 */
public enum Permission {
    /**
     * The Authenticated role means simply that the user is logged in
     */
    Authenticated,
    /**
     * Super Admin can perform the most sensitive tasks
     */
    SuperAdmin,
    /**
     * Owner means the user owns this entity, e.g. Customer's own Order
     */
    Owner,
    /**
     * Public means any unauthenticated user may perform the operation
     */
    Public,

    CreateCatalog,
    ReadCatalog,
    UpdateCatalog,
    DeleteCatalog,

    CreateCustomer,
    ReadCustomer,
    UpdateCustomer,
    DeleteCustomer,

    CreateAdministrator,
    ReadAdministrator,
    UpdateAdministrator,
    DeleteAdministrator,

    CreateOrder,
    ReadOrder,
    UpdateOrder,
    DeleteOrder,

    CreatePromotion,
    ReadPromotion,
    UpdatePromotion,
    DeletePromotion,

    CreateSettings,
    ReadSettings,
    UpdateSettings,
    DeleteSettings
}
