package com.cs.core.bgprocess.services.attributes;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;

import com.cs.constants.Constants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BGPAttributeUpdateListDTO;
import com.cs.core.bgprocess.dto.IBGPAttributeUpdateListDTO;
import com.cs.core.bgprocess.idto.IAttributeCalculationUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.utils.BgprocessUtils;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedHtmlOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.config.strategy.usecase.concatenatedAttribute.IGetConfigDetailsForSaveConcatenatedAttributeStrategy;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;

public class SaveAttributesHandler extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String    PROCESSED_ENTITY_IIDS                  = "processedEntityIIDs";
  Set<Long>                      processedEntityIIDs                    = new HashSet<>();
  
  protected int                  nbBatches                              = 0;
  protected int                  batchSize;
  protected int                  totalContents;
  
  IBGPAttributeUpdateListDTO     bGPAttributeUpdateListDTO              = new BGPAttributeUpdateListDTO();
  
  // {entityId: {localeId: [recordMap]}},
  Map<Long, Object>              entityRecordMapForConcatenated         = new HashMap<Long, Object>();
  
  // {entityId: [propertyIIDs]},
  Map<Long, Object>              entityRecordMapForcalculated           = new HashMap<Long, Object>();
  
  Boolean                        isConcatenatedAttribute                = false;
  Map<Long, Object>              propertyIIDsWithExistingCalculationMap = new HashMap<Long, Object>();
  Map<String, ILocaleCatalogDAO> catalogDAOMap                          = new HashMap<String, ILocaleCatalogDAO>();
  Map<Long, IPropertyDTO>        propertyDTOMap                         = new HashMap<Long, IPropertyDTO>();
  Map<Long, String>              propertyIIDwithAttributeIdMap          = new HashMap<Long, String>();
  WorkflowUtils                  workflowUtils;  
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    bGPAttributeUpdateListDTO.fromJSON(jobData.getEntryData().toString());
    
    fillEnityIIdsWithRecordMap();
    
    workflowUtils =  BGProcessApplication.getApplicationContext().getBean(WorkflowUtils.class);
    
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    
    totalContents = isConcatenatedAttribute ? entityRecordMapForConcatenated.size() : entityRecordMapForcalculated.size();
    nbBatches = totalContents / batchSize;
    if (nbBatches == 0 || totalContents % batchSize > 0)
      nbBatches++;
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
  }
  
  private void fillEnityIIdsWithRecordMap() throws RDBMSException
  {
    if (bGPAttributeUpdateListDTO.getType().equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
      isConcatenatedAttribute = true;
    }
    
    List<IAttributeCalculationUpdateDTO> updatedAttributesList = bGPAttributeUpdateListDTO.getCalculationUpdatedAttributes();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      // for each attribute
      for (IAttributeCalculationUpdateDTO updatedAttribute : updatedAttributesList) {
        Long propertyIID = updatedAttribute.getPropertyIID();
        String existingCalculation = updatedAttribute.getExistingCalculation();
        String attributeId = updatedAttribute.getAttributeId();
        
        String query = "select vr.propertyIID, vr.entityIID, vr.localeID, vr.calculation from pxp.valuerecord vr "
            + "join pxp.baseentity be on be.baseentityIID = vr.entityIID and be.ismerged != true where vr.propertyIID = ?";
        
        PreparedStatement stmt = currentConn.prepareStatement(query);
        stmt.setLong(1, propertyIID);
        stmt.execute();
        IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
        
        while (result.next()) {
          Long entityIID = result.getLong("entityIID");
          String localeID = result.getString("localeID");
          String calculation = result.getString("calculation");
          
          if (isConcatenatedAttribute) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            
            recordMap.put("calculation", calculation);
            recordMap.put("propertyIID", propertyIID);
            
            Map<String, Object> localeIDRecordMap = (Map<String, Object>) entityRecordMapForConcatenated.get(entityIID);
            
            if (localeIDRecordMap == null) {
              localeIDRecordMap = new HashMap<String, Object>();
              entityRecordMapForConcatenated.put(entityIID, localeIDRecordMap);
            }
            
            List<Map<String, Object>> listOfValueRecordMap = (List<Map<String, Object>>) localeIDRecordMap.get(localeID);
            
            if (listOfValueRecordMap == null) {
              listOfValueRecordMap = new ArrayList<Map<String, Object>>();
              localeIDRecordMap.put(localeID, listOfValueRecordMap);
            }
            listOfValueRecordMap.add(recordMap);
            
          }
          
          else {
            // {entityId: [propertyIIDs]},
            List<Long> propertyIIDList = (List<Long>) entityRecordMapForcalculated.get(entityIID);
            if (propertyIIDList == null) {
              propertyIIDList = new ArrayList<Long>();
              entityRecordMapForcalculated.put(entityIID, propertyIIDList);
            }
            propertyIIDList.add(propertyIID);
          }
        }
        
        if (isConcatenatedAttribute) {
          String[] existingCalculationSubStrings = getExpressionString(existingCalculation);
          propertyIIDsWithExistingCalculationMap.put(propertyIID, existingCalculationSubStrings);
        }
        propertyIIDwithAttributeIdMap.put(propertyIID, attributeId);
        
      }
    });
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    int currentBatchNo = getCurrentBatchNo() + 1;
    
    Iterator<Long> remEntityIID = null;
    if (isConcatenatedAttribute) {
      entityRecordMapForConcatenated.keySet().removeAll(processedEntityIIDs);
      remEntityIID = entityRecordMapForConcatenated.keySet().iterator();
    }
    else {
      entityRecordMapForcalculated.keySet().removeAll(processedEntityIIDs);
      remEntityIID = entityRecordMapForcalculated.keySet().iterator();
    }
    
    Set<Long> batchEntityIIDs = new HashSet<>();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchEntityIIDs.add(remEntityIID.next());
    }
    
    try {
      updateAttributeCalculation(batchEntityIIDs);
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
    processedEntityIIDs.addAll(batchEntityIIDs);
    
    jobData.getRuntimeData().setLongArrayField(PROCESSED_ENTITY_IIDS, processedEntityIIDs);
    
    RDBMSLogger.instance().info("Batch %d: processed base entity IIDs %s", getCurrentBatchNo(), batchEntityIIDs.toString());
    
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    
    if (currentBatchNo < nbBatches && !batchEntityIIDs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
  
  private void updateAttributeCalculation(Set<Long> batchEntityIIDs) throws Exception
  {
    
    String dbLocaleID = bGPAttributeUpdateListDTO.getLocaleID();
    
    ILocaleCatalogDAO catalogDAO = openUserSession().openLocaleCatalog(userSession,
        new LocaleCatalogDTO(dbLocaleID, bGPAttributeUpdateListDTO.getCatalogCode(), bGPAttributeUpdateListDTO.getOrganizationCode()));
    
    catalogDAOMap.put(dbLocaleID, catalogDAO);
    
    for (long baseEntityIID : batchEntityIIDs) {
      
      IBaseEntityDTO baseEntityDTO = catalogDAO.getEntityByIID(baseEntityIID);
      
      IConfigDetailsForSaveConcatenatedAttributeModel configDetails = getConfigDetails(baseEntityDTO);
      List<String> changedAttributeIds = new ArrayList<>();
      
      if (isConcatenatedAttribute) {
        Map<String, Object> localeIDRecordMap = (Map<String, Object>) entityRecordMapForConcatenated.get(baseEntityIID);
        
        for (String localeID : localeIDRecordMap.keySet()) {
          if (!localeID.equals(dbLocaleID)) {
            baseEntityDTO = getBaseEntity(localeID, baseEntityIID);
          }
          
          List<Map<String, Object>> listOfValueRecords = (List<Map<String, Object>>) localeIDRecordMap.get(localeID);
          
          for (Map<String, Object> valueRecord : listOfValueRecords) {
            Long propertyIID = (Long) valueRecord.get("propertyIID");
            String calculation = (String) valueRecord.get("calculation");
            
            Boolean isCalculationStringOverrided = checkIfCalculationStringOverrided(propertyIID, calculation, configDetails);
            
            if (!isCalculationStringOverrided) {
              updateCalculation(configDetails, propertyIID, baseEntityDTO, catalogDAOMap.get(localeID),changedAttributeIds);
            }
          }
        }
      }
      else {
        List<Long> propertyIIDList = (List<Long>) entityRecordMapForcalculated.get(baseEntityIID);
        
        for (Long propertyIID : propertyIIDList) {
          updateCalculation(configDetails, propertyIID, baseEntityDTO, catalogDAO, changedAttributeIds);
        }
      }
      if(!changedAttributeIds.isEmpty()) {
        catalogDAO.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
      }
      IBusinessProcessTriggerModel businessProcessModel = BgprocessUtils.getBusinessProcessModelForPropetiesSave(catalogDAO.openBaseEntity(baseEntityDTO),
              changedAttributeIds, new ArrayList<>()); 
      if(businessProcessModel != null) {
        workflowUtils.executeBusinessProcessEvent(businessProcessModel);
      }
    }
    
  }
  
  private IBaseEntityDTO getBaseEntity(String localeID, long baseEntityIID) throws RDBMSException
  {
    ILocaleCatalogDAO catalogDAO = catalogDAOMap.get(localeID);
    
    if (catalogDAO == null) {
      catalogDAO = openUserSession().openLocaleCatalog(userSession,
          new LocaleCatalogDTO(localeID, bGPAttributeUpdateListDTO.getCatalogCode(), bGPAttributeUpdateListDTO.getOrganizationCode()));
      
      catalogDAOMap.put(localeID, catalogDAO);
    }
    
    return catalogDAO.getEntityByIID(baseEntityIID);
  }
  
  private void updateCalculation(IConfigDetailsForSaveConcatenatedAttributeModel configDetails, Long propertyIID,
      IBaseEntityDTO baseEntityDTO, ILocaleCatalogDAO catalogDAO, List<String> changedAttributeIds) throws RDBMSException, CSFormatException
  {
    String attributeId = (String) propertyIIDwithAttributeIdMap.get(propertyIID);
    
    StringBuilder updatedExpression = null;
    IPropertyDTO.PropertyType propertyType = PropertyType.UNDEFINED;
    
    if (isConcatenatedAttribute) {
      updatedExpression = getConcatenatedCalculation(configDetails, attributeId);
      propertyType = PropertyType.CONCATENATED;
    }
    else {
      updatedExpression = getCalculatedCalculation(configDetails, attributeId);
      propertyType = PropertyType.CALCULATED;
    }
    
    IBaseEntityDAO baseEntityDAO = catalogDAO.openBaseEntity(baseEntityDTO);
    
    IPropertyDTO propertyDTO = propertyDTOMap.get(propertyIID);
    if (propertyDTO == null) {
      propertyDTO = baseEntityDAO.newPropertyDTO(propertyIID, attributeId, propertyType);
      propertyDTOMap.put(propertyIID, propertyDTO);
    }
    
    baseEntityDTO = baseEntityDAO.loadPropertyRecords(propertyDTO);
    
    IPropertyRecordDTO updatedPropertyRecord = getUpdatedRecord(updatedExpression.toString(), baseEntityDTO);
    
    if (updatedPropertyRecord != null) {
      changedAttributeIds.add(propertyDTO.getCode());
      baseEntityDAO.updatePropertyRecords(updatedPropertyRecord);
    }
  }
  
  private StringBuilder getConcatenatedCalculation(IConfigDetailsForSaveConcatenatedAttributeModel configDetails, String attributeId)
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    IAttribute referencedAttribute = referencedAttributes.get(attributeId);
    
    IConcatenatedAttribute concatenatedAttribute = (IConcatenatedAttribute) referencedAttribute;
    
    List<IConcatenatedOperator> attributeConcatenatedList = concatenatedAttribute.getAttributeConcatenatedList();
    
    StringBuilder updatedExpression = createExpression(attributeConcatenatedList, referencedAttributes,
        configDetails.getReferencedElements(), configDetails.getReferencedTags());
    
    return updatedExpression;
  }
  
  private StringBuilder getCalculatedCalculation(IConfigDetailsForSaveConcatenatedAttributeModel configDetails, String attributeId)
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, IReferencedSectionElementModel> referencedSectionElements = configDetails.getReferencedElements();
    IAttribute referencedAttribute = referencedAttributes.get(attributeId);
    
    ICalculatedAttribute calculatedAttribute = ((ICalculatedAttribute) referencedAttribute);
    List<IAttributeOperator> attributeOperatorList = calculatedAttribute.getAttributeOperatorList();
    
    StringBuilder mathExpression = new StringBuilder();
    if (!attributeOperatorList.isEmpty()) {
      mathExpression.append("= ");
    }
    
    for (IAttributeOperator attributeOperator : attributeOperatorList) {
      
      String attributeOperatorType = attributeOperator.getType();
      
      switch (attributeOperatorType) {
        
        case "ATTRIBUTE":
          if (referencedSectionElements.containsKey(attributeOperator.getAttributeId())) {
            IAttribute iAttribute = referencedAttributes.get(attributeOperator.getAttributeId());
            mathExpression.append("[" + iAttribute.getCode() + "]");
          }
          else {
            mathExpression.append("0");
          }
          break;
        
        case "ADD":
          mathExpression.append(" +");
          break;
        
        case "SUBTRACT":
          mathExpression.append(" -");
          break;
        
        case "MULTIPLY":
          mathExpression.append(" *");
          break;
        
        case "DIVIDE":
          mathExpression.append(" /");
          break;
        
        case "VALUE":
          mathExpression.append(" " + attributeOperator.getValue());
          break;
        
        case "OPENING_BRACKET":
          mathExpression.append(" (");
          break;
        
        case "CLOSING_BRACKET":
          mathExpression.append(" )");
          break;
      }
    }
    return mathExpression;
  }
  
  private IPropertyRecordDTO getUpdatedRecord(String updatedCalculation, IBaseEntityDTO baseEntity) throws CSFormatException
  {
    IValueRecordDTO currentValueRecord = null;
    Iterator<IPropertyRecordDTO> propertyRecords = baseEntity.getPropertyRecords().iterator();
    
    if (propertyRecords.hasNext()) {
      currentValueRecord = (IValueRecordDTO) propertyRecords.next();
      currentValueRecord.addCalculation(updatedCalculation);
      currentValueRecord.setChanged(true);
    }
    return currentValueRecord;
  }
  
  private IConfigDetailsForSaveConcatenatedAttributeModel getConfigDetails(IBaseEntityDTO baseEntity) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    
    List<String> nonNatureClassifierCodes = new ArrayList<>();
    nonNatureClassifierCodes.add(baseEntity.getNatureClassifier().getCode());
    List<String> selectedTaxonomyCodes = new ArrayList<>();
    baseEntity.getOtherClassifiers().forEach(classifier -> {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        nonNatureClassifierCodes.add(classifier.getCode());
      }
      else {
        selectedTaxonomyCodes.add(classifier.getCode());
      }
    });
    
    multiclassificationRequestModel.setKlassIds(nonNatureClassifierCodes);
    multiclassificationRequestModel.setSelectedTaxonomyIds(selectedTaxonomyCodes);
    
    IGetConfigDetailsForSaveConcatenatedAttributeStrategy getConfigDetailsForSaveConcatenatedAttributeStrategy = BGProcessApplication
        .getApplicationContext().getBean(IGetConfigDetailsForSaveConcatenatedAttributeStrategy.class);
    
    IConfigDetailsForSaveConcatenatedAttributeModel configDetails = getConfigDetailsForSaveConcatenatedAttributeStrategy
        .execute(multiclassificationRequestModel);
    
    return configDetails;
  }
  
  private Boolean checkIfCalculationStringOverrided(Long propertyIID, String calculation,
      IConfigDetailsForSaveConcatenatedAttributeModel configDetails)
  {
    Boolean isCalculationOverrided = false;
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    
    String[] existingCalculationSubStrings = (String[]) propertyIIDsWithExistingCalculationMap.get(propertyIID);
    String[] calculationSubStrings = getExpressionString(calculation);
    int order = 0;
    
    List<String> calculationSubstringWithoutProperties = new ArrayList<String>();
    
    for (String calculationSubString : calculationSubStrings) {
      if (referencedAttributes.containsKey(calculationSubString) || referencedTags.containsKey(calculationSubString)) {
        continue;
      }
      calculationSubstringWithoutProperties.add(calculationSubString);
    }
    
    if (calculationSubstringWithoutProperties.size() != existingCalculationSubStrings.length) {
      isCalculationOverrided = true;
    }
    else {
      for (String calculationSubString : calculationSubstringWithoutProperties) {
        if (!existingCalculationSubStrings[order].equals(calculationSubString)) {
          isCalculationOverrided = true;
          break;
        }
        order++;
      }
    }
    return isCalculationOverrided;
  }
  
  public StringBuilder createExpression(List<IConcatenatedOperator> attributeConcatenatedList, Map<String, IAttribute> attributes,
      Map<String, IReferencedSectionElementModel> referencedSectionElements, Map<String, ITag> tags)
  {
    StringBuilder mathExpression = new StringBuilder();
    for (IConcatenatedOperator concatenatedAttributeOperator : attributeConcatenatedList) {
      
      if (mathExpression.length() == 0) {
        mathExpression.append("=");
      }
      
      String type = concatenatedAttributeOperator.getType();
      switch (type) {
        
        case "attribute":
          IConcatenatedAttributeOperator attributeOperator = (IConcatenatedAttributeOperator) concatenatedAttributeOperator;
          if (referencedSectionElements.containsKey(attributeOperator.getAttributeId())) {
            IAttribute configSourceAttribute = attributes.get(attributeOperator.getAttributeId());
            mathExpression.append("[" + configSourceAttribute.getCode() + "]||");
          }
          break;
        
        case "html":
          IConcatenatedHtmlOperator attributeOperatorForHtml = (IConcatenatedHtmlOperator) concatenatedAttributeOperator;
          String valueAsHtml = attributeOperatorForHtml.getValueAsHtml() == null ? "" : attributeOperatorForHtml.getValueAsHtml();
          mathExpression.append("'" + StringEscapeUtils.escapeHtml4(valueAsHtml) + "'||");
          break;
        
        case "tag":
          IConcatenatedTagOperator tagOperator = (IConcatenatedTagOperator) concatenatedAttributeOperator;
          if (referencedSectionElements.containsKey(tagOperator.getTagId())) {
            ITag tag = tags.get(tagOperator.getTagId());
            mathExpression.append("[" + tag.getCode() + "]||");
          }
          break;
      }
    }
    mathExpression.setLength(mathExpression.length() > 1 ? mathExpression.length() - 2 : 0);
    return mathExpression;
  }
  
  private String[] getExpressionString(String calculation)
  {
    String expressionIfExist = calculation.replace("{", "").replace("}", "");
    String[] expressionSubStrings = new String[0];
    
    if (!expressionIfExist.isEmpty()) {
      String[] expressionSplit = expressionIfExist.split("=", 2);
      String expressionAfterEqual = expressionSplit[1];
      String expressionRemovalOfSpecialChar = null;
      
      if (expressionAfterEqual.contains("[") || expressionAfterEqual.contains("'") || expressionAfterEqual.contains("}")) {
        expressionRemovalOfSpecialChar = expressionAfterEqual.replace("[", "").replace("]", "").replace("\"", "").replace("}", "");
      }
      expressionRemovalOfSpecialChar = StringEscapeUtils.unescapeHtml4(expressionRemovalOfSpecialChar);
      
      expressionSubStrings = expressionRemovalOfSpecialChar.split("\\|\\|");
    }
    return expressionSubStrings;
  }
  
}
