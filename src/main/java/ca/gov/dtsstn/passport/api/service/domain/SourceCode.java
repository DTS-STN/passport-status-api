package ca.gov.dtsstn.passport.api.service.domain;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;
import org.springframework.lang.Nullable;

/**
 * Domain object that represents an IRCC source system (ex: IRIS, GCMS, Tempo)
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
public interface SourceCode extends AbstractDomainObject {

	@Nullable
	String getCode();

	@Nullable
	String getCdoCode();

	@Nullable
	String getDescription();

	@Nullable
	Boolean getIsActive();

}
