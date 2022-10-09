package ca.gov.dtsstn.passport.api.security;

/**
 * Constants for Spring Security authorities.
 *
 * @author Sébastien Comeau <sebastien.comeau@hrsdc-rhdcc.gc.ca>
 */
public abstract class AuthoritiesConstants { // NOSONAR

	public static final String APPLICATION_MANAGE = "Application.Manage";

	public static final String PASSPORTSTATUS_READ = "PassportStatus.Read";

	public static final String PASSPORTSTATUS_WRITE = "PassportStatus.Write";

}
