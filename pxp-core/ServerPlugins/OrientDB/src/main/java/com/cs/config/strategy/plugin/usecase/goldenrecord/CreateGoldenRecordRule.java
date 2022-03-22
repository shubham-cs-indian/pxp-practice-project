package com.cs.config.strategy.plugin.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreateGoldenRecordRule extends AbstractOrientPlugin {
  
  public CreateGoldenRecordRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected List<String> fieldsToExclude = Arrays.asList(IGoldenRecordRule.ATTRIBUTES,
      IGoldenRecordRule.TAGS, IGoldenRecordRule.KLASS_IDS, IGoldenRecordRule.TAXONOMY_IDS,
      IGoldenRecordRule.ORGANIZATIONS, IGoldenRecordRule.ENDPOINTS, IGoldenRecordRule.MERGE_EFFECT);
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOLDEN_RECORD_RULE, CommonConstants.CODE_PROPERTY);
    Vertex ruleNode = UtilClass.createNode(requestMap, vertexType, fieldsToExclude);
    
    vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.MERGE_EFFECT,
        CommonConstants.CODE_PROPERTY);
    Vertex mergeEffectNode = UtilClass.createNode(new HashMap<>(), vertexType, new ArrayList<>());
    ruleNode.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK,
        mergeEffectNode);
    
    List<String> attributes = (List<String>) requestMap.get(IGoldenRecordRule.ATTRIBUTES);
    if (!attributes.isEmpty()) {
      GoldenRecordRuleUtil.handleAddedAttributes(ruleNode, attributes);
    }
    List<String> tags = (List<String>) requestMap.get(IGoldenRecordRule.TAGS);
    if (!tags.isEmpty()) {
      GoldenRecordRuleUtil.handleAddedTags(ruleNode, tags);
    }
    List<String> klassIds = (List<String>) requestMap.get(IGoldenRecordRule.KLASS_IDS);
    if (!klassIds.isEmpty()) {
      GoldenRecordRuleUtil.handleAddedKlasses(ruleNode, klassIds);
    }
    List<String> taxonomyIds = (List<String>) requestMap.get(IGoldenRecordRule.TAXONOMY_IDS);
    if (!taxonomyIds.isEmpty()) {
      GoldenRecordRuleUtil.handleAddedTaxonomies(ruleNode, taxonomyIds);
    }
    List<String> organizationIds = (List<String>) requestMap.get(IGoldenRecordRule.ORGANIZATIONS);
    if (!organizationIds.isEmpty()) {
      GoldenRecordRuleUtil.handleAddedOrganizations(ruleNode, organizationIds);
    }
    List<String> endpointIds = (List<String>) requestMap.get(IGoldenRecordRule.ENDPOINTS);
    if (!endpointIds.isEmpty()) {
      GoldenRecordRuleUtil.handleAddedEndpoints(ruleNode, endpointIds);
    }
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> returnMap = GoldenRecordRuleUtil.getGoldenRecordRuleFromNode(ruleNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, ruleNode, Entities.GOLDEN_RECORD_RULE, Elements.UNDEFINED);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateGoldenRecordRule/*" };
  }
}
