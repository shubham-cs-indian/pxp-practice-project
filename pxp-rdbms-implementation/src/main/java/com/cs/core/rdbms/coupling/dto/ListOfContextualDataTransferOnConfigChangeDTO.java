package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;
import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idto.IListOfContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;

public class ListOfContextualDataTransferOnConfigChangeDTO extends InitializeBGProcessDTO
    implements IListOfContextualDataTransferOnConfigChangeDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public List<IContextualDataTransferOnConfigChangeDTO> contextualDataTransferOnConfigChangeDTOs = new ArrayList<>();

  @Override
  public void setContextualDataTransferOnConfigChangeDTOs(
      List<IContextualDataTransferOnConfigChangeDTO> contextualDataTransferOnConfigChangeDTOs)
  {
    this.contextualDataTransferOnConfigChangeDTOs =  contextualDataTransferOnConfigChangeDTOs;
  }
  
  @Override
  public List<IContextualDataTransferOnConfigChangeDTO> getContextualDataTransferOnConfigChangeDTOs()
  {
    return contextualDataTransferOnConfigChangeDTOs;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    
    return JSONBuilder.assembleJSONBuffer(
        super.toJSONBuffer(), 
        JSONBuilder.newJSONArray(CONTEXTUAL_DATA_TRANSFER_ON_CONFIG_CHANGE_DTOS, 
            contextualDataTransferOnConfigChangeDTOs));
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    
    contextualDataTransferOnConfigChangeDTOs.clear();
    json.getJSONArray(CONTEXTUAL_DATA_TRANSFER_ON_CONFIG_CHANGE_DTOS).forEach((iid) -> {
      IContextualDataTransferOnConfigChangeDTO contextualDataTransferOnConfigChangeDTO = new ContextualDataTransferOnConfigChangeDTO();
      try {
        contextualDataTransferOnConfigChangeDTO.fromJSON(iid.toString());
        contextualDataTransferOnConfigChangeDTOs.add(contextualDataTransferOnConfigChangeDTO);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
    
  }
  
}
