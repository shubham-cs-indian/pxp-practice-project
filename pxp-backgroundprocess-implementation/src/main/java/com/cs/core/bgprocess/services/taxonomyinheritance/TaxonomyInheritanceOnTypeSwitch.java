package com.cs.core.bgprocess.services.taxonomyinheritance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.ListUtils;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.TaxonomyInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.ITaxonomyInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.templating.GetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class TaxonomyInheritanceOnTypeSwitch extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String             PROCESSED_IIDS         = "processedIIDs";

  ITaxonomyInheritanceOnTypeSwitchDTO taxonomyInheritanceForTypeSwitchDTO = new TaxonomyInheritanceOnTypeSwitchDTO();
  
  int                                 currentBatchNo                      = 0;
  
  protected int                       nbBatches                           = 1;
  protected int                       batchSize;
  protected List<Long>                linkedVariantEntityIIDs             = new ArrayList<>();
  protected int                       totalContents;
  protected Set<Long>                 passedBaseEntityIIDs                = new HashSet<>();
  protected Boolean                   isAutoInheritance                   = false;
  protected long                      relationshipIID;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    taxonomyInheritanceForTypeSwitchDTO.fromJSON(jobData.getEntryData().toString());
    isAutoInheritance = taxonomyInheritanceForTypeSwitchDTO.getTaxonomyInheritanceSetting().equals("auto");
    relationshipIID = ConfigurationDAO.instance().getPropertyByCode(taxonomyInheritanceForTypeSwitchDTO.getRelationshipId()).getPropertyIID();
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    ILocaleCatalogDAO localeCatalogDAO = openUserSession().openLocaleCatalog(userSession,
        new LocaleCatalogDTO(taxonomyInheritanceForTypeSwitchDTO.getLocaleID(), taxonomyInheritanceForTypeSwitchDTO.getCatalogCode(), ""));
    linkedVariantEntityIIDs = localeCatalogDAO.getOtherSideInstanceIIds(IRelationship.SIDE1, taxonomyInheritanceForTypeSwitchDTO.getSourceEntityIID().toString(),
        relationshipIID);
    totalContents = linkedVariantEntityIIDs.size();
    nbBatches = totalContents / batchSize;
    if ( nbBatches == 0 || totalContents % batchSize > 0 )
      nbBatches++;
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    int currentBatchNo = getCurrentBatchNo() + 1;
    linkedVariantEntityIIDs.removeAll(passedBaseEntityIIDs);
    Set<Long> batchEntityIIDs = new HashSet<>();
    Iterator<Long> remEntityIID = linkedVariantEntityIIDs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchEntityIIDs.add(remEntityIID.next());
    }
    handleTaxonomyInheritance(batchEntityIIDs);

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
  
  private void handleTaxonomyInheritance(Set<Long> batchEntityIIDs) throws Exception
  {
    RDBMSComponentUtils rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    Long sourceEntityIID = taxonomyInheritanceForTypeSwitchDTO.getSourceEntityIID();
    IBaseEntityDTO sourceEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(sourceEntityIID);
    List<String> sourceEntityTaxonomyIIDs = getTaxonomyIIDs(sourceEntityDTO);

    ITypeSwitchInstance typeSwitchInstance = BGProcessApplication.getApplicationContext().getBean(ITypeSwitchInstance.class);
    for (Long linkedVariantIId : batchEntityIIDs) {
      userSession.setTransactionId(UUID.randomUUID().toString());
      ITaxonomyInheritanceDTO taxonomyInheritanceDTO = rdbmsComponentUtils.newTaxonomyInheritanceDTO(linkedVariantIId, sourceEntityIID, relationshipIID);
      IBaseEntityDTO linkedVariantEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(linkedVariantIId);
      List<String> linkedVariantTaxonomyIIDs = getTaxonomyIIDs(linkedVariantEntityDTO);

      List<String> addedTaxonomiesForInheritance = new ArrayList<String>(taxonomyInheritanceForTypeSwitchDTO.getAddedTaxonomyIds());
      List<String> removedTaxonomiesForInheritance = ListUtils.intersection(linkedVariantTaxonomyIIDs, taxonomyInheritanceForTypeSwitchDTO.getRemovedTaxonomyIds());
      addedTaxonomiesForInheritance.removeAll(linkedVariantTaxonomyIIDs);
      taxonomyInheritanceDTO.setIsResolved(isAutoInheritance);
      Boolean noChangePresent = addedTaxonomiesForInheritance.isEmpty() && removedTaxonomiesForInheritance.isEmpty();
      if (noChangePresent && sourceEntityTaxonomyIIDs.equals(linkedVariantTaxonomyIIDs)) {
        taxonomyInheritanceDTO.setIsResolved(true);
      }
      else if(noChangePresent) {
        continue;
      }
      
      ITaxonomyInheritanceDAO taxonomyInheritanceDAO = rdbmsComponentUtils.getTaxonomyInheritanceDAO(taxonomyInheritanceDTO);
      taxonomyInheritanceDAO.upsertTaxonmyConflict();
      
      if(isAutoInheritance && !noChangePresent) {
        Map<String, Object> requestModel = new HashMap<>();
        List<String> klassIds = new ArrayList<>();
        klassIds.add(linkedVariantEntityDTO.getNatureClassifier().getClassifierCode());
        requestModel.put("klassIds", klassIds);
        Map<String, Object> configDetails = CSConfigServer.instance().request(requestModel, "GetNumberOfVersionsToMaintain", "en_US");
        IGetNumberOfVersionsToMaintainResponseModel configDetailsForNumberOfVersionAllowed = ObjectMapperUtil
            .readValue(ObjectMapperUtil.writeValueAsString(configDetails), GetNumberOfVersionsToMaintainResponseModel.class);
        
        
        IKlassInstanceTypeSwitchModel typeSwitchRequestModel = new KlassInstanceTypeSwitchModel();
        typeSwitchRequestModel.setKlassInstanceId(Long.toString(linkedVariantIId));
        typeSwitchRequestModel.setAddedTaxonomyIds(addedTaxonomiesForInheritance);
        typeSwitchRequestModel.setDeletedTaxonomyIds(removedTaxonomiesForInheritance);
        typeSwitchInstance.execute(typeSwitchRequestModel);
        rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(linkedVariantIId, EventType.ELASTIC_UPDATE);
        rdbmsComponentUtils.createNewRevision(linkedVariantEntityDTO, configDetailsForNumberOfVersionAllowed.getNumberOfVersionsToMaintain());
      }
    }
  }
  
  private List<String> getTaxonomyIIDs(IBaseEntityDTO baseEntityDTO)
  {
    List<String> taxonomy = new ArrayList<>();
    for (IClassifierDTO classifier : baseEntityDTO.getOtherClassifiers()) {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.TAXONOMY)) {
        taxonomy.add(classifier.getClassifierCode());
      }
    }
    return taxonomy;
  }
  
}
