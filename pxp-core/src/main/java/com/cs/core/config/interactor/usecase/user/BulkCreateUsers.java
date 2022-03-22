package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.IBulkCreateUsersService;

@Service
public class BulkCreateUsers 
extends AbstractCreateConfigInteractor<IListModel<IUserModel>, IPluginSummaryModel> implements IBulkCreateUsers {
  
  @Autowired
  IBulkCreateUsersService bulkCreateUsersService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IUserModel> dataModel) throws Exception
  {
    return bulkCreateUsersService.execute(dataModel);
  }
}
