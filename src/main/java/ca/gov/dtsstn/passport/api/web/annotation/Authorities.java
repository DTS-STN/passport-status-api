package ca.gov.dtsstn.passport.api.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Constants for Spring Security authorities.
 *
 * @author Sébastien Comeau <sebastien.comeau@hrsdc-rhdcc.gc.ca>
 */
public interface Authorities {

	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Read')")
	public @interface PassportStatusRead {}

	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Read.All')")
	public @interface PassportStatusReadAll {}

	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Write')")
	public @interface PassportStatusWrite {}

	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Write.All')")
	public @interface PassportStatusWriteAll {}

}
