package ca.gov.dtsstn.passport.api.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationModelMapper;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationStatusModel;

/**
 * Checks that a string is a valid passport status code.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusCodeValidator implements ConstraintValidator<PassportStatusCode, String> {

	private final CertificateApplicationModelMapper mapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO :: GjB :: make this more robust (don't rely on a mapper class)
		return mapper.toStatus(ImmutableCertificateApplicationStatusModel.builder().statusCode(value).build()) != null;
	}

}
