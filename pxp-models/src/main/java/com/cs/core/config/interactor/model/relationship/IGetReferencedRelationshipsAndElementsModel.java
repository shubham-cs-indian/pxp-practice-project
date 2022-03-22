package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;

import java.util.List;
import java.util.Map;

public interface IGetReferencedRelationshipsAndElementsModel extends IModel {
  
  public static final String RELATIONSHIP_IDS                 = "relationshipIds";
  public static final String NATURE_RELATIONSHIP_IDS          = "natureRelationshipIds";
  public static final String REFERENCED_ELEMENTS              = "referencedElements";
  public static final String REFERENCED_RELATIONSHIPS         = "referencedRelationships";
  public static final String REFERENCED_NATURE_RELATIONSHIPS  = "referencedNatureRelationships";
  public static final String KLASS_IDS                        = "klassIds";
  public static final String REFERENCED_RELATIONSHIPS_MAPPING = "referencedRelationshipsMapping";
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public List<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(List<String> natureRelationshipIds);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public Map<String, IRelationship> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IRelationship> referencedNatureRelationships);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public Map<String, String> getReferencedRelationshipsMapping();
  
  public void setReferencedRelationshipsMapping(Map<String, String> referencedRelationshipsMapping);
}
