package io.implermine.desolo.domain.coupon.issued;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.implermine.desolo.support.Delay.*;


@Repository
public class InMemoryCouponRepository implements CouponRepository{

    //userId to Coupons
    private final Map<String, List<Coupon>> coupons = new ConcurrentHashMap<>();

    @Override
    public Collection<Coupon> findByUserId(String userId) {
        delay();
        return coupons.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public Coupon save(Coupon coupon) {
        delay();
        coupons.computeIfAbsent(coupon.userId(), k -> new ArrayList<>()).add(coupon);
        return coupon;
    }

    @Override
    public void delete(Coupon coupon) {
        delay();
        final Collection<Coupon> coupons = this.coupons.get(coupon.userId());
        coupons.remove(coupon);
    }
}
