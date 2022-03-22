package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IContextualDataTransferDTO extends IInitializeBGProcessDTO {
  
  public static final String BGP_COUPLING_DTOs = "bgpCouplingDTOs";
  
  public void setBGPCouplingDTOs(List<IContextualDataTransferGranularDTO> bgpCouplingDTOs);
  public List<IContextualDataTransferGranularDTO> getBGPCouplingDTOs();
  
}
