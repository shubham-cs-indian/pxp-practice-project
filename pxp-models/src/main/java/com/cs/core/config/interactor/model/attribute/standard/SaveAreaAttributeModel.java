package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.AreaAttribute;
import com.cs.core.config.interactor.entity.attribute.IAreaAttribute;

public class SaveAreaAttributeModel extends AbstractSaveUnitAttributeModel
    implements IAreaAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveAreaAttributeModel()
  {
    super(new AreaAttribute(), Renderer.AREA.toString());
  }
  
  public SaveAreaAttributeModel(IAreaAttribute attribute)
  {
    super(attribute, Renderer.AREA.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
