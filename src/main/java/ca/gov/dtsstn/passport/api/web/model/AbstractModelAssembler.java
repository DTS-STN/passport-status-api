package ca.gov.dtsstn.passport.api.web.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.util.Assert;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public abstract class AbstractModelAssembler<T, D extends RepresentationModel<?>> extends RepresentationModelAssemblerSupport<T, D> {

	protected final EmbeddedWrappers embeddedWrappers = new EmbeddedWrappers(false);

	protected final PagedResourcesAssembler<T> pagedResourcesAssembler;

	protected AbstractModelAssembler(Class<?> controllerClass, Class<D> resourceType, PagedResourcesAssembler<T> pagedResourcesAssembler) {
		super(controllerClass, resourceType);

		Assert.notNull(pagedResourcesAssembler, "pagedResourcesAssembler is required; it must not be null");
		this.pagedResourcesAssembler = pagedResourcesAssembler;
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
	 * <p>
	 * If there is a better way to handle this than abusing type erasure, I could not find it.
	 *
	 * TODO :: GjB :: try to make this work without type erasure.
	 */
	@SuppressWarnings({ "unchecked" })
	public <C> CollectionModel<C> wrapCollection(CollectionModel<C> collectionModel, Class<C> type) {
		if (!collectionModel.getContent().isEmpty()) { return collectionModel; }
		return (CollectionModel<C>) CollectionModel.of(List.of(embeddedWrappers.emptyCollectionOf(type)), collectionModel.getLinks());
	}

}
