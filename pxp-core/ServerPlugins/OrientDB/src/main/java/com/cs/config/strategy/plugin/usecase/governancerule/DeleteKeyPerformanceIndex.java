package com.cs.config.strategy.plugin.usecase.governancerule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.strategy.model.governancerule.IBulkDeleteKeyPerformanceIndexStrategyModel;
import com.cs.core.config.strategy.model.governancerule.IBulkDeleteKeyPerformanceIndexSuccessStrategyModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class DeleteKeyPerformanceIndex extends AbstractOrientPlugin {
  
  public DeleteKeyPerformanceIndex(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteKeyPerformanceIndex/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> kpiIds = (List<String>) map.get(IIdsListParameterModel.IDS);
    List<String> deletedIds = new ArrayList<>();
    Set<String> linkedKlassIds = new HashSet<>();
    Set<String> linkedTaxonomyIds = new HashSet<>();
    OrientGraph graph = UtilClass.getGraph();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    List<String> deletedRuleCodes = new ArrayList<String>();
    
    for (String id : kpiIds) {
      try {
        Vertex kpiNode = UtilClass.getVertexById(id, VertexLabelConstants.GOVERNANCE_RULE_KPI);
        GovernanceRuleUtil.deleteKPI(kpiNode, deletedRuleCodes);
        deleteKPITag(kpiNode);
        
        Iterable<Vertex> associatedKlasses = kpiNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.LINK_KLASS);
        Iterable<Vertex> associatedTaxonomies = kpiNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.LINK_TAXONOMY);
        for (Vertex associatedKlass : associatedKlasses) {
          linkedKlassIds.add(UtilClass.getCodeNew(associatedKlass));
        }
        for (Vertex associatedTaxonomy : associatedTaxonomies) {
          linkedTaxonomyIds.add(UtilClass.getCodeNew(associatedTaxonomy));
        }
        AuditLogUtils.fillAuditLoginfo(auditInfoList, kpiNode, Entities.KPI, Elements.UNDEFINED);
        
        kpiNode.remove();
        deletedIds.add(id);
      }
      catch (NotFoundException e) {
        deletedIds.add(id);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    Map<String, Object> success = new HashMap<>();
    success.put(
        IBulkDeleteKeyPerformanceIndexSuccessStrategyModel.DELETED_KEY_PERFORMANCE_INDEX_IDS,
        deletedIds);
    success.put(IBulkDeleteKeyPerformanceIndexSuccessStrategyModel.DELETED_RULE_CODES, deletedRuleCodes);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteKeyPerformanceIndexStrategyModel.FAILURE, failure);
    responseMap.put(IBulkDeleteKeyPerformanceIndexStrategyModel.SUCCESS, success);
    responseMap.put(IBulkDeleteKeyPerformanceIndexStrategyModel.AUDIT_LOG_INFO, auditInfoList);
    
    graph.commit();
    return responseMap;
  }
  
  private void deleteKPITag(Vertex kpiNode)
  {
    Iterable<Vertex> kpiTagNodes = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KPI_TAG);
    for (Vertex kpiTagNode : kpiTagNodes) {
      kpiTagNode.remove();
    }
  }
}
