package com.cs.core.bgprocess.services.dataintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.usecase.assembler.RuleAssembler;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;

public class PXONImportDataRuleDAS {
  
  // Singleton implementation
  private static final PXONImportDataRuleDAS INSTANCE = new PXONImportDataRuleDAS();
  
  private PXONImportDataRuleDAS()
  {
  }
  
  public static PXONImportDataRuleDAS instance()
  {
    return INSTANCE;
  }
  
  public void upsertDataRule(List<Map<String, Object>> dataRuleList) throws Exception
  {
    for (Map<String, Object> dataRule : dataRuleList) {
      DataRuleModel dataRuleModel = ObjectMapperUtil.readValue(dataRule.toString(), DataRuleModel.class);
      String code = dataRuleModel.getCode();
      String ruleExpression = assembleRule(dataRuleModel);
      IConfigRuleDTO rule = ConfigurationDAO.instance().getRuleByCode(code);
      Long ruleExpressionIID = null;
      if (rule == null) {
        ruleExpressionIID = Long.parseLong(RDBMSAppDriverManager.getDriver().newUniqueID("EXPR").split("EXPR")[1]);
      }
      else {
        ruleExpressionIID = rule.getRuleExpressions().iterator().next().getRuleExpressionIId();
      }
      ConfigurationDAO.instance().upsertRule(code, ruleExpressionIID, ruleExpression, RuleType.dataquality);
    }
  }
  
  private String assembleRule(IDataRuleModel responseDataRuleModel)
  {
    String actions = RuleAssembler.instance().assembleActions(responseDataRuleModel);
    if (actions.isEmpty()) {
      return "";
    }
    String scope = assembleScope(responseDataRuleModel);
    String evaluation = RuleAssembler.instance().assembleEvaluation(responseDataRuleModel.getAttributes(), responseDataRuleModel.getTags());
    return RuleAssembler.instance().assembleRule(scope, evaluation, actions);
  }
  
  private String assembleScope(IDataRuleModel responseDataRuleModel)
  {
    String natureClass = getNatureClass(responseDataRuleModel.getConfigDetails().getReferencedKlasses());
    List<String> otherClassifiers = new ArrayList<>(responseDataRuleModel.getTypes());
    otherClassifiers.remove(natureClass);
    
    String scopeForClassifier = RuleAssembler.instance().scopeForClassifiers(natureClass, otherClassifiers,
        responseDataRuleModel.getTaxonomies());
    String scopeForLocale = RuleAssembler.instance().scopeForLocale(responseDataRuleModel.getLanguages());
    String scopeForCatalog = RuleAssembler.instance().scopeForCatalog(responseDataRuleModel.getPhysicalCatalogIds());
    String scopeForOrganization = RuleAssembler.instance().scopeForOrganizations(responseDataRuleModel.getOrganizations());
    
    String scope = String.format("%s %s %s %s", scopeForCatalog, scopeForOrganization, scopeForLocale, scopeForClassifier);
    return scope;
  }
  
  private String getNatureClass(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
    return referencedKlasses.values().stream().filter(x -> x.getIsNature()).map(x -> x.getId()).findFirst().orElse("");
  }
}
