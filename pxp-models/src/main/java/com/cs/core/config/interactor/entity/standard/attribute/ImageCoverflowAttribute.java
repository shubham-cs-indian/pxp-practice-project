package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.config.standard.IStandardConfig;

public class ImageCoverflowAttribute extends AbstractStandardAttribute
    implements IImageCoverflowAttribute {
  
  private static final long serialVersionUID     = 1L;
  
  protected Integer         numberOfItemsAllowed = 1;
  
  protected String          id                   = IStandardConfig.StandardProperty.assetcoverflowattribute
      .toString();
  
  protected String          label                = "File Preview";
  
  protected Boolean         isStandard           = true;
  
  protected String          rendererType         = Renderer.COVERFLOW.name();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Integer getNumberOfItemsAllowed()
  {
    return numberOfItemsAllowed;
  }
  
  @Override
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed)
  {
    this.numberOfItemsAllowed = numberOfItemsAllowed;
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
