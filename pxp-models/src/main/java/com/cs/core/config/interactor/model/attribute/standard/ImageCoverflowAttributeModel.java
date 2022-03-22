package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.IImageCoverflowAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class ImageCoverflowAttributeModel extends AbstractMandatoryAttributeModel
    implements IImageCoverflowAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public ImageCoverflowAttributeModel()
  {
    super(new ImageCoverflowAttribute(), Renderer.COVERFLOW.toString());
  }
  
  public ImageCoverflowAttributeModel(IImageCoverflowAttribute attribute)
  {
    super(attribute, Renderer.COVERFLOW.toString());
  }
  
  @Override
  public Integer getNumberOfItemsAllowed()
  {
    return ((IImageCoverflowAttribute) attribute).getNumberOfItemsAllowed();
  }
  
  @Override
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed)
  {
    ((IImageCoverflowAttribute) attribute).setNumberOfItemsAllowed(numberOfItemsAllowed);
  }
}
