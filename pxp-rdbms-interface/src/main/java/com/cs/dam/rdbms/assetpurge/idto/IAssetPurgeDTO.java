package com.cs.dam.rdbms.assetpurge.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IAssetPurgeDTO extends ISimpleDTO{
  
  public static final String ID_TO_DOWNLOAD = "idToDelete";
  public static final String CONTAINER   = "container";
  
  public String getIdToDelete();

  public void setIdToDelete(String idToDelete);

  public String getContainer();

  public void setContainer(String container);
}
