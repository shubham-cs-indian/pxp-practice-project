package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute;

public class SaveFileTypeAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements IFileTypeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveFileTypeAttributeModel()
  {
    super(new FileTypeAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveFileTypeAttributeModel(FileTypeAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
