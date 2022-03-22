package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.clone.IDataForInstanceCloneModel;
import com.cs.core.runtime.interactor.model.context.CheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.model.context.ICheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.model.instance.DataForInstanceCloneModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCreateKlassInstanceCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateKlassInstanceCloneModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateKlassInstanceSingleCloneModel implements ICreateKlassInstanceSingleCloneModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected IDataForInstanceCloneModel                        cloneData;
  protected IGetConfigDetailsForCreateKlassInstanceCloneModel configDetails;
  protected Boolean                                           shouldCloneAllProperties;
  protected String                                            klassInstanceIdToClone;
  protected String                                            type;
  protected Boolean                                           isBulkClone;
  protected ICheckDuplicateLinkedVariantRequestModel          contextValidateData;
  
  @Override
  public IDataForInstanceCloneModel getCloneData()
  {
    return cloneData;
  }
  
  @Override
  @JsonDeserialize(as = DataForInstanceCloneModel.class)
  public void setCloneData(IDataForInstanceCloneModel cloneData)
  {
    this.cloneData = cloneData;
  }
  
  @Override
  public IGetConfigDetailsForCreateKlassInstanceCloneModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsForCreateKlassInstanceCloneModel.class)
  public void setConfigDetails(IGetConfigDetailsForCreateKlassInstanceCloneModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Boolean getShouldCloneAllProperties()
  {
    if (shouldCloneAllProperties == null) {
      shouldCloneAllProperties = false;
    }
    return shouldCloneAllProperties;
  }
  
  @Override
  public void setShouldCloneAllProperties(Boolean shouldCloneAllProperties)
  {
    this.shouldCloneAllProperties = shouldCloneAllProperties;
  }
  
  @Override
  public String getKlassInstanceIdToClone()
  {
    return klassInstanceIdToClone;
  }
  
  @Override
  public void setKlassInstanceIdToClone(String contentIdToClone)
  {
    this.klassInstanceIdToClone = contentIdToClone;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsBulkClone()
  {
    if (isBulkClone == null) {
      isBulkClone = false;
    }
    return isBulkClone;
  }
  
  @Override
  public void setIsBulkClone(Boolean isBulkClone)
  {
    this.isBulkClone = isBulkClone;
  }
  
  @Override
  public ICheckDuplicateLinkedVariantRequestModel getContextValidateData()
  {
    return contextValidateData;
  }
  
  @Override
  @JsonDeserialize(as=CheckDuplicateLinkedVariantRequestModel.class)
  public void setContextValidateData(ICheckDuplicateLinkedVariantRequestModel contextValidateData)
  {
    this.contextValidateData = contextValidateData;
  }
  
}
