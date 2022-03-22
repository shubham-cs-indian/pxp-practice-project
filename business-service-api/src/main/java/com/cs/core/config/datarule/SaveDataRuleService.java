package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.SaveDataRuleDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.ISaveDataRuleDTO;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.exception.validationontype.InvalidDataRuleTypeException;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import com.cs.core.config.interactor.usecase.assembler.RuleAssembler;
import com.cs.core.config.physicalcatalog.util.PhysicalCatalogUtils;
import com.cs.core.config.strategy.usecase.datarule.ISaveDataRuleStrategy;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("unchecked")
@Service
public class SaveDataRuleService extends AbstractSaveConfigService<ISaveDataRuleModel, IDataRuleModel>
    implements ISaveDataRuleService {
  
  @Autowired
  protected ISaveDataRuleStrategy orientDBSaveDataRuleStrategy;

  private static final String SERVICE = "SAVE_RULE";
  
  @Autowired
  TransactionThreadData            transactionThread;
  
  @Override
  public IDataRuleModel executeInternal(ISaveDataRuleModel saveDataRuleModel) throws Exception
  {
    String dataRuleCode = saveDataRuleModel.getId();
    saveDataRuleModel
        .setAvailablePhysicalCatalogIds(PhysicalCatalogUtils.getAvailablePhysicalCatalogs());

    IDataRuleModel responseDataRuleModel = orientDBSaveDataRuleStrategy.execute(saveDataRuleModel);
    
    String ruleExpression = assembleRule(responseDataRuleModel);
    IConfigRuleDTO rule = ConfigurationDAO.instance().getRuleByCode(dataRuleCode);
    Long ruleExpressionIID = null;
    if(ruleExpression.isEmpty() && rule == null) {
      return responseDataRuleModel;
    }
    if (rule == null) {
      ruleExpressionIID = Long.parseLong(RDBMSUtils.newUniqueID("EXPR").split("EXPR")[1]);
    }
    else {
      ruleExpressionIID = rule.getRuleExpressions().iterator().next().getRuleExpressionIId();
    }
    
    ConfigurationDAO.instance().upsertRule(dataRuleCode, ruleExpressionIID, ruleExpression,  RuleType.dataquality);
    
    IConfigRuleDTO newRule = ConfigurationDAO.instance().getRuleByCode(dataRuleCode);
    
    Set<String> changedCatalogCodes = new HashSet<>();
    Set<String> changedOrganizationCodes = new HashSet<>();
    Set<String> changedLocaleIds = new HashSet<>();
    Set<String> changedClassifierCodes = new HashSet<>();
    
    if (rule != null && newRule != null) {
      fillDiffRelatedToDataRuleSave(rule, newRule, changedCatalogCodes, changedOrganizationCodes, changedLocaleIds, changedClassifierCodes);
    }
    
    changedClassifierCodes.addAll(responseDataRuleModel.getTypes());
    changedClassifierCodes.addAll(responseDataRuleModel.getTaxonomies());
    changedOrganizationCodes.addAll(responseDataRuleModel.getOrganizations());
    changedCatalogCodes.addAll(responseDataRuleModel.getPhysicalCatalogIds());
    
    if (shouldPropagate(saveDataRuleModel, responseDataRuleModel) && (!changedClassifierCodes.isEmpty())) {
      ISaveDataRuleDTO ruleDTO = new SaveDataRuleDTO();
      ruleDTO.setRuleCode(dataRuleCode);
      ruleDTO.setRuleExpressionId(ruleExpressionIID);
      ruleDTO.setChangedCatalogIds(changedCatalogCodes);
      ruleDTO.setChangedClassifierCodes(changedClassifierCodes);
      ruleDTO.setChangedOrganizationIds(changedOrganizationCodes);
      TransactionData transactionData = transactionThread.getTransactionData();
      ruleDTO.setLocaleID(transactionData.getDataLanguage());
      ruleDTO.setCatalogCode(transactionData.getPhysicalCatalogId());
      ruleDTO.setOrganizationCode(transactionData.getOrganizationId());
      ruleDTO.setUserId(transactionData.getUserId());
      ruleDTO.setUserName(transactionData.getUserName());
      
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), SERVICE, "", userPriority,
          new JSONContent(ruleDTO.toJSON()));
     }
 
    return responseDataRuleModel;
  }
  
  private void fillDiffRelatedToDataRuleSave(IConfigRuleDTO existingRule,
      IConfigRuleDTO newRule, Set<String> changedCatalogCodes,
      Set<String> changedOrganizationCodes, Set<String> changedLocaleIds,
      Set<String> changedClassifierCodes) throws CSFormatException
  {
    IRuleExpressionDTO existingRuleDTO = existingRule.getRuleExpressions().stream().findFirst().get();
    IRuleExpressionDTO newRuleDTO = newRule.getRuleExpressions().stream().findFirst().get();
    
    List<String> existingCatalogCodes =  new ArrayList<String>( existingRuleDTO.getCatalogCodes());
    List<String> newCatalogCodes = new ArrayList<>(newRuleDTO.getCatalogCodes());
    
    List<String> existingOrganizationCodes =  new  ArrayList<String>(existingRuleDTO.getOrganizationCodes());
    List<String> newOrganizationCodes = new ArrayList<String>( newRuleDTO.getOrganizationCodes());
    
    List<String> existingClassifierCodes =  new  ArrayList<String>(getClassifierCodes(existingRuleDTO));
    List<String> newClassifierCodes = new  ArrayList<String>(getClassifierCodes(newRuleDTO));
    
    changedCatalogCodes.addAll(ListUtils.subtract(existingCatalogCodes, newCatalogCodes));
    changedCatalogCodes.addAll(ListUtils.subtract(newCatalogCodes, existingCatalogCodes));

    changedOrganizationCodes.addAll(ListUtils.subtract(existingOrganizationCodes, newOrganizationCodes));
    changedOrganizationCodes.addAll(ListUtils.subtract(newOrganizationCodes, existingOrganizationCodes));

    changedClassifierCodes.addAll(ListUtils.subtract(existingClassifierCodes, newClassifierCodes));
    changedClassifierCodes.addAll(ListUtils.subtract(newClassifierCodes, existingClassifierCodes));

  }
  
  private Set<String> getClassifierCodes(IRuleExpressionDTO ruleDTO) throws CSFormatException
  {
    Set<String> classifierCodes = new HashSet<String>();
    ICSERule parseRule = (new CSEParser()).parseRule(ruleDTO.getRuleExpression());
    ICSEEntityFilterNode entityFilter = parseRule.getScope().getEntityFilter();
    if (entityFilter != null && !entityFilter.getIncludingClassifiers().isEmpty()) {
      classifierCodes = (Set<String>) entityFilter.getIncludingClassifiers();
    }
     return classifierCodes;
  }

  private Boolean shouldPropagate(ISaveDataRuleModel saveDataRuleModel,
      IDataRuleModel responseDataRuleModel)
  {
    Boolean shouldPropagate = false;
    if(!isRuleViolationsPresent(responseDataRuleModel)) {
      if(!saveDataRuleModel.getDeletedRuleViolations().isEmpty()) {
        shouldPropagate = true;
      }
    }
    if(isEffectPresent(responseDataRuleModel, shouldPropagate) && isADMChanged(saveDataRuleModel, responseDataRuleModel)) {
      shouldPropagate = true;
    }
    return shouldPropagate;
  }
  
  private Boolean isRuleViolationsPresent(IDataRuleModel responseDataRuleModel)
  {
    return !responseDataRuleModel.getRuleViolations().isEmpty();
  }
  
  private boolean isEffectPresent(IDataRuleModel responseDataRuleModel,
      Boolean shouldPropagate)
  {
    return shouldPropagate
        || (isTypesAvailableInCauseSide(responseDataRuleModel) && isNormalizationPresent(responseDataRuleModel))
        || isRuleViolationsPresent(responseDataRuleModel);
  }
  
  private Boolean isTypesAvailableInCauseSide(IDataRuleModel responseDataRuleModel)
  {
    return !responseDataRuleModel.getKlassIds().isEmpty()
        || !responseDataRuleModel.getTaxonomies().isEmpty();
  }
  
  private boolean isNormalizationPresent(IDataRuleModel responseDataRuleModel)
  {
    return responseDataRuleModel.getNormalizations().stream()
        .filter(normalization -> getFillter(normalization))
        .findAny()
        .isPresent();
  }
  
  private Boolean isADMChanged(ISaveDataRuleModel saveDataRuleModel,
      IDataRuleModel responseDataRuleModel)
  {
    return !saveDataRuleModel.getModifiedNormalizations().isEmpty()
        || !saveDataRuleModel.getAddedNormalizations().isEmpty()
       
        || !saveDataRuleModel.getAddedRuleViolations().isEmpty()
        || !saveDataRuleModel.getModifiedRuleViolations().isEmpty()
       
        || !saveDataRuleModel.getAddedEndpoints().isEmpty()
        || !saveDataRuleModel.getDeletedEndpoints().isEmpty()
       
        || !saveDataRuleModel.getAddedLanguages().isEmpty()
        || !saveDataRuleModel.getDeletedLanguages().isEmpty()
       
        || !saveDataRuleModel.getAddedOrganizationIds().isEmpty()
        || !saveDataRuleModel.getDeletedOrganizationIds().isEmpty()
       
        || !saveDataRuleModel.getAddedAttributeRules().isEmpty()
        || !saveDataRuleModel.getDeletedAttributeRules().isEmpty()
        || !saveDataRuleModel.getModifiedAttributeRules().isEmpty()
       
        || !saveDataRuleModel.getAddedTagRules().isEmpty()
        || !saveDataRuleModel.getModifiedTagRules().isEmpty()
        || !saveDataRuleModel.getDeletedTagRules().isEmpty()
       
        || !saveDataRuleModel.getAddedTaxonomies().isEmpty()
        || !saveDataRuleModel.getDeletedTaxonomies().isEmpty()
       
        || !saveDataRuleModel.getAddedTypes().isEmpty()
        || !saveDataRuleModel.getDeletedTypes().isEmpty()
       
        || responseDataRuleModel.getIsPhysicalCatalogsChanged();
  }
  
  private boolean getFillter(INormalization normalization)
  {
    if(normalization.getType().equals(CommonConstants.TYPE) || normalization.getType().equals(CommonConstants.TAXONOMY)) {
      return !normalization.getValues().isEmpty()|| !normalization.getValueAsHTML().isEmpty();
    }
    else {
      return true;
    }
  }
  
  private String assembleRule(IDataRuleModel responseDataRuleModel) throws InvalidDataRuleTypeException
  {
    String actions = RuleAssembler.instance().assembleActions(responseDataRuleModel);
    if(actions.isEmpty()) {
      return "";
    }
    String scope = assembleScope(responseDataRuleModel);
    String evaluation = RuleAssembler.instance().assembleEvaluation(responseDataRuleModel.getAttributes(), responseDataRuleModel.getTags());
    return RuleAssembler.instance().assembleRule(scope, evaluation, actions);
  }

	private String assembleScope(IDataRuleModel responseDataRuleModel) {
		String natureClass = getNatureClass(responseDataRuleModel.getConfigDetails().getReferencedKlasses());
		List<String> otherClassifiers = new ArrayList<>(responseDataRuleModel.getTypes());
		otherClassifiers.remove(natureClass);
		List<String> organizations = new ArrayList<>(responseDataRuleModel.getOrganizations());

		String scopeForClassifier = RuleAssembler.instance().scopeForClassifiers(natureClass, otherClassifiers, responseDataRuleModel.getTaxonomies());
		String scopeForLocale = RuleAssembler.instance().scopeForLocale(responseDataRuleModel.getLanguages());
		String scopeForCatalog = RuleAssembler.instance().scopeForCatalog(responseDataRuleModel.getPhysicalCatalogIds());
    String scopeForOrganization = RuleAssembler.instance().scopeForOrganizations(organizations);

		String scope = String.format("%s %s %s %s", scopeForCatalog, scopeForOrganization, scopeForLocale, scopeForClassifier);
		return scope;
	}

  private String getNatureClass(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
	return referencedKlasses.values().stream().filter(x -> x.getIsNature()).map(x -> x.getId())
				.findFirst().orElse("");
  }
}