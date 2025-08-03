package io.implermine.desolo.domain.coupon.definition;


import io.implermine.desolo.domain.coupon.model.CouponType;

//segregate between definition and instance (instance will be Coupon)
/**
 * 이 클래스는 지금 당장은 안쓰는데, 다음 요구사항이 필요해지면 추가
 * validFrom과 validTo가 필요
 */
public record CouponDefinition(
        CouponType couponType,
        String name,
        String description
) {
}
