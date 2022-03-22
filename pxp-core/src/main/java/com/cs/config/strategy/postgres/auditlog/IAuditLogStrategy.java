package com.cs.config.strategy.postgres.auditlog;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAuditLogStrategy extends IConfigStrategy<IListModel<IAuditLogModel>, IModel> {
  
}
