package ca.gov.dtsstn.passport.api.web.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DateValidator.class })
@Target({ ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE })
public @interface Date {

	String message();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
