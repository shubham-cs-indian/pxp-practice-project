package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.LanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.ILanguageInheritanceDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class LanguageInheritanceTask extends AbstractBGProcessJob implements IBGProcessJob{

  protected int                nbBatches                   = 1;
  protected int                batchSize;
  int                          currentBatchNo              = 0;
  ILanguageInheritanceDTO languageInheritanceDTO = new LanguageInheritanceDTO();
  protected Set<ILanguageInheritanceDTO> passedBGPCouplingDTOs = new HashSet<>();

  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    
    languageInheritanceDTO.fromJSON(jobData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    currentBatchNo = currentBatchNo + 1;
    Set<ILanguageInheritanceDTO> currentCouplingDTOs = new HashSet<>();
    
    currentCouplingDTOs.add(languageInheritanceDTO);
    currentCouplingDTOs.removeAll(passedBGPCouplingDTOs);
    
    Set<ILanguageInheritanceDTO> batchCouplingDTOs = new HashSet<>();
    Iterator<ILanguageInheritanceDTO> remEntityIID = currentCouplingDTOs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchCouplingDTOs.add(remEntityIID.next());
    }
    
    
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    List<Long> dependentPropertyIIDs = languageInheritanceDTO.getDependentPropertyIIDs();
    Long baseEntityIID = languageInheritanceDTO.getBaseEntityIID();
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    List<String> languageCodesForBaseEntity = couplingDAO.getlanguageCodesByBaseEntityIIDs(baseEntityIID, baseEntityIID);
    List<String> localeIIDs = languageInheritanceDTO.getLocaleIIDs();
    
    for(String targetLocalCode : localeIIDs) {
      
      ILanguageConfigDTO languageConfigDTO = configurationDAO.getLanguageConfig(targetLocalCode);
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, targetLocalCode);
      
      List<Long> parentIIDs = languageConfigDTO.getParentIIDs();
      parentIIDs.remove(-1l);
      
      createConfictingValuesToPullNotificationFromParent(configurationDAO, dependentPropertyIIDs, baseEntityIID, targetLocalCode,
          targetBaseEntityDAO, couplingDAO, parentIIDs, languageCodesForBaseEntity);
      
      createConflictingValuesToPushNotificationToChild(configurationDAO, dependentPropertyIIDs, baseEntityIID, targetLocalCode, couplingDAO,
          languageCodesForBaseEntity);
      
    }
    
    passedBGPCouplingDTOs.addAll(batchCouplingDTOs);
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);
    // Keep continuing as soon as the final number of batches has not been reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchCouplingDTOs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
    
  }

  private void createConflictingValuesToPushNotificationToChild(ConfigurationDAO configurationDAO, List<Long> dependentPropertyIIDs,
      Long baseEntityIID, String targetLocalCode, ICouplingDAO couplingDAO, List<String> languageCodesForBaseEntity)
      throws RDBMSException, Exception
  {
    List<Long> childLanguageIIds = configurationDAO
        .getChildLanguageByLanguageIID(configurationDAO.getLanguageConfig(targetLocalCode).getLanguageIID());
    IBaseEntityDAO sourceBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, targetLocalCode);
    for (Long childLanguageIID : childLanguageIIds) {
      
      List<IPropertyRecordDTO> dataTransferProperties = new ArrayList<>();
      
      if (!languageCodesForBaseEntity.contains(configurationDAO.getLanguageConfigByLanguageIID(childLanguageIID).getLanguageCode())) {
        continue;
      }
      
      String childLanguageCode = configurationDAO.getLanguageConfigByLanguageIID(childLanguageIID).getLanguageCode();
      
      IBaseEntityDAO targetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, childLanguageCode);
      
      List<IPropertyDTO> propertyDTOs = new ArrayList<>();
      for (Long dependentPropertyIID : dependentPropertyIIDs) {
        
        IPropertyDTO propertyDTO = configurationDAO.getPropertyByIID(dependentPropertyIID);
        dataTransferProperties.add(sourceBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "").build());
        propertyDTOs.add(propertyDTO);
      }
      
      ICouplingDTO couplingDTO = DataTransferUtil.prepareCouplingDTO(baseEntityIID, baseEntityIID);
      couplingDTO.setLocaleIID(childLanguageIID);
      couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      couplingDTO.setCouplingSourceType(CouplingType.LANG_INHERITANCE);
      couplingDTO.setCouplingSourceIID(configurationDAO.getLanguageConfig(targetLocalCode).getLanguageIID());
      
      List<IPropertyRecordDTO> applicableProperties = new ArrayList<>();
      
      createPropertyRecordInstance(targetEntityDAO, dataTransferProperties,targetLocalCode, 
          sourceBaseEntityDAO, propertyDTOs, couplingDTO, applicableProperties, childLanguageCode);
      
      
      for (IPropertyRecordDTO propertyRecordDTO : applicableProperties) {
        couplingDTO.setPropertyIID(propertyRecordDTO.getProperty().getPropertyIID());
        List<ICouplingDTO> coupledRecordForTargetLocale = couplingDAO.getCoupledRecordForLocaleIID(couplingDTO);
        
        if (coupledRecordForTargetLocale.size() > 0) {
          
          IValueRecordDTO propertyRecord = targetEntityDAO.newValueRecordDTOBuilder(propertyRecordDTO.getProperty(), "")
              .localeID(childLanguageCode).build();
          
          targetEntityDAO.createPropertyRecords(propertyRecord);
          couplingDAO.deleteCoupledRecordByLocaleIIDAndPropertyIID(coupledRecordForTargetLocale.get(0), rdbmsComponentUtils.getLocaleCatlogDAO());
          couplingDAO.updateConflictingValuesByLocaleIID(coupledRecordForTargetLocale.get(0), RecordStatus.COUPLED);
        }
        couplingDAO.createCoupledRecordForContextual(couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
      }
    }
  }

  private void createConfictingValuesToPullNotificationFromParent(ConfigurationDAO configurationDAO, List<Long> dependentPropertyIIDs,
      Long baseEntityIID, String targetLocalCode, IBaseEntityDAO targetBaseEntityDAO, ICouplingDAO couplingDAO, List<Long> parentIIDs,
      List<String> languageCodesForBaseEntity) throws RDBMSException, Exception
  {
    for (Long parentIID : parentIIDs) {
      
      List<IPropertyRecordDTO> dataTransferProperties = new ArrayList<>();
      
      if (!languageCodesForBaseEntity.contains(configurationDAO.getLanguageConfigByLanguageIID(parentIID).getLanguageCode())) {
        continue;
      }
      
      String parentLanguageCode = configurationDAO.getLanguageConfigByLanguageIID(parentIID).getLanguageCode();
      IBaseEntityDAO sourceBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, parentLanguageCode);
      
      List<IPropertyDTO> propertyDTOs = new ArrayList<>();
      for (Long dependentPropertyIID : dependentPropertyIIDs) {
        
        IPropertyDTO propertyDTO = configurationDAO.getPropertyByIID(dependentPropertyIID);
        dataTransferProperties.add(sourceBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "").build());
        propertyDTOs.add(propertyDTO);
      }
      
      ICouplingDTO couplingDTO = DataTransferUtil.prepareCouplingDTO(baseEntityIID, baseEntityIID);
      couplingDTO.setLocaleIID(configurationDAO.getLanguageConfig(targetLocalCode).getLanguageIID());
      couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      couplingDTO.setCouplingSourceType(CouplingType.LANG_INHERITANCE);
      couplingDTO.setCouplingSourceIID(parentIID);
      
      List<IPropertyRecordDTO> applicableProperties = new ArrayList<>();
      
      createPropertyRecordInstance(targetBaseEntityDAO, dataTransferProperties, parentLanguageCode, 
          sourceBaseEntityDAO, propertyDTOs, couplingDTO, applicableProperties, targetLocalCode);
      
      
      for (IPropertyRecordDTO propertyRecordDTO : applicableProperties) {
        couplingDTO.setPropertyIID(propertyRecordDTO.getProperty().getPropertyIID());
        List<ICouplingDTO> coupledRecordForTargetLocale = couplingDAO.getCoupledRecordForLocaleIID(couplingDTO);
        
        if (coupledRecordForTargetLocale.size() > 0) {
          
          IValueRecordDTO propertyRecord = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyRecordDTO.getProperty(), "")
              .localeID(targetLocalCode).build();
          
          targetBaseEntityDAO.createPropertyRecords(propertyRecord);
          couplingDAO.deleteCoupledRecordByLocaleIIDAndPropertyIID(coupledRecordForTargetLocale.get(0), rdbmsComponentUtils.getLocaleCatlogDAO());
          couplingDAO.updateConflictingValuesByLocaleIID(coupledRecordForTargetLocale.get(0), RecordStatus.COUPLED);
        }
        couplingDAO.createCoupledRecordForContextual(couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
      }
    }
  }
  
  private void createPropertyRecordInstance(IBaseEntityDAO targetBaseEntityDAO, List<IPropertyRecordDTO> dataTransferProperties,
      String parentLanguageCode, IBaseEntityDAO sourceBaseEntityDAO, List<IPropertyDTO> propertyDTOs, 
      ICouplingDTO couplingDTO, List<IPropertyRecordDTO> applicableProperties, String targetLocalCode)
      throws Exception
  {
    IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO
        .loadPropertyRecords(propertyDTOs.toArray(new IPropertyDTO[propertyDTOs.size()]));
    
    IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO
        .loadPropertyRecords(propertyDTOs.toArray(new IPropertyDTO[propertyDTOs.size()]));
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    IConfigDetailsForBulkPropagationResponseModel configDetailForBaseEntity = DataTransferUtil.getConfigDetailForBaseEntity(sourceBaseEntityDAO);
    Map<String, IReferencedSectionElementModel> referencedElements = configDetailForBaseEntity.getReferencedElements();
    
    Set<IPropertyRecordDTO> createRecordsForSource = new HashSet<>();
    Set<IPropertyRecordDTO> createRecordsForTarget = new HashSet<>();
    
    for (IPropertyRecordDTO record : dataTransferProperties) {
      
      if(!referencedElements.containsKey(record.getProperty().getPropertyCode())) {
        continue;
      }
      
      IReferencedSectionAttributeModel sourceAttributeElement = 
          (IReferencedSectionAttributeModel) referencedElements.get(record.getProperty().getPropertyCode());
      
      if(sourceAttributeElement.getAttributeVariantContext() != null ) {
        continue;
      }
      
      couplingDTO.setPropertyIID(record.getProperty().getPropertyIID());
      List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist = couplingDAO.checkWheatherConflictingValuesAlreadyExist(couplingDTO);
      
      if(checkWheatherConflictingValuesAlreadyExist.size() > 0) {
        continue;
      }
      
      applicableProperties.add(record);
      IPropertyRecordDTO sourceProperty = sourceBaseEntityDTO.getPropertyRecord(record.getProperty().getPropertyIID());
      IPropertyRecordDTO targetProperty = targetBaseEntityDTO.getPropertyRecord(record.getProperty().getPropertyIID());
      
      if (sourceProperty == null) {
        
        ICouplingDTO coupledCouplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
        coupledCouplingDTO.setTargetEntityIID(sourceBaseEntityDTO.getBaseEntityIID());
        coupledCouplingDTO.setSourceEntityIID(sourceBaseEntityDTO.getBaseEntityIID());
        coupledCouplingDTO.setPropertyIID(record.getProperty().getPropertyIID());
        coupledCouplingDTO.setLocaleIID(ConfigurationDAO.instance().getLanguageConfig(parentLanguageCode).getLanguageIID());
        List<ICouplingDTO> coupledRecordForLocaleIID = couplingDAO.getCoupledRecordForLocaleIID(coupledCouplingDTO);
        if(coupledRecordForLocaleIID.size() == 0) {
          
          IValueRecordDTO propertyRecord = sourceBaseEntityDAO.newValueRecordDTOBuilder(record.getProperty(), "")
              .localeID(parentLanguageCode).build();
          createRecordsForSource.add(propertyRecord);
        }
      }
      else {
        
        if (targetProperty == null && !((IValueRecordDTO)sourceProperty).getValue().isEmpty()) {
          IValueRecordDTO propertyRecord = targetBaseEntityDAO.newValueRecordDTOBuilder(record.getProperty(), "")
              .localeID(targetLocalCode).build();
          createRecordsForTarget.add(propertyRecord);
        }
      }
    }
    sourceBaseEntityDAO.createPropertyRecords(createRecordsForSource.toArray(new IPropertyRecordDTO[createRecordsForSource.size()]));
    targetBaseEntityDAO.createPropertyRecords(createRecordsForTarget.toArray(new IPropertyRecordDTO[createRecordsForTarget.size()]));
  }
  
}
