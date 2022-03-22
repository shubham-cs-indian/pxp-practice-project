package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IRelationshipDataTransferDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipDataTransferDTO extends InitializeBGProcessDTO implements IRelationshipDataTransferDTO{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  List<IBGPCouplingDTO> bgpCouplingDTOs = new ArrayList<>(); 
  

  @Override
  public void setBGPCouplingDTOs(List<IBGPCouplingDTO> bgpCouplingDTOs)
  {
    this.bgpCouplingDTOs = bgpCouplingDTOs;
  }

  @Override
  public List<IBGPCouplingDTO> getBGPCouplingDTOs()
  {
    return bgpCouplingDTOs;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    json.getJSONArray(BGP_COUPLING_DTOs).forEach((iid) -> {
      IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
      try {
        bgpCouplingDTO.fromJSON(iid.toString());
        bgpCouplingDTOs.add(bgpCouplingDTO);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        super.toJSONBuffer(),
        JSONBuilder.newJSONArray(BGP_COUPLING_DTOs, bgpCouplingDTOs));
  }
}
