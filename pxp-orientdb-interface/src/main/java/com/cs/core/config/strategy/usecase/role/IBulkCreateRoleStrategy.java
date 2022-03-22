package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.IBulkCreateRoleModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkCreateRoleStrategy
    extends IConfigStrategy<IBulkCreateRoleModel, IPluginSummaryModel> {
  
}
