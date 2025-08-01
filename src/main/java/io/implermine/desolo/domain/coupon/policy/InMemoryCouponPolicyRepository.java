package io.implermine.desolo.domain.coupon.policy;

import io.implermine.desolo.domain.coupon.CouponType;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryCouponPolicyRepository implements CouponPolicyRepository{

    private final Map<CouponType, CouponDiscountPolicy> policies = new EnumMap<>(CouponType.class);

    public InMemoryCouponPolicyRepository() {
        //웰컴 키트 정책: 1000원 할인, 최소 구매 금액 10000원
        policies.put(CouponType.WELCOME_KIT, new FixedDiscountPolicy(1000,10000));

        //여름 세일 정책: 15% 할인, 최소 구매 금액 20000원, 최대 5000원 할인
        policies.put(CouponType.SUMMER_SALE, new PercentDiscountPolicy(15, 20000, 5000));
    }

    @Override
    public Optional<CouponDiscountPolicy> findByCouponType(CouponType couponType) {
        return Optional.ofNullable(policies.get(couponType));
    }
}
