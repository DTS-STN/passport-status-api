package ca.gov.dtsstn.passport.api.service.domain.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.entity.DeliveryMethodCodeEntity;
import ca.gov.dtsstn.passport.api.service.DeliveryMethodCodeService;
import ca.gov.dtsstn.passport.api.service.domain.DeliveryMethodCode;
import jakarta.annotation.PostConstruct;

@Mapper(componentModel = "spring")
public abstract class DeliveryMethodCodeMapper {

	@Autowired
	protected DeliveryMethodCodeService deliveryMethodCodeService;

	@PostConstruct
	public void postConstruct() {
		Assert.notNull(deliveryMethodCodeService, "deliveryMethoCodeService is required; it must not be null");
	}

	@Nullable
	public DeliveryMethodCodeEntity fromId(@Nullable String id) {
		return Optional.ofNullable(id)
			.flatMap(deliveryMethodCodeService::read)
			.map(this::toEntity)
			.orElse(null);
	}

	@Nullable
	public abstract DeliveryMethodCode fromEntity(@Nullable DeliveryMethodCodeEntity deliveryMethodCode);

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	public abstract DeliveryMethodCodeEntity toEntity(@Nullable DeliveryMethodCode deliveryMethodCode);

}
