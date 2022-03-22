package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.DataRuleDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IDataRuleDeleteDTO;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.rulelist.IRuleList;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.interactor.model.rulelist.IRuleListStrategySaveModel;
import com.cs.core.config.interactor.model.rulelist.RuleListModel;
import com.cs.core.config.interactor.usecase.assembler.RuleAssembler;
import com.cs.core.config.strategy.usecase.rulelist.ISaveRuleListStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaveRuleListService extends AbstractSaveConfigService<IRuleListModel, IRuleListModel>
    implements ISaveRuleListService {
  
  @Autowired
  protected ISaveRuleListStrategy saveRuleListStrategy;
  
  private static final String SERVICE = "SAVE_RULE";
  
  @Override
  public IRuleListModel executeInternal(IRuleListModel dataRuleModel) throws Exception
  {
    RuleListValidations.validateRuleList(dataRuleModel, false);
    IRuleListStrategySaveModel responseRuleList = saveRuleListStrategy.execute(dataRuleModel);
    IRuleList ruleListEntity = (IRuleList) responseRuleList.getEntity();
    Set<String> klassIds = new HashSet<>();
    Set<String> taxonomyIds = new HashSet<>();
    Set<String> ruleCode = new HashSet<>();
    if(responseRuleList.getIsRuleListModififed()) {
      modifyRuleBasedOnRuleList(klassIds, taxonomyIds, ruleCode, responseRuleList.getDataRuleList());
    }
    
    if (!klassIds.isEmpty() || !taxonomyIds.isEmpty()) {
      IDataRuleDeleteDTO ruleDTO = new DataRuleDeleteDTO();
      ruleDTO.setRuleCodes(ruleCode);
      ruleDTO.setKlassIds(klassIds);
      ruleDTO.setTaxonomyIds(taxonomyIds);
      
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), SERVICE, "", userPriority, new JSONContent(ruleDTO.toJSON()));
     }
    IRuleListModel modelToReturn = new RuleListModel(ruleListEntity);
    modelToReturn.setAuditLogInfo(responseRuleList.getAuditLogInfo());
    return modelToReturn;
  }

  private void modifyRuleBasedOnRuleList(Set<String> klassIds, Set<String> taxonomyIds, Set<String> ruleCode, List<IDataRuleModel> dataRuleList)
      throws Exception
  {
      for (IDataRuleModel dataRule : dataRuleList) {
      String ruleExpression = assembleRule(dataRule);
      IConfigRuleDTO rule = ConfigurationDAO.instance().getRuleByCode(dataRule.getId());
      if(!ruleExpression.isEmpty() && rule != null) {
      Long ruleExpressionIID = null;
      ruleExpressionIID = rule.getRuleExpressions().iterator().next().getRuleExpressionIId();
      ConfigurationDAO.instance().upsertRule(dataRule.getCode(), ruleExpressionIID, ruleExpression, RuleType.dataquality);
      klassIds.addAll(dataRule.getTypes());
      taxonomyIds.addAll(dataRule.getTaxonomies());
      ruleCode.add(dataRule.getCode());
      }
    }
  }
  
  
  private String assembleRule(IDataRuleModel responseDataRuleModel)
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

    String scopeForClassifier = RuleAssembler.instance().scopeForClassifiers(natureClass, otherClassifiers, responseDataRuleModel.getTaxonomies());
    String scopeForLocale = RuleAssembler.instance().scopeForLocale(responseDataRuleModel.getLanguages());
    String scopeForCatalog = RuleAssembler.instance().scopeForCatalog(responseDataRuleModel.getPhysicalCatalogIds());
    String scopeForOrganization = RuleAssembler.instance().scopeForOrganizations(responseDataRuleModel.getOrganizations());

    String scope = String.format("%s %s %s %s", scopeForCatalog, scopeForOrganization, scopeForLocale, scopeForClassifier);
    return scope;
  }

  
  private String getNatureClass(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
  return referencedKlasses.values().stream().filter(x -> x.getIsNature()).map(x -> x.getId())
        .findFirst().orElse("");
  }
}
