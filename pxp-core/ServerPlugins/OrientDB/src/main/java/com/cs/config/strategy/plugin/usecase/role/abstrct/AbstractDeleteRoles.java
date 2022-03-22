package com.cs.config.strategy.plugin.usecase.role.abstrct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.governancerule.GovernanceRuleUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.config.interactor.model.configdetails.IDeleteReturnModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public abstract class AbstractDeleteRoles extends AbstractOrientPlugin {
  
  public AbstractDeleteRoles(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Map<String, Object> delete(Map<String, Object> map, List<String> roleIds, String label)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    List<String> deletedUserNames = new ArrayList<String>();
    List<String> deletedIds = new ArrayList<>();
    for (String id : roleIds) {
      
      Vertex roleNode = UtilClass.getVertexByIndexedId(id, label);
      if(ValidationUtils.vaildateIfStandardEntity(roleNode))
        continue;
      if (roleNode != null) {
    	deleteUserInRelationships(roleNode, deletedUserNames);
        RoleUtils.deleteGlobalPermissions(roleNode);
        // deleteSectionPermissions(roleNode);
        RoleUtils.deleteSectionElementNodesAttached(roleNode);
        DataRuleUtils.deleteVerticesWithInDirection(roleNode,
            RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
        DataRuleUtils.deleteVerticesWithInDirection(roleNode,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        DataRuleUtils.deleteIntermediateVerticesWithInDirection(roleNode,
            RelationshipLabelConstants.ROLE_DATA_RULE_LINK);
        GovernanceRuleUtil.deleteIntermediateVerticesWithInDirection(roleNode,
            RelationshipLabelConstants.GOVERNANCE_RULE_ROLE_LINK);
        
        RoleUtils.deletePropertyPermissionNodesByPropertyNode(roleNode);
        RoleUtils.deleteAllPermissionNodesForRole(roleNode);
        
        RoleUtils.deleteEndpoint(roleNode);
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, roleNode, Entities.ROLES, Elements.UNDEFINED);
        roleNode.remove();
      }
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IDeleteReturnModel.DELETED_USER_NAMES, deletedUserNames);
    responseMap.put("success", deletedIds);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditLogInfoList);
    
    graph.commit();
    
    return responseMap;
  }
  
  private void deleteUserInRelationships(Vertex roleNode, List<String> deletedUserNames)
  {
    Iterable<Edge> UserInRelationships = roleNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Edge userInRelationship : UserInRelationships) {
	  Vertex userNode = userInRelationship.getVertex(Direction.OUT);
	  deletedUserNames.add((String)userNode.getProperty(User.USER_NAME));
      userInRelationship.remove();
    }
  }
}
