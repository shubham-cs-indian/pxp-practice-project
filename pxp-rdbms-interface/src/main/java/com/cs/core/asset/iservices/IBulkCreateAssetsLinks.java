package com.cs.core.asset.iservices;

import java.util.List;

import com.cs.core.bgprocess.idto.IBulkAssetLinkCreationDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface IBulkCreateAssetsLinks {
  
  public List<IBaseEntityDTO> getAssetEntities(IBulkAssetLinkCreationDTO assetLinkCreationDTO,
      IUserSessionDTO session) throws RDBMSException;
}
