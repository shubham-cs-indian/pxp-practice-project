package com.cs.config.strategy.plugin.usecase.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.template.TemplateNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class DeleteTemplate extends AbstractOrientPlugin {
  
  public DeleteTemplate(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteTemplate/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ids = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> deletedIds = new ArrayList<>();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    for (String templateId : ids) {
      Vertex templateNode = null;
      try {
        templateNode = UtilClass.getVertexById(templateId, VertexLabelConstants.TEMPLATE);
      }
      catch (NotFoundException e) {
        throw new TemplateNotFoundException(e);
      }
      
      // delete template permission node
      Iterable<Vertex> permissionVertices = templateNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_ALLOWED_TEMPLATE);
      for (Vertex permissionVertex : permissionVertices) {
        permissionVertex.remove();
      }
      AuditLogUtils.fillAuditLoginfo(auditLogInfoList, templateNode, Entities.TEMPLATES, Elements.UNDEFINED);
      templateNode.remove();
      deletedIds.add(templateId);
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditLogInfoList);

    return responseMap;
  }
}
