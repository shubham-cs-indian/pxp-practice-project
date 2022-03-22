package com.cs.core.config.interactor.usecase.migration;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.coupling.dto.RuleHandlerForMigrationDTO;
import com.cs.core.rdbms.coupling.idto.IRuleHandlerForMigrationDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.model.configuration.IKPIScriptRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.google.common.collect.Lists;

@Service
public class DQRulesMustShouldKPIGenerationOptimizedMigration extends AbstractService<IKPIScriptRequestModel, IModel> implements IDQRulesMustShouldKPIGenerationOptimizedMigration {
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected ISessionContext       context;
  
  protected Integer batchSize =    100;
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }

  @Override
  protected IModel executeInternal(IKPIScriptRequestModel model) throws Exception
  {
    RDBMSLogger.instance().info("DQ-MustShould API initiated");
      RDBMSConnectionManager.instance()
      .runTransaction((RDBMSConnection currentConn) -> {
          
        String FETCH_BUCKET_QUERY = "select res.classifiers, array_agg(res.baseentityiid) as baseentityiids from \r\n" + 
            "(select test.baseentityiid, array_cat(test.nature ,test.nonnature) as classifiers from "
            + "(select be.baseentityiid,array_agg(distinct be.classifieriid) as nature, array_agg(becl.otherclassifieriid order by becl.otherclassifieriid asc) as nonnature\r\n" + 
            "from pxp.baseentity be join pxp.baseentityclassifierlink becl \r\n" + 
            "ON be.baseentityiid = becl.baseentityiid group by be.baseentityiid) as test) as res group by res.classifiers";
        PreparedStatement stmt = currentConn.prepareStatement(FETCH_BUCKET_QUERY);
        stmt.execute();
        IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
        while (result.next()) {
          Long[] classifiers = result.getLongArray("classifiers");
          Long[] baseentityiids = result.getLongArray("baseentityiids");
          List<Long> baseentityiidsAsList = Arrays.asList(baseentityiids);
          List<Long> classifiersAsList = Arrays.asList(classifiers);
          if(baseentityiidsAsList.size() > batchSize) {
            List<List<Long>> partitions = Lists.partition(baseentityiidsAsList, batchSize);
            for (List<Long> iids:partitions) {
              IRuleHandlerForMigrationDTO dto = new RuleHandlerForMigrationDTO();
              dto.setBaseEntityIids(iids);
              dto.setClassifierIids(classifiersAsList);
              dto.setShouldUseCSCache(true);
              dto.setShouldEvaluateIdentifier(model.getShouldEvaluateIdentifier());
              BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), "RULE_HANDLER_FOR_MIGRATION_SERVICE", "", IBGProcessDTO.BGPPriority.HIGH,
                  new JSONContent(dto.toJSON()));

            }
          }
          else {
            IRuleHandlerForMigrationDTO dto = new RuleHandlerForMigrationDTO();
            dto.setBaseEntityIids(baseentityiidsAsList);
            dto.setClassifierIids(classifiersAsList);
            dto.setShouldEvaluateIdentifier(model.getShouldEvaluateIdentifier());
            BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), "RULE_HANDLER_FOR_MIGRATION_SERVICE", "", IBGProcessDTO.BGPPriority.HIGH,
                new JSONContent(dto.toJSON()));
          }
          
            
        }
      });
    
      RDBMSLogger.instance().info("DQ-MustShould API ended");
    return null;
  }

  /* protected void bucketProcessing(Long[] baseentityiids,List<String> classifierCodes )
      throws RDBMSException, CSFormatException, CSInitializationException, Exception
  {
    RuleHandler ruleHandler = new RuleHandler();
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("classifiers", classifierCodes);
    JSONObject detailsFromODB = CSConfigServer.instance().request(requestMap, "GetProductIdentifiersForClassifiers",rdbmsComponentUtils.getDataLanguage());
    IDataRulesHelperModel dataRulesHelperModel = ObjectMapperUtil.readValue(detailsFromODB.toString(), DataRulesHelperModel.class); 
    for (Long baseEntityIID : baseentityiids) {
      try {
        ILocaleCatalogDAO localeCatalogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog(context.getUserSessionDTO(), baseEntityIID);
        IBaseEntityDTO baseEntityDTO = localeCatalogDAO.getEntityByIID(baseEntityIID);
        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntityDTO);
        List<String> baseEntityClassifiers = new ArrayList<String>();
        List<String> baseEntityTaxonomies = new ArrayList<String>();
        ruleHandler.getBaseEntityClassifiersAndTaxonomies(localeCatalogDAO, baseEntityDAO,baseEntityClassifiers, baseEntityTaxonomies);
        IUniquenessViolationDAO openUniquenessDAO = localeCatalogDAO.openUniquenessDAO();
        
        ruleHandler.applyDQRules(localeCatalogDAO, baseEntityIID, baseEntityClassifiers,baseEntityTaxonomies, new ArrayList<>(), true, null,
            dataRulesHelperModel.getReferencedElements(),dataRulesHelperModel.getReferencedAttributes(),dataRulesHelperModel.getReferencedTags());
        ruleHandler.evaluateDQMustShould(baseEntityDTO.getBaseEntityIID(), dataRulesHelperModel,
            baseEntityDAO, localeCatalogDAO);
        ruleHandler.evaluateProductIdentifiers(baseEntityIID, detailsFromODB,
            (BaseEntityDAO) baseEntityDAO, openUniquenessDAO, localeCatalogDAO);
      }
    catch(Exception e) {
      RDBMSLogger.instance().info("Failed entity iid: " + baseEntityIID);
      RDBMSLogger.instance().exception(e);
    }
  }
  }*/
  
}
