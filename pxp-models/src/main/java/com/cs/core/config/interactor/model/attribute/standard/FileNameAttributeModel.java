package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileNameAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class FileNameAttributeModel extends AbstractMandatoryAttributeModel
    implements IFileNameAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public FileNameAttributeModel()
  {
    super(new FileNameAttribute(), Renderer.TEXT.toString());
  }
  
  public FileNameAttributeModel(FileNameAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
