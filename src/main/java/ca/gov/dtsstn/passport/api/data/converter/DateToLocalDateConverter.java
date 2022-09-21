package ca.gov.dtsstn.passport.api.data.converter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ReadingConverter
public class DateToLocalDateConverter implements Converter<Date, LocalDate> {

	@Override
	public LocalDate convert(Date source) {
		return source.toInstant().atOffset(ZoneOffset.UTC).toLocalDate();
	}

}
