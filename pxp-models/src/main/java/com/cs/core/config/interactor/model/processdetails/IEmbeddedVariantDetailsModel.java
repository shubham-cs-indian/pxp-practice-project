package com.cs.core.config.interactor.model.processdetails;

public interface IEmbeddedVariantDetailsModel extends IProcessModel {
  
  public static final String ID                = "id";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String PARENT_ID         = "parentId";
  public static final String BATCH_ID          = "batchId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getBatchId();
  
  public void setBatchId(String batchId);
}
