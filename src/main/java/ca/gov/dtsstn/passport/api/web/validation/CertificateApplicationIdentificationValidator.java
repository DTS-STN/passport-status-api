package ca.gov.dtsstn.passport.api.web.validation;

import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationIdentificationModel;

/**
 * Checks that a collection of {@link CertificateApplicationIdentificationModel} has both
 * {@code APPLICATION_REGISTER_SID_CATEGORY_TEXT} and {@code FILE_NUMBER_CATEGORY_TEXT} entries.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class CertificateApplicationIdentificationValidator implements ConstraintValidator<CertificateApplicationIdentification, Iterable<CertificateApplicationIdentificationModel>> {

	@Override
	public boolean isValid(Iterable<CertificateApplicationIdentificationModel> values, ConstraintValidatorContext context) {
		if (values == null) { return true; }

		final var hasApplicationRegisterSid = validateNotBlank(values, CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT);
		final var hasFileNumber = validateNotBlank(values, CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT);

		return hasApplicationRegisterSid && hasFileNumber;
	}

	protected boolean validateNotBlank(Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications, String identificationCategoryText) {
		return StreamSupport.stream(certificateApplicationIdentifications.spliterator(), false)
			.filter(byIdentificationCategoryText(identificationCategoryText)).findFirst()
			.map(CertificateApplicationIdentificationModel::getIdentificationId)
			.filter(StringUtils::hasText)
			.isPresent();
	}

	protected Predicate<CertificateApplicationIdentificationModel> byIdentificationCategoryText(String applicationRegisterSidCategoryText) {
		return certificateApplicationId -> applicationRegisterSidCategoryText.equals(certificateApplicationId.getIdentificationCategoryText());
	}

}
