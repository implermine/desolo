package io.implermine.desolo.web.coupon;

import io.implermine.desolo.domain.coupon.model.UseBestCouponCommand;

public record UseCouponRequest(long price)  {

    public UseBestCouponCommand toCommand(String userId) {
        return new UseBestCouponCommand(userId, price);
    }
}
