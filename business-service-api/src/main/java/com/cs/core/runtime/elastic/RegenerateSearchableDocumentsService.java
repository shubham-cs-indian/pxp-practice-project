package com.cs.core.runtime.elastic;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.elastic.IDocumentRegenerationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.google.common.collect.Lists;

@Service
public class RegenerateSearchableDocumentsService
    extends AbstractRuntimeService<IDocumentRegenerationModel, IModel>
    implements IRegenerateSearchableDocumentsService {
  
  @Autowired
  RDBMSComponentUtils          rdbmsComponentUtils;
  
  @Autowired
  protected ApplicationContext applicationContext;
  
  @Autowired
  ThreadPoolTaskExecutor       migrationTaskExecutor;
  
  @Override
  protected IModel executeInternal(IDocumentRegenerationModel model) throws Exception
  {
    Set<Long> baseEntityIIDs = model.getBaseEntityIIDs();
    Set<String> classifierCodes = model.getClassifierCodes();
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    Boolean generateForAllBaseEntities = true;
    
    
    if (!classifierCodes.isEmpty()) {
      IConfigurationDAO configDAO = RDBMSAppDriverManager.getDriver()
          .newConfigurationDAO();
      Set<Long> classifierIIDs = configDAO.getClassifierIIDsFromCodes(classifierCodes);
      baseEntityIIDs
          .addAll(configDAO.getBaseentityIIDsForTypes(new ArrayList<Long>(classifierIIDs)));
      generateForAllBaseEntities = false;
    }
    else if (baseEntityIIDs.isEmpty()) {
      generateSearchableForAllEntities(localeCatalogDAO);
    }
    
    if(!generateForAllBaseEntities) {
      if(baseEntityIIDs.size() > 100) {
        List<List<Long>> partitions = Lists.partition(new ArrayList<>(baseEntityIIDs), 100);
        for (List<Long> iids : partitions) {
          RegenerateSearchableTaskForMigration regenerateSearchableTaskForMigration = applicationContext.getBean(RegenerateSearchableTaskForMigration.class);
          regenerateSearchableTaskForMigration.setData(iids, localeCatalogDAO);
          migrationTaskExecutor.submit(regenerateSearchableTaskForMigration);
        }
      }
    }
    return null;
  }

  private void generateSearchableForAllEntities(ILocaleCatalogDAO localeCatalogDAO)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      long initialOffset = 0;
      int responseSize;
      try {
        responseSize = CSProperties.instance().getInt("import.searchable.creation.batch");
      }
      catch (CSInitializationException e) {
        responseSize  = 500;
        RDBMSLogger.instance().exception(e);
        RDBMSLogger.instance().info("Could not read Property for Batching");
      }

      while (true) {
        try {
          String query = "select baseEntityIID from pxp.baseEntity where baseEntityIID > %d and catalogCode in  ('pim') order by baseentityiid asc limit %d";
          String finalQuery = String.format(query, initialOffset, responseSize);

          PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
          IResultSetParser objectResult = currentConn.getResultSetParser(stmt.executeQuery());

          long finalOffset = initialOffset;

          List<Long> entityIIDs = new ArrayList<>();
          while (objectResult.next()) {
            long baseEntityIID = objectResult.getLong("baseEntityIID");
            entityIIDs.add(baseEntityIID);
            initialOffset = baseEntityIID;
          }
          
          RegenerateSearchableTaskForMigration regenerateSearchableTaskForMigration = applicationContext
              .getBean(RegenerateSearchableTaskForMigration.class);
          regenerateSearchableTaskForMigration.setData(entityIIDs, localeCatalogDAO);
          migrationTaskExecutor.submit(regenerateSearchableTaskForMigration);
          
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
  }
  
}
