package ca.gov.dtsstn.passport.api.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "java:S2479" })
public interface PassportStatusRepository extends JpaRepository<PassportStatusEntity, String> {

	@Query("""
		SELECT ps FROM PassportStatus ps
		 WHERE lower(email) = lower(?1)
		   AND dateOfBirth = ?2
		   AND lower(cast(remove_non_alpha_numeric(remove_diacritics(givenName)) as string)) = lower(cast(remove_non_alpha_numeric(remove_diacritics(?3)) as string))
		   AND lower(cast(remove_non_alpha_numeric(remove_diacritics(surname)) as string)) = lower(cast(remove_non_alpha_numeric(remove_diacritics(?4)) as string))
		   AND version = (SELECT max(version) FROM PassportStatus other WHERE other.applicationRegisterSid = ps.applicationRegisterSid)
	""")
	List<PassportStatusEntity> emailSearch(String email, LocalDate dateOfBirth, String givenName, String surname);

	@Query("""
		SELECT ps FROM PassportStatus ps
		 WHERE lower(email) = lower(?1)
		   AND dateOfBirth = ?2
		   AND ps.givenName IS NULL
		   AND lower(cast(remove_non_alpha_numeric(remove_diacritics(surname)) as string)) = lower(cast(remove_non_alpha_numeric(remove_diacritics(?3)) as string))
		   AND version = (SELECT max(version) FROM PassportStatus other WHERE other.applicationRegisterSid = ps.applicationRegisterSid)
	""")
	List<PassportStatusEntity> emailSearchMononym(String email, LocalDate dateOfBirth, String mononym);

	@Query("""
		SELECT ps FROM PassportStatus ps
		 WHERE lower(fileNumber) = lower(?1)
		   AND dateOfBirth = ?2
		   AND lower(cast(remove_non_alpha_numeric(remove_diacritics(givenName)) as string)) = lower(cast(remove_non_alpha_numeric(remove_diacritics(?3)) as string))
		   AND lower(cast(remove_non_alpha_numeric(remove_diacritics(surname)) as string)) = lower(cast(remove_non_alpha_numeric(remove_diacritics(?4)) as string))
		   AND version = (SELECT MAX(version) FROM PassportStatus other WHERE other.applicationRegisterSid = ps.applicationRegisterSid)
	""")
	List<PassportStatusEntity> fileNumberSearch(String fileNumber, LocalDate dateOfBirth, String givenName, String surname);

	@Query("""
		SELECT ps FROM PassportStatus ps
		 WHERE lower(fileNumber) = lower(?1)
		   AND dateOfBirth = ?2
		   AND ps.givenName IS NULL
		   AND lower(cast(remove_non_alpha_numeric(remove_diacritics(surname)) as string)) = lower(cast(remove_non_alpha_numeric(remove_diacritics(?3)) as string))
		   AND version = (SELECT MAX(version) FROM PassportStatus other WHERE other.applicationRegisterSid = ps.applicationRegisterSid)
	""")
	List<PassportStatusEntity> fileNumberSearchMononym(String fileNumber, LocalDate dateOfBirth, String mononym);

	List<PassportStatusEntity> findAllByApplicationRegisterSid(String applicationRegisterSid);

	Optional<PassportStatusEntity> findByApplicationRegisterSidAndVersion(String applicationRegisterSid, Long version);

}
