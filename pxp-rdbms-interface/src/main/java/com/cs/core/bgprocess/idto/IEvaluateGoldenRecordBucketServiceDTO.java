package com.cs.core.bgprocess.idto;


public interface IEvaluateGoldenRecordBucketServiceDTO extends IInitializeBGProcessDTO {
 
public void setBaseEntityIID(Long baseEntityIID);
  
  public Long getBaseEntityIID();

  public Boolean getIsBaseEntityDeleted();
  
  public void setIsBaseEntityDeleted(Boolean isBaseEntityDeleted); 
  
}
