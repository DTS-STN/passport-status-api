package ca.gov.dtsstn.passport.api.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.SourceCodeEntity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface SourceCodeRepository extends JpaRepository<SourceCodeEntity, String> {

	Optional<SourceCodeEntity> findByCdoCode(String cdoCode);

	List<SourceCodeEntity> findAllByIsActive(boolean isActive);

}
