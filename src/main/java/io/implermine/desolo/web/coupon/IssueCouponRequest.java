package io.implermine.desolo.web.coupon;

import io.implermine.desolo.domain.coupon.model.CouponType;
import io.implermine.desolo.domain.coupon.model.IssueCouponCommand;

public record IssueCouponRequest(CouponType couponType) {

    public IssueCouponCommand toCommand(String userId) {
        return new IssueCouponCommand(userId, couponType);
    }
}
