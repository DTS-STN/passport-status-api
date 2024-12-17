package ca.gov.dtsstn.passport.api.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.annotations.IdGeneratorType;

import ca.gov.dtsstn.passport.api.data.UuidGenerator;

@Retention(RetentionPolicy.RUNTIME)
@IdGeneratorType(UuidGenerator.class)
@Target({ ElementType.FIELD, ElementType.METHOD})
public @interface EntityId {}
