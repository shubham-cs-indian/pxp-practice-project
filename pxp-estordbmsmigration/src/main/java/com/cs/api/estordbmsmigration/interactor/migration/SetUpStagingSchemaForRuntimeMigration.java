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
public class SetUpStagingSchemaForRuntimeMigration extends AbstractRuntimeService<IVoidModel, IVoidModel>
    implements ISetUpStagingSchemaForRuntimeMigration {

  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      try {
        Connection connection = currentConn.getConnection();
        connection.setAutoCommit(false);
        
        ScriptRunner sr = new ScriptRunner(connection);
        sr.setSendFullScript(true);
        
        //Create staging schema and helper tables
        runSqlScript(sr, "migration_procedures/helpertables_createscript.sql");
            
        //Create staging schema tables
        runSqlScript(sr, "staging-schema/attributeconflictingvalues.sql");
        runSqlScript(sr, "staging-schema/baseentity.sql");
        runSqlScript(sr, "staging-schema/context.sql");
        runSqlScript(sr, "staging-schema/references.sql");
        runSqlScript(sr, "staging-schema/relationships.sql");
        runSqlScript(sr, "staging-schema/tagconflictingvalues.sql");
        runSqlScript(sr, "staging-schema/tagrecord.sql");
        runSqlScript(sr, "staging-schema/task.sql");
        runSqlScript(sr, "staging-schema/valuerecord.sql");
    
        //Create migration procedures
        runSqlScript(sr, "migration_procedures/sp_migrateallentitiesfromestordbms.sql");
        runSqlScript(sr, "migration_procedures/sp_migrateallvaluerecordsfromestordbms.sql");
        runSqlScript(sr, "migration_procedures/sp_migratealltagswithconflictingvalues_without_loop_approach.sql");
        runSqlScript(sr, "migration_procedures/sp_migrateallrelationshipsandreferencesfromestordbms.sql");
        runSqlScript(sr, "migration_procedures/sp_migratetasksfromestordbms.sql");
        
        //Insert script for helper tables
        runSqlScript(sr, "migration_procedures/helpertables_insertscript.sql");
        
      }
      catch (Exception e) {
        //MigrationLogger.instance().exception(e);
        throw e;
      }
    });
    
    return null;
  }

  private void runSqlScript(ScriptRunner sr, String filePath)
  {
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filePath);
    sr.runScript(new InputStreamReader(stream));
  }
  
}
