package com.cs.core.config.businessapi.tabs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.strategy.usecase.tabs.IBulkSaveTabStrategy;

@Service
public class BulkSaveTabService extends AbstractSaveConfigService<IListModel<ISaveTabModel>, IBulkSaveTabResponseModel> implements IBulkSaveTabService {
  
  @Autowired
  protected IBulkSaveTabStrategy saveTabStrategy;
  
  @Autowired
  protected TabValidations tabValidations;
  
  @Override
  public IBulkSaveTabResponseModel executeInternal(IListModel<ISaveTabModel> dataModel) throws Exception
  {
    tabValidations.validate(dataModel);
    return saveTabStrategy.execute(dataModel);
  }
}
