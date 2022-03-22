package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.cs.core.runtime.interactor.usecase.comparison.RelationshipInstancesCustomDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassInstancesForComparisonModel implements IKlassInstancesForComparisonModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected IKlassInstanceInformationModel                         klassInstance;
  protected Map<String, List<IRelationshipInstance>> contentRelationships;
  protected Map<String, List<IRelationshipInstance>> natureRelationships;
  
  @JsonDeserialize(as = AbstractContentInstance.class)
  @Override
  public IKlassInstanceInformationModel getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstanceInformationModel klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public Map<String, List<IRelationshipInstance>> getContentRelationships()
  {
    return contentRelationships;
  }
  
  @JsonDeserialize(contentUsing = RelationshipInstancesCustomDeserializer.class)
  @Override
  public void setContentRelationships(Map<String, List<IRelationshipInstance>> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public Map<String, List<IRelationshipInstance>> getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @JsonDeserialize(contentUsing = RelationshipInstancesCustomDeserializer.class)
  @Override
  public void setNatureRelationships(Map<String, List<IRelationshipInstance>> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
}
