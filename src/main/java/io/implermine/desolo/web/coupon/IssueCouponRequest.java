package io.implermine.desolo.web.coupon;

import io.implermine.desolo.domain.coupon.CouponType;
import io.implermine.desolo.domain.coupon.IssueCouponCommand;

public record IssueCouponRequest(CouponType couponType) {

    public IssueCouponCommand toCommand(String userId) {
        return new IssueCouponCommand(userId, couponType);
    }
}
