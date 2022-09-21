package ca.gov.dtsstn.passport.api.data.converter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@WritingConverter
public class LocalDateToDateConverter implements Converter<LocalDate, Date> {

	@Override
	public Date convert(LocalDate source) {
		return Date.from(source.atStartOfDay().toInstant(ZoneOffset.UTC));
	}

}
