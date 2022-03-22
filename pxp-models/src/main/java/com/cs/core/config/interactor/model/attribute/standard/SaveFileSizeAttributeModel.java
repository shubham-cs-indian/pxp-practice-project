package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileSizeAttribute;

public class SaveFileSizeAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements IFileSizeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveFileSizeAttributeModel()
  {
    super(new FileSizeAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveFileSizeAttributeModel(FileSizeAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
