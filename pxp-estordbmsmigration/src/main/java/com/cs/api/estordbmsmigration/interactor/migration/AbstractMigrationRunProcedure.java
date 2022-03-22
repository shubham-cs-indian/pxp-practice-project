package com.cs.api.estordbmsmigration.interactor.migration;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public abstract class AbstractMigrationRunProcedure extends AbstractRuntimeService<IVoidModel, IVoidModel> {
  
  protected abstract String getProcedureName();

  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        currentConn.getDriver().getProcedure(currentConn, getProcedureName()).execute();
      });
    }
    catch (RDBMSException e) {
      //MigrationLogger.instance().exception(e);
      throw e;
    }
    
    return null;
  }

}
