package io.implermine.desolo.domain.coupon;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.implermine.desolo.domain.coupon.issued.Coupon;
import io.implermine.desolo.domain.coupon.issued.CouponRepository;
import io.implermine.desolo.domain.coupon.issued.InMemoryCouponRepository;
import io.implermine.desolo.domain.support.PriceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final PriceValidator customPriceValidator;
    private final Cache<CouponIssueKey, Lock> issueCouponLock;

    @Autowired
    public CouponService(InMemoryCouponRepository couponRepository) {
        this.couponRepository = couponRepository;

        // 쿠폰을 사용할 가격을 검증하는 custom 가능한 validator
        this.customPriceValidator = PriceValidator.isPositive()
                .and(PriceValidator.isBiggerThan(10))
                .and(PriceValidator.isTenWonUnit())
                .ifInvalidThrow(() -> new RuntimeException("쿠폰을 사용하는것에 있어 유효하지 않은 결제 금액입니다."));

        // 쿠폰을 발급하는 행위를 CouponIssueKey마다 unique하도록 제한
        this.issueCouponLock = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    public List<Coupon> findByUserId(String userId) {
        return new ArrayList<>(couponRepository.findByUserId(userId));
    }

    public void issueCoupon(IssueCouponCommand command) {

        CouponIssueKey key = CouponIssueKey.fromCommand(command);
        Lock lock = issueCouponLock.get(key, k-> new ReentrantLock());

        Objects.requireNonNull(lock);
        if(lock.tryLock()){
            try{
                //1. 락 유효시간이 짧다면 고려
                //if(existsByUserIdAndCouponType(command.userId(),command.couponType())){
                //    throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
                //}

                Coupon coupon = new Coupon(command.userId(), command.couponType());
                couponRepository.save(coupon);
            }finally {
                lock.unlock();
            }
        }else{
            throw new IllegalStateException("현재 다른 요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    //TBD
    public UseCouponResult useCoupon(UseCouponCommand command) {
        customPriceValidator.validate(command.price());
        return null;
    }


    private boolean existsByUserIdAndCouponType(String userId, CouponType couponType) {
        return couponRepository.findByUserId(userId).stream()
                .anyMatch(coupon -> couponType.equals(coupon.couponType()));
    }
}
