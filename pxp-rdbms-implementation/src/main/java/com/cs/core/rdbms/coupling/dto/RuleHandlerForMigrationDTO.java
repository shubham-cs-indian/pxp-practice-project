package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IRuleHandlerForMigrationDTO;
import com.cs.core.technical.exception.CSFormatException;

public class RuleHandlerForMigrationDTO extends InitializeBGProcessDTO implements IRuleHandlerForMigrationDTO {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected List<Long> baseEntityIids = new ArrayList<>();
  protected List<Long> classifierIids = new ArrayList<>();
  protected Boolean    shouldUseCSCache = false; 
  protected boolean shouldEvaluateIdentifier = false;
  
  @Override
  public void setBaseEntityIids(List<Long> baseEntityIids)
  {
    this.baseEntityIids = baseEntityIids;
  }

  @Override
  public List<Long> getBaseEntityIids()
  {
    return baseEntityIids;
  }

  @Override
  public void setClassifierIids(List<Long> classifierIids)
  {
    this.classifierIids = classifierIids;
  }

  @Override
  public List<Long> getClassifierIids()
  {
    return classifierIids;
  }
  
  @Override
  public Boolean getShouldUseCSCache()
  {
    return shouldUseCSCache;
  }

  @Override
  public void setShouldUseCSCache(Boolean shouldUseCSCache)
  {
    this.shouldUseCSCache = shouldUseCSCache;
  }
  
  @Override
  public boolean getShouldEvaluateIdentifier()
  {
    return shouldEvaluateIdentifier;
  }

  @Override
  public void setShouldEvaluateIdentifier(boolean shouldEvaluateIdentifier)
  {
    this.shouldEvaluateIdentifier = shouldEvaluateIdentifier;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
   
       
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
         JSONBuilder.newJSONLongArray(CLASSIFIER_IIDS, classifierIids),
         JSONBuilder.newJSONLongArray(BASE_ENTITY_IIDS, baseEntityIids),
         JSONBuilder.newJSONField(SHOULD_USE_CS_CACHE, shouldUseCSCache),
         JSONBuilder.newJSONField(SHOULD_EVALUATE_IDENTIFIER, shouldEvaluateIdentifier));
    
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    baseEntityIids.clear();
    json.getJSONArray(BASE_ENTITY_IIDS).forEach((iid) -> {
      baseEntityIids.add((Long) iid);
    });
    classifierIids.clear();
    json.getJSONArray(CLASSIFIER_IIDS).forEach((iid) -> {
      classifierIids.add((Long) iid);
    });
    shouldUseCSCache = json.getBoolean(SHOULD_USE_CS_CACHE);
    shouldEvaluateIdentifier = json.getBoolean(SHOULD_EVALUATE_IDENTIFIER);
  }
}
