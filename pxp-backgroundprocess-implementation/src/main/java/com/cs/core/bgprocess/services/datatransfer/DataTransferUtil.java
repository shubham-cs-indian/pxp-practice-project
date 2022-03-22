package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.CouplingDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

public class DataTransferUtil {
  
  static RDBMSComponentUtils rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
  //public static Map<Long, IConfigDetailsForBulkPropagationResponseModel> baseEntitiesConfigDetails = new HashMap<>();
  
  public static final String VALUE_TO_BE_DISCARED = "Value to be discarded";
  
  protected static void handleHolderSideCouplingProperties(IBaseEntityDAO baseEntityDAO,
      IRelationshipSidePropertiesModel holderSideProperties, IPropertyDTO relationshipPropertyDTO,
      List<Long> addedTargetBaseEntityIIDs, List<Long> deletedTargetBaseEntityIIDs) throws Exception
  {
    List<IPropertyRecordDTO> holderSideCouplingPropertyRecord = new ArrayList<>();
    List<IPropertyRecordDTO> holderSideCouplingDependentAttributes = new ArrayList<>();
    int side = relationshipPropertyDTO.getRelationSide() == RelationSide.SIDE_1 ? 1 : 2;
    getCouplingAttributeProperties(baseEntityDAO, holderSideCouplingPropertyRecord, holderSideProperties, 
        relationshipPropertyDTO, side, holderSideCouplingDependentAttributes);
    getCouplingTagProperties(baseEntityDAO, holderSideCouplingPropertyRecord, holderSideProperties, relationshipPropertyDTO, side);
    
    if(!holderSideCouplingPropertyRecord.isEmpty() || !holderSideCouplingDependentAttributes.isEmpty()) {
      createCouplingPropertyRecords(holderSideCouplingPropertyRecord, holderSideCouplingDependentAttributes, 
          addedTargetBaseEntityIIDs, baseEntityDAO, relationshipPropertyDTO);
    }
    
    if (!deletedTargetBaseEntityIIDs.isEmpty() && holderSideProperties != null) {
      
      for (Long deletedElement : deletedTargetBaseEntityIIDs) {
        
        deleteCoupledRecord(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), deletedElement, baseEntityDAO, holderSideProperties,
            relationshipPropertyDTO);
      }
    }
  }
  
  protected static List<IPropertyRecordDTO> getCouplingAttributeProperties(IBaseEntityDAO baseEntityDAO,
      List<IPropertyRecordDTO> couplingPropertyRecord, IRelationshipSidePropertiesModel sideProperties,
      IPropertyDTO relationshipPropertyDTO, int side, List<IPropertyRecordDTO> dependentAttributes) throws Exception
  {
    // Language independent attributes
    if(!sideProperties.getAttributes().isEmpty()) {
      List<IPropertyRecordDTO> attributePropertyRecord = sideProperties.getAttributes().stream().map(couplingAttribute -> {
        return createCoupledValueRecord(baseEntityDAO, relationshipPropertyDTO, side, couplingAttribute);
      }).collect(Collectors.toList());
      couplingPropertyRecord.addAll(attributePropertyRecord);
    }
    
    // Language dependent attributes
    if(!sideProperties.getDependentAttributes().isEmpty()) {
      List<IPropertyRecordDTO> dependentAttributePropertyRecord = sideProperties.getDependentAttributes().stream().map(couplingAttribute -> {
        return createCoupledValueRecord(baseEntityDAO, relationshipPropertyDTO, side, couplingAttribute);
      }).collect(Collectors.toList());
      dependentAttributes.addAll(dependentAttributePropertyRecord);
    }
    
    return couplingPropertyRecord;
  }
  
  private static IValueRecordDTO createCoupledValueRecord(IBaseEntityDAO baseEntityDAO, IPropertyDTO relationshipPropertyDTO, int side,
      IReferencedRelationshipProperty couplingAttribute)
  {
    try {
      IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(couplingAttribute.getPropertyIID(), couplingAttribute.getId(),
          couplingAttribute.getCode(), couplingAttribute.getType());
      IValueRecordDTO valueRecordDTO = baseEntityDAO.newValueRecordDTOBuilder(propertyDTO, VALUE_TO_BE_DISCARED).build();
      
      String couplingType = couplingAttribute.getCouplingType();
      boolean isDynamic = false;
      if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
        isDynamic = true;
      }
      
      valueRecordDTO.addRelationshipCoupling(relationshipPropertyDTO, side, propertyDTO, isDynamic);
      return valueRecordDTO;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  protected static List<IPropertyRecordDTO> getCouplingTagProperties(IBaseEntityDAO baseEntityDAO,
      List<IPropertyRecordDTO> couplingPropertyRecord, IRelationshipSidePropertiesModel sideProperties,
      IPropertyDTO relationshipPropertyDTO, int side) throws Exception
  {
    if(!sideProperties.getTags().isEmpty()) {
      
      List<IPropertyRecordDTO> tagPropertyRecord = sideProperties.getTags().stream().map(couplingTag -> {
        try {
          IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(couplingTag.getPropertyIID(), couplingTag.getId(), couplingTag.getCode(),
              PropertyType.TAG);
          ITagsRecordDTO tagsRecordDTO = baseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
          
          String couplingType = couplingTag.getCouplingType();
          boolean isDynamic = false;
          if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
            isDynamic = true;
          }
          tagsRecordDTO.addRelationshipCoupling(relationshipPropertyDTO, side, propertyDTO, isDynamic);
          return tagsRecordDTO;
        }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());
      
      couplingPropertyRecord.addAll(tagPropertyRecord);
    }
    return couplingPropertyRecord;
  }
  
  protected static void createCouplingPropertyRecords(List<IPropertyRecordDTO> holderSideCouplingPropertyRecord, List<IPropertyRecordDTO> holderSideCouplingDependentAttributes, List<Long> addedElements,
      IBaseEntityDAO sourceBaseEntityDAO, IPropertyDTO relationshipPropertyDTO) throws Exception
  {
    IConfigDetailsForBulkPropagationResponseModel sourceBaseEntiyConfigDetails = getConfigDetailForBaseEntity(sourceBaseEntityDAO);
    addedElements.forEach(addedElement -> {
      try {
        ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
        IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(addedElement);
        
        IConfigDetailsForBulkPropagationResponseModel targetBaseEntiyConfigDetails = getConfigDetailForBaseEntity(targetBaseEntityDAO);
        
        ICouplingDTO couplingDTO = new CouplingDTO();
        couplingDTO.setCouplingSourceIID(relationshipPropertyDTO.getPropertyIID());
        couplingDTO.setSourceEntityIID(sourceBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
        couplingDTO.setTargetEntityIID(addedElement);

        // For Independent Property Records
        createCouplingRecordsForIndependentProperties(holderSideCouplingPropertyRecord, sourceBaseEntityDAO,
            couplingDTO, addedElement, couplingDAO, targetBaseEntityDAO, sourceBaseEntiyConfigDetails,
            targetBaseEntiyConfigDetails);
        
        // For Dependent Property Records
        createCouplingRecordsForDependentAttributes(holderSideCouplingDependentAttributes, sourceBaseEntityDAO, couplingDTO, 
            addedElement, couplingDAO, sourceBaseEntiyConfigDetails, targetBaseEntiyConfigDetails);
        
        RuleHandler ruleHandler = new RuleHandler();
        IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(targetBaseEntiyConfigDetails.getReferencedElements(),
            targetBaseEntiyConfigDetails.getReferencedAttributes(), targetBaseEntiyConfigDetails.getReferencedTags());
        ruleHandler.evaluateDQMustShould(targetBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), dataRules, targetBaseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO());
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
  
  public static IConfigDetailsForBulkPropagationResponseModel getConfigDetailForBaseEntity(IBaseEntityDAO baseEntityDAO) throws Exception {
    
    List<String> klassIds = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO);

    List<String> selectedTaxonomyIds = BaseEntityUtils
        .getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
    
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("klassIds", klassIds);
    requestModel.put("selectedTaxonomyIds", selectedTaxonomyIds);
    requestModel.put("addedKlassIds", new ArrayList<>());
    requestModel.put("addedTaxonomyIds", new ArrayList<>());
    requestModel.put("collectionIds", new ArrayList<>());
    requestModel.put("organizationId", rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    requestModel.put("physicalCatalogId", rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    requestModel.put("parentKlassIds", new ArrayList<>());
    requestModel.put("parentTaxonomyIds", new ArrayList<>());
    requestModel.put("languageCodes", new ArrayList<>());
    requestModel.put("userId", rdbmsComponentUtils.getUserID());
    
    JSONObject response
    = CSConfigServer.instance().request(requestModel, "GetConfigDetailsForBulkPropagation", null);

    IConfigDetailsForBulkPropagationResponseModel configDetails = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(response), ConfigDetailsForBulkPropagationResponseModel.class);
    
    return configDetails;
  }

  private static void createCouplingRecordsForDependentAttributes(List<IPropertyRecordDTO> holderSideCouplingDependentAttributes,
      IBaseEntityDAO sourceBaseEntityDAO, ICouplingDTO couplingDTO, Long addedElement, ICouplingDAO couplingDAO,
      IConfigDetailsForBulkPropagationResponseModel sourceBaseEntiyConfigDetails, IConfigDetailsForBulkPropagationResponseModel targetBaseEntiyConfigDetails)
      throws RDBMSException, Exception
  {
    long sourceEntityIID = sourceBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    List<String> languageCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(sourceEntityIID, addedElement);
    
    for (String languageCode : languageCodes) {
      ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfig(languageCode);
      
      IBaseEntityDAO targetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(addedElement, languageCode);
      IBaseEntityDAO sourceEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntityIID, languageCode);
      createCoupledRecords(holderSideCouplingDependentAttributes, sourceEntityDAO, addedElement, couplingDAO, targetEntityDAO,
          languageConfigDTO.getLanguageIID(), sourceBaseEntiyConfigDetails, 
          targetBaseEntiyConfigDetails, couplingDTO);
    }
  }

  private static void createCouplingRecordsForIndependentProperties(List<IPropertyRecordDTO> holderSideCouplingPropertyRecord,
      IBaseEntityDAO sourceBaseEntityDAO, ICouplingDTO couplingDTO, Long addedElement, ICouplingDAO couplingDAO,
      IBaseEntityDAO targetBaseEntityDAO, IConfigDetailsForBulkPropagationResponseModel sourceBaseEntiyConfigDetails,
      IConfigDetailsForBulkPropagationResponseModel targetBaseEntiyConfigDetails) throws RDBMSException, Exception
  {
    
    createCoupledRecords(holderSideCouplingPropertyRecord, sourceBaseEntityDAO, addedElement, 
        couplingDAO, targetBaseEntityDAO, 0l, sourceBaseEntiyConfigDetails, 
        targetBaseEntiyConfigDetails, couplingDTO);
  }

  private static void createCoupledRecords(List<IPropertyRecordDTO> holderSideCouplingPropertyRecord, IBaseEntityDAO sourceBaseEntityDAO,
      Long addedElement, ICouplingDAO couplingDAO, IBaseEntityDAO targetEntityDAO, Long languageIID, 
      IConfigDetailsForBulkPropagationResponseModel sourceBaseEntiyConfigDetails, 
      IConfigDetailsForBulkPropagationResponseModel targetBaseEntiyConfigDetails, 
      ICouplingDTO couplingDTO) throws Exception
  {
    List<IPropertyRecordDTO> applicablePropertyRecords = new ArrayList<>();
    Map<Long, RecordStatus> recordStatusInfo = new HashMap<>();
    createPropertyRecordsForCoupling(targetEntityDAO, holderSideCouplingPropertyRecord, sourceBaseEntityDAO, languageIID,
        sourceBaseEntiyConfigDetails, targetBaseEntiyConfigDetails, applicablePropertyRecords, couplingDTO, recordStatusInfo);

    couplingDTO.setLocaleIID(languageIID);

    couplingDAO.createCoupledRecord(applicablePropertyRecords, couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO(),
        targetEntityDAO.getBaseEntityDTO(), recordStatusInfo);
  }
  
  protected static void createPropertyRecordsForCoupling(IBaseEntityDAO targetBaseEntityDAO,
      List<IPropertyRecordDTO> holderSideCouplingPropertyRecord, IBaseEntityDAO sourceBaseEntityDAO, 
      Long languageIID, IConfigDetailsForBulkPropagationResponseModel sourceBaseEntiyConfigDetails, 
      IConfigDetailsForBulkPropagationResponseModel targetBaseEntiyConfigDetails, 
      List<IPropertyRecordDTO> applicablePropertyRecords, ICouplingDTO couplingDTO, Map<Long, RecordStatus> recordStatusInfo) throws Exception
  {
    
    List<IPropertyDTO> propertyDTOs = new ArrayList<>();
    
    for (IPropertyRecordDTO recordDTO : holderSideCouplingPropertyRecord) {
      propertyDTOs.add(recordDTO.getProperty());
    }
    
    Set<IPropertyRecordDTO> sourcePropertyRecordsToCreate = new HashSet<>();
    Set<IPropertyRecordDTO> targetPropertyRecordsToCreate = new HashSet<>();
    
    Map<String, IReferencedSectionElementModel> sourceReferencedElements = sourceBaseEntiyConfigDetails.getReferencedElements();
    Map<String, IReferencedSectionElementModel> targetReferencedElements = targetBaseEntiyConfigDetails.getReferencedElements();
    
        
    for (IPropertyRecordDTO record : holderSideCouplingPropertyRecord) {
      IPropertyDTO property = record.getProperty();
      IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO
          .loadPropertyRecords(property);
      
      IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO
          .loadPropertyRecords(property);
      
      String propertyCode = property.getPropertyCode();
      if(!sourceReferencedElements.containsKey(propertyCode) || 
          !targetReferencedElements.containsKey(propertyCode)) {
        continue;
      }
      
      if(sourceReferencedElements.get(propertyCode).getIsSkipped() || 
          targetReferencedElements.get(propertyCode).getIsSkipped()) {
        continue;
      }
      
      if(record instanceof IValueRecordDTO) {
        IReferencedSectionAttributeModel sourceAttributeElement = 
            (IReferencedSectionAttributeModel) sourceReferencedElements.get(propertyCode);
        
        IReferencedSectionAttributeModel targetAttributeElement = 
            (IReferencedSectionAttributeModel) targetReferencedElements.get(propertyCode);
        
        if(sourceAttributeElement.getAttributeVariantContext() != null || targetAttributeElement.getAttributeVariantContext() != null) {
          continue;
        }
        
      }
      
      couplingDTO.setLocaleIID(languageIID);
      long propertyIID = property.getPropertyIID();
      couplingDTO.setPropertyIID(propertyIID);
      
      List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO()
          .checkWheatherConflictingValuesAlreadyExist(couplingDTO);
      
      if(checkWheatherConflictingValuesAlreadyExist.size() == 0) {
        
        applicablePropertyRecords.add(record);
        IPropertyRecordDTO sourceProperty = sourceBaseEntityDTO.getPropertyRecord(propertyIID);
        IPropertyRecordDTO targetProperty = targetBaseEntityDTO.getPropertyRecord(propertyIID);
        
        if (sourceProperty == null) {
          if (record.getCouplingBehavior().equals(CouplingBehavior.TIGHTLY) && targetProperty == null) {
            couplingDTO.setRecordStatus(RecordStatus.COUPLED);
            IPropertyRecordDTO targetpropertyRecord = createPropertyRecord(targetBaseEntityDAO, record, languageIID);
            targetBaseEntityDAO.createPropertyRecords(targetpropertyRecord);
          }
          
          IPropertyRecordDTO propertyRecord = createPropertyRecord(sourceBaseEntityDAO, record, languageIID);
         sourcePropertyRecordsToCreate.add(propertyRecord);
          
        }
        else {
          if (targetProperty == null) {
            IPropertyRecordDTO propertyRecord = createPropertyRecord(targetBaseEntityDAO, record, languageIID);
            targetPropertyRecordsToCreate.add(propertyRecord);
            //targetBaseEntityDAO.createPropertyRecords(propertyRecord);
            setTightCoupledRecordStatus(couplingDTO, record, sourceProperty);
          }
        } 
        
        ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
        List<ICouplingDTO> coupledRecords = couplingDAO.getCoupledRecord(targetBaseEntityDTO.getBaseEntityIID(),
            propertyIID, languageIID);
        if (coupledRecords.size() > 0) {
          ICouplingDTO iCouplingDTO = coupledRecords.get(0);
          if (!iCouplingDTO.getCouplingSourceType().getBehavior().equals(CouplingBehavior.DYNAMIC)) {
            createPropertyRecordForTarget(targetBaseEntityDAO, record, iCouplingDTO, languageIID, targetPropertyRecordsToCreate);
          }
        }
      }
      recordStatusInfo.put(propertyIID, couplingDTO.getRecordStatus());
    }
    sourceBaseEntityDAO.createPropertyRecords(sourcePropertyRecordsToCreate.toArray(new IPropertyRecordDTO[sourcePropertyRecordsToCreate.size()]));
    targetBaseEntityDAO.createPropertyRecords(targetPropertyRecordsToCreate.toArray(new IPropertyRecordDTO[targetPropertyRecordsToCreate.size()]));
  }

  private static void setTightCoupledRecordStatus(ICouplingDTO couplingDTO, IPropertyRecordDTO record, IPropertyRecordDTO sourceProperty)
  {
    if(record.getCouplingBehavior().equals(CouplingBehavior.TIGHTLY)) {
      if (sourceProperty instanceof IValueRecordDTO) {
        IValueRecordDTO valueRecord = (IValueRecordDTO) sourceProperty;
        if(StringUtils.isEmpty(valueRecord.getValue()) ) {
          couplingDTO.setRecordStatus(RecordStatus.COUPLED);
        }else {
          couplingDTO.setRecordStatus(RecordStatus.NOTIFIED);
        }
      }
      else {
        ITagsRecordDTO tagRecord = (ITagsRecordDTO) sourceProperty;
        if(tagRecord.getTags().isEmpty()) {
          couplingDTO.setRecordStatus(RecordStatus.COUPLED);
        }else {
          couplingDTO.setRecordStatus(RecordStatus.NOTIFIED);
        }
      }
    }
  }
  
  private static void createPropertyRecordForTarget(IBaseEntityDAO targetBaseEntityDAO, IPropertyRecordDTO record,
      ICouplingDTO coupledRecord, Long languageIID, Set<IPropertyRecordDTO> toCreateTargetPropertyRecords) throws Exception, RDBMSException, CSFormatException
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(coupledRecord.getTargetEntityIID());
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(record.getProperty());
    IPropertyRecordDTO propertyRecord = baseEntityDTO.getPropertyRecord(record.getProperty().getPropertyIID());
    
    if (propertyRecord instanceof IValueRecordDTO) {
      IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
      IValueRecordDTOBuilder valueRecordBuilder = targetBaseEntityDAO.newValueRecordDTOBuilder(record.getProperty(), valueRecord.getValue());
      if(languageIID == 0l) {
        if(!StringUtils.isEmpty(valueRecord.getUnitSymbol())) {
          valueRecordBuilder.unitSymbol(valueRecord.getUnitSymbol());
        }
        IValueRecordDTO newValueRecord = valueRecordBuilder.build();
        toCreateTargetPropertyRecords.add(newValueRecord);
        //targetBaseEntityDAO.createPropertyRecords(newValueRecord);
      }else {
        String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(languageIID).getLanguageCode();
        IValueRecordDTO newValueRecord = valueRecordBuilder.localeID(languageCode).build();
        toCreateTargetPropertyRecords.add(newValueRecord);
        //targetBaseEntityDAO.createPropertyRecords(newValueRecord);
      }
    }
    else {
      ITagsRecordDTO tagrecord = (ITagsRecordDTO) propertyRecord;
      ITagsRecordDTO newTagRecord = targetBaseEntityDAO.newTagsRecordDTOBuilder(tagrecord.getProperty()).build();
      Set<ITagDTO> tags = tagrecord.getTags();
      newTagRecord.setTags(tags.toArray(new ITagDTO[tags.size()]));
      toCreateTargetPropertyRecords.add(newTagRecord);
      //targetBaseEntityDAO.createPropertyRecords(newTagRecord);
    }
  }
  
  public static IPropertyRecordDTO createPropertyRecord(IBaseEntityDAO targetBaseEntityDAO, IPropertyRecordDTO record, 
      Long languageIID) throws RDBMSException
  {
    IPropertyRecordDTO propertyToCreate;
    if (record instanceof IValueRecordDTO) {
      IValueRecordDTOBuilder valueRecordDTOBuilder = targetBaseEntityDAO.newValueRecordDTOBuilder(record.getProperty(), "");
      if(languageIID == 0l) {
        propertyToCreate = valueRecordDTOBuilder.build();
      }else {
        String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(languageIID).getLanguageCode();
        propertyToCreate = valueRecordDTOBuilder.localeID(languageCode).build();
      }
    }
    else {
      propertyToCreate = targetBaseEntityDAO.newTagsRecordDTOBuilder(record.getProperty()).build();
    }
    
    return propertyToCreate;
  }
  
  protected static void handleOppositeSideCouplingProperties(IBaseEntityDAO baseEntityDAO,
      IRelationshipSidePropertiesModel oppositeSideProperties, IPropertyDTO relationshipPropertyDTO, List<Long> deletedTargetBaseEntityIIDs,
      List<Long> sourceEntityIIDs) throws Exception
  {
    
    if (!deletedTargetBaseEntityIIDs.isEmpty() && oppositeSideProperties != null) {
      for (Long deletedElement : deletedTargetBaseEntityIIDs) {
        deleteCoupledRecord(deletedElement, baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
            rdbmsComponentUtils.getBaseEntityDAO(deletedElement), oppositeSideProperties, relationshipPropertyDTO);
      }
    }
    
    if(!sourceEntityIIDs.isEmpty()) {
      List<IPropertyRecordDTO> oppositeSideCouplingPropertyRecord = new ArrayList<>();
      List<IPropertyRecordDTO> oppositeSideCouplingDependentRecord = new ArrayList<>();
      
      int side = relationshipPropertyDTO.getRelationSide() == RelationSide.SIDE_1 ? 2 : 1;
      getCouplingAttributeProperties(baseEntityDAO, oppositeSideCouplingPropertyRecord, oppositeSideProperties, relationshipPropertyDTO,
          side, oppositeSideCouplingDependentRecord);
      getCouplingTagProperties(baseEntityDAO, oppositeSideCouplingPropertyRecord, oppositeSideProperties, relationshipPropertyDTO, side);
      
      ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
      
      IConfigDetailsForBulkPropagationResponseModel targetConfigDetailsForCustomTabModel = getConfigDetailForBaseEntity(baseEntityDAO);
      
      
      for (Long sourceEntity : sourceEntityIIDs) {
        
        IBaseEntityDAO sourceBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntity);
        long targetEntityIID = baseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
        
        ICouplingDTO couplingDTO = new CouplingDTO();
        couplingDTO.setCouplingSourceIID(relationshipPropertyDTO.getPropertyIID());
        couplingDTO.setSourceEntityIID(sourceEntity);
        couplingDTO.setTargetEntityIID(targetEntityIID);
        
        IConfigDetailsForBulkPropagationResponseModel sourceConfigDetailsForCustomTabModel = getConfigDetailForBaseEntity(sourceBaseEntityDAO);
          
        if(!oppositeSideCouplingPropertyRecord.isEmpty()) {
           createCoupledRecords(oppositeSideCouplingPropertyRecord, rdbmsComponentUtils.getBaseEntityDAO(sourceEntity), targetEntityIID, 
           couplingDAO, baseEntityDAO, 0l, sourceConfigDetailsForCustomTabModel, 
           targetConfigDetailsForCustomTabModel, couplingDTO);
        }
        
        if(!oppositeSideCouplingDependentRecord.isEmpty()) {
          
          List<String> languageCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(sourceEntity, targetEntityIID);
          
          // For Language dependent Attributes
          for(String languageCode : languageCodes) {
            
            ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfig(languageCode);
            IBaseEntityDAO targetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEntityIID, languageCode);
            IBaseEntityDAO sourceEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntity, languageCode);
            createCoupledRecords(oppositeSideCouplingDependentRecord, sourceEntityDAO, targetEntityIID, 
            couplingDAO, targetEntityDAO, languageConfigDTO.getLanguageIID(), sourceConfigDetailsForCustomTabModel,
            targetConfigDetailsForCustomTabModel, couplingDTO);
          }
        }
      }
      
      RuleHandler ruleHandler = new RuleHandler();
      IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(
          targetConfigDetailsForCustomTabModel.getReferencedElements(), targetConfigDetailsForCustomTabModel.getReferencedAttributes(),
          targetConfigDetailsForCustomTabModel.getReferencedTags());
      ruleHandler.evaluateDQMustShould(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), dataRules, baseEntityDAO,
          rdbmsComponentUtils.getLocaleCatlogDAO());
    }
  }
  
  protected static void deleteCoupledRecord(Long sourceEntityIID, Long targetEntityIID, IBaseEntityDAO baseEntityDAO,
      IRelationshipSidePropertiesModel holderSideProperties, IPropertyDTO relationshipPropertyDTO) throws RDBMSException, Exception
  {
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    attributeToCreateAtTarget(sourceEntityIID, targetEntityIID, baseEntityDAO, relationshipPropertyDTO, couplingDAO, holderSideProperties);
    
    tagsToCreateAtTarget(sourceEntityIID, targetEntityIID, baseEntityDAO, holderSideProperties, relationshipPropertyDTO, couplingDAO);
    
    couplingDAO.deleteCouplingRecord(sourceEntityIID, targetEntityIID, relationshipPropertyDTO.getPropertyIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
    
  }
  
  private static void tagsToCreateAtTarget(Long sourceEntityIID, Long targetEntityIID, IBaseEntityDAO baseEntityDAO,
      IRelationshipSidePropertiesModel holderSideProperties, IPropertyDTO relationshipPropertyDTO, ICouplingDAO couplingDAO) throws RDBMSException, Exception
  {
    List<IReferencedRelationshipProperty> tags = holderSideProperties.getTags();
    
    for (IReferencedRelationshipProperty tag : tags) {
      
      ICouplingDTO couplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
      
      couplingDTO.setSourceEntityIID(sourceEntityIID);
      couplingDTO.setTargetEntityIID(targetEntityIID);
      couplingDTO.setCouplingSourceIID(relationshipPropertyDTO.getPropertyIID());
      couplingDTO.setPropertyIID(tag.getPropertyIID());
      couplingDTO.setRecordStatus(RecordStatus.COUPLED);
      couplingDTO.setLocaleIID(0l);
      
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEntityIID);
      
      createTagRecordAtTargetEntity(targetEntityIID, baseEntityDAO, couplingDAO, couplingDTO, targetBaseEntityDAO);
    }
  }

  public static void createTagRecordAtTargetEntity(Long targetEntityIID, IBaseEntityDAO baseEntityDAO, ICouplingDAO couplingDAO,
      ICouplingDTO couplingDTO, IBaseEntityDAO targetBaseEntityDAO) throws RDBMSException, Exception
  {
    List<ICouplingDTO> coupledConflictingValue = couplingDAO.getCoupledConflictingValue(couplingDTO);
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(couplingDTO.getPropertyIID());
    if (coupledConflictingValue.size() == 1) {
      IBaseEntityDTO sourceEntity = baseEntityDAO.loadPropertyRecords(propertyDTO);
      ITagsRecordDTO tagRecord = (ITagsRecordDTO) sourceEntity.getPropertyRecord(couplingDTO.getPropertyIID());
      targetBaseEntityDAO.loadPropertyRecords(tagRecord.getProperty());
      IPropertyRecordDTO targetPropertyRecord = targetBaseEntityDAO.getBaseEntityDTO().getPropertyRecord(couplingDTO.getPropertyIID());
      if(targetPropertyRecord == null) {
        ITagsRecordDTO targetTagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(tagRecord.getProperty()).build();
        Set<ITagDTO> tagDTO = targetTagRecordDTO.getTags();
        targetTagRecordDTO.setTags(tagDTO.toArray(new ITagDTO[tagDTO.size()]));
        targetBaseEntityDAO.createPropertyRecords(targetTagRecordDTO);
      }
    }
  }
  
  protected static ICouplingDTO prepareCouplingDTO(Long sourceEntityIID, Long targetEntityIID) throws RDBMSException, Exception
  {
    
    ICouplingDTO couplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
    couplingDTO.setSourceEntityIID(sourceEntityIID);
    couplingDTO.setTargetEntityIID(targetEntityIID);
    return couplingDTO;
  }
  
  public static RelationSide getRelationshipSide(String sideId, IRelationship relationshipConfig)
  {
    IRelationshipSide side1 = relationshipConfig.getSide1();
    IRelationshipSide side2 = relationshipConfig.getSide2();
    if (side1.getElementId().equals(sideId)) {
      return RelationSide.SIDE_1;
    }
    else if (side2.getElementId().equals(sideId)) {
      return RelationSide.SIDE_2;
    }
    return RelationSide.UNDEFINED;
  }
  
  private static void attributeToCreateAtTarget(Long sourceEntityIID, Long targetEntityIID, IBaseEntityDAO baseEntityDAO,
      IPropertyDTO relationshipPropertyDTO, ICouplingDAO couplingDAO, IRelationshipSidePropertiesModel holderSideProperties)
      throws RDBMSException, Exception
  {
    List<IReferencedRelationshipProperty> attributes = holderSideProperties.getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = holderSideProperties.getDependentAttributes();
    if (!attributes.isEmpty()) {
      for (IReferencedRelationshipProperty attribute : attributes) {
        ICouplingDTO couplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
        couplingDTO.setSourceEntityIID(sourceEntityIID);
        couplingDTO.setTargetEntityIID(targetEntityIID);
        couplingDTO.setCouplingSourceIID(relationshipPropertyDTO.getPropertyIID());
        couplingDTO.setPropertyIID(attribute.getPropertyIID());
        couplingDTO.setRecordStatus(RecordStatus.COUPLED);
        couplingDTO.setLocaleIID(0l);
        
        IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEntityIID);
        createAttributeAtTarget(targetEntityIID, baseEntityDAO, couplingDAO, couplingDTO, targetBaseEntityDAO);
      }
    }
    else if (!dependentAttributes.isEmpty()) {
      List<String> languageCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(sourceEntityIID, targetEntityIID);
      for (String languageCode : languageCodes) {
        for (IReferencedRelationshipProperty dependentAttribute : dependentAttributes) {
          ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfig(languageCode);
          
          ICouplingDTO couplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
          couplingDTO.setSourceEntityIID(sourceEntityIID);
          couplingDTO.setTargetEntityIID(targetEntityIID);
          couplingDTO.setCouplingSourceIID(relationshipPropertyDTO.getPropertyIID());
          couplingDTO.setPropertyIID(dependentAttribute.getPropertyIID());
          couplingDTO.setRecordStatus(RecordStatus.COUPLED);
          couplingDTO.setLocaleIID(languageConfigDTO.getLanguageIID());
          
          IBaseEntityDAO sourceEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntityIID, languageCode);
          IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEntityIID, languageCode);
          
          createAttributeAtTarget(targetEntityIID, sourceEntityDAO, couplingDAO,  couplingDTO, targetBaseEntityDAO);
          /*List<ICouplingDTO> coupledConflictingValue = couplingDAO.getCoupledConflictingValue(couplingDTO);
          
          if (coupledConflictingValue.size() == 1) {
            IBaseEntityDTO loadPropertyRecords = sourceEntityDAO
                .loadPropertyRecords(sourceEntityDAO.newPropertyDTO(dependentAttribute.getPropertyIID(), dependentAttribute.getCode(),
                    IConfigMap.getPropertyType(dependentAttribute.getType())));
            
            IValueRecordDTO valueRecordDTO = (IValueRecordDTO) loadPropertyRecords.getPropertyRecord(dependentAttribute.getPropertyIID());
            IValueRecordDTO targetValueRecord = targetBaseEntityDAO
                .newValueRecordDTOBuilder(valueRecordDTO.getProperty(), valueRecordDTO.getValue()).localeID(valueRecordDTO.getLocaleID())
                .build();
            
            targetBaseEntityDAO.createPropertyRecords(targetValueRecord);
          }*/
        }
      }
    }
  }
  
  public static IValueRecordDTO createClassificationCoupledValueRecord(IBaseEntityDAO baseEntityDAO, IPropertyDTO propertyDTO,
     IClassifierDTO classifierDTO, String couplingType, String localeID) throws CSFormatException
  {
    IValueRecordDTO valueRecordDTO = baseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "").localeID(localeID).build();
    boolean isDynamic = false;
    if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
      isDynamic = true;
    }
    
    valueRecordDTO.addCLassificationCoupling(classifierDTO, isDynamic);
    return valueRecordDTO;
  }
  
  public static IPropertyRecordDTO createClassificationCoupledTagRecord(IBaseEntityDAO baseEntityDAO, IPropertyDTO propertyDTO,
      IClassifierDTO classifier, String couplingType) throws CSFormatException
  {
    
    ITagsRecordDTO tagsRecordDTO = baseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
    
    boolean isDynamic = false;
    if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
      isDynamic = true;
    }
    tagsRecordDTO.addCLassificationCoupling(classifier, isDynamic);
    return tagsRecordDTO;
  }
  
  public static void deleteCLassificationCoupledRecord(ILocaleCatalogDAO localeDAO, IBaseEntityDAO sourceBaseEntityDAO,
      IBaseEntityDAO targetBaseEntityDAO, ICouplingDAO couplingDAO, List<IPropertyDTO> propertyRecordToBeDeleted, Long localeIID)
      throws RDBMSException, Exception
  {
    List<IPropertyRecordDTO> propertyRecordsToCreateAtTarget = new ArrayList<>();
    
    for (IPropertyDTO propertyDTO : propertyRecordToBeDeleted) {
      handleForExistingConflcitingValue(localeDAO, sourceBaseEntityDAO, targetBaseEntityDAO, couplingDAO, propertyDTO,
          propertyRecordsToCreateAtTarget, localeIID);
      
    }
    if (propertyRecordsToCreateAtTarget.size() > 0)
      targetBaseEntityDAO
          .createPropertyRecords(propertyRecordsToCreateAtTarget.toArray(new IPropertyRecordDTO[propertyRecordsToCreateAtTarget.size()]));
    
    couplingDAO.deleteCouplingRecord(sourceBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
        targetBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
        sourceBaseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierIID(), localeDAO);
    
  }
  
  private static void handleForExistingConflcitingValue(
      ILocaleCatalogDAO localeDAO, IBaseEntityDAO sourceBaseEntityDAO, IBaseEntityDAO targetBaseEntityDAO, ICouplingDAO couplingDAO,
      IPropertyDTO propertyDTO, List<IPropertyRecordDTO> propertyRecordsToCreateAtTarget, Long localeIID) throws RDBMSException, Exception
  {
    ICouplingDTO couplingDTO = localeDAO.newCouplingDTOBuilder().build();
    couplingDTO.setSourceEntityIID(sourceBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
    couplingDTO.setTargetEntityIID(targetBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
    couplingDTO.setCouplingSourceIID(sourceBaseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierIID());
    couplingDTO.setPropertyIID(propertyDTO.getPropertyIID());
    couplingDTO.setLocaleIID(localeIID);
    
    ICouplingDTO conflictingValue = couplingDAO.getConflictingValueForCouplingSource(couplingDTO);
    
    if (conflictingValue.getRecordStatus().equals(RecordStatus.COUPLED)) {
      IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
      
      if (propertyDTO.getSuperType().equals(SuperType.ATTRIBUTE)) {
        
        IValueRecordDTO valueRecordDTO = (IValueRecordDTO) sourceBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
        IValueRecordDTO targetValueRecord = targetBaseEntityDAO
            .newValueRecordDTOBuilder(valueRecordDTO.getProperty(), valueRecordDTO.getValue()).asHTML(valueRecordDTO.getAsHTML())
            .asNumber(valueRecordDTO.getAsNumber()).unitSymbol(valueRecordDTO.getUnitSymbol()).build();
        propertyRecordsToCreateAtTarget.add(targetValueRecord);
      }
      else if (propertyDTO.getSuperType().equals(SuperType.TAGS)) {
        ITagsRecordDTO TagRecord = (ITagsRecordDTO) sourceBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
        ITagsRecordDTO targetTagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(TagRecord.getProperty()).build();
        Set<ITagDTO> tagDTO = targetTagRecordDTO.getTags();
        
        targetTagRecordDTO.setTags(tagDTO.toArray(new ITagDTO[tagDTO.size()]));
        propertyRecordsToCreateAtTarget.add(targetTagRecordDTO);
      }
    }
    else if (conflictingValue.getRecordStatus().equals(RecordStatus.NOTIFIED)) {
      couplingDTO.setRecordStatus(RecordStatus.NOTIFIED);
      List<ICouplingDTO> notifiedConflictingValues = couplingDAO.getNotifiedConflictingValues(couplingDTO);
      
      if (notifiedConflictingValues.size() == 1) {
        deletePropertyRecordFromTargetBaseEntity(notifiedConflictingValues.get(0));
        couplingDAO.updateCouplingTypeForConflictingValues(notifiedConflictingValues.get(0), RecordStatus.COUPLED);
        couplingDAO.createCoupledRecord(notifiedConflictingValues.get(0), localeDAO);
      }
      
    }
  }  
  
  protected static void createPropertyRecordsForCLassificationCoupling(IBaseEntityDAO targetBaseEntityDAO, ICouplingDAO couplingDAO,
      List<IPropertyRecordDTO> propertyRecords, Long languageIID, Map<Long, RecordStatus> recordStatus, Long sourceEntityIID) throws Exception
  {
    Long targetEntityIID = targetBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    List<IPropertyRecordDTO> propertyRecordsToCreateAtTarget = new ArrayList<>();
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    // to fetch default instance property record we need default locale catalog
    // DAO
    ILocaleCatalogDAO defalutCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    IBaseEntityDAO sourceBaseEntityDAO = defalutCatalogDAO
        .openBaseEntity(defalutCatalogDAO.getEntityByIID(sourceEntityIID));
    for (IPropertyRecordDTO record : propertyRecords) {
      
      List<ICouplingDTO> coupledRecords = couplingDAO.getCoupledRecord(targetEntityIID, record.getProperty().getPropertyIID(), languageIID);
      if (coupledRecords.size() > 0 ) {
        ICouplingDTO iCouplingDTO = coupledRecords.get(0);
        createPropertyRecordForTarget(targetBaseEntityDAO, defalutCatalogDAO, localeCatalogDAO, record, iCouplingDTO, languageIID,
            propertyRecordsToCreateAtTarget, recordStatus);
      }else if(record.getCouplingBehavior().equals(CouplingBehavior.TIGHTLY)) {
        IBaseEntityDTO tagregtEntity = targetBaseEntityDAO.loadPropertyRecords(record.getProperty());
        IPropertyRecordDTO propertyRecord = tagregtEntity.getPropertyRecord(record.getProperty().getPropertyIID());
        if(propertyRecord == null) {
          getNewPropertyRecord(targetBaseEntityDAO, record, languageIID, propertyRecordsToCreateAtTarget, record, recordStatus, sourceBaseEntityDAO);
        }
      }
    }
    
    if (propertyRecordsToCreateAtTarget.size() > 0) {
      targetBaseEntityDAO
          .createPropertyRecords(propertyRecordsToCreateAtTarget.toArray(new IPropertyRecordDTO[propertyRecordsToCreateAtTarget.size()]));
    }
    
    
  }
  
  public static void createCLassificationCoupledRecords(List<IPropertyRecordDTO> propertyRecords,
      IBaseEntityDAO targetBaseEntityDAO, Long sourceEntityIID, Long couplingSourceIID, ICouplingDAO couplingDAO, Long languageIID)
      throws Exception, RDBMSException
  {
    // IBaseEntityDAO targetEntityDAO =
    // rdbmsComponentUtils.getBaseEntityDAO(addedElement);
    long targetEntityEntityIID = targetBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    ICouplingDTO couplingDTO = new CouplingDTO();
    couplingDTO.setCouplingSourceIID(couplingSourceIID);
    couplingDTO.setSourceEntityIID(sourceEntityIID);
    couplingDTO.setTargetEntityIID(targetEntityEntityIID);
    couplingDTO.setLocaleIID(languageIID);
    
    if (!couplingDAO.checkWheatherConflictingValuesAlreadyExistForCoupingSource(couplingDTO)) {
      Map<Long, RecordStatus> recordStatus = new HashMap<>();
      createPropertyRecordsForCLassificationCoupling(targetBaseEntityDAO, couplingDAO, propertyRecords, languageIID, recordStatus, sourceEntityIID);
      couplingDTO.setLocaleIID(languageIID);
      couplingDAO.createCoupledRecordsForCLassification(propertyRecords, couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO(), recordStatus);
    }
  }
  
  private static void createPropertyRecordForTarget(IBaseEntityDAO targetBaseEntityDAO, ILocaleCatalogDAO defaultLocaleCatalogDAO,
      ILocaleCatalogDAO localeCatalogDAO, IPropertyRecordDTO record, ICouplingDTO coupledRecord, Long languageIID,
      List<IPropertyRecordDTO> propertyRecordsToCreateAtTarget, Map<Long, RecordStatus> recordStatus) throws Exception, RDBMSException, CSFormatException
  {
    IBaseEntityDAO sourceBaseEntityDAO = null;
    if (coupledRecord.getCouplingSourceType().equals(CouplingType.DYN_CLASSIFICATION)
        || coupledRecord.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION)) {
      sourceBaseEntityDAO = defaultLocaleCatalogDAO
          .openBaseEntity(defaultLocaleCatalogDAO.getEntityByIID(coupledRecord.getSourceEntityIID()));
    }
    else {
      sourceBaseEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(coupledRecord.getSourceEntityIID()));
    }
    
    IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO.loadPropertyRecords(record.getProperty());
    IPropertyRecordDTO propertyRecord = sourceBaseEntityDTO.getPropertyRecord(record.getProperty().getPropertyIID());

    if(!coupledRecord.getCouplingSourceType().getBehavior().equals(CouplingBehavior.DYNAMIC)){
      getNewPropertyRecord(targetBaseEntityDAO, record, languageIID, propertyRecordsToCreateAtTarget, propertyRecord, recordStatus, sourceBaseEntityDAO);
    }
  }

  private static void getNewPropertyRecord(IBaseEntityDAO targetBaseEntityDAO, IPropertyRecordDTO record, Long languageIID,
      List<IPropertyRecordDTO> propertyRecordsToCreateAtTarget, IPropertyRecordDTO propertyRecord, Map<Long, RecordStatus> recordStatus, IBaseEntityDAO sourceBaseEntityDAO) throws RDBMSException, CSFormatException
  {
    IBaseEntityDTO sourceBaseEntity = sourceBaseEntityDAO.loadPropertyRecords(propertyRecord.getProperty());
    long propertyIID = propertyRecord.getProperty().getPropertyIID();
    IPropertyRecordDTO sourceProperty = sourceBaseEntity.getPropertyRecord(propertyIID);
    if (propertyRecord instanceof IValueRecordDTO) {
      IValueRecordDTO valueRecord = prepareRecordStatusForValueRecord(propertyRecord, recordStatus, propertyIID, sourceProperty);
      IValueRecordDTOBuilder valueRecordBuilder = targetBaseEntityDAO.newValueRecordDTOBuilder(record.getProperty(),
          "").asNumber(valueRecord.getAsNumber()).unitSymbol(valueRecord.getUnitSymbol());
      if (languageIID == 0l) {
        IValueRecordDTO newValueRecord = valueRecordBuilder.build();
        propertyRecordsToCreateAtTarget.add(newValueRecord);
      }
      else {
        String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(languageIID).getLanguageCode();
        IValueRecordDTO newValueRecord = valueRecordBuilder.localeID(languageCode).build();
        propertyRecordsToCreateAtTarget.add(newValueRecord);
      }
    }
    else {
      ITagsRecordDTO tagrecord = (ITagsRecordDTO) propertyRecord;
      ITagsRecordDTO newTagRecord = targetBaseEntityDAO.newTagsRecordDTOBuilder(tagrecord.getProperty()).build();
      Set<ITagDTO> tags = tagrecord.getTags();
      newTagRecord.setTags(tags.toArray(new ITagDTO[tags.size()]));
      propertyRecordsToCreateAtTarget.add(newTagRecord);
      prepareRecordStatusForTag(recordStatus, propertyIID, sourceProperty);
    }
  }

  private static void prepareRecordStatusForTag(Map<Long, RecordStatus> recordStatus, long propertyIID, IPropertyRecordDTO sourceProperty)
  {
    if(sourceProperty != null) {
      ITagsRecordDTO sourceTagRecord = (ITagsRecordDTO) sourceProperty;
      if(sourceTagRecord.isEmpty()) {
        recordStatus.put(propertyIID, RecordStatus.COUPLED);
      }else {
        recordStatus.put(propertyIID, RecordStatus.NOTIFIED);
      }
    }
  }

  private static IValueRecordDTO prepareRecordStatusForValueRecord(IPropertyRecordDTO propertyRecord, Map<Long, RecordStatus> recordStatus,
      long propertyIID, IPropertyRecordDTO sourceProperty)
  {
    IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
    if(sourceProperty != null) {
      IValueRecordDTO sourceValueRecord = (IValueRecordDTO) sourceProperty ;
      if(StringUtils.isEmpty(sourceValueRecord.getValue())) {
        recordStatus.put(propertyIID, RecordStatus.COUPLED);
      }else {
        recordStatus.put(propertyIID, RecordStatus.NOTIFIED);
      }
    }
    return valueRecord;
  }

  public static void createAttributeAtTarget(Long targetEntityIID, IBaseEntityDAO sourceBaseEntityDAO, ICouplingDAO couplingDAO,
       ICouplingDTO couplingDTO, IBaseEntityDAO targetBaseEntityDAO) throws RDBMSException, CSFormatException, Exception
  {
    List<ICouplingDTO> coupledConflictingValue = couplingDAO.getCoupledConflictingValue(couplingDTO);
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(couplingDTO.getPropertyIID());
    if (coupledConflictingValue.size() == 1) {
      IBaseEntityDTO loadPropertyRecords = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
      IValueRecordDTO valueRecordDTO = (IValueRecordDTO) loadPropertyRecords.getPropertyRecord(propertyDTO.getPropertyIID());
      targetBaseEntityDAO.loadPropertyRecords(valueRecordDTO.getProperty());
      IPropertyRecordDTO targetPropertyRecord = targetBaseEntityDAO.getBaseEntityDTO().getPropertyRecord(valueRecordDTO.getProperty().getPropertyIID());
      if(targetPropertyRecord == null) {
        IValueRecordDTO targetValueRecord = targetBaseEntityDAO
            .newValueRecordDTOBuilder(valueRecordDTO.getProperty(), valueRecordDTO.getValue()).localeID(valueRecordDTO.getLocaleID())
            .build();
        targetBaseEntityDAO.createPropertyRecords(targetValueRecord);
      }
    }
  }
  
  public static void deletePropertyRecordFromTargetBaseEntity(ICouplingDTO targetCouplingDTO)
      throws Exception, RDBMSException, CSFormatException
  {
    IBaseEntityDAO targetBaseEntityDAO;
    if (targetCouplingDTO.getLocaleIID() == 0l) {
      targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetCouplingDTO.getTargetEntityIID());
    }
    else {
      String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(targetCouplingDTO.getLocaleIID()).getLanguageCode();
      targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetCouplingDTO.getTargetEntityIID(), languageCode);
    }
    IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO
        .loadPropertyRecords(ConfigurationDAO.instance().getPropertyByIID(targetCouplingDTO.getPropertyIID()));
    IPropertyRecordDTO propertyRecord = targetBaseEntityDTO.getPropertyRecord(targetCouplingDTO.getPropertyIID());
    if (propertyRecord != null) {
      rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO().deletePropertyRecord(propertyRecord);
    }
  }
}
