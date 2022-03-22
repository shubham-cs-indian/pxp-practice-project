package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;
import java.util.Map;

public interface IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel extends IModel {
  
  public static final String NATURE_REL_ID_SIDE_ID                    = "natureRelIdSideId";
  public static final String NATURE_REL_SIDE_IDS_TO_INHERIT           = "natureRelSideIdsToInherit";
  public static final String CONIGURED_REL_SIDES_PER_NATURE_REL_SIDES = "configuredRelSidesPerNatureRelSides";
  public static final String REFERENCED_RELATIONSHIPS                 = "referencedRelationships";
  public static final String NOT_APPLICABLE_RELID_SIDEIDS             = "notApplicableRelIdSideIds";
  public static final String NOT_APPLICABLE_NATURE_SIDEIDS            = "notApplicableNatureSideIds";
  
  // key:natureRelId
  public Map<String, String> getNatureRelIdSideId();
  
  public void setNatureRelIdSideId(Map<String, String> natureRelIdSideId);
  
  public Map<String, String> getNatureRelSideIdsToInherit();
  
  public void setNatureRelSideIdsToInherit(Map<String, String> natureRelSideIdsToInherit);
  
  // key:natureRelId_REL_SIDE_natureRelSideId
  // key for internal map :relId_REL_SIDE_relSideId
  public Map<String, Map<String, IRelationshipConflictingSource>> getConfiguredRelSidesPerNatureRelSides();
  
  public void setConfiguredRelSidesPerNatureRelSides(
      Map<String, Map<String, IRelationshipConflictingSource>> configuredRelSidesPerNatureRelSides);
  
  // key:relationshipId
  public Map<String, IReferencedRelationshipInheritanceModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships);
  
  public List<String> getNotApplicableRelIdSideIds();
  
  public void setNotApplicableRelIdSideIds(List<String> notApplicableRelIdSideIds);
  
  public List<String> getNotApplicableNatureSideIds();
  
  public void setNotApplicableNatureSideIds(List<String> notApplicableNatureSideIds);
}
