package ca.gov.dtsstn.passport.api.utils;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-hrdcc.gc.ca)
 */
public class StringUtils {

	protected StringUtils() {
	}

	/**
	 * <p>Removes all non alpha-numeric characters from a string. The case will not be altered.</p>
	 * <p>For instance, '(A)B,.C|d_e1&eacute;@!.>' will be replaced by 'ABCde1'.</p>
	 * <p>Note that accent character will be removed.</p>
	 *
	 * <pre>
	 * StringUtils.stripAccents(null) = null
	 * StringUtils.stripAccents("") = ""
	 * StringUtils.stripAccents("Control") = "Control"
	 * StringUtils.stripAccents("&eacute;clair") = "clair"
	 * StringUtils.stripAccents("(A)B,.C|d_e1&eacute;@!.>") = "ABCde1"
	 * </pre>
	 *
	 * @param input String to be stripped
	 * @return input text with diacritics removed
	 */
	public static String stripNonAlphaNumeric(String input) {
		if (input == null) return null;
		return input.replaceAll("[^a-zA-Z0-9]", "");
	}

}
