package com.cs.core.runtime.interactor.model.version;

import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;

import java.util.List;
import java.util.Map;

public interface IKlassInstanceVersionsComparisonModel extends IGetInstanceRequestStrategyModel {
  
  public static final String REFERENCED_NATURE_RELATIONSHIPS_IDS = "referencedNatureRelationshipsIds";
  public static final String REFERENCED_RELATIONSHIPS_IDS        = "referencedRelationshipsIds";
  public static final String REFERENCED_LIFECYCLE_STATUS_TAGS    = "referencedLifeCycleStatusTags";
  public static final String VERSION_IDS                         = "versionIds";
  public static final String REFERENCED_ELEMENTS                 = "referencedElements";
  
  public List<String> getReferencedNatureRelationshipsIds();
  
  public void setReferencedNatureRelationshipsIds(List<String> referencedNatureRelationships);
  
  public List<String> getReferencedRelationshipsIds();
  
  public void setReferencedRelationshipsIds(List<String> referencedRelationshipsIds);
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
  
  public List<String> getVersionIds();
  
  public void setVersionIds(List<String> versionIds);
  
  // key:propertyId[attributeId, tagId, roleId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
}
