package ca.gov.dtsstn.passport.api.data.converter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

/**
 * Spring {@link Converter} used by Spring Data to convert {@link Date} instances to {@link LocalDate} instances at UTC midnight.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@ReadingConverter
public class DateToLocalDateConverter implements Converter<Date, LocalDate> {

	@Override
	public LocalDate convert(Date source) {
		return source.toInstant().atOffset(ZoneOffset.UTC).toLocalDate();
	}

}
