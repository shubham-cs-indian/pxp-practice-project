package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.relationship.ElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.relationship.IElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassInstanceRelationshipInstance implements IKlassInstanceRelationshipInstance {
  
  private static final long                               serialVersionUID  = 1L;
  
  protected String                                        id;
  protected String                                        relationshipId;
  protected List<String>                                  elementIds;
  protected Long                                          totalCount = 0L;
  protected Map<String, List<IContentTagInstance>>        elementTagMapping = new HashMap<>();
  protected Map<String, IInstanceTimeRange>               elementTimeRangeMapping;
  protected String                                        sideId;
  protected Map<String, IElementsRelationshipInformation> elementsRelationshipInfo;
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
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
  public List<String> getElementIds()
  {
    if (elementIds == null) {
      elementIds = new ArrayList<>();
    }
    return elementIds;
  }
  
  @Override
  public void setElementIds(List<String> elementIds)
  {
    this.elementIds = elementIds;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public Long getTotalCount()
  {
    
    return totalCount;
  }
  
  @Override
  public Map<String, List<IContentTagInstance>> getElementTagMapping()
  {
    return elementTagMapping;
  }
  
  @Override
  public void setElementTagMapping(Map<String, List<IContentTagInstance>> elementTagMapping)
  {
    this.elementTagMapping = elementTagMapping;
  }
  
  @Override
  public Map<String, IInstanceTimeRange> getElementTimeRangeMapping()
  {
    if (elementTimeRangeMapping == null) {
      elementTimeRangeMapping = new HashMap<String, IInstanceTimeRange>();
    }
    return elementTimeRangeMapping;
  }
  
  @Override
  @JsonDeserialize(contentAs = InstanceTimeRange.class)
  public void setElementTimeRangeMapping(Map<String, IInstanceTimeRange> elementTimeRangeMapping)
  {
    this.elementTimeRangeMapping = elementTimeRangeMapping;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public Map<String, IElementsRelationshipInformation> getElementsRelationshipInfo()
  {
    if (elementsRelationshipInfo == null) {
      elementsRelationshipInfo = new HashMap<>();
    }
    return elementsRelationshipInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = ElementsRelationshipInformation.class)
  public void setElementsRelationshipInfo(
      Map<String, IElementsRelationshipInformation> elementsRelationshipInfo)
  {
    this.elementsRelationshipInfo = elementsRelationshipInfo;
  }
  
  /** ******************** Ignore Fields ********************* */
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
}
