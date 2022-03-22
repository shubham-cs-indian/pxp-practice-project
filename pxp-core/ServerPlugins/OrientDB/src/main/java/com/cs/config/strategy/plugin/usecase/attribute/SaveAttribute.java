package com.cs.config.strategy.plugin.usecase.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.GetGridAttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.attribute.BulkSaveAttributeFailedException;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeSuccessModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SaveAttribute extends AbstractUpdateAttribute {
  
  public SaveAttribute(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfAttributes = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSaveAttribute = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    List<Map<String, Object>> updatedAttributeList = new ArrayList<>(); 
    
    for (Map<String, Object> attributeMap : listOfAttributes) {
      try {
        attributeMap = updateAttribute(attributeMap, auditInfoList, updatedAttributeList);
        listOfSuccessSaveAttribute.add(attributeMap);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSaveAttribute.isEmpty()) {
      throw new BulkSaveAttributeFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    
    Map<String, Object> configDetails = GetGridAttributeUtils
        .getConfigDetails(listOfSuccessSaveAttribute);
    
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IBulkSaveAttributeSuccessModel.ATTRIBUTES_LIST, listOfSuccessSaveAttribute);
    successMap.put(IBulkSaveAttributeSuccessModel.CONFIG_DETAILS, configDetails);
    successMap.put(IBulkSaveAttributeSuccessModel.UPDATED_ATTRIBUTE_LIST, updatedAttributeList);
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveAttributeResponse = new HashMap<String, Object>();
    bulkSaveAttributeResponse.put(IBulkSaveAttributeResponseModel.AUDIT_LOG_INFO, auditInfoList);
    bulkSaveAttributeResponse.put(IBulkSaveAttributeResponseModel.SUCCESS, successMap);
    bulkSaveAttributeResponse.put(IBulkSaveAttributeResponseModel.FAILURE, failure);
    return bulkSaveAttributeResponse;
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveAttribute/*" };
  }
}
