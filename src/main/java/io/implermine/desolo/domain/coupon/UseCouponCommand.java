package io.implermine.desolo.domain.coupon;

public record UseCouponCommand(
        String userId,
        long price
) {
}
