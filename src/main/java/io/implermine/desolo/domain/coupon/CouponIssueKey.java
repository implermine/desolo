package io.implermine.desolo.domain.coupon;

/**
 * userId + couponType needs to be unique
 */
public record CouponIssueKey(String userId, CouponType couponType) {

    public static CouponIssueKey fromCommand(IssueCouponCommand command) {
        return new CouponIssueKey(command.userId(), command.couponType());
    }
}
