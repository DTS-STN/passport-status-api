package ca.gov.dtsstn.passport.api.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.StatusCodeEntity;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
public interface StatusCodeRepository extends JpaRepository<StatusCodeEntity, String> {

	Optional<StatusCodeEntity> findByCdoCode(String cdoCode);

	List<StatusCodeEntity> findAllByIsActive(boolean isActive);

}
