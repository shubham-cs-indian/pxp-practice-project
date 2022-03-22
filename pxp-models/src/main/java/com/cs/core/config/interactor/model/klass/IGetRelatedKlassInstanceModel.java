package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetRelatedKlassInstanceModel extends IModel {
  
  public String getRelationshipInstanceId();
  
  public void setRelationshipInstanceId(String relationshipInstanceId);
  
  public String getSelfKlassId();
  
  public void setSelfKlassId(String selfKlassId);
}
