package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileMappedToKlassAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute;

public class SaveFileMappedToKlassAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements IFileMappedToKlassAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveFileMappedToKlassAttributeModel()
  {
    super(new FileTypeAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveFileMappedToKlassAttributeModel(FileMappedToKlassAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
