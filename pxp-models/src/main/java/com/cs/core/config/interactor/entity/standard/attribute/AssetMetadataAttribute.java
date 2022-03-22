package com.cs.core.config.interactor.entity.standard.attribute;

public class AssetMetadataAttribute extends AbstractStandardAttribute
    implements IAssetMetadataAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected String          label;
  
  protected Boolean         isStandard       = true;
  
  protected Boolean         isDisabled       = true;
  
  protected String          rendererType     = Renderer.TEXT.name();
  
  public AssetMetadataAttribute()
  {
  }
  
  public AssetMetadataAttribute(String label)
  {
    this.label = label;
  }
  
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
  public String getRendererType()
  {
    return rendererType;
  }
  
  @Override
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
}
