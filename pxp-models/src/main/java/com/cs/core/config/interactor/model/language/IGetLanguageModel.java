package com.cs.core.config.interactor.model.language;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;

public interface IGetLanguageModel extends IConfigResponseWithAuditLogModel {
  
  public static final String ENTITY = "entity";
  
  public ILanguage getEntity();
  
  public void setEntity(ILanguage entity);
}
