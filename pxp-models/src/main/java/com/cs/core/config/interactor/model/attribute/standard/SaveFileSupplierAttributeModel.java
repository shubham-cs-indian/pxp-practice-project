package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileSupplierAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute;

public class SaveFileSupplierAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements IFileSupplierAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveFileSupplierAttributeModel()
  {
    super(new FileTypeAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveFileSupplierAttributeModel(FileSupplierAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
