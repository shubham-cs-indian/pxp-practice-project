package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ContentRelationshipsInformationModel implements IContentRelationshipInformationModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected List<IKlassInstanceRelationshipInstance> relationships    = new ArrayList<>();
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getRelationships()
  {
    return relationships;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  @Override
  public void setRelationships(List<IKlassInstanceRelationshipInstance> relationships)
  {
    this.relationships = relationships;
  }
}
