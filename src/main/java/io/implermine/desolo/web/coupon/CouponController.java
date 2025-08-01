package io.implermine.desolo.web.coupon;

import io.implermine.desolo.domain.coupon.CouponService;
import io.implermine.desolo.domain.coupon.UseCouponResult;
import io.implermine.desolo.domain.coupon.issued.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/users/{userId}/coupons")
    public List<Coupon> getCoupon(
            @PathVariable("userId") final String userId)
    {
        return couponService.findByUserId(userId);
    }

    @PostMapping("/api/users/{userId}/coupons/issue")
    public void issueCoupon(
            @PathVariable("userId") final String userId,
            @RequestBody final IssueCouponRequest request
    ) {
        couponService.issueCoupon(request.toCommand(userId));
    }

    @PostMapping("/api/users/{userId}/coupons/use-best")
    public UseCouponResponse useBestCoupon(
            @PathVariable("userId") final String userId,
            @RequestBody final UseCouponRequest request
    ) {
        UseCouponResult useCouponResult = couponService.useCoupon(request.toCommand(userId));
        return UseCouponResponse.from(useCouponResult);
    }



}
