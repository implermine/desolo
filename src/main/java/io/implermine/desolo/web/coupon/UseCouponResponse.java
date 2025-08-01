package io.implermine.desolo.web.coupon;

import io.implermine.desolo.domain.coupon.CouponType;
import io.implermine.desolo.domain.coupon.UseCouponResult;

public record UseCouponResponse(
        long originalPrice,
        long discount,
        long netPrice,
        CouponType couponType
) {

    public static UseCouponResponse from(UseCouponResult result) {
        //TBD
        return null;
    }
}
