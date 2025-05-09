package ca.gov.dtsstn.passport.api.web.validation;

import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.service.DeliveryMethodCodeService;
import ca.gov.dtsstn.passport.api.service.domain.DeliveryMethodCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks that a string is a valid passport status code.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportDeliveryMethodCodeValidator implements ConstraintValidator<PassportDeliveryMethodCode, String> {

	private final DeliveryMethodCodeService deliveryMethodCodeService;

	public PassportDeliveryMethodCodeValidator(DeliveryMethodCodeService deliveryMethodCodeService) {
		this.deliveryMethodCodeService = deliveryMethodCodeService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }
		return deliveryMethodCodeService.readByCdoCode(value)
			.map(DeliveryMethodCode::getIsActive)
			.filter(Boolean.TRUE::equals)
			.isPresent();
	}

}
