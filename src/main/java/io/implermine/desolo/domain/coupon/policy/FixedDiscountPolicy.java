package io.implermine.desolo.domain.coupon.policy;


import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class FixedDiscountPolicy implements CouponDiscountPolicy {
    private final BigDecimal discount;
    private final BigDecimal minPrice;

    public FixedDiscountPolicy(long discount, long minPrice) {
        this.discount = BigDecimal.valueOf(discount);
        this.minPrice = BigDecimal.valueOf(minPrice);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal price) {
        return discount;
    }

    @Override
    public boolean isDiscountable(BigDecimal price) {
        return price.compareTo(minPrice) >= 0;
    }
}
