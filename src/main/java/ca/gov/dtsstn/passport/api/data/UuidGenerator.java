package ca.gov.dtsstn.passport.api.data;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * A Hibernate {@link IdentifierGenerator} that will generate a string-representation of a {@link UUID}.
 * <p>
 * Usage example:
 *
 * <pre>
 * &#64;Id
 * &#64;Column(nullable = false, updatable = false)
 * &#64;GeneratedValue(generator = "uuid-generator")
 * &#64;GenericGenerator(name = "uuid-generator", strategy = UuidGenerator.STRATEGY)
 * private String id;
 * </pre>
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public class UuidGenerator implements IdentifierGenerator {

	public static final String STRATEGY = "ca.gov.dtsstn.passport.api.data.UuidGenerator";

	private final UUIDGenerator delegate;

	public UuidGenerator() {
		this(new UUIDGenerator());
	}

	public UuidGenerator(UUIDGenerator delegate) {
		this.delegate = delegate;
	}

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
		delegate.configure(type, params, serviceRegistry);
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
		final Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
		return Optional.ofNullable(id).orElseGet(() -> delegate.generate(session, object).toString());
	}

}
