package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IEvaluateGoldenRecordBucketServiceDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class EvaluateGoldenRecordServiceDTO extends InitializeBGProcessDTO
    implements IEvaluateGoldenRecordBucketServiceDTO {
  
  public static final String BASE_ENTITY_IID  = "baseEntityIID";
  public static final String IS_BASE_ENTITY_DELETED  = "isBaseEntityDeleted";
  
  private static final long  serialVersionUID = 1L;
  private Long               baseEntityIID;
  private Boolean            isBaseEntityDeleted = false;
  
  @Override
  public void setBaseEntityIID(Long baseEntityIID)
  {
    this.baseEntityIID = baseEntityIID;
  }
  
  @Override
  public Long getBaseEntityIID()
  {
    return baseEntityIID;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(BASE_ENTITY_IID, baseEntityIID),
        JSONBuilder.newJSONField(IS_BASE_ENTITY_DELETED, isBaseEntityDeleted)
        );
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    baseEntityIID = json.getLong(BASE_ENTITY_IID);
    isBaseEntityDeleted = json.getBoolean(IS_BASE_ENTITY_DELETED);
  }
  
  @Override
  public Boolean getIsBaseEntityDeleted()
  {
    return isBaseEntityDeleted;
  }

  @Override
  public void setIsBaseEntityDeleted(Boolean isBaseEntityDeleted)
  {
    this.isBaseEntityDeleted = isBaseEntityDeleted;
  }
}
