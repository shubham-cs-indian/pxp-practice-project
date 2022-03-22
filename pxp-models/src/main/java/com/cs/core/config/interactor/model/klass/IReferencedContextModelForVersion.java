package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IReferencedContextModelForVersion extends IModel {
  
  public static final String ATTRIBUTE_IDS   = "attributeIds";
  public static final String TAG_IDS         = "tagIds";
  public static final String ID              = "id";
  public static final String LABEL           = "label";
  public static final String TYPE            = "type";
  public static final String CONTEXT_TAG_IDS = "contextTagIds";
  public static final String IS_TIME_ENABLED = "isTimeEnabled";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public List<IReferencedVariantContextTagsModel> getContextTagIds();
  
  public void setContextTagIds(List<IReferencedVariantContextTagsModel> contextTagIds);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public Boolean getIsTimeEnabled();
  
  public void setIsTimeEnabled(Boolean isTimeEnabled);
}
