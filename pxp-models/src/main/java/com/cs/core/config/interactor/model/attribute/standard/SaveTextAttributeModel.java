package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.ITextAttribute;
import com.cs.core.config.interactor.entity.attribute.TextAttribute;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SaveTextAttributeModel extends AbstractSaveAttributeModel
    implements ITextAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveTextAttributeModel()
  {
    super(new TextAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveTextAttributeModel(ITextAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
