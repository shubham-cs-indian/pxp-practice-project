package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.relationship.IElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import java.util.List;
import java.util.Map;

public interface IKlassInstanceRelationshipInstance extends IEntity {
  
  public static final String RELATIONSHIP_ID            = "relationshipId";
  public static final String ELEMENT_IDS                = "elementIds";
  public static final String TOTAL_COUNT                = "totalCount";
  public static final String ELEMENT_TAGS_MAPPING       = "elementTagMapping";
  public static final String ELEMENT_TIME_RANGE_MAPPING = "elementTimeRangeMapping";
  public static final String SIDE_ID                    = "sideId";
  public static final String ELEMENTS_RELATIONSHIP_INFO = "elementsRelationshipInfo";
  
  public void setRelationshipId(String relationshipId);
  
  public String getRelationshipId();
  
  public List<String> getElementIds();
  
  public void setElementIds(List<String> elementIds);
  
  public void setTotalCount(Long totalCount);
  
  public Long getTotalCount();
  
  public Map<String, IInstanceTimeRange> getElementTimeRangeMapping();
  
  public void setElementTimeRangeMapping(Map<String, IInstanceTimeRange> elementTimeRangeMapping);
  
  public Map<String, List<IContentTagInstance>> getElementTagMapping();
  
  public void setElementTagMapping(Map<String, List<IContentTagInstance>> elementTagMapping);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public Map<String, IElementsRelationshipInformation> getElementsRelationshipInfo();
  
  public void setElementsRelationshipInfo(
      Map<String, IElementsRelationshipInformation> elementsRelationshipInfo);
}
