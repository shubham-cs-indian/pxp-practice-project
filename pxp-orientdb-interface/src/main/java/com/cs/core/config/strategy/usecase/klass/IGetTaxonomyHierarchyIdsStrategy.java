package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetTaxonomyHierarchyIdsStrategy
    extends IConfigStrategy<IIdLabelCodeModel, IIIDsListModel> {
  
}
