package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;
import com.cs.core.config.interactor.model.relationship.IConfigDetailsForRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.config.interactor.model.tabs.ITabModel;
import com.cs.core.exception.BulkSaveRelationshipsFailedException;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class BulkSaveRelationships extends AbstractOrientPlugin {
  
  public BulkSaveRelationships(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveRelationships/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> successRelationshipList = new ArrayList<>();
    Map<String, Object> referencedTabs = new HashMap<>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (Map<String, Object> relationship : relationships) {
      Vertex relationshipNode = null;
      try {
        Map<String, Object> addedTab = (Map<String, Object>) relationship
            .remove(IBulkSaveRelationshipsModel.ADDED_TAB);
        String deletedTab = (String) relationship.remove(IBulkSaveRelationshipsModel.DELETED_TAB);
        
        relationshipNode = UtilClass.getVertexByIndexedId(
            (String) relationship.get(IBulkSaveRelationshipsModel.ID),
            VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
        
        TabUtils.manageAddedAndDeletedTab(relationshipNode, addedTab, deletedTab,
            (String) relationshipNode.getProperty(IRelationship.TYPE));
        
        UtilClass.saveNode(relationship, relationshipNode,
            Arrays.asList(IRelationshipInformationModel.TAB_ID));
        
        AuditLogUtils.fillAuditLoginfo(auditInfoList, relationshipNode,
            Entities.RELATIONSHIPS, Elements.RELATIONSHIPS);
        
        Map<String, Object> relationshipMap = UtilClass
            .getMapFromVertex(
                Arrays.asList(IRelationshipInformationModel.LABEL,
                    IRelationshipInformationModel.TYPE, IRelationshipInformationModel.CODE,
                    IRelationshipInformationModel.ICON, CommonConstants.CODE_PROPERTY, 
                    IRelationshipInformationModel.IS_LITE),
                relationshipNode);
        
        Map<String, Object> tabDetails = TabUtils.getMapFromConnectedTabNode(relationshipNode,
            Arrays.asList(CommonConstants.CODE_PROPERTY, IIdLabelModel.LABEL));
        
        relationshipMap.put(IRelationshipInformationModel.TAB_ID, tabDetails.get(ITabModel.ID));
        successRelationshipList.add(relationshipMap);
        referencedTabs.put((String) tabDetails.get(ITabModel.ID), tabDetails);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (successRelationshipList.isEmpty()) {
      throw new BulkSaveRelationshipsFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_TABS, referencedTabs);
    
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetAllRelationshipsResponseModel.LIST, successRelationshipList);
    successMap.put(IGetAllRelationshipsResponseModel.CONFIG_DETAILS, configDetails);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkSaveRelationshipsResponseModel.SUCCESS, successMap);
    responseMap.put(IBulkSaveRelationshipsResponseModel.AUDIT_LOG_INFO, auditInfoList);
    
    return responseMap;
  }
}
