package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.DigitalStorageAttribute;
import com.cs.core.config.interactor.entity.attribute.IDigitalStorageAttribute;

public class SaveDigitalStorageAttributeModel extends AbstractSaveUnitAttributeModel
    implements IDigitalStorageAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveDigitalStorageAttributeModel()
  {
    super(new DigitalStorageAttribute(), Renderer.DIGITAL_STORAGE.toString());
  }
  
  public SaveDigitalStorageAttributeModel(IDigitalStorageAttribute attribute)
  {
    super(attribute, Renderer.DIGITAL_STORAGE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
