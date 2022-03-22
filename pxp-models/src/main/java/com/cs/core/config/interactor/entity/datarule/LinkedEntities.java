package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.propertycollection.SectionElementPosition;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class LinkedEntities implements ILinkedEntities {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          contentId;
  protected String          elementId;
  protected String          variantId;
  protected String          type;
  protected IPosition       position;
  
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
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getElementId()
  {
    return elementId;
  }
  
  @Override
  public void setElementId(String elementId)
  {
    this.elementId = elementId;
  }
  
  @Override
  public String getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(String variantId)
  {
    this.variantId = variantId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public IPosition getPosition()
  {
    if(position == null) {
      position = new SectionElementPosition();
    }
    return position;
  }
  
  @JsonDeserialize(as = SectionElementPosition.class)
  @Override
  public void setPosition(IPosition position)
  {
    this.position = (SectionElementPosition) position;
  }
}
