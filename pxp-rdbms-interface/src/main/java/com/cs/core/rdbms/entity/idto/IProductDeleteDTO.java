package com.cs.core.rdbms.entity.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IProductDeleteDTO extends IInitializeBGProcessDTO{
  
  public static final String BASE_ENTITY_IIDs   = "baseEntityIIDs";
  public static final String SOURCE_ENTITY_IIDs = "sourceEntityIIDs";
  
  public List<Long> getBaseEntityIIDs();
  public void setBaseEntityIIDs(List<Long> baseEntityIIDs);
  
  public List<Long> getSourceEntityIIDs();
  public void setSourceEntityIIDs(List<Long> sourceEntityIIDs);
  
}

