package com.cs.core.bgprocess.services.savekpirule;

import com.cs.core.bgprocess.dto.DataRuleDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IDataRuleDeleteDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.utils.BgprocessUtils;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.dao.RuleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveKPIRule extends AbstractBGProcessJob
implements IBGProcessJob {

  
  private static final String             PROCESSED_IIDS         = "processedIIDs";
  protected int                           nbBatches              = 0;
  protected int                           batchSize;
  protected final IDataRuleDeleteDTO kpiRuleDto = new DataRuleDeleteDTO();
  protected Set<Long>                     passedBaseEntityIIDs   = new HashSet<>();
  protected int                           totalContents;
  protected Set<Long>                     entityIIDs          = new HashSet<>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    kpiRuleDto.fromJSON(jobData.getEntryData().toString());
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    Set<String> classifierCodes = kpiRuleDto.getKlassIds();
    classifierCodes.addAll(kpiRuleDto.getTaxonomyIds());
    BgprocessUtils.getBaseEntityIIDs(classifierCodes, entityIIDs);
    
    totalContents = entityIIDs.size();
    nbBatches = totalContents / batchSize;
    if ( nbBatches == 0 || totalContents % batchSize > 0 )
      nbBatches++;
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
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
    
    saveBulkKpiRule(batchEntityIIDs);

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
  
  private void saveBulkKpiRule(Set<Long> batchEntityIIDs) throws RDBMSException, CSFormatException
  {
    for (Long baseEntityIID : batchEntityIIDs) {
      ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
      
        RuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(catalogDao, openUserSession(), userSession);
        List<IBaseEntityDTO> baseEntitiesByIIDs = catalogDao.getBaseEntitiesByIIDs(List.of(String.valueOf(baseEntityIID)));
        IBaseEntityDAO baseEntityDAO = catalogDao.openBaseEntity(baseEntitiesByIIDs.get(0));
        for (String ruleCode : kpiRuleDto.getRuleCodes()) {
          if(checkIfEntityIsValid(ruleCode,baseEntityIID,catalogDao)) {

            ruleCatalogDAO.refreshDataQualityResult(ruleCode, baseEntityDAO, true);
          }
        }
    }
  }

  private boolean checkIfEntityIsValid(String ruleCode, Long baseEntityIID, ILocaleCatalogDAO catalogDao) throws RDBMSException, CSFormatException
  {
    RuleHandler ruleHandler = new RuleHandler();
    IConfigRuleDTO ruleByCode = ConfigurationDAO.instance().getRuleByCode(ruleCode);
    Collection<IRuleExpressionDTO> ruleExpressions = ruleByCode.getRuleExpressions();
    for (IRuleExpressionDTO ruleExpression : ruleExpressions) {
      ICSERule parseRule = (new CSEParser()).parseRule(ruleExpression.getRuleExpression());
      ICSEEntityFilterNode entityFilter = parseRule.getScope()
          .getEntityFilter();
      Set<String> catalogCodes = parseRule.getScope().getCatalogCodes();
      RuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(catalogDao);
      
      ruleCatalogDAO.deleteKpiFromKpiRuleLink(baseEntityIID, ruleExpression.getRuleExpressionIId());
      ruleCatalogDAO.deleteFromKpiUniquenessViolation(baseEntityIID);
      
      if (entityFilter == null) {
        continue;
      }
      
      Collection<String> classifierCodes = entityFilter.getIncludingClassifiers();
      
      Map<ClassifierType, List<String>> groupedClassifiers = ConfigurationDAO.instance()
          .groupClassifiers(classifierCodes);
      
      List<String> classes = groupedClassifiers.getOrDefault(ClassifierType.CLASS,
          new ArrayList<>());
      List<String> taxonomies = Stream
          .of(groupedClassifiers.getOrDefault(ClassifierType.TAXONOMY, new ArrayList<>())
              .stream(),
              groupedClassifiers.getOrDefault(ClassifierType.MINOR_TAXONOMY, new ArrayList<>())
                  .stream())
          .flatMap(i -> i)
          .collect(Collectors.toList());
      
      IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
      BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
      IClassifierDTO classifier =  entityDao.getBaseEntityDTO().getNatureClassifier();
     
      List<String> baseEntityClasses = new ArrayList<>();
      List<String> baseEntityTaxanomies = new ArrayList<>();
      
        switch (classifier.getClassifierType()) {
          case CLASS:
            baseEntityClasses.add(classifier.getClassifierCode());
            break;
          case MINOR_TAXONOMY:
            baseEntityTaxanomies.add(classifier.getClassifierCode());
            break;
          default:
            break;
      }
      Boolean evaluateCatalogFilter = evaluateCatalogFilters(catalogCodes, entityDao.getBaseEntityDTO().getCatalog());
      Boolean evaluateClassifierFilters = ruleHandler.evaluateClassifierFilters(baseEntityClasses,
          baseEntityTaxanomies, taxonomies, classes);
      if (evaluateClassifierFilters && evaluateCatalogFilter) {
        return true;
      }
    }
    return false;
    
  }
  private Boolean evaluateCatalogFilters(Set<String> catalogCodes, ICatalogDTO catalog)
  {
    if(catalogCodes.contains(catalog.getCatalogCode())) {
      return true;
    }
    return false;
  }
}
