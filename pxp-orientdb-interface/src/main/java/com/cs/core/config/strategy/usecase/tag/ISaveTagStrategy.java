package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveTagStrategy
    extends IConfigStrategy<IListModel<ISaveTagModel>, IBulkSaveTagResponseModel> {
  
}
