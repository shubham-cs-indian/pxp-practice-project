package com.cs.config.strategy.plugin.usecase.variantcontext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class DeleteVariantContext extends AbstractOrientPlugin {
  
  public DeleteVariantContext(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteVariantContext/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> idsToDelete = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> deletedIds = new ArrayList<>();
    List<String> relationshipIdsToDelete = new ArrayList<>();
    List<Map<String , Object>> auditLogInfo = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (String id : idsToDelete) {
      
      Vertex contextNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.VARIANT_CONTEXT);
      
      Iterable<Vertex> contextTags = contextNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_TAG);
      
      // delete relationship nodes.
      Iterable<Vertex> relationshipNodes = fillAssociatedNatureRelationshipVertices(contextNode);
      if (relationshipNodes != null) {
        List<String> deletedRelationshipIds = RelationshipUtils
            .deleteRelationships(relationshipNodes);
        relationshipIdsToDelete.addAll(deletedRelationshipIds);
      }
      for (Vertex contextTag : contextTags) {
        contextTag.remove();
      }
      deletedIds.add(id);
      TabUtils.updateTabOnEntityDelete(contextNode);
      AuditLogUtils.fillAuditLoginfo(auditLogInfo, contextNode, Entities.CONTEXT, Elements.UNDEFINED);
      contextNode.remove();
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> response = new HashMap<>();
    response.put(IBulkDeleteVariantContextReturnModel.SUCCESS, deletedIds);
    response.put(IBulkDeleteVariantContextReturnModel.FAILURE, failure);
    response.put(IBulkDeleteVariantContextReturnModel.RELATIONSHIP_IDS, relationshipIdsToDelete);
    response.put(IBulkDeleteVariantContextReturnModel.AUDIT_LOG_INFO, auditLogInfo);
    return response;
  }
  
  private Iterable<Vertex> fillAssociatedNatureRelationshipVertices(Vertex contextNode)
  {
    String contextType = contextNode.getProperty(IVariantContext.TYPE);
    if (contextType.equals(Constants.LINKED_VARIANT)) {
      String query = "select from(select expand(In('"
          + RelationshipLabelConstants.VARIANT_CONTEXT_OF + "').out('"
          + RelationshipLabelConstants.HAS_PROPERTY + "')) from " + contextNode.getId() + ")";
      return UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    return null;
  }
}
