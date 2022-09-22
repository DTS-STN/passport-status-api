package ca.gov.dtsstn.passport.api.data.converter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

/**
 * Spring {@link Converter} used by Spring Data to convert {@link LocalDate} instances at UTC midnight to {@link Date} instances.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@WritingConverter
public class LocalDateToDateConverter implements Converter<LocalDate, Date> {

	@Override
	public Date convert(LocalDate source) {
		return Date.from(source.atStartOfDay().toInstant(ZoneOffset.UTC));
	}

}
