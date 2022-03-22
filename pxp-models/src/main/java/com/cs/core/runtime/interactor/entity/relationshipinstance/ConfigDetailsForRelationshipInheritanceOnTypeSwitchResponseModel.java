package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel  implements IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected Map<String, String>                                           natureRelIdSideId;
  protected Map<String, Map<String, IRelationshipConflictingSource>>      configuredRelSidesPerNatureRelSides;
  protected Map<String, IReferencedRelationshipInheritanceModel>          referencedRelationships;
  protected Map<String, String>                                           natureRelSideIdsToInherit;
  protected List<String>                                                  notApplicableRelIdSideIds;
  protected List<String>                                                  notApplicableNatureSideIds;
  
  @Override
  public Map<String, String> getNatureRelIdSideId()
  {
    if(natureRelIdSideId == null) {
      natureRelIdSideId = new HashMap<>();
    }
    return natureRelIdSideId;
  }
  
  @Override
  public void setNatureRelIdSideId(Map<String, String> natureRelIdSideId)
  {
    this.natureRelIdSideId = natureRelIdSideId;
  }
  
  @Override
  public Map<String, String> getNatureRelSideIdsToInherit()
  {
    if(natureRelSideIdsToInherit == null) {
      natureRelSideIdsToInherit = new HashMap<>();
    }
    return natureRelSideIdsToInherit;
  }
  
  @Override
  public void setNatureRelSideIdsToInherit(Map<String, String> natureRelSideIdsToInherit)
  {
   this.natureRelSideIdsToInherit = natureRelSideIdsToInherit; 
  }
  
  @Override
  public Map<String, Map<String, IRelationshipConflictingSource>> getConfiguredRelSidesPerNatureRelSides()
  {
    if(configuredRelSidesPerNatureRelSides == null) {
      configuredRelSidesPerNatureRelSides = new HashMap<>();
    }
    return configuredRelSidesPerNatureRelSides;
  }
  
  @JsonDeserialize(contentUsing = ConfiguredRelsPerNatureRelsCustomDeserializer.class)
  @Override
  public void setConfiguredRelSidesPerNatureRelSides(
      Map<String, Map<String, IRelationshipConflictingSource>> configuredRelSidesPerNatureRelSides)
  {
    this.configuredRelSidesPerNatureRelSides = configuredRelSidesPerNatureRelSides;
  }
  
  @Override
  public Map<String, IReferencedRelationshipInheritanceModel> getReferencedRelationships()
  {
    if(referencedRelationships == null) {
      referencedRelationships = new HashMap<>();
    }
    return referencedRelationships;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipInheritanceModel.class)
  @Override
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }

  @Override
  public List<String> getNotApplicableRelIdSideIds()
  {
    if(notApplicableRelIdSideIds == null) {
      notApplicableRelIdSideIds = new ArrayList<>();
    }
    return notApplicableRelIdSideIds;
  }

  @Override
  public void setNotApplicableRelIdSideIds(List<String> notApplicableRelIdSideIds)
  {
    this.notApplicableRelIdSideIds = notApplicableRelIdSideIds;
  }

  @Override
  public List<String> getNotApplicableNatureSideIds()
  {
    if(notApplicableNatureSideIds == null) {
      notApplicableNatureSideIds = new ArrayList<>();
    }
    return notApplicableNatureSideIds;
  }

  @Override
  public void setNotApplicableNatureSideIds(List<String> notApplicableNatureSideIds)
  {
    this.notApplicableNatureSideIds = notApplicableNatureSideIds;
  }
  
  
}