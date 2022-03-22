package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.DataRuleDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IDataRuleDeleteDTO;
import com.cs.core.config.interactor.entity.governancerule.*;
import com.cs.core.config.interactor.model.governancerule.*;
import com.cs.core.config.interactor.usecase.assembler.KpiAssembler;
import com.cs.core.config.interactor.usecase.assembler.RuleAssembler;
import com.cs.core.config.physicalcatalog.util.PhysicalCatalogUtils;
import com.cs.core.config.strategy.usecase.governancerule.ISaveKeyPerformanceIndexStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class SaveKeyPerformanceIndexService extends AbstractSaveConfigService<ISaveKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel>
    implements ISaveKeyPerformanceIndexService {
  

  private static final String SERVICE = "SAVE_KEY_PERFORMANCE_INDEX";

  @Autowired
  protected ISaveKeyPerformanceIndexStrategy saveKeyPerformanceIndexStrategy;
  
  @Autowired
  protected KpiValidations                   kpiValidations;
  
  @Override
  public IGetKeyPerformanceIndexModel executeInternal(ISaveKeyPerformanceIndexModel dataModel)
      throws Exception
  {
    kpiValidations.validate(dataModel);
    
    dataModel.setAvailablePhysicalCatalogIds(PhysicalCatalogUtils.getAvailablePhysicalCatalogs());
    List<IGovernanceRule> addedRules = dataModel.getAddedRules();
    
    for(IGovernanceRule addedrule: addedRules) {
      String code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.KPI.getPrefix());
      addedrule.setCode(code);
    }
    
    ISaveKPIResponseModel returnModel = saveKeyPerformanceIndexStrategy.execute(dataModel);
    Map<String, IGovernanceRuleBlock> referencedRules = returnModel.getStrategyResponse().getReferencedRules();
    Map<String,String> ruleExpressionIIds = new HashMap<>();
    Map<String, String> modifiedAttributesMap = new HashMap<>();
    Map<String, IGovernanceRule> modifiedRulesMap = new HashMap<>();
    
    IKeyPerformanceIndicator kpi = returnModel.getStrategyResponse().getKeyPerformanceIndex();
    ITargetFilters targetFilters = kpi.getTargetFilters();
    
    IModifiedTargetFiltersModel modifiedTargetFilters = dataModel.getModifiedTargetFilters();
    
    List<String> klassIds = new ArrayList<>(targetFilters.getKlassIds());
    klassIds.addAll(modifiedTargetFilters.getDeletedKlassIds());
    
    List<String> taxonomyIds = new ArrayList<>(targetFilters.getTaxonomyIds());
    taxonomyIds.addAll(modifiedTargetFilters.getDeletedTaxonomyIds());
    
    Set<String> ruleCodes = new HashSet<>();
    
    fillmodifiedRuleMap(referencedRules, ruleExpressionIIds, modifiedAttributesMap, modifiedRulesMap);
    List<IModifiedKPIRuleModel> modifiedRules = dataModel.getModifiedRules();
    List<String> deletedRules = dataModel.getDeletedRules();
    
    handleModifiedTargetFilters(returnModel, ruleExpressionIIds, modifiedRulesMap, modifiedTargetFilters, ruleCodes);
    
    handleDeletedKPIRules(ruleExpressionIIds, ruleCodes, deletedRules);
    
    handleModifiedKPIRules(returnModel, ruleExpressionIIds, modifiedRulesMap, ruleCodes, modifiedRules);
    
    handleAddedKPIRules(addedRules, returnModel, ruleExpressionIIds, modifiedRulesMap, ruleCodes);
      
    IGetKeyPerformanceIndexModel keyPerformanceIndexModel = returnModel.getStrategyResponse();
    keyPerformanceIndexModel.setAuditLogInfo(returnModel.getAuditLogInfo());
    
    if ((!klassIds.isEmpty() || !taxonomyIds.isEmpty()) && isPropertiesChanged(dataModel, returnModel)) {
      IDataRuleDeleteDTO ruleDTO = new DataRuleDeleteDTO();
      ruleDTO.setRuleCodes(new HashSet<>(ruleCodes));
      ruleDTO.setCatalogIds(new HashSet<>(dataModel.getPhysicalCatalogIds()));
      ruleDTO.setKlassIds(new HashSet<>(klassIds));
      ruleDTO.setTaxonomyIds(new HashSet<>(taxonomyIds));
      
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), SERVICE, "", userPriority,
          new JSONContent(ruleDTO.toJSON()));
    }
    
    return keyPerformanceIndexModel;
  }

  private void handleModifiedTargetFilters(ISaveKPIResponseModel returnModel, Map<String, String> ruleExpressionIIds,
      Map<String, IGovernanceRule> modifiedRulesMap, IModifiedTargetFiltersModel modifiedTargetFilters, Set<String> ruleCode)
      throws RDBMSException, CSFormatException
  {
    if (isModifiedTargetFiltersValid(returnModel, modifiedTargetFilters)) {
      for (String ruleId : modifiedRulesMap.keySet()) {
        String ruleExpression = createRuleExpression(returnModel, modifiedRulesMap, ruleId);
        String ruleExpressionIID = null;
        fillRuleCodeAndUpdateRule(ruleExpressionIIds, ruleCode, ruleId, ruleExpression, ruleExpressionIID);
      }
    }
  }

  private void handleAddedKPIRules(List<IGovernanceRule> addedRules, ISaveKPIResponseModel returnModel, Map<String, String> ruleExpressionIIds,
      Map<String, IGovernanceRule> modifiedRulesMap, Set<String> ruleCode) throws RDBMSException, CSFormatException
  {
    for (IGovernanceRule addedRule : addedRules) {
      if (!addedRule.getAttributes().isEmpty() || !addedRule.getTags().isEmpty()) {
        String ruleId = addedRule.getCode();
        String ruleExpression = createRuleExpression(returnModel, modifiedRulesMap, ruleId);
        String ruleExpressionIID = null;
        fillRuleCodeAndUpdateRule(ruleExpressionIIds, ruleCode, ruleId, ruleExpression, ruleExpressionIID);
      }
    }
  }

  private void handleModifiedKPIRules(ISaveKPIResponseModel returnModel, Map<String, String> ruleExpressionIIds,
      Map<String, IGovernanceRule> modifiedRulesMap, Set<String> ruleCode, List<IModifiedKPIRuleModel> modifiedRules)
      throws RDBMSException, CSFormatException
  {
    for (IModifiedKPIRuleModel modifiedRule : modifiedRules) {
      String ruleId = modifiedRule.getId();
      String ruleExpression = createRuleExpression(returnModel, modifiedRulesMap, ruleId);
      String ruleExpressionIID = null;
      fillRuleCodeAndUpdateRule(ruleExpressionIIds, ruleCode, ruleId, ruleExpression, ruleExpressionIID);
    }
  }

  private void handleDeletedKPIRules(Map<String, String> ruleExpressionIIds, Set<String> ruleCode, List<String> deletedRules)
      throws RDBMSException, CSFormatException
  {
    for (String deletedRule : deletedRules) {
      String ruleExpressionIID = null;
      if (deletedRule.contains(IStandardConfig.UniquePrefix.KPI.getPrefix())) {
        ruleExpressionIID = deletedRule.replace(IStandardConfig.UniquePrefix.KPI.getPrefix(), "");
      }
      ConfigurationDAO.instance().upsertRule(ruleExpressionIIds.get(deletedRule), Long.parseLong(ruleExpressionIID), "",
          RuleType.kpi);
    }
  }

  private boolean isModifiedTargetFiltersValid(ISaveKPIResponseModel returnModel, IModifiedTargetFiltersModel modifiedTargetFilters)
  {
    return !modifiedTargetFilters.getAddedKlassIds().isEmpty() || !modifiedTargetFilters.getAddedTaxonomyIds().isEmpty()
        || !modifiedTargetFilters.getDeletedKlassIds().isEmpty() || !modifiedTargetFilters.getDeletedTaxonomyIds().isEmpty()
        || returnModel.getSaveKPIDiff().getCatalogChangedStatus();
  }

  private String createRuleExpression(ISaveKPIResponseModel returnModel,
      Map<String, IGovernanceRule> modifiedRulesMap, String ruleId)
  {
    IGovernanceRule governanceRule = modifiedRulesMap.get(ruleId);
    Collector<CharSequence, ?, String> AND_JOIN = Collectors.joining(" + "," ( ", " ) ");
    String evaluation = null;
    
    if(governanceRule.getType().equals("completeness")) {
      
      List<IGovernanceRuleIntermediateEntity> attributes = governanceRule.getAttributes();
      List<IGovernanceRuleTags> tags = governanceRule.getTags();
      evaluation = KpiAssembler.instance().assembleEvaluationForCompletenessAndUniqueness(attributes, tags);
     
    }else if(governanceRule.getType().equals("uniqueness")) {
      
      List<IGovernanceRuleIntermediateEntity> attributes = governanceRule.getAttributes();
      List<IGovernanceRuleTags> tags = governanceRule.getTags();
      evaluation = KpiAssembler.instance().assembleEvaluationForUniqueness(attributes, tags);
      
    }
    else {
      List<IGovernanceRuleIntermediateEntity> attributes = governanceRule.getAttributes();
      List<IGovernanceRuleTags> tags = governanceRule.getTags();
      evaluation = KpiAssembler.instance().assembleEvaluation(attributes, tags);
    }
     
    String scope = assembleScope(returnModel);
    if(scope == null || evaluation == null) {
      return null;
    }
    String ruleExpression = RuleAssembler.instance().assembleRule(scope, evaluation, "");
    
    return ruleExpression;
    
  }
  
  private boolean isPropertiesChanged(ISaveKeyPerformanceIndexModel dataModel, ISaveKPIResponseModel returnModel) {
    Boolean isPropagationNeeded = false;
    if (!shouldPropagate(dataModel)) {
 
      if (!dataModel.getModifiedRules().isEmpty()) {
        for (IModifiedKPIRuleModel data : dataModel.getModifiedRules()) {
          if (!data.getAddedAttributeRules().isEmpty()
              || !data.getAddedTagRules().isEmpty()
              || !data.getModifiedAttributeRules().isEmpty() 
              || !data.getModifiedTagRules().isEmpty()
              || !data.getDeletedAttributeRules().isEmpty() 
              || !data.getDeletedTagRules().isEmpty()) {
            isPropagationNeeded = true;
          }
       }
      }
 
      if (!dataModel.getAddedRules().isEmpty()) {
        for (IGovernanceRule data : dataModel.getAddedRules()) {
          if (!data.getAttributes().isEmpty() || !data.getTags().isEmpty()) {
            isPropagationNeeded = true;
          }
        }
      }
   /**
    * Required in case of: 
    * Steps: 
    * 1-Create KPI, add rule with attributes or tags (without adding class). 
    * 2-Create a content of any class(singaleArticle). 
    * 3-Update KPI - add/remove class(singaleArticle) and save.
    * Same for taxonomy. 
    * Existing content should be update when adding/removing a class from KPI.
    */
   IModifiedTargetFiltersModel modifiedTargetFilters = dataModel.getModifiedTargetFilters();
   
   if ((!modifiedTargetFilters.getAddedKlassIds().isEmpty() 
       || !modifiedTargetFilters.getAddedTaxonomyIds().isEmpty()
       || !modifiedTargetFilters.getDeletedKlassIds().isEmpty()
       || !modifiedTargetFilters.getDeletedTaxonomyIds().isEmpty())
       && returnModel.getSaveKPIDiff().getIsPropertyExist()) {
     isPropagationNeeded = true;
   }
   else if (!dataModel.getDeletedRules().isEmpty() && returnModel.getSaveKPIDiff().getIsPropertyExistInDeletedRule()) {
     isPropagationNeeded = true;
   }
    } else {
      isPropagationNeeded = true;
    }
 
    if (returnModel.getSaveKPIDiff().getCatalogChangedStatus()) {
      isPropagationNeeded = true;
    }
 
    return isPropagationNeeded;
  }
      private Boolean shouldPropagate(ISaveKeyPerformanceIndexModel dataModel)
    {
      return !dataModel.getAddedEndpoints()
              .isEmpty()
          
         || !dataModel.getAddedOrganizationIds()
             .isEmpty()
          || !dataModel.getAddedRoles()
              .isEmpty()
          
          || !dataModel.getAddedTags()
              .isEmpty()
          || !dataModel.getDeletedEndpoints()
              .isEmpty()
          
          || !dataModel.getDeletedTags()
              .isEmpty()
          || !dataModel.getDeletedOrganizationIds()
              .isEmpty()
          
          || !dataModel.getModifiedDrillDowns()
              .isEmpty()
          || !dataModel.getModifiedGovernanceRuleBlocks()
              .isEmpty()
          
          || !dataModel.getModifiedRoles()
              .isEmpty()
         || !dataModel.getModifiedTags()
              .isEmpty();
    }    
  
  private void fillmodifiedRuleMap(Map<String, IGovernanceRuleBlock> referencedRules,
      Map<String, String> ruleExpressionIIds, Map<String, String> modifiedAttributesMap,
      Map<String, IGovernanceRule> modifiedRulesMap)
  {
    for(Entry<String, IGovernanceRuleBlock> referencedRule: referencedRules.entrySet()) {
      IGovernanceRuleBlock governanceRuleBlock = referencedRule.getValue();
      String ruleExpressionIID = governanceRuleBlock.getId();
      List<IGovernanceRule> rules = governanceRuleBlock.getRules();
      
      for(IGovernanceRule governanceBlock: rules) {
        ruleExpressionIIds.put(governanceBlock.getCode(), ruleExpressionIID);
        modifiedRulesMap.put(governanceBlock.getCode(), governanceBlock);
        List<IGovernanceRuleIntermediateEntity> attributes = governanceBlock.getAttributes();
        
        for(IGovernanceRuleIntermediateEntity governanceRuleIntermediateEntity: attributes) {
          modifiedAttributesMap.put(governanceRuleIntermediateEntity.getCode(), governanceRuleIntermediateEntity.getEntityId());
        }
      }
    }
  }
  
  private void fillRuleCodeAndUpdateRule(Map<String, String> ruleExpressionIIds, Set<String> ruleCode, String ruleId, String ruleExpression,
      String ruleExpressionIID) throws RDBMSException, CSFormatException
  {
    if (ruleId.contains(IStandardConfig.UniquePrefix.KPI.getPrefix())) {
      ruleExpressionIID = ruleId.replace(IStandardConfig.UniquePrefix.KPI.getPrefix(), "");
    }
    ConfigurationDAO.instance().upsertRule(ruleExpressionIIds.get(ruleId), Long.parseLong(ruleExpressionIID), ruleExpression,
        RuleType.kpi);
    if(!StringUtils.isEmpty(ruleExpression)) {
      ruleCode.add(ruleExpressionIIds.get(ruleId));
    }
  }
  
  public String assembleScope(ISaveKPIResponseModel returnModel) {
    
    IKeyPerformanceIndicator keyPerformanceIndex = returnModel.getStrategyResponse().getKeyPerformanceIndex();
    ITargetFilters targetFilters = keyPerformanceIndex.getTargetFilters();
    if(!targetFilters.getKlassIds().isEmpty() || !targetFilters.getTaxonomyIds().isEmpty()) {
      
      Map<String, IConfigEntityInformationModel> referencedKlasses = returnModel.getStrategyResponse().getReferencedKlasses();
      
      StringBuilder scope = new StringBuilder();
      String natureClass = getNatureClass(referencedKlasses);
      scope.append(RuleAssembler.instance().scopeForCatalog(keyPerformanceIndex.getPhysicalCatalogIds()));
      scope.append(RuleAssembler.instance().scopeForOrganizations(keyPerformanceIndex.getOrganizations()));
      scope.append(RuleAssembler.instance().scopeForClassifiers(natureClass, targetFilters.getKlassIds(), targetFilters.getTaxonomyIds()));
      
      return scope.toString();
    }
    return null;
  }
  
  private String getNatureClass(Map<String, IConfigEntityInformationModel> referencedklasses) 
  {
  return referencedklasses.values().stream().map(x -> x.getId())
        .findFirst().orElse("");
  }
}
