package com.cs.config.strategy.plugin.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.exception.goldenrecord.BulkSaveGoldenRecordRuleFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class BulkSaveGoldenRecordRule extends AbstractOrientPlugin {
  
  public BulkSaveGoldenRecordRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveGoldenRecordRule/*" };
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
      Vertex goldenRecordRule = null;
      try {
        goldenRecordRule = UtilClass.getVertexById(ruleId, VertexLabelConstants.GOLDEN_RECORD_RULE);
        goldenRecordRule = UtilClass.saveNode(ruleMap, goldenRecordRule, new ArrayList<>());
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, goldenRecordRule, Entities.GOLDEN_RECORD_RULE, Elements.UNDEFINED);
        listOfSuccessSaveRules.add(getRuleMapToReturn(goldenRecordRule));
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSaveRules.isEmpty()) {
      throw new BulkSaveGoldenRecordRuleFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveGoldenRecordResponse = new HashMap<String, Object>();
    bulkSaveGoldenRecordResponse.put(IBulkSaveGoldenRecordRuleResponseModel.SUCCESS, listOfSuccessSaveRules);
    bulkSaveGoldenRecordResponse.put(IBulkSaveGoldenRecordRuleResponseModel.FAILURE, failure);
    bulkSaveGoldenRecordResponse.put(IBulkSaveGoldenRecordRuleResponseModel.AUDIT_LOG_INFO, auditLogInfoList);
    return bulkSaveGoldenRecordResponse;
  }
  
  private Map<String, Object> getRuleMapToReturn(Vertex goldenRecordRuleVertex)
  {
    Map<String, Object> ruleMap = new HashMap<>();
    ruleMap.put(IIdLabelCodeModel.ID, UtilClass.getCodeNew(goldenRecordRuleVertex));
    ruleMap.put(IIdLabelCodeModel.LABEL,
        UtilClass.getValueByLanguage(goldenRecordRuleVertex, IIdLabelModel.LABEL));
    ruleMap.put(IIdLabelCodeModel.CODE, goldenRecordRuleVertex.getProperty(IGoldenRecordRule.CODE));
    return ruleMap;
  }
}
