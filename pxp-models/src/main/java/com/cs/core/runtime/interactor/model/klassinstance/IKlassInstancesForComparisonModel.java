package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassInstancesForComparisonModel extends IModel {
  
  public static String KLASSINSTANCE         = "klassInstance";
  public static String CONTENT_RELATIONSHIPS = "contentRelationships";
  public static String NATURE_RELATIONSHIPS  = "natureRelationships";
  
  public IKlassInstanceInformationModel getKlassInstance();
  
  public void setKlassInstance(IKlassInstanceInformationModel klassInstance);
  
  public Map<String, List<IRelationshipInstance>> getContentRelationships();
  
  public void setContentRelationships(
      Map<String, List<IRelationshipInstance>> contentRelationships);
  
  public Map<String, List<IRelationshipInstance>> getNatureRelationships();
  
  public void setNatureRelationships(Map<String, List<IRelationshipInstance>> natureRelationships);
}
