package io.implermine.desolo.domain.coupon.issued;

import lombok.Getter;

@Getter
public enum CouponStatus {
    ACTIVE("사용 가능"),

    USED("사용 완료"),

    EXPIRED("기간 만료"),

    // 관리자에 의해 무효화됨
    REVOKED("발급 취소"),

    // 특정 조건 충족 시 활성화됨
    PENDING("대기중");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }

}
