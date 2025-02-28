package ca.gov.dtsstn.passport.api.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.ServiceLevelCodeEntity;

public interface ServiceLevelCodeRepository extends JpaRepository<ServiceLevelCodeEntity, String> {

	Optional<ServiceLevelCodeEntity> findByCdoCode(String cdoCode);

	List<ServiceLevelCodeEntity> findAllByIsActive(boolean isActive);

}
