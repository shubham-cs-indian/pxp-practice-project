package com.cs.core.config.interactor.entity.visualattribute;

public class ImageVisualAttributeFrameStructureValidator extends ImageFrameStructureValidator
    implements IImageVisualAttributeStructureValidator {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         shouldVersion;
  
  @Override
  public Boolean getShouldVersion()
  {
    return shouldVersion;
  }
  
  @Override
  public void setShouldVersion(Boolean shouldVersion)
  {
    this.shouldVersion = shouldVersion;
  }
}
