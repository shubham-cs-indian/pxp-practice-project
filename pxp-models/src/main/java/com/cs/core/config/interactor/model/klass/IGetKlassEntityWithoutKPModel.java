package com.cs.core.config.interactor.model.klass;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetKlassEntityWithoutKPModel extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String ENTITY         = "entity";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public IKlass getEntity();
  
  
  public void setEntity(IKlass entity);
  public IGetKlassEntityConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetKlassEntityConfigDetailsModel configDetails);
}
