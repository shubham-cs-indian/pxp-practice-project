package com.cs.config.strategy.plugin.usecase.propertycollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.propertycollection.util.CreatePropertyCollectionUtil;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class BulkCreatePropertyCollections extends AbstractOrientPlugin {
  
  public BulkCreatePropertyCollections(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> propertyCollectionList = new ArrayList<>();
    propertyCollectionList = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> createdPropertyCollectionList = new ArrayList<>();
    List<Map<String, Object>> failedPropertyCollectionList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    
    for (Map<String, Object> propertyCollection : propertyCollectionList) {
      
      Vertex propertyCollectionNode = CreatePropertyCollectionUtil
          .createPropertyCollection(propertyCollection);
      
      Map<String, Object> propertyCollectionMap = PropertyCollectionUtils
          .getPropertyCollection(propertyCollectionNode);
      
      Map<String, Object> returnMap = new HashMap<>();
      returnMap.put(ISummaryInformationModel.ID,
          propertyCollectionMap.get(IPropertyCollectionModel.ID));
      returnMap.put(ISummaryInformationModel.LABEL,
          propertyCollectionMap.get(IPropertyCollectionModel.LABEL));
      createdPropertyCollectionList.add(returnMap);
      AuditLogUtils.fillAuditLoginfo(auditInfoList, propertyCollectionNode,
          Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE, Elements.PROPERTY_COLLECTION);
    }
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdPropertyCollectionList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedPropertyCollectionList);
    result.put(IPluginSummaryModel.AUDIT_LOG_INFO, auditInfoList);
    return result;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedPropertyCollectionList,
      Map<String, Object> propertyCollection)
  {
    Map<String, Object> failedPropertyCollectionMap = new HashMap<>();
    failedPropertyCollectionMap.put(ISummaryInformationModel.ID,
        propertyCollection.get(IPropertyCollectionModel.ID));
    failedPropertyCollectionMap.put(ISummaryInformationModel.LABEL,
        propertyCollection.get(IPropertyCollectionModel.LABEL));
    failedPropertyCollectionList.add(failedPropertyCollectionMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreatePropertyCollections/*" };
  }
}
