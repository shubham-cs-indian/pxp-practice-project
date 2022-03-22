package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveTabStrategy
    extends IConfigStrategy<IListModel<ISaveTabModel>, IBulkSaveTabResponseModel> {
  
}
