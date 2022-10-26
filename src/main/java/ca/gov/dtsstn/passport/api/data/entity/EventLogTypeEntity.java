package ca.gov.dtsstn.passport.api.data.entity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public enum EventLogTypeEntity {

	/*
	 * ESRF read events
	 */

	GET_ESRF_REQUEST,
	GET_ESRF_SUCCESS,
	GET_ESRF_FAIL,

	/*
	 * PassportStatus create/read events
	 */

	CREATE_STATUS_REQUEST,
	CREATE_STATUS_SUCCESS,
	CREATE_STATUS_FAIL,
	GET_STATUS_REQUEST,
	GET_STATUS_SUCCESS,
	GET_STATUS_FAIL

}
