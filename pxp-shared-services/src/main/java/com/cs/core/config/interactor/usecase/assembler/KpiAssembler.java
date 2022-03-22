package com.cs.core.config.interactor.usecase.assembler;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleEntityRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTagRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTags;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

public class KpiAssembler extends Assembler {
  
  
  private static KpiAssembler instance = new KpiAssembler();
  
  Collector<CharSequence, ?, String> OR_JOIN = Collectors.joining(" or "," ( ", " ) ");
  Collector<CharSequence, ?, String> AND_JOIN = Collectors.joining(" + "," ( ", " ) ");
  Collector<CharSequence, ?, String> AND_JOIN_For_KPI = Collectors.joining(" and "," ( ", " ) ");

  public static KpiAssembler instance()
  {
    if (instance == null) {
      instance = new KpiAssembler();
    }
    return instance;
    
  }
  
  public String assembleEvaluation(List<IGovernanceRuleIntermediateEntity> attributes, List<IGovernanceRuleTags> tags) {
    
    String attributeFinalExpression = null;
    String tagFinalExpression = null;
    

    if(attributes != null && !attributes.isEmpty()) {
      String attributeConditionExpression = assembleAttributesForRule(attributes);
      attributeFinalExpression = attributeConditionExpression.concat("/" +attributes.size());
    }

    if(tags != null && !tags.isEmpty()) {
      String tagConditionExpression = assembleTagForRules(tags);
      tagFinalExpression = tagConditionExpression.concat("/" +tags.size());
    }
    if(attributeFinalExpression != null && tagFinalExpression != null) {
      //StringBuilder finalString = new StringBuilder();
      
      return String.format("(( %s ) + ( %s ))/2", attributeFinalExpression, tagFinalExpression); 
      /*finalString.append("((");
      finalString.append(attributeFinalExpression);
      finalString.append(") + ");
      finalString.append("(");
      finalString.append(tagFinalExpression);
      finalString.append("))/2");
      
      return finalString.toString();*/
    }
    else if(attributeFinalExpression != null && !attributeFinalExpression.isEmpty()) {
      return attributeFinalExpression;
    }
    else {
      return tagFinalExpression;
    }
  }
  
  
  private String assembleTagForRules(List<IGovernanceRuleTags> tags)
  {
    if(tags != null && !tags.isEmpty() ) {
      return tags.stream().map(tag -> tag.getRules().stream()
          .map(tagRule -> generateTagExpression(tagRule, tag.getEntityId())).collect(OR_JOIN)).collect(AND_JOIN); 
    }
    return null;
  }
  
  private String generateTagExpression(IGovernanceRuleTagRule dataRule, String entityId) {
    String entityExpression = String.format("[%s]", entityId);
    String type = dataRule.getType();

    List<IDataRuleTagValues> tagValues = dataRule.getTagValues();

    String tagValuesExpression = tagValues.stream().filter(tag -> !tag.getFrom().equals(0l))
        .map(tag -> String.format("[T>%s $range=%s]", tag.getId(), tag.getFrom())).collect(Collectors.joining(",","{","}"));

    return getTagConditionExpression(entityExpression, type, tagValuesExpression);
  }
  
  protected String getTagConditionExpression(String entityExpression, String type, String tagValuesExpression)
  {
    StringBuilder expressionForValues = new StringBuilder();
    switch (type) {
      case "empty":
        expressionForValues.append(String.format(" %s = $null ", entityExpression));
        break;
      case "notempty":
        expressionForValues.append(String.format(" %s <> $null ", entityExpression));
        break; 
      case "contains":
      expressionForValues.append(String.format(" %s contains %s ", entityExpression, tagValuesExpression));  
        break;
      case "exact":
        expressionForValues.append(String.format("%s = %s", entityExpression, tagValuesExpression));
        break;
    }
    return expressionForValues.toString();
  }

  public String assembleAttributesForRule(List<IGovernanceRuleIntermediateEntity> attributes) {
    
    if(attributes != null && !attributes.isEmpty() ) {
      return attributes.stream().map(attribute ->  attribute.getRules().stream()
          .map(attributeRule -> generateAttributeExpression(attributeRule, attribute.getEntityId())).collect(OR_JOIN)).collect(AND_JOIN);
    }
    return null;
  }
  
  
  
  public String generateAttributeExpression(IGovernanceRuleEntityRule rule, String entityId)
  {
    StringBuilder expressionForValues = new StringBuilder();
    
    List<String> values = rule.getValues().stream()
        .map(x -> String.format("'%s'", x))
        .collect(Collectors.toList());
    
    if (rule.getShouldCompareWithSystemDate()) {
      values.add(String.valueOf(System.currentTimeMillis()));
    }
    else if (StringUtils.isNoneEmpty(rule.getAttributeLinkId())) {
      values.add(getElementExpression(CSEObjectType.Property, rule.getAttributeLinkId()));
    }
    
    String type = rule.getType();
    String value = StringUtils.firstNonEmpty(values.toArray(new String[0]));
    String from = rule.getFrom();
    String to = rule.getTo();
    
    String entityExpression = getElementExpression(CSEObjectType.Property, entityId);
    expressionForValues.append(attributeConditionalExpression(value, type, entityExpression));
    expressionForValues.append(nonValueExpressions(entityExpression, type));
    expressionForValues.append(expressionForRange(entityExpression, type, from, to));
    
    return expressionForValues.toString();
  }
  
  public String nonValueExpressions(String entityId)
  {
    StringBuilder expressionForValues = new StringBuilder();
    
    String entityExpression1 = getElementExpression(CSEObjectType.Property, entityId);
    expressionForValues.append("(");
    expressionForValues.append(String.format(" %s <> $null", entityExpression1))/*.append(" or")*/;
/*    expressionForValues.append(String.format(" %s <> ''", entityExpression1));*/
    return expressionForValues.append(")").toString();
  }

  public String nonValueExpressionsForUniqueness(String entityId)
  {
    StringBuilder expressionForValues = new StringBuilder();
    
    String entityExpression1 = getElementExpression(CSEObjectType.Property, entityId);
    expressionForValues.append("unique (");
    expressionForValues.append(String.format(" %s ", entityExpression1));
    return expressionForValues.append(")").toString();
  }
  
  public String assembleEvaluationForCompletenessAndUniqueness(List<IGovernanceRuleIntermediateEntity> attributes, List<IGovernanceRuleTags> tags) {
    
    String evaluationFinalForAttributes = null;
    String evaluationFinalForTags = null;
    
    
    if(attributes != null && !attributes.isEmpty()) {
      String evaluationForAttributes = attributes.stream().map(attribute -> KpiAssembler.instance().nonValueExpressions(attribute.getEntityId())).collect(AND_JOIN);
      evaluationFinalForAttributes = evaluationForAttributes.concat("/" +attributes.size());
    }
    
    if(tags != null && !tags.isEmpty()) {
      String evaluationForTags = tags.stream().map(tag -> KpiAssembler.instance().nonValueExpressions(tag.getEntityId())).collect(AND_JOIN);
      evaluationFinalForTags = evaluationForTags.concat("/" +tags.size());
    }
    
    if(evaluationFinalForAttributes != null && evaluationFinalForTags != null) {
      return String.format("(( %s ) + ( %s ))/2", evaluationFinalForAttributes, evaluationFinalForTags);
    }
    else if(evaluationFinalForAttributes != null && !evaluationFinalForAttributes.isEmpty()) {
      return evaluationFinalForAttributes;
    }
    else {
      return evaluationFinalForTags;
    }
  }
  
public String assembleEvaluationForUniqueness(List<IGovernanceRuleIntermediateEntity> attributes, List<IGovernanceRuleTags> tags) {
    
    String evaluationFinalForAttributes = null;
    String evaluationFinalForTags = null;
    
    
    if(attributes != null && !attributes.isEmpty()) {
      evaluationFinalForAttributes = attributes.stream().map(attribute -> KpiAssembler.instance().nonValueExpressionsForUniqueness(attribute.getEntityId())).collect(AND_JOIN_For_KPI);
    }
    
    if(tags != null && !tags.isEmpty()) {
      evaluationFinalForTags = tags.stream().map(tag -> KpiAssembler.instance().nonValueExpressionsForUniqueness(tag.getEntityId())).collect(AND_JOIN);
    }
    
    if(evaluationFinalForAttributes != null && evaluationFinalForTags != null) {
      return String.format("(( %s ) and ( %s ))", evaluationFinalForAttributes, evaluationFinalForTags);
    }
    else if(evaluationFinalForAttributes != null && !evaluationFinalForAttributes.isEmpty()) {
      return evaluationFinalForAttributes;
    }
    else {
      return evaluationFinalForTags;
    }
  }
}
