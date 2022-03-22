package com.cs.core.config.strategy.configuration.base;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IChangeTypeKlassToDefaultStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IContentTypeIdsInfoModel>> {
  
}
