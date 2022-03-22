package com.cs.config.strategy.plugin.usecase.base.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.governancerule.GovernanceRuleUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractDeleteOrganizations extends AbstractOrientPlugin {
  
  public AbstractDeleteOrganizations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> idsToDelete = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    IExceptionModel failure = new ExceptionModel();
    List<String> success = new ArrayList<>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    for (String id : idsToDelete) {
      try {
        Vertex organizationVertex = UtilClass.getVertexById(id, VertexLabelConstants.ORGANIZATION);
        if(ValidationUtils.vaildateIfStandardEntity(organizationVertex))
          continue;
        deleteAssociatedRoleNodes(organizationVertex);
        AuditLogUtils.fillAuditLoginfo(auditInfoList, organizationVertex, Entities.PARTNERS, Elements.UNDEFINED);
        organizationVertex.remove();
        success.add(id);
      }
      catch (NotFoundException e) {
        
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IBulkDeleteReturnModel.SUCCESS, success);
    returnMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    returnMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditInfoList);
    return returnMap;
  }
  
  private void deleteAssociatedRoleNodes(Vertex organizationVertex)
  {
    Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    List<Vertex> userNodesToBeDelete = new ArrayList<>();
    
    for (Vertex roleVertex : roleVertices) {
      if ((Boolean) roleVertex.getProperty(IRole.IS_BACKGROUND_ROLE)) {
        fillUserNodesToBeDelete(roleVertex, userNodesToBeDelete);
      }
      RoleUtils.deleteGlobalPermissions(roleVertex);
      RoleUtils.deleteSectionElementNodesAttached(roleVertex);
      DataRuleUtils.deleteVerticesWithInDirection(roleVertex,
          RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
      DataRuleUtils.deleteVerticesWithInDirection(roleVertex,
          RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
      DataRuleUtils.deleteIntermediateVerticesWithInDirection(roleVertex,
          RelationshipLabelConstants.ROLE_DATA_RULE_LINK);
      GovernanceRuleUtil.deleteIntermediateVerticesWithInDirection(roleVertex,
          RelationshipLabelConstants.GOVERNANCE_RULE_ROLE_LINK);
      RoleUtils.deleteEndpoint(roleVertex);
      
      RoleUtils.deletePropertyPermissionNodesByPropertyNode(roleVertex);
      RoleUtils.deleteAllPermissionNodesForRole(roleVertex);
      
      roleVertex.remove();
    }
    deleteAssociatedBackgroundUserNode(userNodesToBeDelete);
  }
  
  /**
   * @param roleVertex
   * @param userNodesToBeDelete
   */
  private void fillUserNodesToBeDelete(Vertex roleVertex, List<Vertex> userNodesToBeDelete)
  {
    Iterable<Vertex> userVertices = roleVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    
    for (Vertex userVertex : userVertices) {
      userNodesToBeDelete.add(userVertex);
    }
  }
  
  /**
   * @param userNodesToBeDelete
   */
  private void deleteAssociatedBackgroundUserNode(List<Vertex> userNodesToBeDelete)
  {
    for (Vertex userNode : userNodesToBeDelete) {
      UserUtils.deleteUserNode(userNode);
      userNode.remove();
    }
  }
}
