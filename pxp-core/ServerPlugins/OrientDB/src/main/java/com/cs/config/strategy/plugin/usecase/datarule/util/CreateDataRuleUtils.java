package com.cs.config.strategy.plugin.usecase.datarule.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateDataRuleUtils {
  
  protected static List<String> fieldsToIgnore = Arrays.asList("attributes", "tags", "roles",
      "relationships", "types", "ruleViolations", "normalizations");
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> createDataRuleNode(Map<String, Object> requestMap)
      throws Exception
  {
    
    List<Map<String, Object>> attributeRules = new ArrayList<>();
    attributeRules = (List<Map<String, Object>>) requestMap.get("attributes");
    
    List<Map<String, Object>> tagRules = new ArrayList<>();
    tagRules = (List<Map<String, Object>>) requestMap.get("tags");
    
    List<Map<String, Object>> roleRules = new ArrayList<>();
    roleRules = (List<Map<String, Object>>) requestMap.get("roles");
    
    List<Map<String, Object>> relationshipRules = new ArrayList<>();
    relationshipRules = (List<Map<String, Object>>) requestMap.get("relationships");
    
    /*List<Map<String, Object>> typeRules = new ArrayList<>();
    typeRules = (List<Map<String, Object>>) requestMap.get("types");*/
    
    List<Map<String, Object>> ruleViolations = new ArrayList<>();
    ruleViolations = (List<Map<String, Object>>) requestMap.get("ruleViolations");
    
    List<Map<String, Object>> normalizations = new ArrayList<>();
    normalizations = (List<Map<String, Object>>) requestMap.get("normalizations");
    
    List<String> klassIds = new ArrayList<>();
    klassIds = (List<String>) requestMap.get(IDataRule.TYPES);
    
    List<String> taxonomyIds = new ArrayList<>();
    taxonomyIds = (List<String>) requestMap.get(IDataRule.TAXONOMIES);
    
    List<String> languages = (List<String>) requestMap.get("languages");
    
    for (String field : fieldsToIgnore) {
      requestMap.remove(field);
    }
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.DATA_RULES,
        CommonConstants.CODE_PROPERTY);
    
    Vertex dataRuleNode = UtilClass.createNode(requestMap, vertexType);
    
    DataRuleUtils.addAttributeRules(dataRuleNode, attributeRules);
    DataRuleUtils.addRuleViolations(dataRuleNode, ruleViolations);
    DataRuleUtils.addRoleRules(dataRuleNode, roleRules);
    // DataRuleUtils.addTypeRules(dataRuleNode, typeRules);
    DataRuleUtils.addRelationshipRules(dataRuleNode, relationshipRules);
    DataRuleUtils.addTagRules(dataRuleNode, tagRules);
    DataRuleUtils.addNormalizations(dataRuleNode, normalizations);
    DataRuleUtils.addKlassRules(dataRuleNode, klassIds);
    DataRuleUtils.addTaxonomyRules(dataRuleNode, taxonomyIds);
    DataRuleUtils.addLanguages(dataRuleNode, languages);
    
    Map<String, Object> returnMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, dataRuleNode, Entities.RULES, Elements.UNDEFINED);
    return returnMap;
  }
}
