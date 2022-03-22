package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstancesByKlassAndTaxonomyIdsStrategy
    extends IRuntimeStrategy<ITypesListModel, IListModel<IContentTypeIdsInfoModel>> {
  
}
