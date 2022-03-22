package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.config.interactor.model.klass.IGetRelatedKlassInstanceModel;

public class GetRelatedKlassInstanceModel implements IGetRelatedKlassInstanceModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          relationshipInstanceId;
  
  protected String          selfKlassId;
  
  public String getRelationshipInstanceId()
  {
    return relationshipInstanceId;
  }
  
  public void setRelationshipInstanceId(String relationshipInstanceId)
  {
    this.relationshipInstanceId = relationshipInstanceId;
  }
  
  public String getSelfKlassId()
  {
    return selfKlassId;
  }
  
  public void setSelfKlassId(String selfKlassId)
  {
    this.selfKlassId = selfKlassId;
  }
}
