package com.cs.dam.rdbms.assetpurge.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IAssetPurgeWrapperDTO extends ISimpleDTO{
  
  public static final String ASSET_PURGE_DTO_LIST = "assetPurgeDTOList";
  
  List<IAssetPurgeDTO> getAssetPurgeDTOList();
  
  void setAssetPurgeDTOList(List<IAssetPurgeDTO> assetPurgeDTOList);  
}
