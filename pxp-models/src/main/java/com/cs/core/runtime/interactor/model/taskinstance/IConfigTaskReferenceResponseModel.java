package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigTaskReferenceResponseModel extends IModel {
  
  public static final String LABEL         = "label";
  public static final String PREVIEW_IMAGE = "previewImage";
  public static final String CODE          = "code";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getPreviewImage();
  
  public void setPreviewImage(String previewImage);
  
  public String getCode();
  
  public void setCode(String code);
}
