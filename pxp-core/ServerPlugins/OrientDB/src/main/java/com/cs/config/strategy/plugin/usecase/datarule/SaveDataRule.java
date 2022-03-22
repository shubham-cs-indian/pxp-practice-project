package com.cs.config.strategy.plugin.usecase.datarule;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.datarule.util.SaveDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IRuleViolationEntity;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import com.cs.core.exception.InvalidRuleViolationException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.collections.ListUtils;

import java.util.*;

public class SaveDataRule extends AbstractOrientPlugin {
  
  public SaveDataRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> ruleMap = new HashMap<String, Object>();
    List<String> physicalCatalogList = new ArrayList<>();
    ruleMap = (HashMap<String, Object>) map.get(CommonConstants.RULE_PROPERTY);
    
    OrientGraph graph = UtilClass.getGraph();
    
    Vertex dataRuleNode = UtilClass.getVertexById((String) ruleMap.get(CommonConstants.ID_PROPERTY),
        VertexLabelConstants.DATA_RULE);
    
    List<String> physicalCatalogIds = (List<String>) ruleMap
        .get(CommonConstants.PHYSICAL_CATALOG_IDS);
    Boolean isPhysicalCatalogsChanged = isPhysicalCatalogsChanged(ruleMap, dataRuleNode,
        physicalCatalogIds, physicalCatalogList);

    validateRule(ruleMap, dataRuleNode);
    SaveDataRuleUtils.execute(ruleMap);
    Map<String, Object> returnMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode);
    
    Boolean isLanguageDependent = (Boolean) returnMap.get(IDataRule.IS_LANGUAGE_DEPENDENT);
    if(isLanguageDependent) {
    	List<String> languages = (List<String>) returnMap.get(IDataRule.LANGUAGES);
    	if(languages.isEmpty()) {
    		Iterable<Vertex> verticesOfClass = UtilClass.getGraph()
    		        .getVerticesOfClass(VertexLabelConstants.LANGUAGE);
    		    
    		    for (Vertex language : verticesOfClass) {
    		    	languages.add(language.getProperty(CommonConstants.CODE_PROPERTY));
    		    }
    	}
    }
    returnMap.put(IDataRuleModel.IS_PHYSICAL_CATALOGS_CHANGED, isPhysicalCatalogsChanged);
    GetDataRuleUtils.fillConfigDetails(returnMap);
    AuditLogUtils.fillAuditLoginfo(returnMap, dataRuleNode, Entities.RULES, Elements.UNDEFINED);
    
    graph.commit();
    
    String ruleId = (String) ruleMap.get("id");
    List<String> klassIds = new ArrayList<>();
    Vertex ruleNode = UtilClass.getVertexByIndexedId(ruleId, VertexLabelConstants.DATA_RULE);
    Iterator<Vertex> vertexIterator = ruleNode
        .getVertices(Direction.IN, RelationshipLabelConstants.DATA_RULES)
        .iterator();
    while (vertexIterator.hasNext()) {
      Vertex klassNode = vertexIterator.next();
      klassIds.add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    returnMap.put("klassIds", klassIds);
    returnMap.put("physicalCatalogList", physicalCatalogList);
    
    return returnMap;
  }

  private void validateRule(Map<String, Object> map, Vertex dataRuleNode) throws Exception
  {
    Boolean isLanguageDependent = dataRuleNode.getProperty(IDataRule.IS_LANGUAGE_DEPENDENT);
    List<Map<String, Object>> addedRuleViolations = (List<Map<String, Object>>) map.get(ISaveDataRuleModel.ADDED_RULE_VIOLATIONS);
    for( Map<String, Object> addedRuleViolation: addedRuleViolations){
      String type = (String) addedRuleViolation.get(IRuleViolationEntity.TYPE);
      String entityId = (String) addedRuleViolation.get(IRuleViolationEntity.ENTITY_ID);
      String VLC = type.equals(CommonConstants.ATTRIBUTE) ? VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE : VertexLabelConstants.ENTITY_TAG;
      Vertex vertex = UtilClass.getVertexByCode(entityId, VLC);
      if(VLC.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)){
        Boolean isTrans = vertex.getProperty(IAttribute.IS_TRANSLATABLE);
        if (isLanguageDependent != isTrans) {
            throw new InvalidRuleViolationException();
        }
      }
    }
  }

  private Boolean isPhysicalCatalogsChanged(Map<String, Object> map, Vertex dataRuleNode,
      List<String> physicalCatalogIds, List<String> physicalCatalogList)
  {
    
    List<String> existingPhysicalCatalogIds = dataRuleNode
        .getProperty(IDataRuleModel.PHYSICAL_CATALOG_IDS);
    List<String> newPhysicalCatalogIds = (List<String>) map
        .get(IDataRuleModel.PHYSICAL_CATALOG_IDS);
    Boolean isPhysicalCatalog = false;
    
    if (existingPhysicalCatalogIds.isEmpty()) {
      existingPhysicalCatalogIds.addAll(physicalCatalogIds);
    }
    if (newPhysicalCatalogIds.isEmpty()) {
      newPhysicalCatalogIds.addAll(physicalCatalogIds);
    }
    
    List<String> nonMatchedPysicalCatalog = new ArrayList<>();
    for (String catalog : existingPhysicalCatalogIds)
      if (!newPhysicalCatalogIds.contains(catalog))
        nonMatchedPysicalCatalog.add(catalog);
    for (String catalog : newPhysicalCatalogIds)
      if (!existingPhysicalCatalogIds.contains(catalog))
        nonMatchedPysicalCatalog.add(catalog);
      
    if (newPhysicalCatalogIds.size() == existingPhysicalCatalogIds.size()) {
      List intersection = ListUtils.intersection(existingPhysicalCatalogIds, newPhysicalCatalogIds);
      if (intersection.size() == newPhysicalCatalogIds.size()) {
        isPhysicalCatalog = false;
        physicalCatalogList.addAll(existingPhysicalCatalogIds);
      }
      else {
        isPhysicalCatalog = true;
        physicalCatalogList.addAll(nonMatchedPysicalCatalog);
      }
    }
    else {
      isPhysicalCatalog = true;
      physicalCatalogList.addAll(nonMatchedPysicalCatalog);
    }
    
    if (dataRuleNode.getProperty(IDataRuleModel.TYPE)
        .equals(Constants.CLASSIFICATION)
        || dataRuleNode.getProperty(IDataRuleModel.TYPE)
            .equals(Constants.STANDARDIZATION_AND_NORMALIZATION)) {
      isPhysicalCatalog = !existingPhysicalCatalogIds.containsAll(newPhysicalCatalogIds);
    }
    return isPhysicalCatalog;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveDataRule/*" };
  }
}
