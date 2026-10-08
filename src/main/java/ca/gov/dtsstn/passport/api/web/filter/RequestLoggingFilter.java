package ca.gov.dtsstn.passport.api.web.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	private boolean enabled;

	private List<String> includeUrls = Collections.emptyList();

	private List<String> excludeUrls = Collections.emptyList();

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
		return includeUrls.isEmpty() || includeUrls.stream().anyMatch(includeUrl -> antPathMatcher.match(includeUrl, urlPathHelper.getPathWithinApplication(request)));
	}

	protected boolean isExcluded(HttpServletRequest request) {
		return excludeUrls.stream().anyMatch(excludeUrl -> antPathMatcher.match(excludeUrl, urlPathHelper.getPathWithinApplication(request)));
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setIncludeUrls(Collection<String> includeUrls) {
		Assert.notNull(includeUrls, "includeUrls is required; it must not be null");
		this.includeUrls = List.copyOf(includeUrls);
	}

	public void setExcludeUrls(Collection<String> excludeUrls) {
		Assert.notNull(excludeUrls, "excludeUrls is required; it must not be null");
		this.excludeUrls = List.copyOf(excludeUrls);
	}

}
