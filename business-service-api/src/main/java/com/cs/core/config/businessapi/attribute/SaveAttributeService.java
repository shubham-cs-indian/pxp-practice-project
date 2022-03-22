package com.cs.core.config.businessapi.attribute;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.Constants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.AttributeCalculationUpdateDTO;
import com.cs.core.bgprocess.dto.BGPAttributeUpdateListDTO;
import com.cs.core.bgprocess.dto.IBGPAttributeUpdateListDTO;
import com.cs.core.bgprocess.idto.IAttributeCalculationUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedHtmlOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeSuccessModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.attribute.IUpdatedAttributeListModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.attribute.ISaveAttributeStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class SaveAttributeService extends AbstractSaveConfigService<IListModel<ISaveAttributeModel>, IBulkSaveAttributeResponseModel>
    implements ISaveAttributeService {
  
  @Autowired
  protected ISaveAttributeStrategy neo4jSaveAttributeStrategy;
  
  @Autowired
  TransactionThreadData            transactionThread;
  
  @Autowired
  protected AttributeValidations attributeValidations;
  
  @Override
  public IBulkSaveAttributeResponseModel executeInternal(IListModel<ISaveAttributeModel> attributeModel) throws Exception
  {
    attributeValidations.validateAttributeUpdation(attributeModel);
    IBulkSaveAttributeResponseModel response = neo4jSaveAttributeStrategy.execute(attributeModel);
    
    IBulkSaveAttributeSuccessModel success = (IBulkSaveAttributeSuccessModel) response.getSuccess();
    List<IUpdatedAttributeListModel> updatedAttributeList = success.getUpdatedAttributeList();
    
    if (!updatedAttributeList.isEmpty() && !success.getAttributesList().isEmpty()) {
      String type = success.getAttributesList().get(0).getType();
      updateAttributeCalculation(updatedAttributeList, type);
    }
    
    return response;
  }
  
  private void updateAttributeCalculation(List<IUpdatedAttributeListModel> updatedAttributeList, String type)
      throws RDBMSException, CSFormatException
  {
    IBGPAttributeUpdateListDTO bGPAttributeUpdateListDTO = new BGPAttributeUpdateListDTO();
    List<IAttributeCalculationUpdateDTO> calculationUpdatedAttributes = new ArrayList<>();
    Boolean isConcatenatedAttribute = type.equals(Constants.CONCATENATED_ATTRIBUTE_TYPE);
    
    for (IUpdatedAttributeListModel updatedAttribute : updatedAttributeList) {
      IAttributeCalculationUpdateDTO attributeCalculationUpdateDTO = new AttributeCalculationUpdateDTO();
      
      String propertyIID = updatedAttribute.getPropertyIID();
      
      if (isConcatenatedAttribute) {
        List<IConcatenatedOperator> attributeConcatenatedList = updatedAttribute.getAttributeConcatenatedList();
        StringBuilder mathExpression = createExpression(attributeConcatenatedList);
        attributeCalculationUpdateDTO.setExistingCalculation(mathExpression.toString());
      }
      
      attributeCalculationUpdateDTO.setPropertyIID(Long.parseLong(propertyIID));
      attributeCalculationUpdateDTO.setAttributeId(updatedAttribute.getAttributeId());
      
      calculationUpdatedAttributes.add(attributeCalculationUpdateDTO);
    }
    bGPAttributeUpdateListDTO.setCalculationUpdatedAttributes(calculationUpdatedAttributes);
    bGPAttributeUpdateListDTO.setType(type);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    String service = "SAVE_ATTRIBUTES";
    
    TransactionData transactionData = transactionThread.getTransactionData();
    bGPAttributeUpdateListDTO.setLocaleID(transactionData.getDataLanguage());
    bGPAttributeUpdateListDTO.setCatalogCode(transactionData.getPhysicalCatalogId());
    bGPAttributeUpdateListDTO.setOrganizationCode(transactionData.getOrganizationId());
    bGPAttributeUpdateListDTO.setUserId(transactionData.getUserId());
    
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), service, "", userPriority,
        new JSONContent(bGPAttributeUpdateListDTO.toJSON()));
  }
  
  public StringBuilder createExpression(List<IConcatenatedOperator> attributeConcatenatedList)
  {
    StringBuilder mathExpression = new StringBuilder();
    for (IConcatenatedOperator concatenatedAttributeOperator : attributeConcatenatedList) {
      
      if (mathExpression.length() == 0) {
        mathExpression.append("=");
      }
      
      String type = concatenatedAttributeOperator.getType();
      if (type.equals("html")) {
        IConcatenatedHtmlOperator attributeOperatorForHtml = (IConcatenatedHtmlOperator) concatenatedAttributeOperator;
        String valueAsHtml = attributeOperatorForHtml.getValueAsHtml() == null ? "" : attributeOperatorForHtml.getValueAsHtml();
        mathExpression.append("'" + StringEscapeUtils.escapeHtml4(valueAsHtml) + "'||");
      }
    }
    mathExpression.setLength(mathExpression.length() > 1 ? mathExpression.length() - 2 : 0);
    return mathExpression;
  }
}
