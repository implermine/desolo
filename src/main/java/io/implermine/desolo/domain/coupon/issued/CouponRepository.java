package io.implermine.desolo.domain.coupon.issued;

import java.util.Collection;

public interface CouponRepository {
    Collection<Coupon> findByUserId(String userId);
    Coupon save(Coupon coupon);
    void delete(Coupon coupon);
}
