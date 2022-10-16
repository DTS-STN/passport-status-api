package ca.gov.dtsstn.passport.api.security;

/**
 * Constants for Spring Security authorities.
 *
 * @author Sébastien Comeau <sebastien.comeau@hrsdc-rhdcc.gc.ca>
 */
public final class AuthoritiesConstants {

	public static final String APPLICATION_MANAGE = "Application.Manage";

	public static final String PASSPORTSTATUS_READ = "PassportStatus.Read";

	public static final String PASSPORTSTATUS_READ_ALL = "PassportStatus.Read.All";

	public static final String PASSPORTSTATUS_WRITE = "PassportStatus.Write";

	public static final String PASSPORTSTATUS_WRITE_ALL = "PassportStatus.Write.All";

	private AuthoritiesConstants() {}

}
