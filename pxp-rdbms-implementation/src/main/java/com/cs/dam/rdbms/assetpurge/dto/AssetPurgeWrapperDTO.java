package com.cs.dam.rdbms.assetpurge.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.dam.rdbms.assetpurge.idto.IAssetPurgeDTO;
import com.cs.dam.rdbms.assetpurge.idto.IAssetPurgeWrapperDTO;

public class AssetPurgeWrapperDTO extends SimpleDTO implements IAssetPurgeWrapperDTO {
  
  /**
   * 
   */
  private static final long    serialVersionUID  = 1L;
  private List<IAssetPurgeDTO> assetPurgeDTOList = new ArrayList<IAssetPurgeDTO>();
  
  @Override
  public List<IAssetPurgeDTO> getAssetPurgeDTOList()
  {
    return assetPurgeDTOList;
  }
  
  @Override
  public void setAssetPurgeDTOList(List<IAssetPurgeDTO> assetPurgeDTOList)
  {
    this.assetPurgeDTOList = assetPurgeDTOList;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONArray(IAssetPurgeWrapperDTO.ASSET_PURGE_DTO_LIST, assetPurgeDTOList));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    assetPurgeDTOList.clear();
    JSONArray jsonArray = json.getJSONArray(IAssetPurgeWrapperDTO.ASSET_PURGE_DTO_LIST);
    Iterator<?> iterator = jsonArray.iterator();
    for (Object assetPurgeJson: jsonArray) {
      IAssetPurgeDTO assetPurgeDTO = new AssetPurgeDTO();
      assetPurgeDTO.fromJSON(assetPurgeJson.toString());
      assetPurgeDTOList.add(assetPurgeDTO);
    }
  }
}
