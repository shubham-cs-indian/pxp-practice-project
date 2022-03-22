package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileMappedToKlassAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class FileMappedToKlassAttributeModel extends AbstractMandatoryAttributeModel
    implements IFileMappedToKlassAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public FileMappedToKlassAttributeModel()
  {
    super(new FileTypeAttribute(), Renderer.TEXT.toString());
  }
  
  public FileMappedToKlassAttributeModel(FileMappedToKlassAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
