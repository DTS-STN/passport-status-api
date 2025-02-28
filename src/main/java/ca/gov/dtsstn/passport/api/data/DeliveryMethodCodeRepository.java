package ca.gov.dtsstn.passport.api.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.DeliveryMethodCodeEntity;

public interface DeliveryMethodCodeRepository extends JpaRepository<DeliveryMethodCodeEntity, String> {

	Optional<DeliveryMethodCodeEntity> findByCdoCode(String cdoCode);

	List<DeliveryMethodCodeEntity> findAllByIsActive(boolean isActive);

}
