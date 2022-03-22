package com.cs.core.bgprocess.services.asset;

import java.util.Set;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Uploads generated smart documents to swift server.
 * @author Santosh.Kumar
 *
 */
public class BulkSmartDocumentUpload extends BulkAssetUpload {
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws RDBMSException, CSFormatException
  {
    super.runBaseEntityBatch(batchIIDs);
  }
}