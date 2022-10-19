package ca.gov.dtsstn.passport.api.web.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.AbstractDomainObject;

/**
 * A base model assembler that can automate creation of resources and guarantee that a self link is always added.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public abstract class AbstractResponseModelAssembler<T extends AbstractDomainObject, D extends AbstractResponseModel<?>> extends RepresentationModelAssemblerSupport<T, D> {

	protected final EmbeddedWrappers embeddedWrappers = new EmbeddedWrappers(false);

	protected final PagedResourcesAssembler<T> pagedResourcesAssembler;

	protected AbstractResponseModelAssembler(Class<?> controllerClass, Class<D> resourceType, PagedResourcesAssembler<T> pagedResourcesAssembler) {
		super(controllerClass, resourceType);

		Assert.notNull(pagedResourcesAssembler, "pagedResourcesAssembler is required; it must not be null");
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@Override
	protected abstract D instantiateModel(T entity);

	@Override
	public D toModel(T entity) {
		return createModelWithId(entity.getId(), entity); // NOSONAR (nullable value)
	}

	@SuppressWarnings({ "unchecked" })
	public PagedModel<D> toModel(Page<T> page) {
		Assert.notNull(page, "page is required; it must not be null");
		return page.isEmpty() ? (PagedModel<D>) pagedResourcesAssembler.toEmptyModel(page, getResourceType()) : pagedResourcesAssembler.toModel(page, this);
	}

	/**
	 * Convenience method to wrap a {@link CollectionModel} in a Spring HATEOAS
	 * {@link EmbeddedWrapper}. This ensures that any empty collections are
	 * represented as <code>[]</code> in the response (instead of null).
	 * <p>
	 * Note that this method severely abuses Java type erasure to allow controllers to return
	 * {@code CollectionModel<MyModel>} when in fact they could potentially be returning
	 * {@code CollectionModel<EmbeddedWrapper>}, which this method returns if the incoming collection is empty.
	 */
	@SuppressWarnings({ "unchecked" })
	public <C> CollectionModel<C> wrapCollection(CollectionModel<C> collectionModel, Class<C> type) {
		final var embeddedWrapper = collectionModel.getContent().isEmpty() ? embeddedWrappers.emptyCollectionOf(type) : embeddedWrappers.wrap(collectionModel);
		return (CollectionModel<C>) CollectionModel.of(List.of(embeddedWrapper), collectionModel.getLinks());
	}

}
