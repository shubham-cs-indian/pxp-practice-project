package com.cs.core.config.interactor.model.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RelationshipInstanceModel implements IRelationshipInstanceModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements;
  protected Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements;
  protected List<IKlassInstanceRelationshipInstance>          contentRelationships;
  protected List<IKlassInstanceRelationshipInstance>          natureRelationships;
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements()
  {
    if (referenceRelationshipInstanceElements == null) {
      referenceRelationshipInstanceElements = new HashMap<>();
    }
    return referenceRelationshipInstanceElements;
  }
  
  @JsonDeserialize(contentUsing = customDeserializer.class)
  @Override
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements)
  {
    this.referenceRelationshipInstanceElements = referenceRelationshipInstanceElements;
  }
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements()
  {
    if (referencedNatureRelationshipInstanceElements == null) {
      referencedNatureRelationshipInstanceElements = new HashMap<>();
    }
    return referencedNatureRelationshipInstanceElements;
  }
  
  @Override
  @JsonDeserialize(contentUsing = customDeserializer.class)
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements)
  {
    this.referencedNatureRelationshipInstanceElements = referencedNatureRelationshipInstanceElements;
  }
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getContentRelationships()
  {
    if (contentRelationships == null) {
      contentRelationships = new ArrayList<>();
    }
    return contentRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  public void setContentRelationships(List<IKlassInstanceRelationshipInstance> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships()
  {
    if (natureRelationships == null) {
      natureRelationships = new ArrayList<>();
    }
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
}
