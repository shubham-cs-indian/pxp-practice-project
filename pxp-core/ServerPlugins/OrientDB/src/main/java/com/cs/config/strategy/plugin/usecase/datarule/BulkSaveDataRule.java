package com.cs.config.strategy.plugin.usecase.datarule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.datarule.BulkSaveDataRuleFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class BulkSaveDataRule extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToExclude = Arrays.asList(IBulkSaveDataRuleModel.TYPE,
      IBulkSaveDataRuleModel.IS_STANDARD);
  
  public BulkSaveDataRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveDataRule/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfRules = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSaveRules = new ArrayList<>();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> ruleMap : listOfRules) {
      String ruleId = (String) ruleMap.get(ISaveGoldenRecordRuleModel.ID);
      Vertex dataRule = null;
      try {
        dataRule = UtilClass.getVertexById(ruleId, VertexLabelConstants.DATA_RULE);
        dataRule = UtilClass.saveNode(ruleMap, dataRule, fieldsToExclude);
        listOfSuccessSaveRules.add(getRuleMapToReturn(dataRule));
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, dataRule, Entities.RULES, Elements.UNDEFINED);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSaveRules.isEmpty()) {
      throw new BulkSaveDataRuleFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveGoldenRecordResponse = new HashMap<String, Object>();
    bulkSaveGoldenRecordResponse.put(IBulkSaveDataRuleResponseModel.SUCCESS,
        listOfSuccessSaveRules);
    bulkSaveGoldenRecordResponse.put(IBulkSaveDataRuleResponseModel.FAILURE, failure);
    bulkSaveGoldenRecordResponse.put(IBulkSaveDataRuleResponseModel.AUDIT_LOG_INFO, auditLogInfoList);
    return bulkSaveGoldenRecordResponse;
  }
  
  private Map<String, Object> getRuleMapToReturn(Vertex dataRuleVertex)
  {
    Map<String, Object> ruleMap = new HashMap<>();
    ruleMap.put(IBulkSaveDataRuleModel.ID, UtilClass.getCodeNew(dataRuleVertex));
    ruleMap.put(IBulkSaveDataRuleModel.LABEL,
        UtilClass.getValueByLanguage(dataRuleVertex, IIdLabelModel.LABEL));
    ruleMap.put(IBulkSaveDataRuleModel.CODE, dataRuleVertex.getProperty(IDataRule.CODE));
    ruleMap.put(IBulkSaveDataRuleModel.TYPE, dataRuleVertex.getProperty(IDataRule.TYPE));
    ruleMap.put(IBulkSaveDataRuleModel.IS_LANGUAGE_DEPENDENT,
        dataRuleVertex.getProperty(IDataRule.IS_LANGUAGE_DEPENDENT));
    ruleMap.put(IBulkSaveDataRuleModel.LANGUAGES, dataRuleVertex.getProperty(IDataRule.LANGUAGES));
    ruleMap.put(IBulkSaveDataRuleModel.IS_STANDARD,
        dataRuleVertex.getProperty(IDataRule.IS_STANDARD));
    return ruleMap;
  }
}
