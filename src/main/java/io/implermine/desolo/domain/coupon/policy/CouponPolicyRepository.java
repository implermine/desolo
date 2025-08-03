package io.implermine.desolo.domain.coupon.policy;

import io.implermine.desolo.domain.coupon.model.CouponType;

import java.util.Optional;

public interface CouponPolicyRepository {

    Optional<CouponDiscountPolicy> findByCouponType(CouponType couponType);
}
