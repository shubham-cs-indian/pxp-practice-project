package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IListOfContextualDataTransferOnConfigChangeDTO extends IInitializeBGProcessDTO{
  
  public static final String CONTEXTUAL_DATA_TRANSFER_ON_CONFIG_CHANGE_DTOS = "contextualDataTransferOnConfigChangeDTOs";
  
  public void setContextualDataTransferOnConfigChangeDTOs(List<IContextualDataTransferOnConfigChangeDTO> contextualDataTransferOnConfigChangeDTOs);
  
  public List<IContextualDataTransferOnConfigChangeDTO> getContextualDataTransferOnConfigChangeDTOs();
}
