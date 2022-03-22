package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileNameAttribute;

public class SaveFileNameAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements IFileNameAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveFileNameAttributeModel()
  {
    super(new FileNameAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveFileNameAttributeModel(FileNameAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
