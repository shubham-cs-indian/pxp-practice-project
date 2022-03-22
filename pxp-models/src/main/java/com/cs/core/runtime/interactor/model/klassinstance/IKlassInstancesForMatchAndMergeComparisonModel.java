package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IKlassInstancesForMatchAndMergeComparisonModel extends IModel {
  
  public static String KLASSINSTANCE         = "klassInstance";
  public static String CONTENT_RELATIONSHIPS = "contentRelationships";
  public static String NATURE_RELATIONSHIPS  = "natureRelationships";
  
  public IContentInstance getKlassInstance();
  
  public void setKlassInstance(IContentInstance klassInstance);
  
  public Map<String, List<IRelationshipInstance>> getContentRelationships();
  
  public void setContentRelationships(
      Map<String, List<IRelationshipInstance>> contentRelationships);
  
  public Map<String, List<IRelationshipInstance>> getNatureRelationships();
  
  public void setNatureRelationships(Map<String, List<IRelationshipInstance>> natureRelationships);
}
