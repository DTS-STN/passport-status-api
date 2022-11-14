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
public interface PassportStatusRepository extends JpaRepository<PassportStatusEntity, String> {

	@Query("""
    SELECT ps FROM PassportStatus ps
    WHERE lower(email) = lower(?1) AND dateOfBirth = ?2 AND
    lower(remove_non_alpha_numeric(remove_diacritics(givenName))) = lower(remove_non_alpha_numeric(remove_diacritics(?3))) AND
    lower(remove_non_alpha_numeric(remove_diacritics(surname))) = lower(remove_non_alpha_numeric(remove_diacritics(?4)))
    """)
	List<PassportStatusEntity> emailSearch(String email, LocalDate dateOfBirth, String givenName, String surname);

	@Query("""
    SELECT ps FROM PassportStatus ps
    WHERE lower(fileNumber) = lower(?1) AND dateOfBirth = ?2 AND
    lower(remove_non_alpha_numeric(remove_diacritics(givenName))) = lower(remove_non_alpha_numeric(remove_diacritics(?3))) AND
    lower(remove_non_alpha_numeric(remove_diacritics(surname))) = lower(remove_non_alpha_numeric(remove_diacritics(?4)))
    """)
	List<PassportStatusEntity> fileNumberSearch(String fileNumber, LocalDate dateOfBirth, String givenName, String surname);

	List<PassportStatusEntity> findAllByApplicationRegisterSid(String applicationRegisterSid);

	Optional<PassportStatusEntity> findByApplicationRegisterSidAndVersion(String applicationRegisterSid, Long version);

}
