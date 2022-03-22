/*
package com.cs.core.config.interactor.usecase.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.role.IBulkCreateRoleModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.usecase.role.IBulkCreateRoleStrategy;

@Component
public class BulkCreateRoles
    extends AbstractCreateConfigInteractor<IBulkCreateRoleModel, IPluginSummaryModel>
    implements IBulkCreateRoles {
  
  @Autowired
  IBulkCreateRoleStrategy bulkCreateRoleStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IBulkCreateRoleModel bulkRoleModel) throws Exception
  {
    return bulkCreateRoleStrategy.execute(bulkRoleModel);
  }
}
*/
