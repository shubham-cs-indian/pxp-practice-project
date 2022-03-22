package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.exception.PluginException;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.coupling.dto.BGPCouplingDTO;
import com.cs.core.rdbms.coupling.dto.ClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.DataTransferForClonedEntitiesDTO;
import com.cs.core.rdbms.coupling.dto.LanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.dto.RelationshipDataTransferDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.IDataTransferForClonedEntitiesDTO;
import com.cs.core.rdbms.coupling.idto.ILanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.idto.IRelationshipDataTransferDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class HandleDataTransferForClonedEntities extends AbstractBGProcessJob implements IBGProcessJob {

  protected int                          nbBatches                     = 1;
  protected int                          batchSize;
  protected int                          currentBatchNo                = 0;
  
  private static final BGPPriority                             BGP_PRIORITY                 = BGPPriority.HIGH;
  
  private static final String                                  SERVICE_FOR_RDT              = "RELATIONSHIP_DATA_TRANSFER";
  
  private static final String                                  SERVICE                      = "CLASSIFICATION_DATA_TRANSFER";
  
  private static final String                                  SERVICE_FOR_CDT              = "CONTEXTUAL_DATA_TRANSFER_TASK";
  
  private static final String                                  SERVICE_FOR_LANG_INHERITANCE = "LANGUAGE_INHERITANCE_TASK";
  private IDataTransferForClonedEntitiesDTO dataTransferForClonedEntitiesDTO = new DataTransferForClonedEntitiesDTO();
  
  IGetConfigDetailsWithoutPermissionsStrategy configDetailsStrategy = BGProcessApplication.getApplicationContext()
      .getBean(IGetConfigDetailsWithoutPermissionsStrategy.class);
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    
    dataTransferForClonedEntitiesDTO.fromJSON(jobData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();

    List<IBGPCouplingDTO> bgpcouplingDTOs = new ArrayList<>();
    IContextualDataTransferDTO contextualDataTransferDTO = new ContextualDataTransferDTO();
    
    for(Long entityIID : dataTransferForClonedEntitiesDTO.getClonedBaseEntityIIDs()) {
      IBaseEntityDTO clonedBaseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(entityIID);
      Collection<IPropertyDTO> propertiesToClone = rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntityProperties(entityIID);
      
      IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(clonedBaseEntityDTO);
      
      List<String> relationshipsToBeCloned = new ArrayList<>();
      for (IPropertyDTO property : propertiesToClone) {
        if (property.getPropertyType().equals(PropertyType.RELATIONSHIP)) {
          relationshipsToBeCloned.add(property.getPropertyCode());
        }
      }
      Set<String> linkedVariantRelationship = configDetails.getReferencedNatureRelationships().keySet();
      
      List<IContextualDataTransferGranularDTO> DataTransferDTOs = localeCatlogDAO.
          openCouplingDAO().triggerContextualDataTransferFromCloning(entityIID);
      
      if(!DataTransferDTOs.isEmpty()) {
        contextualDataTransferDTO.getBGPCouplingDTOs().addAll(DataTransferDTOs);
      }
      
      long originBaseEntityIID = clonedBaseEntityDTO.getOriginBaseEntityIID();
        bgpcouplingDTOs.addAll(prepareDataForRelationshipDataTransfer(originBaseEntityIID, entityIID,
            relationshipsToBeCloned, localeCatlogDAO, linkedVariantRelationship));
      
      initiateClassificationDataTransfer(localeCatlogDAO, clonedBaseEntityDTO);
      
      initiateBackgroundTaskForLanguageInheritance(configDetails, entityIID);
    }
    
    prepareContextualDataTransfer(contextualDataTransferDTO);
    
    prepareDataForRelationshipDataTransfer(bgpcouplingDTOs);
    
    setCurrentBatchNo(++currentBatchNo);
    IBGProcessDTO.BGPStatus status = null;
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    
    return status;
  }
  
  private IGetConfigDetailsForCustomTabModel getConfigDetails(IBaseEntityDTO baseEntityDTO)
      throws CSFormatException, CSInitializationException, Exception, JsonProcessingException
  {
    Set<IClassifierDTO> classifiers = baseEntityDTO.getOtherClassifiers();
    classifiers.add(baseEntityDTO.getNatureClassifier());
    List<String> types = new ArrayList<>();
    List<String> taxonomyIds = new ArrayList<>();
    
    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types .add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(types);
    multiclassificationRequestModel.setSelectedTaxonomyIds(taxonomyIds);
    multiclassificationRequestModel.setUserId(rdbmsComponentUtils.getUserID());
    
    IGetConfigDetailsForCustomTabModel configDetails = configDetailsStrategy.execute(multiclassificationRequestModel);
    return configDetails;
  }
  
  private void prepareDataForRelationshipDataTransfer(List<IBGPCouplingDTO> bgpcouplingDTOs) throws RDBMSException, CSFormatException
  {
    if(bgpcouplingDTOs.isEmpty()) {
      return;
    }
    IRelationshipDataTransferDTO entryData = new RelationshipDataTransferDTO();
    entryData.setBGPCouplingDTOs(bgpcouplingDTOs);
    entryData.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    entryData.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    entryData.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    entryData.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(userSession.getUserName(), SERVICE_FOR_RDT, "", BGP_PRIORITY, new JSONContent(entryData.toJSON()));
  }

  public void prepareContextualDataTransfer(IContextualDataTransferDTO dataTransferDTO) throws Exception
  {
    if(dataTransferDTO.getBGPCouplingDTOs().isEmpty()) {
      return;
    }
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    dataTransferDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    dataTransferDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    dataTransferDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    dataTransferDTO.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CDT, "", userPriority,
        new JSONContent(dataTransferDTO.toJSON()));
  }
  
  private List<IBGPCouplingDTO> prepareDataForRelationshipDataTransfer(long parentEntityIID, long clonedEntityIID, List<String> relationshipIds,
      ILocaleCatalogDAO localeCatalogDAO, Set<String> linkedVariantRelationship) throws Exception
  {
    List<IBGPCouplingDTO> bgpcouplingDTOs = new ArrayList<>();
    ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
    List<IBaseEntityDTO> baseEntityIIDs = new ArrayList<>();
    baseEntityIIDs.add(rdbmsComponentUtils.getBaseEntityDTO(clonedEntityIID));
    Map<Long, Long> linkedVariantIds = localeCatalogDAO.getLinkedVariantIds(baseEntityIIDs, linkedVariantRelationship);
    Long originBaseEntityIID = parentEntityIID == 0 ? linkedVariantIds.get(clonedEntityIID) : parentEntityIID;
    for (String relationshipId : relationshipIds) {
      Long relationshipIID = ConfigurationDAO.instance().getPropertyByCode(relationshipId).getPropertyIID();
      List<ICouplingDTO> couplingDTOs = couplingDAO.getConflictingValuesFromRelationship(originBaseEntityIID, relationshipIID);
      List<Long> sourceEntityIIDs = new ArrayList<>();
      List<Long> targetEntityIIDs = new ArrayList<>();

      if (!couplingDTOs.isEmpty()) {
        IBGPCouplingDTO coupling = new BGPCouplingDTO();
        for (ICouplingDTO couplingDTO : couplingDTOs) {
          sourceEntityIIDs.add(couplingDTO.getSourceEntityIID());
          targetEntityIIDs.add(couplingDTO.getTargetEntityIID());
        }

        if(sourceEntityIIDs.contains(originBaseEntityIID)) {
          coupling.setAddedEntityIIDs(targetEntityIIDs);
          sourceEntityIIDs.clear();
          sourceEntityIIDs.add(clonedEntityIID);
          coupling.setSourceBaseEntityIIDs(sourceEntityIIDs);
        }
        else {
          coupling.setSourceBaseEntityIIDs(sourceEntityIIDs);
          targetEntityIIDs.clear();
          targetEntityIIDs.add(clonedEntityIID);
          coupling.setAddedEntityIIDs(targetEntityIIDs);
        }

        coupling.setRelationshipId(relationshipId);
        coupling.setSideId(null);
        bgpcouplingDTOs.add(coupling);
      }
    }

    return bgpcouplingDTOs;
  }
  
  private void initiateClassificationDataTransfer(ILocaleCatalogDAO localeCatlogDAO, IBaseEntityDTO baseEntityDTO) throws Exception
  {
    Set<IClassifierDTO> addedClassifiers = baseEntityDTO.getOtherClassifiers();
    addedClassifiers.add(baseEntityDTO.getNatureClassifier());
    IClassificationDataTransferDTO classificationDataTransfer = new ClassificationDataTransferDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    classificationDataTransfer.setLocaleID(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setCatalogCode(localeCatalogDTO.getCatalogCode());
    classificationDataTransfer.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    classificationDataTransfer.setUserId(rdbmsComponentUtils.getUserID());
    classificationDataTransfer.setBaseEntityIID(baseEntityDTO.getBaseEntityIID());
    classificationDataTransfer.setAddedOtherClassifiers(addedClassifiers);
    classificationDataTransfer.setRemovedOtherClassifiers(new HashSet<IClassifierDTO>());
    
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE, "", BGP_PRIORITY,
        new JSONContent(classificationDataTransfer.toJSON()));
  }
  
  private void initiateBackgroundTaskForLanguageInheritance(IGetConfigDetailsForCustomTabModel configDetails, long baseEntityIID)
      throws RDBMSException, Exception, CSFormatException
  {
    List<Long> dependentPropertyIIDs = new ArrayList<>();
    
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    for (IAttribute referencedAttribute : referencedAttributes.values()) {
      if (referencedAttribute.getIsTranslatable()) {
        dependentPropertyIIDs.add(referencedAttribute.getPropertyIID());
      }
    }
    
    if(dependentPropertyIIDs.isEmpty()) {
      return;
    }
    
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    List<String> languageCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(baseEntityIID, baseEntityIID);
    
    ILanguageInheritanceDTO languageInheritanceDTO = new LanguageInheritanceDTO();
    languageInheritanceDTO.setDependentPropertyIIDs(dependentPropertyIIDs);
    languageInheritanceDTO.setBaseEntityIID(baseEntityIID);
    
    for(String languageCode : languageCodes) {
      languageInheritanceDTO.getLocaleIIDs().add(languageCode);
    }
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    languageInheritanceDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    languageInheritanceDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    languageInheritanceDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    languageInheritanceDTO.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_LANG_INHERITANCE, "", userPriority,
        new JSONContent(languageInheritanceDTO.toJSON()));
  }
}
