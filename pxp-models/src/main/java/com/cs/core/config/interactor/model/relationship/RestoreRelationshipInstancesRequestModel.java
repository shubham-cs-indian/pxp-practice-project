package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;

import java.util.ArrayList;
import java.util.List;

public class RestoreRelationshipInstancesRequestModel
    implements IRestoreRelationshipInstancesRequestModel {
  
  private static final long                                            serialVersionUID = 1L;
  protected String                                                     klassInstanceId;
  protected Long                                                       versionId;
  protected String                                                     baseType;
  protected List<String>                                               criids;
  protected List<String>                                               criidsToAdd;
  protected List<String>                                               criidsToDelete;
  protected List<String>                                               natureCriids;
  protected List<String>                                               natureCriidsToAdd;
  protected List<String>                                               natureCriidsToDelete;
  protected IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails;
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getCriidsToAdd()
  {
    if (criidsToAdd == null) {
      criidsToAdd = new ArrayList<>();
    }
    return criidsToAdd;
  }
  
  @Override
  public void setCriidsToAdd(List<String> cRIIDSToAdd)
  {
    criidsToAdd = cRIIDSToAdd;
  }
  
  @Override
  public List<String> getCriidsToDelete()
  {
    if (criidsToDelete == null) {
      criidsToDelete = new ArrayList<>();
    }
    return criidsToDelete;
  }
  
  @Override
  public void setCriidsToDelete(List<String> cRIIDSToRemove)
  {
    criidsToDelete = cRIIDSToRemove;
  }
  
  @Override
  public List<String> getNatureCriidsToAdd()
  {
    if (natureCriidsToAdd == null) {
      natureCriidsToAdd = new ArrayList<>();
    }
    return natureCriidsToAdd;
  }
  
  @Override
  public void setNatureCriidsToAdd(List<String> natureCRIIDSToAdd)
  {
    this.natureCriidsToAdd = natureCRIIDSToAdd;
  }
  
  @Override
  public List<String> getNatureCriidsToDelete()
  {
    if (natureCriidsToDelete == null) {
      natureCriidsToDelete = new ArrayList<>();
    }
    return natureCriidsToDelete;
  }
  
  @Override
  public void setNatureCriidsToDelete(List<String> natureCRIIDSToRemove)
  {
    this.natureCriidsToDelete = natureCRIIDSToRemove;
  }
  
  @Override
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  public void setConfigDetails(
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public List<String> getCriids()
  {
    if (criids == null) {
      criids = new ArrayList<>();
    }
    return criids;
  }
  
  @Override
  public void setCriids(List<String> criids)
  {
    this.criids = criids;
  }
  
  @Override
  public List<String> getNatureCriids()
  {
    if (natureCriids == null) {
      natureCriids = new ArrayList<>();
    }
    return natureCriids;
  }
  
  @Override
  public void setNatureCriids(List<String> natureCriids)
  {
    this.natureCriids = natureCriids;
  }
}
