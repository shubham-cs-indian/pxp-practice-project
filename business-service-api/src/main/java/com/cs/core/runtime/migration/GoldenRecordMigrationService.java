package com.cs.core.runtime.migration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.elastic.Index;
import com.cs.core.elastic.iservices.IIndexServices;
import com.cs.core.elastic.services.IndexServices;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.intialize.elastic.ICreateIndices;
import com.cs.core.technical.exception.CSInitializationException;

@Service
public class GoldenRecordMigrationService
    extends AbstractRuntimeService<IModel, IModel> implements IGoldenRecordMigrationService{

  @Override
  protected IModel executeInternal(IModel model) throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      ScriptRunner sr = new ScriptRunner(currentConn.getConnection());
      InputStream stream = this.getClass().getClassLoader().getResourceAsStream("rdbmsmigrations/goldenrecordmigration.sql");
      sr.setSendFullScript(true);
      sr.runScript(new InputStreamReader(stream));

      try {
        IIndexServices indexServices = new IndexServices();
        List<Index> indices = new ArrayList<>();
        indices.add(Index.goldenrecordbucketcache);
        indexServices.createIndices(indices);
      }
      catch (CSInitializationException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    });

    return null;
  }
  
}
