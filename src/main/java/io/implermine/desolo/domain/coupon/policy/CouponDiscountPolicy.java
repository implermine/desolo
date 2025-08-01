package io.implermine.desolo.domain.coupon.policy;

import java.math.BigDecimal;

//behavior
public interface CouponDiscountPolicy {
    BigDecimal calculateDiscount(BigDecimal price); //얼마나 할인되어야 하는가?
    boolean isDiscountable(BigDecimal price);
}
