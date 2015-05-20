package no.mesan.fagark.reaktiv.logistikk.domain.constraint;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobilTelefon.Validator.class)
public @interface MobilTelefon {

    String message() default "{no.mesan.fagark.reaktiv.logistikk.domain.constraint.message.eiendel.mobil}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<MobilTelefon, Eiendel>, Serializable {

        private static final long serialVersionUID = 1L;
        List<String> mobilTelefoner = Arrays.asList("Nokia", "Iphone", "Motorola", "Samsung", "Sony");

        @Override
        public void initialize(final MobilTelefon hasId) {
        }

        @Override
        public boolean isValid(final Eiendel eiendel, final ConstraintValidatorContext constraintValidatorContext) {
            if (mobilTelefoner.contains(eiendel.getNavn())) {
                return false;
            }

            if (mobilTelefoner.contains(eiendel.getTekniskBeskrivelse())) {
                return false;
            }

            return true;
        }
    }
}
