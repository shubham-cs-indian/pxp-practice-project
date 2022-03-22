package com.cs.config.strategy.plugin.usecase.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class DeleteTabs extends AbstractOrientPlugin {
  
  public DeleteTabs(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    List<String> idsToDelete = (List<String>) requestMap.get("ids");
    
    List<String> deletedIds = new ArrayList<String>();
    for (String id : idsToDelete) {
      try {
        Vertex tabNode = UtilClass.getVertexById(id, VertexLabelConstants.TAB);
        if(ValidationUtils.vaildateIfStandardEntity(tabNode))
          continue;
        connectTabEntitiesToDefaultTab(tabNode);
        tabNode.remove();
        deletedIds.add(id);
        AuditLogUtils.fillAuditLoginfo(auditInfoList, tabNode, Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE,
            Elements.TABS_MENU_ITEM_TILE);
      }
      catch (NotFoundException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> tabSequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    tabSequenceList.removeAll(deletedIds);
    tabSequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, tabSequenceList);
    
    UtilClass.getGraph()
        .commit();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, idsToDelete);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditInfoList);
    return responseMap;
  }
  
  private void connectTabEntitiesToDefaultTab(Vertex tabNode) throws Exception
  {
    Iterable<Vertex> vertices = tabNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_TAB);
    for (Vertex vertex : vertices) {
      String entityType = EntityUtil.getEntityTypeByOrientClassType(
          vertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY));
      TabUtils.linkAddedOrDefaultTab(vertex, null, entityType);
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteTabs/*" };
  }
}
