package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileSizeAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class FileSizeAttributeModel extends AbstractMandatoryAttributeModel
    implements IFileSizeAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public FileSizeAttributeModel()
  {
    super(new FileSizeAttribute(), Renderer.TEXT.toString());
  }
  
  public FileSizeAttributeModel(FileSizeAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
