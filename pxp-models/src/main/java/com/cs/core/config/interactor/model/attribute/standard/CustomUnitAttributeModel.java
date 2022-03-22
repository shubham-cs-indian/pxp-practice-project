package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.CustomUnitAttribute;
import com.cs.core.config.interactor.entity.attribute.ICustomUnitAttribute;

public class CustomUnitAttributeModel extends AbstractUnitAttributeModel
    implements ICustomUnitAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public CustomUnitAttributeModel()
  {
    super(new CustomUnitAttribute(), Renderer.CUSTOM.toString());
  }
  
  public CustomUnitAttributeModel(ICustomUnitAttribute attribute)
  {
    super(attribute, Renderer.CUSTOM.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
  
  @Override
  public String getDefaultUnitAsHTML()
  {
    return ((ICustomUnitAttribute) attribute).getDefaultUnitAsHTML();
  }
  
  @Override
  public void setDefaultUnitAsHTML(String defaultUnitAsHTML)
  {
    ((ICustomUnitAttribute) attribute).setDefaultUnitAsHTML(defaultUnitAsHTML);
  }
}
