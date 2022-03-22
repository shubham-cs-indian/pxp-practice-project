package com.cs.config.strategy.plugin.usecase.datarule.util;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;

public class DeleteDataRuleUtils {
  
  public static void deleteLinkedNodes(Vertex dataRule)
  {
    
    Iterable<Vertex> attributeIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
    for (Vertex attributeIntermediate : attributeIntermediates) {
      DataRuleUtils.deleteIntermediateWithRuleNodes(attributeIntermediate);
    }
    
    Iterable<Vertex> ruleViolationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_VIOLATION_LINK);
    for (Vertex ruleViolationVertex : ruleViolationVertices) {
      ruleViolationVertex.remove();
    }
    
    Iterable<Vertex> normalizationVertices = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.NORMALIZATION_LINK);
    for (Vertex normalizationVertex : normalizationVertices) {
      String transformationType = normalizationVertex
          .getProperty(INormalization.TRANSFORMATION_TYPE);
      if (CommonConstants.CONCAT_TRANSFORMATION_TYPE.equals(transformationType)) {
        SaveDataRuleUtils.deleteNormalizationConcatenatedNodes(normalizationVertex);
      }
      normalizationVertex.remove();
    }
    
    Iterable<Vertex> roleIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.ROLE_DATA_RULE);
    for (Vertex roleIntermediate : roleIntermediates) {
      DataRuleUtils.deleteIntermediateWithRuleNodes(roleIntermediate);
    }
    
    /*Iterable<Vertex> klassIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.TYPE_DATA_RULE);
    for(Vertex klassIntermediate : klassIntermediates){
      DataRuleUtils.deleteIntermediateWithRuleNodes(klassIntermediate);
    }*/
    
    Iterable<Vertex> relationshipIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIP_DATA_RULE);
    for (Vertex relationshipIntermediate : relationshipIntermediates) {
      DataRuleUtils.deleteIntermediateWithRuleNodes(relationshipIntermediate);
    }
    
    Iterable<Vertex> tagIntermediates = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.TAG_DATA_RULE);
    for (Vertex tagIntermediate : tagIntermediates) {
      DataRuleUtils.deleteIntermediateWithRuleNodes(tagIntermediate);
    }
    
    Iterator<Vertex> klassIntermediates = dataRule
        .getVertices(Direction.OUT, RelationshipLabelConstants.TYPE_DATA_RULE)
        .iterator();
    if (klassIntermediates.hasNext()) {
      klassIntermediates.next()
          .remove();
    }
    
    Iterator<Vertex> taxonomyIntermediates = dataRule
        .getVertices(Direction.OUT, RelationshipLabelConstants.TAXONOMY_DATA_RULE)
        .iterator();
    if (taxonomyIntermediates.hasNext()) {
      taxonomyIntermediates.next()
          .remove();
    }
  }
}
