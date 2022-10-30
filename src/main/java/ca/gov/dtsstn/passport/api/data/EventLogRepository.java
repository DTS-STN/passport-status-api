package ca.gov.dtsstn.passport.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.EventLogEntity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface EventLogRepository extends JpaRepository<EventLogEntity, String> {}
