package ca.gov.dtsstn.passport.api.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.SourceCodeService;
import ca.gov.dtsstn.passport.api.service.domain.SourceCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks that a string is a reference data ID.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class ReferenceDataIdValidator implements ConstraintValidator<ReferenceDataId, String> {

	private final SourceCodeService sourceCodeService;

	public ReferenceDataIdValidator(SourceCodeService sourceCodeService) {
		Assert.notNull(sourceCodeService, "sourceCodeService is required; it must not be null");
		this.sourceCodeService = sourceCodeService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true;}

		return sourceCodeService.readByCdoCode(value)
			.map(SourceCode::getIsActive)
			.filter(Boolean.TRUE::equals)
			.isPresent();
	}

}
