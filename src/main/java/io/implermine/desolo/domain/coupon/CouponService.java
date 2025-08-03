package io.implermine.desolo.domain.coupon;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.implermine.desolo.domain.coupon.issued.Coupon;
import io.implermine.desolo.domain.coupon.issued.CouponRepository;
import io.implermine.desolo.domain.coupon.issued.InMemoryCouponRepository;
import io.implermine.desolo.domain.coupon.model.*;
import io.implermine.desolo.domain.coupon.policy.CouponPolicyRepository;
import io.implermine.desolo.domain.coupon.policy.InMemoryCouponPolicyRepository;
import io.implermine.desolo.domain.support.PriceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;
    private final PriceValidator customPriceValidator;
    private final Cache<CouponKey, Lock> couponLock;

    @Autowired
    public CouponService(
            InMemoryCouponRepository couponRepository,
            InMemoryCouponPolicyRepository couponPolicyRepository
    ) {
        this.couponRepository = couponRepository;
        this.couponPolicyRepository = couponPolicyRepository;

        // 쿠폰을 사용할 가격을 검증하는 custom 가능한 validator
        this.customPriceValidator = PriceValidator.isPositive()
                .and(PriceValidator.isBiggerThan(10))
                .and(PriceValidator.isTenWonUnit())
                .ifInvalidThrow(() -> new RuntimeException("쿠폰을 사용하는것에 있어 유효하지 않은 결제 금액입니다."));

        // 쿠폰을 발급하는 행위를 CouponKey마다 unique하도록 제한
        this.couponLock = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    public List<Coupon> findByUserId(String userId) {
        return new ArrayList<>(couponRepository.findByUserId(userId));
    }

    public void issueCoupon(IssueCouponCommand command) {

        CouponKey key = CouponKey.fromCommand(command);
        Lock lock = couponLock.get(key, k -> new ReentrantLock());

        Objects.requireNonNull(lock);
        if (lock.tryLock()) {
            try {
                if(existsByUserIdAndCouponType(command.userId(),command.couponType())){
                    throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
                }

                Coupon coupon = new Coupon(command.userId(), command.couponType());
                couponRepository.save(coupon);
            } finally {
                lock.unlock();
            }
        } else {
            throw new IllegalStateException("현재 다른 요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    //TBD
    public UseCouponResult useBestCoupon(UseBestCouponCommand useBestCouponCommand) {
        customPriceValidator.validate(useBestCouponCommand.price());

        Optional<CouponType> bestCouponType = findBestCouponType(useBestCouponCommand);
        if (bestCouponType.isEmpty()) {
            return new UseCouponResult(useBestCouponCommand.userId(), useBestCouponCommand.price()); // 그대로 반환
        }

        UseCouponCommand useCouponCommand = UseCouponCommand.from(useBestCouponCommand, bestCouponType.get());
        CouponKey key = CouponKey.fromCommand(useCouponCommand);
        Lock lock = couponLock.get(key, k -> new ReentrantLock());

        if (lock.tryLock()) {
            try{
                boolean b = couponRepository.findByUserId(useCouponCommand.userId())
                        .stream().anyMatch(coupon -> coupon.couponType().equals(bestCouponType.get()));
                if(!b){
                    throw new IllegalStateException("이미 사용된 쿠폰 입니다.");
                }

                couponRepository.delete(new Coupon(useCouponCommand.userId(), bestCouponType.get()));

            }finally {
                lock.unlock();
            }
        } else {
            throw new IllegalStateException("현재 다른 요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");

        }


        return null;
    }

    private Optional<CouponType> findBestCouponType(UseBestCouponCommand command) {
        Collection<Coupon> coupons = couponRepository.findByUserId(command.userId());
        BigDecimal price = BigDecimal.valueOf(command.price());

        BigDecimal maxDiscount = BigDecimal.ZERO;
        Coupon bestCoupon = null; // 가장 좋았던 쿠폰을 저장할 변수

        for (Coupon coupon : coupons) {
            BigDecimal currentDiscount = couponPolicyRepository.findByCouponType(coupon.couponType())
                    .map(policy -> policy.calculateDiscount(price))
                    .orElse(BigDecimal.ZERO); // 할인이 없으면 0으로 처리

            if (currentDiscount.compareTo(maxDiscount) > 0) {
                maxDiscount = currentDiscount;
                bestCoupon = coupon; // 할인액이 더 크면, 쿠폰 자체를 교체
            }
        }

        return Optional.ofNullable(bestCoupon).map(Coupon::couponType);
    }


    private boolean existsByUserIdAndCouponType(String userId, CouponType couponType) {
        return couponRepository.findByUserId(userId).stream()
                .anyMatch(coupon -> couponType.equals(coupon.couponType()));
    }
}
