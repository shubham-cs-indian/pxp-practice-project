package com.cs.core.runtime.interactor.model.taskinstance;

public class ConfigTaskReferenceResponseModel implements IConfigTaskReferenceResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  protected String          previewImage;
  protected String          code;
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getPreviewImage()
  {
    return this.previewImage;
  }
  
  @Override
  public void setPreviewImage(String previewImage)
  {
    this.previewImage = previewImage;
  }
  
  @Override
  public String getCode()
  {
    return this.code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
}
