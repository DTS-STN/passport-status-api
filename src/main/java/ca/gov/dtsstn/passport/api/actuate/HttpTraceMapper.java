package ca.gov.dtsstn.passport.api.actuate;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.boot.actuate.trace.http.HttpTrace;

import ca.gov.dtsstn.passport.api.data.document.HttpTraceDocument;

/**
 * MapStruct mapper that maps {@link HttpTrace} instances to {@link HttpTraceDocument} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface HttpTraceMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "principalName", source = "principal.name")
	@Mapping(target = "sessionId", source = "session.id")
	@Mapping(target = "requestMethod", source = "request.method")
	@Mapping(target = "requestUri", source = "request.uri")
	@Mapping(target = "requestHeaders", source = "request.headers")
	@Mapping(target = "requestRemoteAddress", source = "request.remoteAddress")
	@Mapping(target = "putRequestHeaders", ignore = true)
	@Mapping(target = "putAllRequestHeaders", ignore = true)
	@Mapping(target = "responseStatus", source = "response.status")
	@Mapping(target = "responseHeaders", source = "response.headers")
	@Mapping(target = "timeTakenMillis", source = "timeTaken")
	// for some reason, MapStruct thinks these are target fields 🤷
	@Mapping(target = "putResponseHeaders", ignore = true)
	@Mapping(target = "putAllResponseHeaders", ignore = true)
	HttpTraceDocument toDocument(HttpTrace httpTrace);

	@InheritInverseConfiguration
	HttpTrace fromDocument(HttpTraceDocument httpTrace);

}
