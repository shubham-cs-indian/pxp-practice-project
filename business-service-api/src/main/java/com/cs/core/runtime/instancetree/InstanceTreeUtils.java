package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.data.Text;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.NewApplicableFilterModel;

@Component
public class InstanceTreeUtils {
  
  @Autowired
  private SearchAssembler searchAssembler;
  
  public static String getModuleIdByEntityId(String module)
  {
    switch(module) {
      case CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY :
         return Constants.PIM_MODULE;
      
      case CommonConstants.ASSET_INSTANCE_MODULE_ENTITY :
        return Constants.MAM_MODULE;
        
      case CommonConstants.SUPPLIER_INSTANCE_MODULE_ENTITY :
        return Constants.SUPPLIER_MODULE;
        
      case CommonConstants.MARKET_INSTANCE_MODULE_ENTITY:
        return Constants.TARGET_MODULE;
        
      case CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
        return Constants.TEXT_ASSET_MODULE;
        
      default: return null;
    }
  }
  
  public static List<String> getModuleIdByEntityId(List<String> modules)
  {
    List<String> moduleIds = new ArrayList<String>();
    
    for (String module : modules) {
      moduleIds.add(getModuleIdByEntityId(module));
    }
    return moduleIds;
  } 
  
  public static BaseType getBaseTypeByModultId(String moduleId)
  {
    switch (moduleId) {
      case Constants.PIM_MODULE:
        return BaseType.ARTICLE;
      case Constants.MAM_MODULE:
        return BaseType.ASSET;
      case Constants.SUPPLIER_MODULE:
        return BaseType.SUPPLIER;
      case Constants.TARGET_MODULE:
        return BaseType.TARGET;
      case Constants.TEXT_ASSET_MODULE:
        return BaseType.TEXT_ASSET;
      default:
        return null;
    }
  }
  
  public String generateSearchExpression(INewInstanceTreeRequestModel dataModel)
  {
    String moduleId = dataModel.getModuleId();
    StringBuilder searchExpression = searchAssembler.getScope(dataModel, searchAssembler.getBaseTypeByModule(moduleId));

    searchAssembler.generateEntityFilterExpression(List.copyOf(dataModel.getKlassIdsHavingRP()), List.copyOf(dataModel.getTaxonomyIdsHavingRP()), true, dataModel.getMajorTaxonomyIds());
    String evaluationExpression = evaluationExpressionForAttributeAndTags(dataModel);
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ")
          .append(evaluationExpression);
    }
    return searchExpression.toString();
  }

  @SuppressWarnings("rawtypes")
  public String evaluationExpressionForAttributeAndTags(INewInstanceTreeRequestModel dataModel)
  {
    String allSearch = dataModel.getAllSearch();
    List<IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    List<IPropertyInstanceFilterModel> tags = dataModel.getTags();
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, tags, allSearch );
    if (!allSearch.isEmpty()) {
      String join = String.format(" [P>search] contains %s ", Text.escapeStringWithQuotes(allSearch));
      String searchEpxression = String.format("(%s)", join);
      
      if (!join.isEmpty() && !evaluationExpression.isEmpty()) {
        evaluationExpression = evaluationExpression + " and " + searchEpxression;
      }
      else if (!join.isEmpty()) {
        evaluationExpression = searchEpxression;
      }
    }
    return evaluationExpression;
  }
  
  public String generateSearchExpressionForTaxonomy(INewInstanceTreeRequestModel dataModel, String moduleId)
  {
    StringBuilder searchExpression = searchAssembler.getBaseQuery(searchAssembler.getBaseTypeByModule(moduleId));
    StringBuilder permission = searchAssembler.generateEntityFilterExpression(List.copyOf(dataModel.getKlassIdsHavingRP()), List.copyOf(dataModel.getTaxonomyIdsHavingRP()), true, dataModel.getMajorTaxonomyIds());
    if(!permission.toString().isEmpty()){
      searchExpression.append(permission);
    }
    String evaluationExpression = evaluationExpressionForAttributeAndTags(dataModel);
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ").append(evaluationExpression);
    }
    return searchExpression.toString();
  }
  
  public void addRuleViolationFilter(Boolean isFilterDataRequired, List<INewApplicableFilterModel> filterData)
  {
    if (!isFilterDataRequired) {
      return;
    }
    INewApplicableFilterModel dataRuleVoilation = new NewApplicableFilterModel();
    dataRuleVoilation.setId(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setCode(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setType(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setPropertyType(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setLabel("Rule Violations");
    filterData.add(dataRuleVoilation);
  }
}
