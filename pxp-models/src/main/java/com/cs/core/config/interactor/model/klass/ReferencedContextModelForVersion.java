package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedVariantContextTagsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ReferencedContextModelForVersion implements IReferencedContextModelForVersion {
  
  private static final long                          serialVersionUID = 1L;
  
  protected List<String>                             attributeIds     = new ArrayList<>();
  protected List<String>                             tagIds           = new ArrayList<>();
  protected String                                   id;
  protected String                                   label;
  protected String                                   type;
  protected List<IReferencedVariantContextTagsModel> contextTagIds;
  protected Boolean                                  isTimeEnabled    = false;
  
  @Override
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getTagIds()
  {
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
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
  public List<IReferencedVariantContextTagsModel> getContextTagIds()
  {
    return this.contextTagIds;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextTagsModel.class)
  public void setContextTagIds(List<IReferencedVariantContextTagsModel> contextTagIds)
  {
    this.contextTagIds = contextTagIds;
  }
  
  @Override
  public Boolean getIsTimeEnabled()
  {
    return isTimeEnabled;
  }
  
  @Override
  public void setIsTimeEnabled(Boolean isTimeEnabled)
  {
    this.isTimeEnabled = isTimeEnabled;
  }
}
