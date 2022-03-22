package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.attribute.util.GetGridAttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeValueNormalization;
import com.cs.core.config.interactor.entity.datarule.IConcatenatedNormalization;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.datarule.IConfigDetailsForDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleEntityRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleIntermediateEntitys;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportRuleList extends AbstractOrientPlugin {
  
  public ExportRuleList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<>();
    
    List<String> taskCodes = (List<String>) requestMap.get("itemCodes");
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(taskCodes, IDataRule.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.DATA_RULE + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex ruleNode : resultIterable) {
      Map<String, Object> dataRule = GetDataRuleUtils.getDataRuleFromNode(ruleNode);
      List<Map<String, Object>> tags = (List<Map<String, Object>>) dataRule.get(IDataRule.TAGS);
      removeUnselectedTagValuesFromTags(tags);
      list.add(dataRule);
      fillConfigDetails(dataRule);
    }
    
    responseMap.put("list", list);
    return responseMap;
  }

  private void removeUnselectedTagValuesFromTags(List<Map<String, Object>> tags)
  {
    for (Map<String, Object> tag : tags) {
      List<Map<String, Object>> ruleList = (List<Map<String, Object>>) tag.get(CommonConstants.RULES_PROPERTY);
      for (Map<String, Object> rule : ruleList) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) rule.get(CommonConstants.TAG_VALUES);
        List<Map<String, Object>> selectedTagValues = new ArrayList<>();
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          if (to != 0 && from != 0) {
            selectedTagValues.add(tagValue);
          }
        }
        rule.put(CommonConstants.TAG_VALUES, selectedTagValues);
      }
    }
  }
  
  public void fillConfigDetails(Map<String, Object> returnMap) throws Exception
  {
    for (Map<String, Object> attribute : (List<Map<String, Object>>) returnMap.get(IDataRuleModel.ATTRIBUTES)) {
      String attributeId = (String) attribute.get(IDataRuleIntermediateEntitys.ENTITY_ID);
      for (Map<String, Object> rule : (List<Map<String, Object>>) attribute.get(IDataRuleIntermediateEntitys.RULES)) {
        String attributeLinkId = (String) rule.get(IDataRuleEntityRule.ATTRIBUTE_LINK_ID);
        /*
         * null check because when attribute is not selected by default it's value is null
         * */
        if (attributeLinkId != null) {
          fillReferencedAttribute(attributeLinkId, rule);
        }
      }
      fillReferencedAttribute(attributeId, attribute);
    }
    
    for (Map<String, Object> tag : (List<Map<String, Object>>) returnMap.get(IDataRuleModel.TAGS)) {
      String tagId = (String) tag.get(IDataRuleIntermediateEntitys.ENTITY_ID);
      fillReferencedTag(tagId, tag);
    }
    
    List<Map<String, Object>> effects = new ArrayList<>();
    effects.addAll((List<Map<String, Object>>) returnMap.get(IDataRuleModel.NORMALIZATIONS));
    effects.addAll((List<Map<String, Object>>) returnMap.get(IDataRuleModel.RULE_VIOLATIONS));
    for (Map<String, Object> effect : effects) {
      String entityId = (String) effect.get(INormalization.ENTITY_ID);
      String type = (String) effect.get(INormalization.TYPE);
      switch (type) {
        case CommonConstants.ATTRIBUTE:
          fillReferencedAttribute(entityId, effect);
          break;
        
        case CommonConstants.TAG:
          fillReferencedTag(entityId, effect);
          break;
        case CommonConstants.TYPE:
          effect.put(CommonConstants.ENTITYTYPE, CommonConstants.TYPE);
          break;
        case CommonConstants.TAXONOMY:
          effect.put(CommonConstants.ENTITYTYPE, CommonConstants.TAXONOMY);
          break;
      }
    }
  }
  
  private  void fillReferencedAttribute(String attributeId,  Map<String, Object> referencedAttribute) throws Exception
  {
    Vertex attributeNode = null;
    try {
      attributeNode = UtilClass.getVertexById(attributeId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    }
    catch (NotFoundException e) {
      throw new AttributeNotFoundException();
    }
    referencedAttribute.put(CommonConstants.ENTITYTYPE, attributeNode.getProperty(IAttribute.TYPE));
  }
  
  private  void fillReferencedTag(String tagId, Map<String, Object> referencedTag) throws Exception
  {
    Vertex tagNode;
    try {
      tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    referencedTag.put(CommonConstants.ENTITYTYPE, tagNode.getProperty(ITag.TYPE));
    referencedTag.put(CommonConstants.ENTITYATTRIBUTETYPE, tagNode.getProperty(ITag.TAG_TYPE));
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportRuleList/*" };
  }
}