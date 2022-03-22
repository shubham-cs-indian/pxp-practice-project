package com.cs.core.runtime.interactor.usecase.instance;


import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetTaxonomyHierarchyForMulticlassification<P extends IIdPaginationModel, R extends IConfigEntityTreeInformationModel>
    extends AbstractRuntimeInteractor<P, R> {
}
