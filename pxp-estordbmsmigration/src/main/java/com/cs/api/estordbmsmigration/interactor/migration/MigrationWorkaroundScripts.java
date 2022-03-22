package com.cs.api.estordbmsmigration.interactor.migration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class MigrationWorkaroundScripts extends AbstractRuntimeService<IVoidModel, IVoidModel>
    implements IMigrateFromStagingToPXP {
  
  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      try {
        Connection connection = currentConn.getConnection();
        connection.setAutoCommit(false);
        
        ScriptRunner sr = new ScriptRunner(connection);
        sr.setSendFullScript(true);
        
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("staging-schema/workarounds.sql");
        sr.runScript(new InputStreamReader(stream));
      }
      catch (Exception e) {
        throw e;
      }
    });
    return null;
  }
}
