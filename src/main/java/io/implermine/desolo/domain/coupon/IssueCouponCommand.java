package io.implermine.desolo.domain.coupon;

public record IssueCouponCommand(
        String userId,
        CouponType couponType
) {
}
