package com.cs.core.bgprocess.services.taxonomyinheritance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.ListUtils;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BGPTaxonomyInheritanceDTO;
import com.cs.core.bgprocess.idto.IBGPTaxonomyInheritanceDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.configdetails.GetInheritenceTaxonomyIdsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetInheritanceTaxonomyIdsResponseModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.templating.GetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict.Setting;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TaxonomyInheritance extends AbstractBGProcessJob implements IBGProcessJob {
  
  IBGPTaxonomyInheritanceDTO taxonomyInheritanceDTO = new BGPTaxonomyInheritanceDTO();
  
  private String[]           stepLabels             = { "AddTaxonomyConflicts", "DeleteTaxonomyConflicts" };
  RDBMSComponentUtils        rdbmsComponentUtils;
  
  private enum STEPS
  {
    
    ADD_TAXONOMY_CONFLICTS, DELETE_TAXONOMY_CONFLICTS;
    
    static STEPS valueOf(int i)
    {
      return STEPS.values()[i];
    }
  }
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    taxonomyInheritanceDTO.fromJSON(jobData.getEntryData().toString());
    rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    this.initProgressData();
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  protected void initProgressData()
  {
    if (jobData.getProgress().getStepNames().size() == 1) {
      jobData.getProgress().initSteps(stepLabels);
    }
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    int currentStepNo = jobData.getProgress().getCurrentStepNo();
    STEPS currentStep = STEPS.valueOf(currentStepNo - 1);
    
    switch (currentStep) {
      case ADD_TAXONOMY_CONFLICTS:
        addTaxonomyConflicts();
        break;
      case DELETE_TAXONOMY_CONFLICTS:
        deleteTaxonomyConflicts();
        break;
      default:
        throw new RDBMSException(100, "Programm Error", "Unexpected step-no: " + currentStepNo);
    }
    
    jobData.getProgress().incrStepNo();
    
    // Return of status
    IBGProcessDTO.BGPStatus status = null;
    if (jobData.getProgress().getPercentageCompletion() == 100)
      status = jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS : IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    else
      status = BGPStatus.RUNNING;
    
    return status;
  }
  
  private void addTaxonomyConflicts() throws RDBMSException, Exception
  {
    Long sourceEntityIID = taxonomyInheritanceDTO.getSourceEntityIID();
    Long propertyIID = taxonomyInheritanceDTO.getPropertyIID();
    String taxonomySetting = taxonomyInheritanceDTO.getTaxonomyInheritanceSetting();
    Setting taxonomyInheritanceSetting = Setting.valueOf(taxonomySetting);
    IBaseEntityDTO sourceBaseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(sourceEntityIID);
    List<String> sourceTaxonomyIIDs = getTaxonomyIIDs(sourceBaseEntityDTO);
    Map<String, IReferencedTaxonomyParentModel> referencedTaxonomies = getReferencedTaxonomiesFromConfig(sourceTaxonomyIIDs);
    List<String> majorTaxonomies = new ArrayList<>(referencedTaxonomies.keySet());
    List<String> sourceMajorTaxonomies = ListUtils.retainAll(sourceTaxonomyIIDs, majorTaxonomies);
    ITypeSwitchInstance typeSwitchInstance = BGProcessApplication.getApplicationContext().getBean(ITypeSwitchInstance.class);
    
    for (Long linkedVariantIID : taxonomyInheritanceDTO.getAddedElementIIDs()) {
      IBaseEntityDTO variantBaseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(linkedVariantIID);
      userSession.setTransactionId(UUID.randomUUID().toString());
      List<String> variantTaxonomyIIDs = getTaxonomyIIDs(variantBaseEntityDTO);
      ITaxonomyInheritanceDTO taxonomyInheritanceDTO = rdbmsComponentUtils.newTaxonomyInheritanceDTO(linkedVariantIID, sourceEntityIID,
          propertyIID);
      
      List<String> addedTaxonomyIds = ListUtils.subtract(sourceMajorTaxonomies, variantTaxonomyIIDs);
      if (addedTaxonomyIds.isEmpty()) {
        taxonomyInheritanceDTO.setIsResolved(true);
      }
      if (taxonomyInheritanceSetting.equals(Setting.auto)) {
        
        Map<String, Object> requestModel = new HashMap<>();
        List<String> klassIds = new ArrayList<>();
        klassIds.add(variantBaseEntityDTO.getNatureClassifier().getClassifierCode());
        requestModel.put("klassIds", klassIds);
        Map<String, Object> configDetails = CSConfigServer.instance().request(requestModel, "GetNumberOfVersionsToMaintain", "en_US");
        IGetNumberOfVersionsToMaintainResponseModel configDetailsForNumberOfVersionAllowed = ObjectMapperUtil
            .readValue(ObjectMapperUtil.writeValueAsString(configDetails), GetNumberOfVersionsToMaintainResponseModel.class);
        
        taxonomyInheritanceDTO.setIsResolved(true);
        Boolean isTaxonomyApplied = applyTaxonomies(typeSwitchInstance, variantBaseEntityDTO, addedTaxonomyIds);
        rdbmsComponentUtils.createNewRevision(variantBaseEntityDTO, configDetailsForNumberOfVersionAllowed.getNumberOfVersionsToMaintain());
        if(isTaxonomyApplied)
            rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(variantBaseEntityDTO.getBaseEntityIID(), EventType.ELASTIC_UPDATE);
      }
      ITaxonomyInheritanceDAO taxonomyInheritanceDAO = rdbmsComponentUtils.getTaxonomyInheritanceDAO(taxonomyInheritanceDTO);
      taxonomyInheritanceDAO.upsertTaxonmyConflict();
    }
  }

  private Boolean applyTaxonomies(ITypeSwitchInstance typeSwitchInstance, IBaseEntityDTO variantBaseEntityDTO, List<String> addedTaxonomyIds) throws Exception
  {
    Boolean isTaxonomyApplied = false;
    IKlassInstanceTypeSwitchModel typeSwitchRequestModel = new KlassInstanceTypeSwitchModel();
    typeSwitchRequestModel.setAddedTaxonomyIds(addedTaxonomyIds);
    typeSwitchRequestModel.setKlassInstanceId(Long.toString(variantBaseEntityDTO.getBaseEntityIID()));
    if (!addedTaxonomyIds.isEmpty()) {
      typeSwitchInstance.execute(typeSwitchRequestModel);
      isTaxonomyApplied = true;
    }
    return isTaxonomyApplied;
  }

  private Map<String, IReferencedTaxonomyParentModel> getReferencedTaxonomiesFromConfig(List<String> sourceTaxonomyIIDs)
      throws CSFormatException, CSInitializationException, Exception, JsonProcessingException
  {
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("ids", sourceTaxonomyIIDs);
    Map<String, Object> configDetails = CSConfigServer.instance().request(requestModel, "GetConfigDetailsForTaxonomyInheritance",
        taxonomyInheritanceDTO.getLocaleID());
    IGetInheritanceTaxonomyIdsResponseModel getInheritanceTaxonomyIdsResponseModel = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(configDetails), GetInheritenceTaxonomyIdsResponseModel.class);
    
    Map<String, IReferencedTaxonomyParentModel> referencedTaxonomies = getInheritanceTaxonomyIdsResponseModel.getReferencedTaxonomies();
    return referencedTaxonomies;
  }
  
  private void deleteTaxonomyConflicts() throws RDBMSException, Exception
  {
    List<Long> deleteElementIIDs = taxonomyInheritanceDTO.getDeletedElementIIDs();
    if (!deleteElementIIDs.isEmpty()) {
      ITaxonomyInheritanceDAO taxonomyInheritanceDAO = null;
      taxonomyInheritanceDAO = rdbmsComponentUtils
          .getTaxonomyInheritanceDAO(rdbmsComponentUtils.getTaxonomyInheritanceDTO(deleteElementIIDs.get(0)));
      taxonomyInheritanceDAO.deleteTaxonmyConflict(deleteElementIIDs);
    }
  }
  
  private List<String> getTaxonomyIIDs(IBaseEntityDTO sourceBaseEntityDTO)
  {
    List<String> taxonomy = new ArrayList<>();
    for (IClassifierDTO classifier : sourceBaseEntityDTO.getOtherClassifiers()) {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.TAXONOMY)) {
        taxonomy.add(classifier.getClassifierCode());
      }
    }
    return taxonomy;
  }
}
