package com.cs.core.config.strategy.usecase.language;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;

@Component
public class GetAllAuditLogLabelStrategy extends OrientDBBaseStrategy
    implements IGetAllAuditLogLabelStrategy {
  
  @Override
  public IIdAndTypeModel execute(IModel model) throws Exception
  {
    return execute(GET_ALL_AUDIT_LOG_LABEL, model, IdAndTypeModel.class);
  }
  
}