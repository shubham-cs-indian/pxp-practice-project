package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.Map;

public interface IKlassInstanceWithDataRuleAndNatureRelationshipsModel
    extends IKlassInstanceWithDataRuleModel {
  
  public static final String REFERENCED_NATURE_RELATIONSHIPS    = "referencedNatureRelationships";
  public static final String REFERENCED_RELATIONSHIP_PROPERTIES = "referencedRelationshipProperties";
  
  public Map<String, IReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
}
