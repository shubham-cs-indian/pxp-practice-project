package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IContentRelationshipInformationModel extends IModel {
  
  public static final String RELATIONSHIPS = "relationships";
  
  public List<IKlassInstanceRelationshipInstance> getRelationships();
  
  public void setRelationships(List<IKlassInstanceRelationshipInstance> relationships);
}
