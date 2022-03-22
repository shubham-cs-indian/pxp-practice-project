package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllRelationshipInstancesModel implements IGetAllRelationshipInstancesModel {
  
  private static final long                                    serialVersionUID                     = 1L;
  protected String                                             id;
  protected Map<String, IReferencedSectionElementModel>        referencedElements;
  protected Map<String, IRelationship>                         referencedRelationships;
  protected Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships;
  protected Map<String, List<IKlassInstanceInformationModel>>  referenceRelationshipInstances       = new HashMap<>();
  protected Map<String, List<IKlassInstanceInformationModel>>  referenceNatureRelationshipInstances = new HashMap<>();
  protected List<IKlassInstanceRelationshipInstance>           contentRelationships;
  protected List<IKlassInstanceRelationshipInstance>           natureRelationships;
  protected Map<String, String>                                referencedRelationshipsMapping;
  
  @Override
  public String getId()
  {
    
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
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
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements()
  {
    return referenceRelationshipInstances;
  }
  
  @JsonDeserialize(contentUsing = customDeserializer.class)
  @Override
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances)
  {
    this.referenceRelationshipInstances = referenceRelationshipInstances;
  }
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getContentRelationships()
  {
    return this.contentRelationships;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  @Override
  public void setContentRelationships(List<IKlassInstanceRelationshipInstance> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
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
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships()
  {
    
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
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
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements()
  {
    
    return referenceNatureRelationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentUsing = customDeserializer.class)
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipInstances)
  {
    this.referenceNatureRelationshipInstances = referenceNatureRelationshipInstances;
  }
}
