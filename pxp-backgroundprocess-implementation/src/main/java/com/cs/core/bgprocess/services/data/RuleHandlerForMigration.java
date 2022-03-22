  package com.cs.core.bgprocess.services.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.RuleHandlerForMigrationDTO;
import com.cs.core.rdbms.coupling.idto.IRuleHandlerForMigrationDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSCache;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;


public class RuleHandlerForMigration extends AbstractBGProcessJob implements IBGProcessJob {
  
  protected final IRuleHandlerForMigrationDTO    ruleHandlerDTO          = new RuleHandlerForMigrationDTO();
  IDataRulesHelperModel                          dataRulesHelperModel;
  JSONObject                                     detailsFromODB;
  protected int                                     nbBatches                 = 1;
  protected int                                     batchSize;
  int                                               currentBatchNo            = 0;
  protected Set<Long>                               passedBaseEntityIids = new HashSet<>();
  protected boolean                                 shouldEvaluateIdentifier;
  protected Map<Long, IBaseEntityDTO>               entityIIdVsDto = new HashMap<>();
  

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));

    ruleHandlerDTO.fromJSON(jobData.getEntryData().toString());
    
    nbBatches = ruleHandlerDTO.getBaseEntityIids().size() / batchSize;
    Boolean shouldUseCSCache = ruleHandlerDTO.getShouldUseCSCache();
    shouldEvaluateIdentifier = ruleHandlerDTO.getShouldEvaluateIdentifier();
    
    rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    List<Long> classifierIids = ruleHandlerDTO.getClassifierIids();
    Collections.sort(classifierIids);
    String key = Text.join("_", classifierIids) + "_configdata";
    if (shouldUseCSCache  && CSCache.instance().isKept(key)) {
      detailsFromODB = (JSONObject) CSCache.instance().get(key);
    }
    else {
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("classifiers", classifierIids);
      detailsFromODB = CSConfigServer.instance().request(requestMap, "GetProductIdentifiersForClassifiersForMigration",
              rdbmsComponentUtils.getDataLanguage());
      if (shouldUseCSCache) {
        CSCache.instance().keep(key, detailsFromODB);
      }
      
    }
    try {
      dataRulesHelperModel = ObjectMapperUtil.readValue(detailsFromODB.toString(),
          DataRulesHelperModel.class);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    if(currentBatchNo == 0) {
      ILocaleCatalogDAO localeCatalogDAO = new LocaleCatalogDAO(userSession, new LocaleCatalogDTO("en_US","pim","stdo"));
      List<IBaseEntityDTO> baseEntityDTOsByIIDs = localeCatalogDAO.getBaseEntityDTOsByIIDs(new HashSet<>(ruleHandlerDTO.getBaseEntityIids()));
      for (IBaseEntityDTO iBaseEntityDTO : baseEntityDTOsByIIDs) {
        entityIIdVsDto.put(iBaseEntityDTO.getBaseEntityIID(), iBaseEntityDTO);
      }
    }
    currentBatchNo = currentBatchNo + 1;
    List<Long> currentBaseEntityIids = new ArrayList<>(ruleHandlerDTO.getBaseEntityIids());
    currentBaseEntityIids.removeAll(passedBaseEntityIids);
    RuleHandler ruleHandler = new RuleHandler();
    
    Set<Long> batchBaseEntityIids = new HashSet<>();
    Iterator<Long> remEntityIID = currentBaseEntityIids.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      Long baseEntityIID = remEntityIID.next();
      IBaseEntityDTO baseEntityDTO = entityIIdVsDto.remove(baseEntityIID);
      ILocaleCatalogDAO localeCatalogDAO = new LocaleCatalogDAO(userSession, new LocaleCatalogDTO(baseEntityDTO.getBaseLocaleID(), 
          baseEntityDTO.getCatalog().getCatalogCode(),baseEntityDTO.getCatalog().getOrganizationCode()));
      IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntityDTO);
      List<String> baseEntityClassifiers = new ArrayList<String>();
      List<String> baseEntityTaxonomies = new ArrayList<String>();
      ruleHandler.getBaseEntityClassifiersAndTaxonomies(localeCatalogDAO, baseEntityDAO,baseEntityClassifiers, baseEntityTaxonomies);
      IUniquenessViolationDAO openUniquenessDAO = localeCatalogDAO.openUniquenessDAO();
      
      ruleHandler.applyDQRules(localeCatalogDAO, baseEntityIID, baseEntityClassifiers,baseEntityTaxonomies, new ArrayList<>(), true, RuleType.kpi,
          dataRulesHelperModel.getReferencedElements(),dataRulesHelperModel.getReferencedAttributes(),dataRulesHelperModel.getReferencedTags());
      /*ruleHandler.evaluateDQMustShould(baseEntityDTO.getBaseEntityIID(), dataRulesHelperModel,
          baseEntityDAO, localeCatalogDAO);*/
      if(shouldEvaluateIdentifier) {
        evaluateProductIdentifiers(baseEntityIID,detailsFromODB, (BaseEntityDAO) baseEntityDAO, openUniquenessDAO, localeCatalogDAO);
      }
      batchBaseEntityIids.add(baseEntityIID);
    }
    
    
    passedBaseEntityIids.addAll(batchBaseEntityIids);
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);

    // Keep continuing as soon as the final number of batches has not been reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchBaseEntityIids.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
    
  }
  
  
  @SuppressWarnings("unchecked")
  public void evaluateProductIdentifiers(long baseEntityIID, JSONObject detailsFromODB,
      BaseEntityDAO entityDao, IUniquenessViolationDAO uniquenessDAO, ILocaleCatalogDAO localCatalogDAO)
      throws RDBMSException, CSFormatException
  {
    List<IUniquenessViolationDTO> entryDataForBackgroundProcess = new ArrayList<>();
    Map<String, List<Long>> productIdentifiers = (Map<String, List<Long>>) detailsFromODB.get(IDataRulesHelperModel.PRODUCT_IDENTIFIERS);
    // evaluate identifier
    if (productIdentifiers == null || productIdentifiers.isEmpty()) {
      return;
    }
    Set<Long> allProductIdentifierIIds = productIdentifiers.values().stream().flatMap(list -> list.stream()).collect(Collectors.toSet());
    List<IPropertyDTO> propertyDTOs = new ArrayList<>();
    for (Long propertyIID : allProductIdentifierIIds) {
      propertyDTOs.add(ConfigurationDAO.instance().getPropertyByIID(propertyIID));
    }
    
    Map<Long, Set<IPropertyRecordDTO>> propertyRecordsForEntities = localCatalogDAO.getPropertyRecordsForEntities(Set.of(baseEntityIID), 
        propertyDTOs.toArray(new IPropertyDTO[] {}));
    Set<IPropertyRecordDTO> propertyRecords = propertyRecordsForEntities.computeIfAbsent(baseEntityIID, k -> new HashSet<>());
    
    Map<Long, IPropertyRecordDTO> iidVsPropertyDTO = new HashMap<>();
    for (IPropertyRecordDTO iPropertyRecordDTO : propertyRecords) {
      iidVsPropertyDTO.put(iPropertyRecordDTO.getProperty().getPropertyIID(), iPropertyRecordDTO);
    }
    
    for (Map.Entry<String, List<Long>> entry : productIdentifiers.entrySet()) {
      List<Long> productIdentifierIIds = new ArrayList<>(entry.getValue());
      long classifierIID = Long.parseLong(entry.getKey());
      
      uniquenessDAO.deleteBeforeEvaluateIdentifier(baseEntityIID, productIdentifierIIds , classifierIID);
      for ( long identifierIID : productIdentifierIIds) {
        
        IPropertyRecordDTO propertyRecord = iidVsPropertyDTO.get(identifierIID);
        if(propertyRecord == null) {
          continue;
        }
        List<Long> violatedEntites = uniquenessDAO.evaluateProductIdentifierForMigration(identifierIID, classifierIID, 
            entityDao.getBaseEntityDTO().getCatalog().getCatalogCode(), ((ValueRecordDTO)propertyRecord).getValue());
        
        if(violatedEntites.size() > 1) {
          violatedEntites.remove(baseEntityIID);
          List<IUniquenessViolationDTO> toInsertViolatedEntities = new ArrayList<>();
          
          for(long targetEntityIID: violatedEntites) {
            IUniquenessViolationDTO uniquenessViolationDTO = localCatalogDAO.newUniquenessViolationBuilder(baseEntityIID, 
                targetEntityIID, identifierIID, classifierIID)
                .build();
          
            toInsertViolatedEntities.add(uniquenessViolationDTO);
            
            IUniquenessViolationDTO DtoForBackGroundProcess = localCatalogDAO.newUniquenessViolationBuilder(targetEntityIID, 
                baseEntityIID, identifierIID, classifierIID)
            .build();
            
            entryDataForBackgroundProcess.add(DtoForBackGroundProcess);
          }
          uniquenessDAO.insertViolatedEntity(toInsertViolatedEntities);
        }
        
      }
      
    }
    
    if(!entryDataForBackgroundProcess.isEmpty()) {
      uniquenessDAO.insertViolatedEntity(entryDataForBackgroundProcess);
      
    }
  }

}
