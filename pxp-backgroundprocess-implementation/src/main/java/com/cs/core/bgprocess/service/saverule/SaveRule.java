package com.cs.core.bgprocess.service.saverule;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.json.simple.JSONObject;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.SaveDataRuleDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.ISaveDataRuleDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.utils.BgprocessUtils;
import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IChangedPropertiesDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;

public class SaveRule extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String             PROCESSED_IIDS         = "processedIIDs";
  protected int                           nbBatches              = 0;
  protected int                           batchSize;
  protected final ISaveDataRuleDTO ruleDTO = new SaveDataRuleDTO();
  protected Set<Long>                     passedBaseEntityIIDs   = new HashSet<>();
  protected int                           totalContents;
  protected Long                          ruleExpressionId;
  protected Set<Long>                     entityIIDs          = new HashSet<>();
  protected WorkflowUtils                 workflowUtils;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    ruleDTO.fromJSON(jobData.getEntryData().toString());
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    Set<String> classifierCodes = ruleDTO.getChangedClassifierCodes();
    Set<String> organizationCodes = ruleDTO.getChangedOrganizationIds();
    Set<String> catalogCodes = ruleDTO.getChangedCatalogIds();
   
    getBaseEntityIIDs(classifierCodes, catalogCodes, getOrganizationCode(organizationCodes), entityIIDs);
    
    ruleExpressionId = ruleDTO.getRuleExpressionId();
    totalContents = entityIIDs.size();
    nbBatches = totalContents / batchSize;
    if ( nbBatches == 0 || totalContents % batchSize > 0 )
      nbBatches++;
    
    workflowUtils = BGProcessApplication.getApplicationContext().getBean(WorkflowUtils.class);
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }

  @Override
  public BGPStatus runBatch() throws Exception
  {
    int currentBatchNo = getCurrentBatchNo() + 1;
    entityIIDs.removeAll(passedBaseEntityIIDs);
    Set<Long> batchEntityIIDs = new HashSet<>();
    Iterator<Long> remEntityIID = entityIIDs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchEntityIIDs.add(remEntityIID.next());
    }

    saveBulkDataRule(batchEntityIIDs);

    passedBaseEntityIIDs.addAll(batchEntityIIDs);
    jobData.getRuntimeData().setLongArrayField(PROCESSED_IIDS, passedBaseEntityIIDs);
    RDBMSLogger.instance().info(
            "Batch %d: processed base entity IIDs %s", getCurrentBatchNo(), batchEntityIIDs.toString());

    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);

    if (currentBatchNo < nbBatches && !batchEntityIIDs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
  
  private void saveBulkDataRule(Set<Long> batchIIDs) throws Exception
  {
    RuleHandler ruleHandler = new RuleHandler();
    for (long baseEntityIID : batchIIDs) {
      
      ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
      
      deleteRuleForEntity(ruleExpressionId ,baseEntityIID);
      IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
      BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
      
      List<String> baseEntityClassifiers = new ArrayList<>();
      List<String> baseEntityTaxonomies = new ArrayList<>();
      List<String> removedClassifiers = new ArrayList<>();
      ruleHandler.getBaseEntityClassifiersAndTaxonomies(catalogDao, entityDao, baseEntityClassifiers, baseEntityTaxonomies); 
      Map<String, Object> requestModel = new HashMap<>();
      requestModel.put("classifiers", ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies));
      requestModel.put("removedClassifiers", removedClassifiers);
      
      JSONObject detailsFromODB = CSConfigServer.instance().request(requestModel, "GetProductIdentifiersForClassifiers",
          catalogDao.getLocaleCatalogDTO().getLocaleID());
      IDataRulesHelperModel dataRulesHelperModel = ObjectMapperUtil.readValue(ObjectMapperUtil.writeValueAsString(detailsFromODB),
          DataRulesHelperModel.class);
      
      IChangedPropertiesDTO changedProperties = ruleHandler.applyDQRules(catalogDao, baseEntityIID, baseEntityClassifiers, baseEntityTaxonomies, new ArrayList<String>(), true,
          Arrays.asList(ruleDTO.getRuleCode()), RuleType.dataquality, dataRulesHelperModel.getReferencedElements(),
          dataRulesHelperModel.getReferencedAttributes(), dataRulesHelperModel.getReferencedTags());
      
      catalogDao.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
      IBusinessProcessTriggerModel businessProcessEventModel = BgprocessUtils.getBusinessProcessModelForPropetiesSave(entityDao,new ArrayList<>(changedProperties.getAttributeCodes()),
              new ArrayList<>(changedProperties.getTagCodes()));
      if(businessProcessEventModel != null) {
        workflowUtils.executeBusinessProcessEvent(businessProcessEventModel);
      }
    }
  }

  private static final String GET_BASE_ENITY_IIDS_FROM_CLASSIFIER_IID = "Select baseentityiid from pxp.baseentity where ismerged != true and classifieriid in (";

  private void getBaseEntityIIDs(Set<String> classifierCodes, Set<String> catalogIds, Set<String> organizationIds, Set<Long> baseEntityIIDs) throws RDBMSException
  {
  
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      Set<Long> classifierIIDs= new HashSet<>();
      String classifierQuery = String.format("Select classifieriid from pxp.classifierconfig where classifiercode in (%s)",Text.join(",", classifierCodes, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(classifierQuery);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        classifierIIDs.add(result.getLong("classifieriid"));
      }
      
      StringBuilder query = new StringBuilder(GET_BASE_ENITY_IIDS_FROM_CLASSIFIER_IID).append(Text.join(",", classifierIIDs)).append(")");
      if (catalogIds != null && !catalogIds.isEmpty()) {
        StringBuilder catalogList = new StringBuilder(" and catalogcode in ( ");
        catalogList.append("'" + Text.join("','", catalogIds) + "'").append(")");
        query.append(catalogList);
      }
      
      if (organizationIds != null && !organizationIds.isEmpty()) {
        StringBuilder organizationList = new StringBuilder(" and organizationcode in (" );
        organizationList.append("'" + Text.join("','", organizationIds) + "'").append(")");
        query.append(organizationList);
      }
    
      query.append(
          " UNION ALL Select becl.baseentityiid from pxp.baseentityclassifierlink becl join pxp.baseentity be on becl.baseentityiid = be.baseentityiid and be.ismerged != true"
          + " where becl.otherclassifieriid in (")
          .append(Text.join(",", classifierIIDs)).append(")");
      
      stmt = currentConn.prepareStatement(query);
      stmt.execute();
      result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        baseEntityIIDs.add(result.getLong("baseentityiid"));
      }
    });
  }
  
  private static final String DELETE_DQ_ENTITY = "delete from pxp.baseentityqualityrulelink where ruleexpressioniid = ? and baseentityiid = ? ";
  
  public void deleteRuleForEntity(Long ruleExpressionIID, long entityId) throws RDBMSException, SQLException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuilder deleteExpressions = new StringBuilder(DELETE_DQ_ENTITY);
      PreparedStatement stm = currentConn.prepareStatement(deleteExpressions.toString());
      stm.setLong(1, ruleExpressionIID);
      stm.setLong(2, entityId);
      stm.execute();
    });
  }
  
  
  private Set<String> getOrganizationCode(Set<String> organizationIds)
  {
    if(organizationIds.isEmpty()){
      return null;
    }
    if(organizationIds.contains("-1")){
      organizationIds.remove("-1");
      organizationIds.add("stdo");
    }
    return organizationIds;
  }
  
}