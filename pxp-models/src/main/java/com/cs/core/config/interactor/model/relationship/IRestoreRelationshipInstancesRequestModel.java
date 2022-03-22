package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRestoreRelationshipInstancesRequestModel extends IModel {
  
  String KLASS_INSTANCE_ID       = "klassInstanceId";
  String VERSION_ID              = "versionId";
  String BASE_TYPE               = "baseType";
  String CRIIDS                  = "criids";
  String CRIIDS_TO_ADD           = "criidsToAdd";
  String CRIIDS_TO_DELETE        = "criidsToDelete";
  String NATURE_CRIIDS           = "natureCriids";
  String NATURE_CRIIDS_TO_ADD    = "natureCriidsToAdd";
  String NATURE_CRIIDS_TO_DELETE = "natureCriidsToDelete";
  String CONFIG_DETAILS          = "configDetails";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public List<String> getCriidsToAdd();
  
  public void setCriidsToAdd(List<String> cRIIDSToAdd);
  
  public List<String> getCriidsToDelete();
  
  public void setCriidsToDelete(List<String> cRIIDSToDelete);
  
  public List<String> getNatureCriidsToAdd();
  
  public void setNatureCriidsToAdd(List<String> natureCRIIDSToAdd);
  
  public List<String> getNatureCriidsToDelete();
  
  public void setNatureCriidsToDelete(List<String> natureCRIIDSToDelete);
  
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails();
  
  public void setConfigDetails(
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public List<String> getCriids();
  
  public void setCriids(List<String> criids);
  
  public List<String> getNatureCriids();
  
  public void setNatureCriids(List<String> natureCriids);
}
