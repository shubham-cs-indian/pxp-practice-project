package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface IBulkCreateUsersService
    extends IConfigService<IListModel<IUserModel>, IPluginSummaryModel> {
  
}
