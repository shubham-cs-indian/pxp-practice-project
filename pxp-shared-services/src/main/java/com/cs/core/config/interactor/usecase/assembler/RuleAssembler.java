package com.cs.core.config.interactor.usecase.assembler;

import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedHtmlOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.datarule.*;
import com.cs.core.config.interactor.model.attribute.AttributeValueNormalization;
import com.cs.core.config.interactor.model.datarule.*;
import com.cs.core.data.Text;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByClassifierFilter.FilterOperator;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.cs.core.config.interactor.usecase.assembler.RuleAssembler.NumericOperations.numericOperations;

public class RuleAssembler extends Assembler {
  
  // Singleton implementation
  private static final RuleAssembler INSTANCE = new RuleAssembler();

  public enum NumericOperations {
    lt,gt,lte,gte,length_lt,length_gt, length_equal, range;
    public static List<String> numericOperations = Arrays.stream(values()).map(x -> x.name()).collect(Collectors.toList());
  }
  private RuleAssembler()
  {
  }
  
  public static RuleAssembler instance()
  {
    return INSTANCE;
  }
  
  Collector<CharSequence, ?, String> OR_JOIN  = Collectors.joining(" or ", "(", ")");
  Collector<CharSequence, ?, String> AND_JOIN = Collectors.joining(" and ", "(", ")");
  
  /**
   * Assemble Scope For Rule
   * 
   * @param natureClass nature class in rule
   * @param types types in rule
   * @param taxonomies taxonomies in rule
   * @param joining the string on which the join is required.
   * @return expression for scope
   */
  public String scopeForClassifiers(String natureClass, List<String> types, List<String> taxonomies)
  {
    List<String> classifiers = new ArrayList<>();
    if (!natureClass.isEmpty()) {
      classifiers.add(fillClassExpressions(natureClass, types));
    }
    classifiers.add(fillClassifierExpressions(taxonomies, FilterOperator.in));
    classifiers.removeAll(Collections.singletonList(""));
    return Text.join(Operator.Land.getSymbol(), classifiers, " (%s) ");
  }
  
  private String fillClassExpressions(String natureClass, List<String> types)
  {
    StringBuilder classes = new StringBuilder();
    if (!natureClass.isEmpty()) {
      String natureClassExpr = getEntityExpression(FilterOperator.is, getElementExpression(CSEObjectType.Classifier, natureClass), false);
      String classesExpression = fillClassifierExpressions(types, FilterOperator.in);
      if (!natureClassExpr.isEmpty()) {
        classes.append(natureClassExpr);
        if (!classesExpression.isEmpty()) {
          classes.append(String.format(" %s ", Operator.Lor.getSymbol()));
        }
      }
      classes.append(classesExpression);
    }
    return classes.toString();
  }
  
  private String generateAttributeExpression(IDataRuleEntityRule rule, String entityId)
  {
    List<String> values = new ArrayList<>(rule.getValues());
    if (!numericOperations.contains(rule.getType())) {
      values.clear();
      values.addAll(rule.getValues().stream().map(x -> String.format("'%s'", x)).collect(Collectors.toList()));
    }

    if (rule.getShouldCompareWithSystemDate()) {
      values.add(String.valueOf(System.currentTimeMillis()));
    }
    else if (StringUtils.isNoneEmpty(rule.getAttributeLinkId())) {
      values.add(getElementExpression(CSEObjectType.Property, rule.getAttributeLinkId()));
    }
    
    String collect = values.stream().map(inValue -> generateAttributeValueExpression(rule, entityId, inValue)).collect(OR_JOIN);
    
    if (values.isEmpty()) {
      return generateAttributeValueExpression(rule, entityId, "");
    }
    return collect;
  }
  
  private String generateAttributeValueExpression(IDataRuleEntityRule rule, String entityId, String value)
  {
    
    StringBuilder expressionForValues = new StringBuilder();
    String type = rule.getType();
    // String value = StringUtils.firstNonEmpty(values.toArray(new String[0]));
    String from = rule.getFrom();
    String to = rule.getTo();
    
    String entityExpression = getElementExpression(CSEObjectType.Property, entityId);
    expressionForValues.append(attributeConditionalExpression(value, type, entityExpression));
    expressionForValues.append(nonValueExpressions(entityExpression, type));
    expressionForValues.append(expressionForRange(entityExpression, type, from, to));
    
    return expressionForValues.toString();
  }
  
  private String assembleAttributesForRule(List<IDataRuleIntermediateEntitys> attributes)
  {
    
    String collect = attributes.stream()
        .map(betweenAttributes -> betweenAttributes.getRules().stream()
            .map(inAttribute -> generateAttributeExpression(inAttribute, betweenAttributes.getEntityId())).collect(OR_JOIN))
        .collect(AND_JOIN);
    return collect.equals("()") ? "" : collect;
  }
  
  private String generateTagExpression(IDataRuleTagRule dataRule, String entityId)
  {
    String entityExpression = String.format("[%s]", entityId);
    String type = dataRule.getType();
    
    List<IDataRuleTagValues> tagValues = dataRule.getTagValues();
    
    String tagValuesExpression = tagValues.stream().filter(tag -> !tag.getFrom().equals(0l))
        .map(tag -> String.format("[T>%s $range=%s]", tag.getId(), tag.getFrom())).collect(Collectors.joining(",", "{", "}"));
    
    return getTagConditionExpression(entityExpression, type, tagValuesExpression);
  }
  
  public String assembleTagForRules(List<IDataRuleTags> tags)
  {
    String collect = tags.stream().map(betweenTags -> betweenTags.getRules().stream()
        .map(inTag -> generateTagExpression(inTag, betweenTags.getEntityId())).collect(OR_JOIN)).collect(AND_JOIN);
    return collect.equals("()") ? "" : collect;
  }
  
  public String assembleEvaluation(List<IDataRuleIntermediateEntitys> attributes, List<IDataRuleTags> tags)
  {
    String attributeConditionExpression = assembleAttributesForRule(attributes);
    String tagConditionExpression = assembleTagForRules(tags);
    if (attributeConditionExpression.isEmpty()) {
      return tagConditionExpression;
    }
    if (tagConditionExpression.isEmpty()) {
      return attributeConditionExpression;
    }
    return Text.join(Operator.Land.getSymbol(), attributeConditionExpression, tagConditionExpression);
    
  }
  
  /*************************************************
   * ACTIONS
   ***********************************************************************/
  
  /**
   * Assemble all the actions that may be required to be performed for given
   * rule
   * 
   * @param responseDataRuleModel
   * @return Expression for actions of the assembled rule
   */
  public String assembleActions(IDataRuleModel responseDataRuleModel)
  {
    StringBuilder effects = new StringBuilder();
    String assembleNormalization = assembleNormalization(responseDataRuleModel.getNormalizations());
    if (!assembleNormalization.isEmpty()) {
      effects.append(assembleNormalization);
    }
    
    String violations = assembleViolations(responseDataRuleModel.getRuleViolations());
    if (!violations.isEmpty()) {
      if (!effects.toString().isEmpty()) {
        effects.append(",");
      }
      effects.append(violations);
    }
    return effects.toString();
  }
  
  private String assembleViolations(List<IRuleViolationEntity> list)
  {
    StringBuilder violationRule = new StringBuilder();
    Iterator<IRuleViolationEntity> ruleViolations = list.iterator();
    while (ruleViolations.hasNext()) {
      IRuleViolationEntity next = ruleViolations.next();
      String color = next.getColor();
      String description = next.getDescription();
      description = !description.isEmpty() ? String.format("('%s')", description) : description;
      String entityId = next.getEntityId();
      String violation = String.format("[%s] >> $%s %s", entityId, color, description);
      violationRule.append(violation);
      if (ruleViolations.hasNext()) {
        violationRule.append(",");
      }
    }
    return violationRule.toString();
  }
  
  private String assembleNormalization(List<? extends INormalization> list)
  {
    Iterator<? extends INormalization> normalizations = list.iterator();
    StringBuilder normalizationRule = new StringBuilder();
    while (normalizations.hasNext()) {
      INormalization normalization = normalizations.next();
      String type = normalization.getType();
      switch (type) {
        case "attribute":
          normalizeAttribute(normalizationRule, normalization);
          if (normalizations.hasNext()) {
            normalizationRule.append(",");
          }
          break;
        
        case "tag":
          normalizeTags(normalizationRule, normalization);
          if (normalizations.hasNext()) {
            normalizationRule.append(",");
          }
          break;
        
        case "type":
        case "taxonomy":
          normalizeClassifier(normalizationRule, normalization);
          if (normalizations.hasNext() && !normalization.getValues().isEmpty()) {
            normalizationRule.append(",");
          }
          break;
        default:
          break;
      }
    }
    return normalizationRule.toString();
  }
  
  private void normalizeClassifier(StringBuilder normalizationRule, INormalization normalization)
  {
    Iterator<String> values = normalization.getValues().iterator();
    while (values.hasNext()) {
      normalizationRule.append(applyClassification(values.next()));
      if (values.hasNext()) {
        normalizationRule.append(",");
      }
    }
  }
  
  private void normalizeTags(StringBuilder normalizationRule, INormalization normalization)
  {
    String propertyExpression = normalization.getEntityId();//getElementExpression(CSEObjectType.TagValue, normalization.getEntityId());
    String assembledTag = fillTagValueForNormalisation(normalization, propertyExpression);
    String expression = String.format("[%s] := %s ", propertyExpression, assembledTag);
    normalizationRule.append(expression);
  }
  
  private String fillTagValueForNormalisation(INormalization normalization, String propertyExpression)
  {
    List<IDataRuleTagValues> tagValues = normalization.getTagValues();
    String transformationType = normalization.getTransformationType();
    String normalizationString = "";
    switch(transformationType) {
      case "add":
      case "remove":
        normalizationString = String.format("%s([%s],%s)", transformationType, propertyExpression, assembleTagValues(tagValues));
        break;
      case "replacewith":
        normalizationString = assembleTagValues(tagValues);
        break;
    }
    return normalizationString;
  }

  protected StringBuilder createExpression(List<IConcatenatedOperator> attributeConcatenatedList)
  {
    
    StringBuilder mathExpression = new StringBuilder();
    for (IConcatenatedOperator concatenatedAttributeOperator : attributeConcatenatedList) {
      
      if (mathExpression.length() != 0) {
        mathExpression.append("||");
      }
      
      String type = concatenatedAttributeOperator.getType();
      switch (type) {
        
        case "attribute":
          IConcatenatedAttributeOperator attributeOperator = (IConcatenatedAttributeOperator) concatenatedAttributeOperator;
          mathExpression.append("[" + attributeOperator.getAttributeId() + "]");
          break;
        
        case "html":
          IConcatenatedHtmlOperator attributeOperatorForHtml = (IConcatenatedHtmlOperator) concatenatedAttributeOperator;
          String valueAsHtml = attributeOperatorForHtml.getValueAsHtml();
          String value = attributeOperatorForHtml.getValue();
          String valueExpression = valueAsHtml != null ? valueAsHtml : value;
          mathExpression.append("'" + valueExpression + "'");
          break;
        
        case "tag":
          IConcatenatedTagOperator tagOperator = (IConcatenatedTagOperator) concatenatedAttributeOperator;
          mathExpression.append("[" + tagOperator.getTagId() + "]");
          break;
      }
    }
    return mathExpression;
  }
  
  private void normalizeAttribute(StringBuilder normalizationRule, INormalization normalization)
  {
    String propertyExpression = getElementExpression(CSEObjectType.Property, normalization.getEntityId());
    String valueExpression = fillValueForExpression(normalization, propertyExpression);
    String expression = String.format(" %s := %s ", propertyExpression, valueExpression);
    normalizationRule.append(expression);
  }
  
  private String fillValueForExpression(INormalization normalization, String propertyExpression)
  {
    if (normalization instanceof AttributeValueNormalization) {
      AttributeValueNormalization attributeValueNorm = (AttributeValueNormalization) normalization;
      return getElementExpression(CSEObjectType.Property, attributeValueNorm.getValueAttributeId());
    }
    else if (normalization instanceof ConcatenatedNormalization) {
      IConcatenatedNormalization attributeValueNorm = (ConcatenatedNormalization) normalization;
      List<IConcatenatedOperator> attributeConcatenatedList = attributeValueNorm.getAttributeConcatenatedList();
      return createExpression(attributeConcatenatedList).toString();
    }
    else if (normalization instanceof FindReplaceNormalization) {
      IFindReplaceNormalizatiom findReplaceNorm = (FindReplaceNormalization) normalization;
      String findText = findReplaceNorm.getFindText();
      String replaceText = findReplaceNorm.getReplaceText();
      return String.format(" replace(%s,'%s','%s') ", propertyExpression, findText, replaceText);
    }
    else if (normalization instanceof SubStringNormalization) {
      ISubStringNormalization subStringNorm = (SubStringNormalization) normalization;
      Integer startIndex = subStringNorm.getStartIndex();
      Integer endIndex = subStringNorm.getEndIndex();
      
      return String.format(" substring(%s,%d,%d) ", propertyExpression, startIndex, endIndex);
      
    }
    else if (normalization instanceof Normalization) {
      Normalization attributeValueNorm = (Normalization) normalization;
      String transformationType = attributeValueNorm.getTransformationType();
      String valueInRule = attributeValueNorm.getValueAsHTML().isEmpty() ? (attributeValueNorm.getValues().isEmpty() ? "" : attributeValueNorm.getValues().get(0))
          : attributeValueNorm.getValueAsHTML();
      String literalValue = String.format("'%s'", valueInRule);
      String transformation = "";
      switch (transformationType) {
        case "lowercase":
          transformation = "lower";
          break;
        
        case "uppercase":
          transformation = "upper";
          break;
        
        case "propercase":
          transformation = "proper";
          break;
        
        case "trim":
          transformation = "trim";
          break;
        
        default:
          break;
      }
      return !StringUtils.isEmpty(transformation) ? String.format("%s(%s)", transformation, propertyExpression) : literalValue;
    }
    return "";
  }
  
  private String assembleTagValues(List<IDataRuleTagValues> tagValues)
  {
    StringBuilder tagValuesAssembler = new StringBuilder();
    Iterator<IDataRuleTagValues> iterator = tagValues.iterator();
    while (iterator.hasNext()) {
      if (tagValuesAssembler.toString().isEmpty()) {
        tagValuesAssembler.append("{");
      }
      
      IDataRuleTagValues tagValue = iterator.next();
      Long range = tagValue.getFrom();
      
      tagValuesAssembler.append(String.format("[T>%s $range=%s]", tagValue.getInnerTagId(), range));
      
      if (iterator.hasNext()) {
        tagValuesAssembler.append(",");
      }
      else {
        tagValuesAssembler.append("}");
      }
    }
    return tagValuesAssembler.toString();
  }
  
  private static final String ENTITY   = "$entity";
  private static final String CLASSIFY = " => ";
  
  private String applyClassification(String value)
  {
    return ENTITY + CLASSIFY + "[c>" + value + "]";
  }
  
  public String assembleRule(String scope, String evalution, String actions)
  {
    StringBuilder rule = new StringBuilder();
    
    rule.append("for ");
    rule.append(scope);
    if (!evalution.isEmpty()) {
      rule.append(" when ");
      rule.append(evalution);
    }
    if (!actions.isEmpty()) {
      rule.append(" then ");
      rule.append(actions);
    }
    return rule.toString();
  }
}
