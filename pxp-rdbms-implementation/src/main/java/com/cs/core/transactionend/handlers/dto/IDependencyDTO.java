package com.cs.core.transactionend.handlers.dto;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

import java.util.Map;

public interface IDependencyDTO extends IInitializeBGProcessDTO {

  Map<Long, IDependencyChangeDTO> getDependencies();

  void setDependencies(Map<Long, IDependencyChangeDTO> dependencies);
}
