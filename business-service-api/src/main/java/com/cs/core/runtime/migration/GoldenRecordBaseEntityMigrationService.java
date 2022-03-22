package com.cs.core.runtime.migration;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GoldenRecordBaseEntityMigrationService
    extends AbstractRuntimeService<IModel, IModel> implements IGoldenRecordBaseEntityMigrationService{

  @Override
  protected IModel executeInternal(IModel model) throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      ScriptRunner sr = new ScriptRunner(currentConn.getConnection());
      InputStream stream = this.getClass().getClassLoader().getResourceAsStream("rdbmsmigrations/goldenrecordbaseentitymigration.sql");
      sr.setSendFullScript(true);
      sr.runScript(new InputStreamReader(stream));
    });

    return null;
  }
  
}
