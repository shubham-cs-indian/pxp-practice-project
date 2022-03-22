package com.cs.core.config.interactor.model.smartdocument.template;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.smartdocument.template.ISmartDocumentTemplate;
import com.cs.core.config.interactor.entity.smartdocument.template.SmartDocumentTemplate;

public class SmartDocumentTemplateModel extends SmartDocumentTemplate
    implements ISmartDocumentTemplateModel {
  
  private static final long        serialVersionUID      = 1L;
  protected ISmartDocumentTemplate smartDocumentTemplate = new SmartDocumentTemplate();
  
  @Override
  public IEntity getEntity()
  {
    return smartDocumentTemplate;
  }
}
