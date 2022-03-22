package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.FileSupplierAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class FileSupplierAttributeModel extends AbstractMandatoryAttributeModel
    implements IFileSupplierAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public FileSupplierAttributeModel()
  {
    super(new FileTypeAttribute(), Renderer.TEXT.toString());
  }
  
  public FileSupplierAttributeModel(FileSupplierAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
