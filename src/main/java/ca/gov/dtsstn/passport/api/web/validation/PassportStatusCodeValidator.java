package ca.gov.dtsstn.passport.api.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationStatusModel;

/**
 * Checks that a string is a valid passport status code.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusCodeValidator implements ConstraintValidator<PassportStatusCode, String> {

	private final StatusCodeService statusCodeService;

	public PassportStatusCodeValidator(StatusCodeService statusCodeService) {
		this.statusCodeService = statusCodeService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		final var certificateApplicationStatusModel = ImmutableCertificateApplicationStatusModel.builder().statusCode(value).build();
		final var statusCode = statusCodeService.readByCdoCode(certificateApplicationStatusModel.getStatusCode());
		return statusCode.isPresent() == true && BooleanUtils.isTrue(statusCode.get().getIsActive());
	}

}
