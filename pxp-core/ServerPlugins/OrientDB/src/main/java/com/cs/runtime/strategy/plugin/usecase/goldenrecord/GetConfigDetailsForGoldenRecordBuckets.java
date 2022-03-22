package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForGoldenRecord;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGoldenRecordBuckets extends AbstractConfigDetailsForGoldenRecord {
  
  public GetConfigDetailsForGoldenRecordBuckets(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGoldenRecordBuckets/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ruleIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    Iterable<Vertex> ruleVertices = UtilClass.getVerticesByIndexedIds(ruleIds,
        VertexLabelConstants.GOLDEN_RECORD_RULE);
    
    Map<String, Object> configDetails = getMapToReturn();
    
    Map<String, Object> referencedGoldenRecordRules = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_GOLDEN_RECORD_RULES);
    for (Vertex vertex : ruleVertices) {
      HashMap<String, Object> ruleMap = UtilClass.getMapFromNode(vertex);
      
      referencedGoldenRecordRules.put((String) ruleMap.get(IGoldenRecordRule.ID), ruleMap);
      
      fillReferencedAttributes(configDetails, vertex, ruleMap);
      fillReferencedTags(configDetails, vertex, ruleMap);
      fillReferencedKlasses(configDetails, vertex, ruleMap);
      fillReferencedTaxonomies(configDetails, vertex, ruleMap);
      fillMandatoryReferencedAttributes(configDetails);
    }
    return configDetails;
  }
  
  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_ATTRIBUTES,
        new HashMap<>());
    configDetails.put(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_TAGS,
        new HashMap<>());
    configDetails.put(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFRENCED_KLASSES,
        new HashMap<>());
    configDetails.put(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_TAXONOMIES,
        new HashMap<>());
    configDetails.put(
        IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_GOLDEN_RECORD_RULES,
        new HashMap<>());
    return configDetails;
  }
  
  private void fillReferencedTags(Map<String, Object> conigDetails, Vertex vertex,
      HashMap<String, Object> ruleMap) throws Exception
  {
    Map<String, Object> referencedTags = (Map<String, Object>) conigDetails
        .get(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_TAGS);
    Iterable<Vertex> tagVertices = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK);
    
    List<String> tags = new ArrayList<>();
    ruleMap.put(IGoldenRecordRule.TAGS, tags);
    
    for (Vertex tagsVertex : tagVertices) {
      HashMap<String, Object> tagMap = TagUtils.getTagMap(tagsVertex, true);
      String tagId = (String) tagMap.get(ITag.ID);
      tags.add(tagId);
      referencedTags.put(tagId, tagMap);
    }
  }
  
  private void fillReferencedAttributes(Map<String, Object> conigDetails, Vertex vertex,
      HashMap<String, Object> ruleMap)
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) conigDetails
        .get(IGetConfigDetailsForGoldenRecordRuleResponseModel.REFERENCED_ATTRIBUTES);
    
    Iterable<Vertex> attributeVertices = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK);
    
    List<String> attributes = new ArrayList<>();
    ruleMap.put(IGoldenRecordRule.ATTRIBUTES, attributes);
    
    for (Vertex attributeVertex : attributeVertices) {
      Map<String, Object> attributeMap = AttributeUtils.getAttributeMap(attributeVertex);
      String attributeId = (String) attributeMap.get(IAttribute.ID);
      referencedAttributes.put(attributeId, attributeMap);
      attributes.add(attributeId);
    }
  }
}
