package ca.gov.dtsstn.passport.api.service.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

import org.mapstruct.Qualifier;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface BaseDomainMapper {

	@Qualifier
	@Target({ ElementType.METHOD })
	@Retention(RetentionPolicy.CLASS)
	@interface Searchable {}

	/**
	 * Replaces all diacritic candidate characters in a regular expression
	 * with regular expression character classes to allow for bilingual searching.
	 */
	@Searchable
	default String withDiacritics(String regex) {
		return Optional.ofNullable(regex)
			.map(str -> str.replaceAll("[AÁÀÂÄ]", "[AÁÀÂÄ]"))
			.map(str -> str.replaceAll("[aáàâä]", "[aáàâä]"))
			.map(str -> str.replaceAll("[EÉÈÊË]", "[EÉÈÊË]"))
			.map(str -> str.replaceAll("[eéèêë]", "[eéèêë]"))
			.map(str -> str.replaceAll("[IÍÌÎÏ]", "[IÍÌÎÏ]"))
			.map(str -> str.replaceAll("[iíìîï]", "[iíìîï]"))
			.map(str -> str.replaceAll("[OÓÒÔÖ]", "[OÓÒÔÖ]"))
			.map(str -> str.replaceAll("[oóòôö]", "[oóòôö]"))
			.map(str -> str.replaceAll("[UÚÙÛÜ]", "[UÚÙÛÜ]"))
			.map(str -> str.replaceAll("[uúùûü]", "[uúùûü]"))
			.orElseThrow(() -> new IllegalArgumentException("regex is required; it should not be null"));
	}

}
