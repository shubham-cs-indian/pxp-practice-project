package com.cs.core.config.role;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.role.IBulkCreateRoleModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkCreateRolesService
    extends ICreateConfigService<IBulkCreateRoleModel, IPluginSummaryModel> {
  
}
