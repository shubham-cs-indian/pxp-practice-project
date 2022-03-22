package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveAttributeStrategy
    extends IConfigStrategy<IListModel<ISaveAttributeModel>, IBulkSaveAttributeResponseModel> {
  
}
