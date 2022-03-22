package com.cs.core.config.interactor.model.language;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.attributiontaxonomy.Language;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetLanguageModel extends ConfigResponseWithAuditLogModel implements IGetLanguageModel {
  
  private static final long serialVersionUID = 1L;
  protected ILanguage       entity;
  
  @Override
  public ILanguage getEntity()
  {
    return entity;
  }
  
  @JsonDeserialize(as = Language.class)
  @Override
  public void setEntity(ILanguage entity)
  {
    this.entity = entity;
  }
}
