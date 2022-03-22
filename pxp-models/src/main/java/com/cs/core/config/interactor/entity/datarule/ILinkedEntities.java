package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.propertycollection.IPosition;

import java.io.Serializable;

public interface ILinkedEntities extends Serializable {
  
  public static final String ID         = "id";
  public static final String CONTENT_ID = "contentId";
  public static final String ELEMENT_ID = "elementId";
  public static final String VARIANT_ID = "variantId";
  public static final String TYPE       = "type";
  public static final String POSITION   = "position";
  
  public String getId();
  
  public void setId(String id);
  
  public String getElementId();
  
  public void setElementId(String contentId);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
  
  public String getType();
  
  public void setType(String type);
  
  public IPosition getPosition();
  
  public void setPosition(IPosition position);
}
