package ca.gov.dtsstn.passport.api.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.annotate.InjectAnnotation;
import org.immutables.annotate.InjectAnnotation.Where;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue;

/**
 * An immutables.org injectable annotation used to inject SpringDoc's annotation of the same name.
 *
 * @see io.swagger.v3.oas.annotations.media.Schema
 * @see https://immutables.github.io/immutable.html#annotation-injection
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@InjectAnnotation(type = io.swagger.v3.oas.annotations.media.Schema.class, target = Where.FIELD, code = "([[*]])")
public @interface Schema {

	Class<?> implementation() default Void.class;

	Class<?> not() default Void.class;

	Class<?>[] oneOf() default {};

	Class<?>[] anyOf() default {};

	Class<?>[] allOf() default {};

	String name() default "";

	String title() default "";

	double multipleOf() default 0;

	String maximum() default "";

	boolean exclusiveMaximum() default false;

	String minimum() default "";

	boolean exclusiveMinimum() default false;

	int maxLength() default Integer.MAX_VALUE;

	int minLength() default 0;

	String pattern() default "";

	int maxProperties() default 0;

	int minProperties() default 0;

	String[] requiredProperties() default {};

	boolean required() default false;

	String description() default "";

	String format() default "";

	String ref() default "";

	boolean nullable() default false;

	AccessMode accessMode() default AccessMode.AUTO;

	String example() default "";

	ExternalDocumentation externalDocs() default @ExternalDocumentation();

	boolean deprecated() default false;

	String type() default "";

	String[] allowableValues() default {};

	String defaultValue() default "";

	String discriminatorProperty() default "";

	DiscriminatorMapping[] discriminatorMapping() default {};

	boolean hidden() default false;

	boolean enumAsRef() default false;

	Class<?>[] subTypes() default {};

	Extension[] extensions() default {};

	AdditionalPropertiesValue additionalProperties() default AdditionalPropertiesValue.USE_ADDITIONAL_PROPERTIES_ANNOTATION;

}
