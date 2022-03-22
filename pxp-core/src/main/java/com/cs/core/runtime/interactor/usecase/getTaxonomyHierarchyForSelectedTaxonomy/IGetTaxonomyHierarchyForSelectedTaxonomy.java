package com.cs.core.runtime.interactor.usecase.getTaxonomyHierarchyForSelectedTaxonomy;

import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetTaxonomyHierarchyForSelectedTaxonomy
    extends IRuntimeInteractor<IConfigDetailsForSwitchTypeRequestModel, IGetConfigDetailsModel> {
}
