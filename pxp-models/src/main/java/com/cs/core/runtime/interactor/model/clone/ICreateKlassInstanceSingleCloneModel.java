package com.cs.core.runtime.interactor.model.clone;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.context.ICheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateKlassInstanceCloneModel;

public interface ICreateKlassInstanceSingleCloneModel extends IModel {
  
  public static final String CLONE_DATA                  = "cloneData";
  public static final String CONFIG_DETAILS              = "configDetails";
  public static final String SHOULD_CLONE_ALL_PROPERTIES = "shouldCloneAllProperties";
  public static final String KLASS_INSTANCE_ID_TO_CLONE  = "klassInstanceIdToClone";
  public static final String TYPE                        = "type";
  public static final String IS_BULK_CLONE               = "isBulkClone";
  public static final String CONTEXT_VALIDATE_DATA       = "contextValidateData";
  
  public IDataForInstanceCloneModel getCloneData();
  
  public void setCloneData(IDataForInstanceCloneModel cloneData);
  
  public IGetConfigDetailsForCreateKlassInstanceCloneModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCreateKlassInstanceCloneModel configDetails);
  
  public String getKlassInstanceIdToClone();
  
  public void setKlassInstanceIdToClone(String contentIdToClone);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getShouldCloneAllProperties();
  
  public void setShouldCloneAllProperties(Boolean shouldCloneAllProperties);
  
  public Boolean getIsBulkClone();
  
  public void setIsBulkClone(Boolean isBulkClone);
  
  public ICheckDuplicateLinkedVariantRequestModel getContextValidateData();
  public void setContextValidateData(ICheckDuplicateLinkedVariantRequestModel contextValidateData);
}
