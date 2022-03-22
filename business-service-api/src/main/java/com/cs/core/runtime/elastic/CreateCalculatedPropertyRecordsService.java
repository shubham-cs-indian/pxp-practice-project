package com.cs.core.runtime.elastic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.config.strategy.model.attribute.ConfigDetailsForCalculatedAttributeRequestModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributeRequestModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributesResponseModel;
import com.cs.core.config.strategy.usecase.concatenatedAttribute.IGetConfigDetailsForCalculatedAttributeMigrationStrategy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.elastic.IDocumentRegenerationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.google.common.collect.Lists;

@Service
public class CreateCalculatedPropertyRecordsService extends AbstractRuntimeService<IDocumentRegenerationModel, IModel>
implements ICreateCalculatedPropertyRecordsService {

  @Autowired
  RDBMSComponentUtils          rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsForCalculatedAttributeMigrationStrategy getConfigDetailsForCalculatedAttributeMigrationStrategy;
  
  @Autowired
  protected ApplicationContext applicationContext;
  
  @Autowired
  ThreadPoolTaskExecutor       migrationTaskExecutor;

  IBaseEntityDAO baseEntityDAO;
  Map<String, IAttribute> referencedAttributes;
  
  protected Integer batchSize = 500;
  
  @Override
  protected IModel executeInternal(IDocumentRegenerationModel model) throws Exception
  {
    IConfigDetailsForCalculatedAttributesResponseModel configDetails = getConfigDetails(model.getPropertyCodes());
    referencedAttributes = configDetails.getReferencedAttributes();
    Map<Long, Set<String>> classifierIIDsVSPropertyCodes = configDetails.getClassifierIIDsVSPropertyCodes();
    Set<Long> classifierIIDs = classifierIIDsVSPropertyCodes.keySet();
    List<Long> baseEntityIIDs = ConfigurationDAO.instance().getBaseentityIIDsForTypes(new ArrayList<>(classifierIIDs));
    
    if(baseEntityIIDs.isEmpty()) {
      return null;
    }
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    Collection<IAttribute> calculatedAttributes = configDetails.getReferencedAttributesForCalculated().values();
    if(calculatedAttributes.isEmpty()) {
      return null;
    }
    
    BaseEntityDTO dto = new BaseEntityDTO();
    dto.setIID(baseEntityIIDs.iterator().next());
    dto.setLocaleCatalog((LocaleCatalogDTO)localeCatalogDAO.getLocaleCatalogDTO());
    
    baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(dto);
    List<IPropertyRecordDTO> records = createAttributePropertyRecordInstance(calculatedAttributes);
    if(baseEntityIIDs.size() > batchSize) {
      List<List<Long>> partitions = Lists.partition(baseEntityIIDs, batchSize);
      for (List<Long> iids:partitions) {
        createCalculatedPropertyRecordForBaseEntities(classifierIIDsVSPropertyCodes, iids, localeCatalogDAO, records);
      }
    }
    else {
      createCalculatedPropertyRecordForBaseEntities(classifierIIDsVSPropertyCodes, baseEntityIIDs, localeCatalogDAO, records);
    }

    return null;
  }

  private void createCalculatedPropertyRecordForBaseEntities(Map<Long, Set<String>> classifierIIDsVSPropertyCodes,
      List<Long> baseEntityIIDs, ILocaleCatalogDAO localeCatalogDAO, List<IPropertyRecordDTO> records)
  {
    CreateCalculatedPropertyRecordsTask createCalculatedPropertyRecordsTask = applicationContext
        .getBean(CreateCalculatedPropertyRecordsTask.class);
    
    createCalculatedPropertyRecordsTask.setData(baseEntityIIDs, localeCatalogDAO, records, classifierIIDsVSPropertyCodes);
    migrationTaskExecutor.submit(createCalculatedPropertyRecordsTask);
  }
  
  private IConfigDetailsForCalculatedAttributesResponseModel getConfigDetails(Set<String> propertyCodes) throws Exception
  {
    IConfigDetailsForCalculatedAttributeRequestModel requestModel = new ConfigDetailsForCalculatedAttributeRequestModel();
    requestModel.setPropertyCodesOfCalculatedAttributes(new ArrayList<>(propertyCodes));
    IConfigDetailsForCalculatedAttributesResponseModel configDetails = getConfigDetailsForCalculatedAttributeMigrationStrategy.execute(requestModel);
    
    return configDetails;
  }
  
  protected List<IPropertyRecordDTO> createAttributePropertyRecordInstance(Collection<IAttribute> calculatedAttributes) throws Exception
  {
    List<IPropertyRecordDTO> calculatedRecordDTOs = new ArrayList<>();
    for(IAttribute calculatedAttributeConfig : calculatedAttributes) {
      calculatedRecordDTOs.add(createCalculatedAttribute(calculatedAttributeConfig));
    }
    return calculatedRecordDTOs;
  }
  public IValueRecordDTO createCalculatedAttribute(IAttribute calculatedAttributeConfig) throws Exception
  {
    ICalculatedAttribute calculatedAttribute = ((ICalculatedAttribute) calculatedAttributeConfig);
    List<IAttributeOperator> attributeOperatorList = calculatedAttribute.getAttributeOperatorList();
    
    StringBuilder mathExpression = new StringBuilder();
    if (!attributeOperatorList.isEmpty()) {
      mathExpression.append("= ");
    }
    
    for (IAttributeOperator attributeOperator : attributeOperatorList) {
      
      String attributeOperatorType = attributeOperator.getType();
      
      switch (attributeOperatorType) {
        case "ATTRIBUTE":
          IAttribute iAttribute = referencedAttributes.get(attributeOperator.getAttributeId());
          mathExpression.append("[" + iAttribute.getCode() + "]");
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
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(calculatedAttribute.getPropertyIID());
    IValueRecordDTOBuilder valueRecordDTOBuilder = baseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "0").valueIID(0l);
    
    IValueRecordDTO valueRecordDTO = valueRecordDTOBuilder.build();
    
    if (valueRecordDTO != null && mathExpression.length() != 0) {
      try {
        valueRecordDTO.addCalculation(mathExpression.toString());
      }
      catch (CSFormatException e) {
        RDBMSLogger.instance().exception(e);
      }
    }
    return valueRecordDTO;
  }
  
}
