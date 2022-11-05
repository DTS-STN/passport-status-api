package ca.gov.dtsstn.passport.api.web.validation;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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

	private final List<String> requiredIdentificationCategoryTexts = List.of(
		CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT,
		CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT
	);

	@Override
	public boolean isValid(Iterable<CertificateApplicationIdentificationModel> values, ConstraintValidatorContext context) {
		if (values == null) { return true; }
		return hasRequiredIdentificationCategoryTexts(values) && hasRequiredIdentificationIds(values);
	}

	protected boolean hasRequiredIdentificationIds(Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		if (certificateApplicationIdentifications == null) { return false; }
		final var hasApplicationRegisterSidIdentificationId = validateIdentificationIdNotBlank(certificateApplicationIdentifications, CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT);
		final var hasFileNumberIdentificationId = validateIdentificationIdNotBlank(certificateApplicationIdentifications, CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT);
		return hasApplicationRegisterSidIdentificationId && hasFileNumberIdentificationId;
	}

	protected boolean hasRequiredIdentificationCategoryTexts(Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		if (certificateApplicationIdentifications == null) { return false; }
		return StreamSupport.stream(certificateApplicationIdentifications.spliterator(), false)
			.map(CertificateApplicationIdentificationModel::getIdentificationCategoryText).toList()
			.containsAll(requiredIdentificationCategoryTexts);
	}

	protected boolean validateIdentificationIdNotBlank(Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications, String identificationCategoryText) {
		Assert.hasText(identificationCategoryText, "identificationCategoryText is required; it must not be null or blank");
		if (certificateApplicationIdentifications == null) { return false; }
		return StreamSupport.stream(certificateApplicationIdentifications.spliterator(), false)
			.filter(byIdentificationCategoryText(identificationCategoryText)).findFirst()
			.map(CertificateApplicationIdentificationModel::getIdentificationId)
			.filter(StringUtils::hasText)
			.isPresent();
	}

	protected Predicate<CertificateApplicationIdentificationModel> byIdentificationCategoryText(String identificationCategoryText) {
		Assert.hasText(identificationCategoryText, "identificationCategoryText is required; it must not be null or blank");
		return certificateApplicationId -> identificationCategoryText.equals(certificateApplicationId.getIdentificationCategoryText());
	}

}
