package com.cs.core.config.interactor.usecase.assembler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
import com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel;
import com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel;
import com.cs.core.runtime.interactor.model.filter.IFilterValueModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IRuleColorModel;
import com.cs.core.runtime.interactor.model.instancetree.ISpecialUsecaseFiltersModel;
import com.cs.core.runtime.interactor.model.instancetree.RuleColorModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.searchable.IInstanceSearchModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByClassifierFilter.FilterOperator;

@Service
@SuppressWarnings("rawtypes")
public class SearchAssembler extends Assembler {
  
   @Autowired
   TransactionThreadData transactionThreadData;
  
  public String generateExpressionBetweenFilters(IPropertyInstanceFilterModel propertyFilterModel, SuperType superType)
  {
    List<IFilterValueModel> filters = propertyFilterModel.getMandatory();
    StringBuilder expr = new StringBuilder();
    String sep = " or ";
    String id = propertyFilterModel.getId();
    for (IFilterValueModel filter : filters) {
      String type = propertyFilterModel.getType();
      String generateExpression = generateExpression(filter, id, superType, type);
      if (StringUtils.isNotEmpty(generateExpression)) {
        expr.append(generateExpression);
        expr.append(sep);
      }
      
    }
    if (StringUtils.isNotEmpty(expr)) {
      expr.setLength(expr.length() - sep.length());
    }
    return expr.toString();
    
  }
  
  private String generateExpression(IFilterValueModel filter, String entityId, SuperType superType, String type)
  {
    String format = CoreConstant.NUMERIC_TYPE.contains(type) ? "[%s].number" : "[%s]";
    String entityExpression = String.format(format, entityId);
    if (filter instanceof FilterValueMatchModel) {
      FilterValueMatchModel valueCondition = (FilterValueMatchModel) filter;
      String value = valueCondition.getValue();
      String actionType = valueCondition.getType();
      String attributeConditionalExpression = attributeConditionalExpression(String.format("'%s'", value), actionType, entityExpression);
      if (attributeConditionalExpression.isEmpty()) {
        attributeConditionalExpression = nonValueExpressions(String.format("[%s]", entityId), actionType);
      }
      return attributeConditionalExpression;
    }
    else if (filter instanceof FilterValueRangeModel) {
      FilterValueRangeModel valueCondition = (FilterValueRangeModel) filter;
      switch (superType) {
        case ATTRIBUTE:
          return expressionForRange(entityExpression, valueCondition.getType(), String.format("%f", valueCondition.getFrom()),
              String.format("%f", valueCondition.getTo()));
        case TAGS:
          return expressionForTagSearch(entityExpression, valueCondition);
        default:
          return "";
        
      }
    }
    return "";
  }
  
  private String expressionForTagSearch(String entityExpression, FilterValueRangeModel filter)
  {
    StringBuilder expressionForValues = new StringBuilder();
    Double from = filter.getFrom();
    Double to = filter.getTo();
    if (from.equals(to)) {
      expressionForValues.append(String.format("%s = [T>%s $range=%s]", entityExpression, filter.getId(), from.intValue()));
    }
    else {
      String tagValueExpression = String.format("[T>%s]", filter.getId());
      String format = String.format(" (%s > %s and %s < %s) ", tagValueExpression, from, tagValueExpression, to.intValue());
      expressionForValues.append(entityExpression).append(" = ").append(format);
    }
    return expressionForValues.toString();
  }
  
  public String generateExpressionForFilters(List<? extends IPropertyInstanceFilterModel> attributes, SuperType superType)
  {
    String sep = " and ";
    StringBuilder expressions = new StringBuilder();
    for (IPropertyInstanceFilterModel property : attributes) {
      String expression = generateExpressionBetweenFilters(property, superType);
      if (StringUtils.isNotEmpty(expression)) {
        expressions.append(expression);
        expressions.append(sep);
      }
    }
    if (StringUtils.isNotEmpty(expressions)) {
      expressions.setLength(expressions.length() - sep.length());
    }
    
    return expressions.toString();
  }
  
  
  public StringBuilder getBaseQuery(BaseType baseType)
  {
    StringBuilder searchExpression = new StringBuilder();
    // Locale and Base Type Scope Expression
    TransactionData transactionData = transactionThreadData.getTransactionData();
    String baseTypeExpression = baseType.equals(BaseType.UNDEFINED) ? "" : String.format("$basetype = %s", getBaseTypeTokens(baseType));
    searchExpression
        .append(String.format(" select %s $ctlg=%s $org=%s $locale=%s ", baseTypeExpression, transactionData.getPhysicalCatalogId(),
            transactionData.getOrganizationId().equals("-1") ? "$stdo" : transactionData.getOrganizationId(),
            transactionData.getDataLanguage()));
    if(!StringUtils.isEmpty(transactionData.getEndpointId())){
      searchExpression.append(String.format(" $endpoint=%s ", transactionData.getEndpointId()));
    }
    return searchExpression;
    
  }
  
  private String getBaseTypeTokens(BaseType baseType)
  {
    switch (baseType) {
      case ARTICLE:
        return "$article";
      case ASSET:
        return "$asset";
      case SUPPLIER:
        return "$supplier";
      case TARGET:
        return "$target";
      case TEXT_ASSET:
        return "$textasset";
      default:
        return "";
    }
  }
  
  public StringBuilder generateEntityFilterExpression(List<String> selectedKlassIds, List<String> selectedTaxonomyIds, Boolean isPermissionCheck, List<String> majorTaxonomyIds)
  {
    String typesExpression = "";
    String typesExpressionIn = "";
    if (!selectedKlassIds.contains("-1")) {
      typesExpression = fillClassifierExpressions(selectedKlassIds, FilterOperator.is);
      typesExpressionIn = fillClassifierExpressions(selectedKlassIds, FilterOperator.in);
    }
    
    String taxonomyExpression = fillClassifierExpressions(selectedTaxonomyIds, FilterOperator.in);

    if(!taxonomyExpression.isEmpty() && isPermissionCheck){
      if(!majorTaxonomyIds.isEmpty()) {
        String taxonomyExpression1 = fillClassifierExpressions(majorTaxonomyIds, FilterOperator.in, true);
        taxonomyExpression = "("+ taxonomyExpression+" | $empty )";
        taxonomyExpression += " or ("+ taxonomyExpression1+")";
      }
      else {
        taxonomyExpression += " | $empty ";
      }
    }
    
    StringBuilder entityFilterExpression = new StringBuilder();
    if (!StringUtils.isEmpty(typesExpression) && !StringUtils.isEmpty(taxonomyExpression)) {
      entityFilterExpression.append(String.format(" (%s or %s) and %s ", typesExpression, typesExpressionIn, taxonomyExpression));
    }
    else if (!StringUtils.isEmpty(typesExpression)) {
      entityFilterExpression.append(String.format(" (%s or %s) ", typesExpression, typesExpressionIn));
    }
    else if (!StringUtils.isEmpty(taxonomyExpression)) {
      entityFilterExpression.append(String.format(" (%s) ", taxonomyExpression));
    }
    // Data Quality Expression
    /*String dataQuality = getDataQualityExpression(dataModel);
    if(!dataQuality.isEmpty()) {
      if(!entityFilterExpression.toString().isEmpty()) {
        entityFilterExpression.append(" and ");
      }
      entityFilterExpression.append(dataQuality);
    }*/
    return entityFilterExpression;
  }
  
  
  public String getEvaluationExpression(List<? extends IPropertyInstanceFilterModel> attributes,
      List<? extends IPropertyInstanceFilterModel> tags, String allSearch)
  {
    String attributesExpression = generateExpressionForFilters(attributes, SuperType.ATTRIBUTE);
    String tagsExpression = generateExpressionForFilters(tags, SuperType.TAGS);
    if (StringUtils.isNotEmpty(attributesExpression) && StringUtils.isNotEmpty(tagsExpression)) {
      return attributesExpression + " and " + tagsExpression;
    }
    else if (StringUtils.isNotEmpty(attributesExpression)) {
      return attributesExpression;
    }
    else if (StringUtils.isNotEmpty(tagsExpression)) {
      return tagsExpression;
    }
    return "";
  }
  
  private String getDataQualityExpression(IInstanceSearchModel dataModel)
  {
    boolean isRed = dataModel.getIsRed();
    boolean isOrange = dataModel.getIsOrange();
    boolean isYellow = dataModel.getIsYellow();
    boolean isGreen = dataModel.getIsGreen();
    
    if (isRed && isOrange && isYellow && isGreen) {
      return "";
    }
    
    StringBuilder flagExpression = new StringBuilder();
    if (isRed) {
      flagExpression.append("$red");
    }
    if (isYellow) {
      if (!flagExpression.toString().isEmpty()) {
        flagExpression.append(" | ");
      }
      flagExpression.append("$yellow");
    }
    if (isOrange) {
      if (!flagExpression.toString().isEmpty()) {
        flagExpression.append(" | ");
      }
      flagExpression.append("$orange");
    }
    StringBuilder violationExpression = new StringBuilder();
    if (!flagExpression.toString().isEmpty()) {
      violationExpression = new StringBuilder(String.format(" $entity.quality = (%s) ", flagExpression));
      flagExpression.setLength(0);
    }
    if (isGreen) {
      if (!isRed) {
        flagExpression.append("$red");
      }
      if (!isYellow) {
        if (!flagExpression.toString().isEmpty()) {
          flagExpression.append(" | ");
        }
        flagExpression.append("$yellow");
      }
      if (!isOrange) {
        if (!flagExpression.toString().isEmpty()) {
          flagExpression.append(" | ");
        }
        flagExpression.append("$orange");
      }
    }
    if (!flagExpression.toString().isEmpty()) {
      String nonViolationExpression = String.format("not $entity.quality = (%s) ", flagExpression);
      violationExpression.append(nonViolationExpression);
    }
    return violationExpression.toString();
  }
  
  private String getDataQualityExpression(IRuleColorModel ruleColorModel)
  {
    boolean isRed = ruleColorModel.getIsRed();
    boolean isOrange = ruleColorModel.getIsOrange();
    boolean isYellow = ruleColorModel.getIsYellow();
    boolean isGreen = ruleColorModel.getIsGreen();
    
    if (isRed && isOrange && isYellow && isGreen) {
      return "";
    }
    
    StringBuilder flagExpression = new StringBuilder();
    if (isRed) {
      flagExpression.append("$red");
    }
    if (isYellow) {
      if (!flagExpression.toString().isEmpty()) {
        flagExpression.append(" | ");
      }
      flagExpression.append("$yellow");
    }
    if (isOrange) {
      if (!flagExpression.toString().isEmpty()) {
        flagExpression.append(" | ");
      }
      flagExpression.append("$orange");
    }
    StringBuilder violationExpression = new StringBuilder();
    if (!flagExpression.toString().isEmpty()) {
      violationExpression = new StringBuilder(String.format(" $entity.quality = (%s) ", flagExpression));
      flagExpression.setLength(0);
    }
    if (isGreen) {
      if (!isRed) {
        flagExpression.append("$red");
      }
      if (!isYellow) {
        if (!flagExpression.toString().isEmpty()) {
          flagExpression.append(" | ");
        }
        flagExpression.append("$yellow");
      }
      if (!isOrange) {
        if (!flagExpression.toString().isEmpty()) {
          flagExpression.append(" | ");
        }
        flagExpression.append("$orange");
      }
    }
    if (!flagExpression.toString().isEmpty()) {
      String nonViolationExpression = String.format("not $entity.quality = (%s) ", flagExpression);
      violationExpression.append(nonViolationExpression);
    }
    return violationExpression.toString();
  }
  
  public BaseType getBaseTypeByModule(String moduleId)
  {
    switch (moduleId) {
      case Constants.PIM_MODULE:
        return BaseType.ARTICLE;
      case Constants.MAM_MODULE:
        return BaseType.ASSET;
      case Constants.TEXT_ASSET_MODULE:
        return BaseType.TEXT_ASSET;
      case Constants.TARGET_MODULE:
        return BaseType.TARGET;
      case Constants.SUPPLIER_MODULE:
        return BaseType.SUPPLIER;
      default:
        return BaseType.UNDEFINED;
    }
  }
  
  public List<BaseType> getBaseTypeByModule(List<String> moduleIds)
  {
    List<BaseType> basetypes = new ArrayList<BaseType>();
    
    for (String moduleId : moduleIds) {
      basetypes.add(getBaseTypeByModule(moduleId));
    }
    return basetypes;
  } 
  
  @Override
  public String nonValueExpressions(String entityExpression, String type)
  {
    switch (type) {
      case "empty":
        return String.format(" not (%s <> $null) or %s.length = 0 ", entityExpression, entityExpression);
      case "notempty":
        return String.format(" %s <> $null and %s.length > 0", entityExpression, entityExpression);
      case "isunique":
        return String.format(" unique (%s) ", entityExpression);
      default:
        return "";
    }
  }
  
  public StringBuilder getScope(List<String> selectedKlassIds, List<String> selectedTaxonomyIds, BaseType baseTypeByModule)
  {
    StringBuilder searchExpression = getBaseQuery(baseTypeByModule);
    searchExpression.append(generateEntityFilterExpression(selectedKlassIds, selectedTaxonomyIds, false, new ArrayList<>()));
    return searchExpression;
  }
  
  public StringBuilder getScope(INewInstanceTreeRequestModel dataModel, BaseType baseTypeByModule)
  {
    StringBuilder searchExpression = getBaseQuery(baseTypeByModule);
    List<String> taxonomyIds = new ArrayList<>(dataModel.getTaxonomyIdsHavingRP());
    List<String> klassIds = new ArrayList<>(dataModel.getKlassIdsHavingRP());
    //for permission

    StringBuilder permissionExpression = generateEntityFilterExpression(klassIds, taxonomyIds, true, dataModel.getMajorTaxonomyIds());
    StringBuilder entityFilterExpression = generateEntityFilterExpression(dataModel.getSelectedTypes(), dataModel.getSelectedTaxonomyIds(), false, dataModel.getMajorTaxonomyIds());
    if(!entityFilterExpression.toString().isEmpty() && !permissionExpression.toString().isEmpty()){
      entityFilterExpression.append(" and ");
    }
    entityFilterExpression.append(permissionExpression.toString());

    List<ISpecialUsecaseFiltersModel> specialUsecaseFilters = dataModel.getSpecialUsecaseFilters();
    String dataQualityExpression = generateSpecialUsecaseFilterExpression(specialUsecaseFilters);
    if (!entityFilterExpression.toString().isEmpty() && !dataQualityExpression.isEmpty()) {
      entityFilterExpression.append(" and ");
    }
    entityFilterExpression.append(dataQualityExpression);
    searchExpression.append(entityFilterExpression);
    return searchExpression;
  }

  private String generateDataQualityRuleExpression(ISpecialUsecaseFiltersModel iSpecialUsecaseFiltersModel)
  {
    List<String> appliedValues = iSpecialUsecaseFiltersModel.getAppliedValues();
    IRuleColorModel ruleColorModel = new RuleColorModel();
    for (String value : appliedValues) {
      if (value.equals(CommonConstants.RED_VOILATION_FILTER)) {
        ruleColorModel.setIsRed(true);
      }
      else if (value.equals(CommonConstants.ORANGE_VOILATION_FILTER)) {
        ruleColorModel.setIsOrange(true);
      }
      else if (value.equals(CommonConstants.YELLOW_VOILATION_FILTER)) {
        ruleColorModel.setIsYellow(true);
      }
      else if (value.equals(CommonConstants.GREEN_VOILATION_FILTER)) {
        ruleColorModel.setIsGreen(true);
      }
    }
    return getDataQualityExpression(ruleColorModel);
  }

  public String getAssetExpiryFilter(ISpecialUsecaseFiltersModel specialUsecaseFilter)
  {
    StringBuilder expiryFilter = new StringBuilder();
    List<String> appliedValues = specialUsecaseFilter.getAppliedValues();
    if(!appliedValues.containsAll(List.of(CommonConstants.EXPIRED_FILTER,CommonConstants.NON_EXPIRED_FILTER))){
      for(String appliedValue : appliedValues){
        String isExpired = "";
        if(appliedValue.equals(CommonConstants.EXPIRED_FILTER)){
          isExpired =  ICSEElement.Keyword.$true.toString();
        }
        else if(appliedValue.equals(CommonConstants.NON_EXPIRED_FILTER)){
          isExpired = ICSEElement.Keyword.$false.toString();
        }
        if(!StringUtils.isEmpty(isExpired)){
          expiryFilter.append(String.format(" %s.expired = %s ", ICSEElement.Keyword.$entity, isExpired));
        }
      }
    }
    return expiryFilter.toString();
  }

  public String generateSpecialUsecaseFilterExpression(List<ISpecialUsecaseFiltersModel> specialUsecaseFilters)
  {
    StringBuilder specialFilter = new StringBuilder();
    for (ISpecialUsecaseFiltersModel specialUsecaseFilter : specialUsecaseFilters) {
      if (specialUsecaseFilter.getType().equals(CommonConstants.COLOR_VOILATION_FILTER)) {
        String ruleExpression = generateDataQualityRuleExpression(specialUsecaseFilter);
        specialFilter.append(ruleExpression);
      }
      else if(specialUsecaseFilter.getType().equals(CommonConstants.ASSET_EXPIRY_FILTER)){
        specialFilter.append(getAssetExpiryFilter(specialUsecaseFilter));
      }
      else if(specialUsecaseFilter.getType().equals(CommonConstants.DUPLICATE_ASSETS_FILTER)){
        specialFilter.append(getDuplicateAssetFilter(specialUsecaseFilter));
      }
    }
    return specialFilter.toString();
  }
  
  /**
   * Get searchExpression for isDuplicate
   * @param specialUsecaseFilter
   * @return
   */
  public String getDuplicateAssetFilter(ISpecialUsecaseFiltersModel specialUsecaseFilter)
  {
    StringBuilder duplicateFilter = new StringBuilder();
    List<String> appliedValues = specialUsecaseFilter.getAppliedValues();
    for (String appliedValue : appliedValues) {
      String isDuplicate = "";
      if (appliedValue.equals(CommonConstants.DUPLICATE)) {
        isDuplicate = ICSEElement.Keyword.$true.toString();
      }
      if (!StringUtils.isEmpty(isDuplicate)) {
        duplicateFilter.append(String.format(" %s.duplicate = %s ", ICSEElement.Keyword.$entity, isDuplicate));
      }
    }
    return duplicateFilter.toString();
  }
}
