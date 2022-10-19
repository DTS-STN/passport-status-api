package ca.gov.dtsstn.passport.api.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Constants for Spring Security authorities.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface Authorities {

	/**
	 * {@link JsonView} to apply for anonymous users
	 */
	public interface AnonymousView {}

	/**
	 * {@link JsonView} to apply for authenticated users
	 */
	public interface AuthenticatedView {}

	/**
	 * {@link JsonView} to apply for users with the {@code PassportStatus.Read} authority.
	 */
	public interface PassportStatusReadView extends AuthenticatedView {}

	/**
	 * {@link JsonView} to apply for users with the {@code PassportStatus.Read.All} authority.
	 */
	public interface PassportStatusReadAllView extends PassportStatusReadView {}

	/**
	 * {@link JsonView} to apply for users with the {@code PassportStatus.Write} authority.
	 */
	public interface PassportStatusWriteView extends AuthenticatedView {}

	/**
	 * {@link JsonView} to apply for users with the {@code PassportStatus.Write.All} authority.
	 */
	public interface PassportStatusWriteAllView extends PassportStatusWriteView {}

	/**
	 * Spring Security {@link PreAuthorize} meta annotation to enforce {@code PassportStatus.Read} access.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Read')")
	public @interface HasPassportStatusRead {}

	/**
	 * Spring Security {@link PreAuthorize} meta annotation to enforce {@code PassportStatus.Read.All} access.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Read.All')")
	public @interface HasPassportStatusReadAll {}

	/**
	 * Spring Security {@link PreAuthorize} meta annotation to enforce {@code PassportStatus.Write} access.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Write')")
	public @interface HasPassportStatusWrite {}

	/**
	 * Spring Security {@link PreAuthorize} meta annotation to enforce {@code PassportStatus.Write.All} access.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@PreAuthorize("hasAuthority('PassportStatus.Write.All')")
	public @interface HasPassportStatusWriteAll {}

}
