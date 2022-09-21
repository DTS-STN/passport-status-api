package ca.gov.dtsstn.passport.api.web.exception;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
public class PassportStatusNotFoundException extends RuntimeException {

	public PassportStatusNotFoundException(String id) {
		super("Could not find passport status " + id);
	}

}