package io.implermine.desolo.domain.coupon.model;

/**
 * userId + couponType needs to be unique
 */
public record CouponKey(String userId, CouponType couponType) {

    public static CouponKey fromCommand(IssueCouponCommand command) {
        return new CouponKey(command.userId(), command.couponType());
    }

    public static CouponKey fromCommand(UseCouponCommand command) {
        return new CouponKey(command.userId(), command.couponType());
    }
}
