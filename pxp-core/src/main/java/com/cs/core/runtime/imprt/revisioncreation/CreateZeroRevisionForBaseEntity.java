package com.cs.core.runtime.imprt.revisioncreation;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.imprt.revision.ZeroRevisionCreator;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateZeroRevisionForBaseEntity extends AbstractRuntimeService<IVoidModel, IVoidModel>
    implements ICreateZeroRevisionForBaseEntity {

  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;

  @Autowired
  protected ApplicationContext applicationContext;

  @Autowired
  protected ThreadPoolTaskExecutor migrationTaskExecutor;

  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      long initialOffset = 0;
      int responseSize;
      try {
        responseSize = CSProperties.instance().getInt("import.revision.creation.batch");
      }
      catch (CSInitializationException e) {
        responseSize  = 10000;
        RDBMSLogger.instance().exception(e);
        RDBMSLogger.instance().info("Could not read Property for Batching");
      }

      while (true) {
        try {
          String query = "select baseEntityIID from pxp.baseEntity where baseEntityIID > %d and catalogCode = 'pim' order by baseentityiid asc limit %d";
          String finalQuery = String.format(query, initialOffset, responseSize);

          PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
          IResultSetParser objectResult = currentConn.getResultSetParser(stmt.executeQuery());

          long finalOffset = initialOffset;

          List<String> baseEntityIIDs = new ArrayList<>(10000);
          while (objectResult.next()) {
            long baseEntityIID = objectResult.getLong("baseEntityIID");
            baseEntityIIDs.add(String.valueOf(baseEntityIID));
            initialOffset = baseEntityIID;
          }

          createZeroRevision(baseEntityIIDs);
          if (initialOffset == finalOffset) {
            break;
          }
        }
        catch (Exception e) {
          RDBMSLogger.instance().exception(e);
          RDBMSLogger.instance().info("Batch Failed after ID:" + initialOffset);
        }
      }
    });
    return null;
  }

  private void createZeroRevision(List<String> baseEntityIIDs) throws RDBMSException
  {
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();

    ZeroRevisionCreator revisionCreator = applicationContext.getBean(ZeroRevisionCreator.class);
    revisionCreator.setData(baseEntityIIDs, localeCatalogDAO);
    migrationTaskExecutor.submit(revisionCreator);
  }

}


