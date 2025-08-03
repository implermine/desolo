package io.implermine.desolo.domain.coupon.issued;

import io.implermine.desolo.domain.coupon.model.CouponType;

public record Coupon(
        String userId,
        CouponType couponType) {

    public Coupon {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }

}
