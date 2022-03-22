package com.cs.core.config.taxonomy;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;

public interface IGetTaxonomyHierarchyForSelectedTaxonomyService
    extends IRuntimeService<IConfigDetailsForSwitchTypeRequestModel, IGetConfigDetailsModel> {
}
