package no.mesan.fagark.reaktiv.logistikk.domain.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinstEnEiendel.Validator.class)
public @interface MinstEnEiendel {

    String message() default "{no.mesan.fagark.reaktiv.logistikk.domain.constraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<MinstEnEiendel, Eier> {
        @Override
        public void initialize(final MinstEnEiendel hasId) {
        }

        @Override
        public boolean isValid(final Eier eier, final ConstraintValidatorContext constraintValidatorContext) {
            return !eier.getEiendeler().isEmpty();
        }
    }
}
