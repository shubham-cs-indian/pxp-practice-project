package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigDetailsForQuickListModel implements IConfigDetailsForQuickListModel {
  
  private static final long         serialVersionUID = 1L;
  protected List<String>            displayTagIds;
  protected List<String>            relevanceTagIds;
  protected Set<String>             klassIdsHavingRP;
  protected Set<String>             entities;
  protected List<String>            allowedTypes;
  protected String                  klassType;
  protected IXRayConfigDetailsModel xRayConfigDetails;
  protected Set<String>             taxonomyIdsHavingRP;
  protected Set<String>             klassIdsHavingCP;
  protected IRelationship           relationshipConfig;
  protected List<String>            klassRelationshipIds;
  protected List<String>            side2LinkedVariantKrIds;
  
  @Override
  public List<String> getDisplayTagIds()
  {
    return displayTagIds;
  }
  
  @Override
  public void setDisplayTagIds(List<String> displayTagIds)
  {
    this.displayTagIds = displayTagIds;
  }
  
  @Override
  public List<String> getRelevanceTagIds()
  {
    return relevanceTagIds;
  }
  
  @Override
  public void setRelevanceTagIds(List<String> relevanceTagIds)
  {
    this.relevanceTagIds = relevanceTagIds;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(Set<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public List<String> getAllowedTypes()
  {
    return allowedTypes;
  }
  
  @Override
  public void setAllowedTypes(List<String> allowedTypes)
  {
    this.allowedTypes = allowedTypes;
  }
  
  @Override
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public Set<String> getKlassIdsHavingCP()
  {
    if (klassIdsHavingCP == null) {
      klassIdsHavingCP = new HashSet<>();
    }
    return klassIdsHavingCP;
  }
  
  @Override
  public void setKlassIdsHavingCP(Set<String> klassIdsHavingCP)
  {
    this.klassIdsHavingCP = klassIdsHavingCP;
  }
  
  @Override
  public IRelationship getRelationshipConfig()
  {
    return this.relationshipConfig;
  }
  
  @JsonDeserialize(as = Relationship.class)
  @Override
  public void setRelationshipConfig(IRelationship relationshipConfig)
  {
    this.relationshipConfig = relationshipConfig;
  }
  
  @Override
  public List<String> getKlassRelationshipIds()
  {
    
    if (klassRelationshipIds == null) {
      klassRelationshipIds = new ArrayList<>();
    }
    return klassRelationshipIds;
  }
  
  @Override
  public void setKlassRelationshipIds(List<String> klassRelationshipIds)
  {
    this.klassRelationshipIds = klassRelationshipIds;
  }
  
  @Override
  public List<String> getSide2LinkedVariantKrIds()
  {
    if (side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<String>();
    }
    return side2LinkedVariantKrIds;
  }
  
  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }
}
