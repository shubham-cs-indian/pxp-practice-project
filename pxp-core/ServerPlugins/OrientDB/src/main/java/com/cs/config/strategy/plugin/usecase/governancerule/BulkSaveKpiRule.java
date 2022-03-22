package com.cs.config.strategy.plugin.usecase.governancerule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.exception.kpi.BulkSaveKpiRuleFailedException;
import com.cs.core.config.interactor.model.attribute.IBulkSaveKpiRuleSuccessModel;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridKpiRulesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class BulkSaveKpiRule extends AbstractOrientPlugin {
  
  public BulkSaveKpiRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveKpiRule/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfKpi = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSaveRules = new ArrayList<>();
    Map<String, Object> referencedDashboardTabs = new HashMap<>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> kpiMap : listOfKpi) {
      String ruleId = (String) kpiMap.get(ISaveGoldenRecordRuleModel.ID);
      Vertex kpiVertex = null;
      try {
        kpiVertex = UtilClass.getVertexById(ruleId, VertexLabelConstants.GOVERNANCE_RULE_KPI);
        kpiVertex = UtilClass.saveNode(kpiMap, kpiVertex,
            Arrays.asList(IBulkSaveKpiRuleRequestModel.ADDED_DASHBOARD_TAB_ID,
                IBulkSaveKpiRuleRequestModel.DELETED_DASHBOARD_TAB_ID));
        TabUtils.manageAddedDashboardTabId(kpiMap, kpiVertex);
        TabUtils.manageDeletedDashboardTabId(kpiMap, kpiVertex);
        listOfSuccessSaveRules.add(getKpiMapToReturn(kpiVertex, referencedDashboardTabs));
        AuditLogUtils.fillAuditLoginfo(auditInfoList, kpiVertex, Entities.KPI, Elements.UNDEFINED);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSaveRules.isEmpty()) {
      throw new BulkSaveKpiRuleFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> success = new HashMap<>();
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForGridKpiRulesModel.REFERENCED_DASHBOARD_TABS,
        referencedDashboardTabs);
    success.put(IBulkSaveKpiRuleSuccessModel.KPI_RULE_LIST, listOfSuccessSaveRules);
    success.put(IBulkSaveKpiRuleSuccessModel.CONFIG_DETAILS, configDetails);
    
    Map<String, Object> bulkSaveGoldenRecordResponse = new HashMap<String, Object>();
    bulkSaveGoldenRecordResponse.put(IBulkSaveGoldenRecordRuleResponseModel.SUCCESS, success);
    bulkSaveGoldenRecordResponse.put(IBulkSaveGoldenRecordRuleResponseModel.FAILURE, failure);
    bulkSaveGoldenRecordResponse.put(IBulkSaveGoldenRecordRuleResponseModel.AUDIT_LOG_INFO, auditInfoList);
    return bulkSaveGoldenRecordResponse;
  }
  
  private Map<String, Object> getKpiMapToReturn(Vertex kpiVertex,
      Map<String, Object> referencedDashboardTabs)
  {
    Map<String, Object> kpiMap = new HashMap<>();
    kpiMap.put(IBulkSaveKpiRuleModel.ID, UtilClass.getCodeNew(kpiVertex));
    kpiMap.put(IBulkSaveKpiRuleModel.LABEL,
        UtilClass.getValueByLanguage(kpiVertex, IIdLabelModel.LABEL));
    kpiMap.put(IBulkSaveKpiRuleModel.CODE, kpiVertex.getProperty(IGoldenRecordRule.CODE));
    Iterator<Vertex> vertices = kpiVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DASHBOARD_TAB)
        .iterator();
    if (vertices.hasNext()) {
      Vertex dashboardVertex = vertices.next();
      String dashboardTabId = UtilClass.getCodeNew(dashboardVertex);
      kpiMap.put(IBulkSaveKpiRuleModel.DASHBOARD_TAB_ID, dashboardTabId);
      Map<String, Object> referencedDashboard = new HashMap<>();
      referencedDashboard.put(IIdLabelModel.ID, dashboardTabId);
      referencedDashboard.put(IIdLabelModel.LABEL,
          UtilClass.getValueByLanguage(dashboardVertex, IIdLabelModel.LABEL));
      referencedDashboardTabs.put(dashboardTabId, referencedDashboard);
    }
    return kpiMap;
  }
}
