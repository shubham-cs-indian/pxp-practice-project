package com.cs.config.strategy.plugin.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleStrategyModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleSuccessStrategyModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
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

public class DeleteGoldenRecordRules extends AbstractOrientPlugin {
  
  public DeleteGoldenRecordRules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ruleIdsToDelete = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    IExceptionModel failure = new ExceptionModel();
    List<String> deletedIds = new ArrayList<>();
    List<String> klassIds = new ArrayList<>();
    List<String> taxonomyIds = new ArrayList<>();
    List<String> organizations = new ArrayList<>();
    List<String> endpointids = new ArrayList<>();
    List<String> physicalcatalogIds = new ArrayList<>();
    Map<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();

    
    for (String id : ruleIdsToDelete) {
      try {
        Vertex goldenRecordRuleNode = UtilClass.getVertexById(id,
            VertexLabelConstants.GOLDEN_RECORD_RULE);
        
        Map<String, Object> goldenrecord = getGoldenRecordRuleFromNode(goldenRecordRuleNode);
        klassIds
            .addAll((Collection<? extends String>) goldenrecord.get(IGoldenRecordRule.KLASS_IDS));
        taxonomyIds.addAll(
            (Collection<? extends String>) goldenrecord.get(IGoldenRecordRule.TAXONOMY_IDS));
        organizations.addAll(
            (Collection<? extends String>) goldenrecord.get(IGoldenRecordRule.ORGANIZATIONS));
        endpointids
            .addAll((Collection<? extends String>) goldenrecord.get(IGoldenRecordRule.ENDPOINTS));
        physicalcatalogIds.addAll((Collection<? extends String>) goldenrecord
            .get(IGoldenRecordRule.PHYSICAL_CATALOG_IDS));

        AuditLogUtils.fillAuditLoginfo(auditInfoList, goldenRecordRuleNode, Entities.GOLDEN_RECORD_RULE, Elements.UNDEFINED);
        deleteLinkedNodes(goldenRecordRuleNode);
        goldenRecordRuleNode.remove();
        deletedIds.add(id);
      }
      catch (NotFoundException e) {
        
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> BulkDeleteGoldenRecordRuleSuccess = new HashMap<>();
    BulkDeleteGoldenRecordRuleSuccess.put(
        IBulkDeleteGoldenRecordRuleSuccessStrategyModel.DELETED_GOLDEN_RECORD_RULE_IDS, deletedIds);
    BulkDeleteGoldenRecordRuleSuccess
        .put(IBulkDeleteGoldenRecordRuleSuccessStrategyModel.LINKED_KLASS_IDS, klassIds);
    BulkDeleteGoldenRecordRuleSuccess
        .put(IBulkDeleteGoldenRecordRuleSuccessStrategyModel.LINKED_TAXONOMY_IDS, taxonomyIds);
    BulkDeleteGoldenRecordRuleSuccess
        .put(IBulkDeleteGoldenRecordRuleSuccessStrategyModel.LINKED_ORGANIZATIONS, organizations);
    BulkDeleteGoldenRecordRuleSuccess
        .put(IBulkDeleteGoldenRecordRuleSuccessStrategyModel.LINKED_ENDPOINTIDS, endpointids);
    BulkDeleteGoldenRecordRuleSuccess.put(
        IBulkDeleteGoldenRecordRuleSuccessStrategyModel.LINKED_PHYSICALCATALOG_IDS,
        physicalcatalogIds);
    
    responseMap.put(IBulkDeleteGoldenRecordRuleStrategyModel.SUCCESS,
        BulkDeleteGoldenRecordRuleSuccess);
    responseMap.put(IBulkDeleteGoldenRecordRuleStrategyModel.FAILURE, failure);
    responseMap.put(IBulkDeleteGoldenRecordRuleStrategyModel.AUDIT_LOG_INFO, auditInfoList);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteGoldenRecordRules/*" };
  }
  
  protected void deleteLinkedNodes(Vertex goldenRecordRuleNode)
  {
    Iterator<Vertex> mergeEffectIterator = goldenRecordRuleNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK)
        .iterator();
    if (!mergeEffectIterator.hasNext()) {
      return;
    }
    Vertex mergeEffect = mergeEffectIterator.next();
    Iterable<Vertex> effectTypes = mergeEffect.getVertices(Direction.OUT,
        RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK);
    for (Vertex effectType : effectTypes) {
      effectType.remove();
    }
    mergeEffect.remove();
  }
  
  public Map<String, Object> getGoldenRecordRuleFromNode(Vertex goldenRecordNode) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> ruleMap = UtilClass.getMapFromNode(goldenRecordNode);
    returnMap.put(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE, ruleMap);
    GoldenRecordRuleUtil.fillKlassesData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillTaxonomiesData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillOrganizationsData(goldenRecordNode, returnMap);
    GoldenRecordRuleUtil.fillEndpointsData(goldenRecordNode, returnMap);
    
    return ruleMap;
  }
}
