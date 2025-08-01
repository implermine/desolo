package io.implermine.desolo.domain.coupon.policy;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class PercentDiscountPolicy implements CouponDiscountPolicy {
    private final BigDecimal discountPercent;
    private final BigDecimal minPrice;
    private final BigDecimal maxDiscount;

    public PercentDiscountPolicy(int discountPercent, long minPrice, long maxDiscount) {
        if(discountPercent < 0 || discountPercent > 100) {
            log.error("discountPercent must be between 0 and 100");
            throw new IllegalArgumentException("discountPercent must be between 0 and 100");
        }
        this.discountPercent = BigDecimal.valueOf(discountPercent);
        this.minPrice = BigDecimal.valueOf(minPrice);
        this.maxDiscount = BigDecimal.valueOf(maxDiscount);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal price) {
        BigDecimal discountPrice  = price.multiply(discountPercent);
        return discountPrice.min(maxDiscount);
    }

    @Override
    public boolean isDiscountable(BigDecimal price) {
        return price.compareTo(minPrice) >= 0;
    }
}
