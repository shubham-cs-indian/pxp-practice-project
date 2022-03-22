package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class FileTypeAttributeModel extends AbstractMandatoryAttributeModel
    implements IFileTypeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public FileTypeAttributeModel()
  {
    super(new FileTypeAttribute(), Renderer.TEXT.toString());
  }
  
  public FileTypeAttributeModel(FileTypeAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
