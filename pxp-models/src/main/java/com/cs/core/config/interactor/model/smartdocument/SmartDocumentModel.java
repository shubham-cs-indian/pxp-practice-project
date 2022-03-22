package com.cs.core.config.interactor.model.smartdocument;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.smartdocument.ISmartDocument;
import com.cs.core.config.interactor.entity.smartdocument.SmartDocument;

public class SmartDocumentModel extends SmartDocument implements ISmartDocumentModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ISmartDocument  smartDocument    = new SmartDocument();
  
  @Override
  public IEntity getEntity()
  {
    return smartDocument;
  }
}
