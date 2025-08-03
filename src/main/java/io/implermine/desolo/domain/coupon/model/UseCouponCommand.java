package io.implermine.desolo.domain.coupon.model;

public record UseCouponCommand(
        String userId,
        CouponType couponType,
        long price
) {

    public static UseCouponCommand from(UseBestCouponCommand command, CouponType bestCouponType) {
        return new UseCouponCommand(command.userId(), bestCouponType, command.price());
    }
}
