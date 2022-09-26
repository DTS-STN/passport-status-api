package ca.gov.dtsstn.passport.api.web.assembler;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.util.Assert;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public abstract class AbstractModelAssembler<T, D extends RepresentationModel<?>> extends RepresentationModelAssemblerSupport<T, D> {

	protected final PagedResourcesAssembler<T> pagedResourcesAssembler;

	protected AbstractModelAssembler(Class<?> controllerClass, Class<D> resourceType, PagedResourcesAssembler<T> pagedResourcesAssembler) {
		super(controllerClass, resourceType);

		Assert.notNull(pagedResourcesAssembler, "pagedResourcesAssembler is required; it must not be null");
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@SuppressWarnings({ "unchecked" })
	public PagedModel<D> toEmptyPagedModel(Page<T> page) {
		Assert.notNull(page, "page is required; it must not be null");
		return (PagedModel<D>) pagedResourcesAssembler.toEmptyModel(page, getResourceType());
	}

	public PagedModel<D> toModel(Page<T> page) {
		Assert.notNull(page, "page is required; it must not be null");
		return page.isEmpty() ? toEmptyPagedModel(page) : pagedResourcesAssembler.toModel(page, this);
	}

}
