package com.cs.core.config.interactor.usecase.tab;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;

public interface IBulkSaveTab
    extends ISaveConfigInteractor<IListModel<ISaveTabModel>, IBulkSaveTabResponseModel> {
  
}
