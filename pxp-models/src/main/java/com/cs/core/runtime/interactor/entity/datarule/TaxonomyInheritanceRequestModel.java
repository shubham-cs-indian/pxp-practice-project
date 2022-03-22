package com.cs.core.runtime.interactor.entity.datarule;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict.Setting;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TaxonomyInheritanceRequestModel implements ITaxonomyInheritanceRequestModel {
  
  private static final long      serialVersionUID   = 1L;
  protected String                 contentId;
  protected String               contentBaseType;
  protected String               sourceContentId;
  protected String               sourceContentBaseType;
  protected String               relationshipId;
  protected String               relationshipSideId;
  protected Setting              taxonomyInheritanceSetting;
  protected List<IIdAndBaseType> addedElements;
  protected List<String>         addedTaxonomyIds   = new ArrayList<String>();
  protected List<String>         removedTaxonomyIds = new ArrayList<String>();
  
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
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
  @Override
  public String getSourceContentBaseType()
  {
    return sourceContentBaseType;
  }
  
  @Override
  public void setSourceContentBaseType(String sourceContentBaseType)
  {
    this.sourceContentBaseType = sourceContentBaseType;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getRelationshipSideId()
  {
    return relationshipSideId;
  }
  
  @Override
  public void setRelationshipSideId(String relationshipSideId)
  {
    this.relationshipSideId = relationshipSideId;
  }
  
  @Override
  public Setting getTaxonomyInheritanceSetting()
  {
    if (taxonomyInheritanceSetting == null) {
      taxonomyInheritanceSetting = Setting.off;
    }
    return taxonomyInheritanceSetting;
  }
  
  @Override
  public void setTaxonomyInheritanceSetting(Setting taxonomyInheritanceSetting)
  {
    this.taxonomyInheritanceSetting = taxonomyInheritanceSetting;
  }
  
  @Override
  public List<IIdAndBaseType> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  @Override
  public void setAddedElements(List<IIdAndBaseType> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public String getContentBaseType()
  {
    return contentBaseType;
  }
  
  @Override
  public void setContentBaseType(String contentBaseType)
  {
    this.contentBaseType = contentBaseType;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getRemovedTaxonomyIds()
  {
    return removedTaxonomyIds;
  }
  
  @Override
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds)
  {
    this.removedTaxonomyIds = removedTaxonomyIds;
  }
  
}
