package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IRuleHandlerForMigrationDTO extends IInitializeBGProcessDTO {
  
  public static final String BASE_ENTITY_IIDS           = "baseEntityIids";
  public static final String CLASSIFIER_IIDS            = "classifierIids";
  public static final String SHOULD_USE_CS_CACHE        = "shouldUseCSCache";
  public static String SHOULD_EVALUATE_IDENTIFIER       = "shouldEvaluateIdentifier";
  
  public void setBaseEntityIids(List<Long> baseEntityIids);
  
  public List<Long> getBaseEntityIids();
  
  public void setClassifierIids(List<Long> classifierIids);
  
  public List<Long> getClassifierIids();
    
  public Boolean getShouldUseCSCache();
  
  public void setShouldUseCSCache(Boolean shouldUseCSCache);
  
  public boolean getShouldEvaluateIdentifier();
  
  public void setShouldEvaluateIdentifier(boolean shouldEvaluateIdentifier);
    
}
