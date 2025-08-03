package io.implermine.desolo.domain.coupon.model;

public record IssueCouponCommand(
        String userId,
        CouponType couponType
) {
}
