package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;

public interface IBulkSaveTabService extends ISaveConfigService<IListModel<ISaveTabModel>, IBulkSaveTabResponseModel>{

}
