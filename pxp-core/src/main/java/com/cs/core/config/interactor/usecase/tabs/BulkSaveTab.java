package com.cs.core.config.interactor.usecase.tabs;

import com.cs.core.config.businessapi.tabs.IBulkSaveTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.interactor.usecase.tab.IBulkSaveTab;
import com.cs.core.config.strategy.usecase.tabs.IBulkSaveTabStrategy;

@Service
public class BulkSaveTab
    extends AbstractSaveConfigInteractor<IListModel<ISaveTabModel>, IBulkSaveTabResponseModel>
    implements IBulkSaveTab {
  
  @Autowired
  protected IBulkSaveTabService bulkSaveTabAPI;
  
  @Override
  public IBulkSaveTabResponseModel executeInternal(IListModel<ISaveTabModel> dataModel) throws Exception
  {
    return bulkSaveTabAPI.execute(dataModel);
  }
}
