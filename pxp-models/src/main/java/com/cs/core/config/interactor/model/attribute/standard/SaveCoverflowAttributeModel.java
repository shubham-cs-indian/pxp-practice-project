package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.CoverflowAttribute;
import com.cs.core.config.interactor.entity.attribute.ICoverflowAttribute;

public class SaveCoverflowAttributeModel extends AbstractSaveAttributeModel
    implements ICoverflowAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveCoverflowAttributeModel()
  {
    super(new CoverflowAttribute(), Renderer.COVERFLOW.toString());
  }
  
  public SaveCoverflowAttributeModel(ICoverflowAttribute attribute)
  {
    super(attribute, Renderer.COVERFLOW.toString());
  }
  
  @Override
  public Integer getNumberOfItemsAllowed()
  {
    return ((ICoverflowAttribute) attribute).getNumberOfItemsAllowed();
  }
  
  @Override
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed)
  {
    ((ICoverflowAttribute) attribute).setNumberOfItemsAllowed(numberOfItemsAllowed);
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
