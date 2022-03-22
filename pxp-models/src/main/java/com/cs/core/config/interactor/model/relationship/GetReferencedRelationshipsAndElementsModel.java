package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class GetReferencedRelationshipsAndElementsModel
    implements IGetReferencedRelationshipsAndElementsModel {
  
  private static final long                                    serialVersionUID = 1L;
  
  protected Map<String, IReferencedSectionElementModel>        referencedElements;
  protected Map<String, IRelationship>                         referencedRelationships;
  protected Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships;
  protected List<String>                                       relationshipIds;
  protected List<String>                                       natureRelationshipIds;
  protected List<String>                                       klassIds;
  protected Map<String, String>                                referencedRelationshipsMapping;
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, IRelationship> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = Relationship.class)
  public void setReferencedRelationships(Map<String, IRelationship> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public Map<String, String> getReferencedRelationshipsMapping()
  {
    return referencedRelationshipsMapping;
  }
  
  @Override
  public void setReferencedRelationshipsMapping(Map<String, String> referencedRelationshipsMapping)
  {
    this.referencedRelationshipsMapping = referencedRelationshipsMapping;
  }
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    
    return natureRelationshipIds;
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    this.natureRelationshipIds = natureRelationshipIds;
  }
}
