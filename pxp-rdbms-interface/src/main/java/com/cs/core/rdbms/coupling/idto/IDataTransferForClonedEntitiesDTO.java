package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IDataTransferForClonedEntitiesDTO extends IInitializeBGProcessDTO {
  
  public static final String CLONED_BASE_ENTITY_IIDS           = "clonedBaseEntityIIDs";

  public void setClonedBaseEntityIIDs(List<Long> clonedbaseEntityIIDs);
  
  public List<Long> getClonedBaseEntityIIDs();
  
}

