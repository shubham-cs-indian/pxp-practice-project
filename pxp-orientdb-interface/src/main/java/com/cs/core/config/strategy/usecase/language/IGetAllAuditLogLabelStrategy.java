package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllAuditLogLabelStrategy extends
    IConfigStrategy<IModel, IIdAndTypeModel> {
  
}
