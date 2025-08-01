package io.implermine.desolo.web.coupon;

import io.implermine.desolo.domain.coupon.UseCouponCommand;

public record UseCouponRequest(long price)  {

    public UseCouponCommand toCommand(String userId) {
        return new UseCouponCommand(userId, price);
    }
}
