package io.implermine.desolo.domain.support;

import java.util.function.Function;
import java.util.function.Supplier;

public interface PriceValidator extends Function<Long, PriceValidator.ValidationResult> {

    default PriceValidator.ValidationResult validate(Long price) {
        return apply(price);
    }

    static PriceValidator isPositive() {
        return price -> price > 0 ?
                ValidationResult.SUCCESS : ValidationResult.NOT_POSITIVE;
    }

    static PriceValidator isBiggerThan(long minimum) {
        return price -> price >= minimum ?
                ValidationResult.SUCCESS : ValidationResult.PRICE_TOO_LOW;
    }

    static PriceValidator isTenWonUnit() {
        return price -> price % 10 == 0 ?
                ValidationResult.SUCCESS : ValidationResult.NOT_TEN_WON_UNIT;
    }

    static PriceValidator isMultipleOf(long unit) {
        return price -> price % unit == 0 ?
                ValidationResult.SUCCESS : ValidationResult.NOT_UNIT_MULTIPLE;
    }

    static PriceValidator isLowerThan(long maximum) {
        return price -> price <= maximum ?
                ValidationResult.SUCCESS : ValidationResult.PRICE_TOO_HIGH;
    }

    default PriceValidator and(PriceValidator other) {
        return price -> {
            ValidationResult result = this.validate(price);
            return result.equals(ValidationResult.SUCCESS) ? other.validate(price) : result;
        };
    }

    default PriceValidator ifInvalidThrow(Supplier<? extends RuntimeException> exception) {
        return price -> {
            ValidationResult result = this.validate(price);
            if (result != ValidationResult.SUCCESS) {
                throw exception.get();
            }
            return result;
        };
    }

    enum ValidationResult {
        SUCCESS,
        PRICE_TOO_LOW,
        PRICE_TOO_HIGH,
        NOT_TEN_WON_UNIT,
        NOT_UNIT_MULTIPLE,
        NOT_POSITIVE
    }
}