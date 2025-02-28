package ca.gov.dtsstn.passport.api.service.domain;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;
import org.springframework.lang.Nullable;

/**
 * Domain object that represents a passport service level status code.
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
public interface ServiceLevelCode extends AbstractDomainObject {

	@Nullable
	String getCode();

	@Nullable
	String getCdoCode();

	@Nullable
	String getDescription();

	@Nullable
	Boolean getIsActive();

}
