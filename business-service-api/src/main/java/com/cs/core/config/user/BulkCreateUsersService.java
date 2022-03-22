package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IBulkCreateUsersStrategy;

@Service
public class BulkCreateUsersService extends AbstractCreateConfigService<IListModel<IUserModel>, IPluginSummaryModel> implements IBulkCreateUsersService {
  
  @Autowired
  IBulkCreateUsersStrategy orientDBBulkCreateUsersStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IUserModel> dataModel) throws Exception
  {
    return orientDBBulkCreateUsersStrategy.execute(dataModel);
  }
}
