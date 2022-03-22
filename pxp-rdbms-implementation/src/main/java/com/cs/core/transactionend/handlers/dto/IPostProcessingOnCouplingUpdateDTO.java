package com.cs.core.transactionend.handlers.dto;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IPostProcessingOnCouplingUpdateDTO extends IInitializeBGProcessDTO {

  public IDependencyChangeDTO getChanges();
  public void setChanges(IDependencyChangeDTO propertiesChange);

}
