package ca.gov.dtsstn.passport.api.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.annotate.InjectAnnotation;
import org.immutables.annotate.InjectAnnotation.Where;

import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An immutables.org injectable annotation used to inject SpringDoc's annotation of the same name.
 *
 * @see io.swagger.v3.oas.annotations.Parameter
 * @see https://immutables.github.io/immutable.html#annotation-injection
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@InjectAnnotation(type = io.swagger.v3.oas.annotations.Parameter.class, target = Where.FIELD, code = "([[*]])")
public @interface Parameter {

	String name() default "";

	ParameterIn in() default ParameterIn.DEFAULT;

	String description() default "";

	boolean required() default false;

	boolean deprecated() default false;

	boolean allowEmptyValue() default false;

	ParameterStyle style() default ParameterStyle.DEFAULT;

	Explode explode() default Explode.DEFAULT;

	boolean allowReserved() default false;

	Schema schema() default @Schema();

	ArraySchema array() default @ArraySchema();

	Content[] content() default {};

	boolean hidden() default false;

	ExampleObject[] examples() default {};

	String example() default "";

	Extension[] extensions() default {};

	String ref() default "";

}
