package com.cs.api.estordbmsmigration.interactor.migration;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class MigrateBaseEntitiesFromStagingToPXP extends AbstractMigrationRunProcedure
    implements IMigrateFromStagingToPXP {
  
  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    //Create Indices
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      ScriptRunner sr = new ScriptRunner(currentConn.getConnection());
      InputStream stream = this.getClass().getClassLoader().getResourceAsStream("migration_procedures/CreateIndices.sql");
      sr.runScript(new InputStreamReader(stream));
    });
    
    return super.executeInternal(model);
  }
  
  @Override
  protected String getProcedureName()
  {
    return "staging.sp_migrateallentitiesfromestordbms";
  }
  
}
