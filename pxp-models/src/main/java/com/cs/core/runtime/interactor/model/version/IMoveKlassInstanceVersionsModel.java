package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IMoveKlassInstanceVersionsModel extends IModel {
  
  String INSTANCE_ID             = "instanceId";
  String VERSION_NUMBERS         = "versionNumbers";
  String IS_DELETE_FROM_ARCHIVAL = "isDeleteFromArchival";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public List<Integer> getVersionNumbers();
  
  public void setVersionNumbers(List<Integer> versionNumbers);
  
  public Boolean getIsDeleteFromArchival();
  
  public void setIsDeleteFromArchival(Boolean isDeleteFromArchival);
}
