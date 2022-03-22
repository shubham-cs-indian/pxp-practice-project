package com.cs.dam.rdbms.assetpurge.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.dam.rdbms.assetpurge.idto.IAssetPurgeDTO;

public class AssetPurgeDTO extends SimpleDTO implements IAssetPurgeDTO{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String            idToDelete;
  private String            container;
  
  public AssetPurgeDTO(String idToDelete, String container)
  {
    this.idToDelete = idToDelete;
    this.container = container;
  }

  public AssetPurgeDTO()
  {
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getIdToDelete()
  {
    return idToDelete;
  }
  
  @Override
  public void setIdToDelete(String idToDelete)
  {
    this.idToDelete = idToDelete;
  }
  
  @Override
  public String getContainer()
  {
    return container;
  }
  
  @Override
  public void setContainer(String container)
  {
    this.container = container;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(IAssetPurgeDTO.ID_TO_DOWNLOAD, idToDelete),
        JSONBuilder.newJSONField(IAssetPurgeDTO.CONTAINER, container)
        );
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    idToDelete = json.getString(IAssetPurgeDTO.ID_TO_DOWNLOAD);
    container = json.getString(IAssetPurgeDTO.CONTAINER);    
  }
}
