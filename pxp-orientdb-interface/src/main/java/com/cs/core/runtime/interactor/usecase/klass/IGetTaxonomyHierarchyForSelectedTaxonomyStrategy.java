package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public interface IGetTaxonomyHierarchyForSelectedTaxonomyStrategy extends
    IConfigStrategy<IConfigDetailsForSwitchTypeRequestModel, IGetConfigDetailsForCustomTabModel> {
}
