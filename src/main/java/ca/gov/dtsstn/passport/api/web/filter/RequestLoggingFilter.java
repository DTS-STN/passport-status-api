package ca.gov.dtsstn.passport.api.web.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	private boolean enabled;

	private List<AntPathRequestMatcher> includeUrls = Collections.emptyList();

	private List<AntPathRequestMatcher> excludeUrls = Collections.emptyList();

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return enabled && isIncluded(request) && !isExcluded(request);
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		log.info(message);
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		log.info(message);
	}

	protected boolean isIncluded(HttpServletRequest request) {
		return includeUrls.isEmpty() || includeUrls.stream().anyMatch(includeUrl -> includeUrl.matches(request));
	}

	protected boolean isExcluded(HttpServletRequest request) {
		return excludeUrls.stream().anyMatch(excludeUrl -> excludeUrl.matches(request));
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setIncludeUrls(Collection<AntPathRequestMatcher> includeUrls) {
		Assert.notNull(includeUrls, "includeUrls is required; it must not be null");
		this.includeUrls = List.copyOf(includeUrls);
	}

	public void setExcludeUrls(Collection<AntPathRequestMatcher> excludeUrls) {
		Assert.notNull(excludeUrls, "excludeUrls is required; it must not be null");
		this.excludeUrls = List.copyOf(excludeUrls);
	}

}
