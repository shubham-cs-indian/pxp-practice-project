package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.Map;

import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.ReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassInstanceWithDataRuleAndNatureRelationshipsModel
    extends KlassInstanceWithDataRuleModel
    implements IKlassInstanceWithDataRuleAndNatureRelationshipsModel {
  
  private static final long                                     serialVersionUID = 1L;
  
  protected Map<String, IReferencedNatureRelationshipModel>     referencedNatureRelationships;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  
  @Override
  public Map<String, IReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ReferencedNatureRelationshipModel.class)
  @Override
  public void setReferencedNatureRelationships(
      Map<String, IReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  @Override
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
}
