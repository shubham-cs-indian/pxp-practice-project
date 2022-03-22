package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridKpiRulesModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllKeyPerformanceIndexModel extends IModel {
  
  public static final String KPI_LIST       = "kpiList";
  public static final String COUNT          = "count";
  public static final String CONFIG_DETAILS = "configDetails";
  // public static final String REFERENCED_ROLES = "referencedRoles";
  
  public List<IBulkSaveKpiRuleModel> getKpiList();
  
  public void setKpiList(List<IBulkSaveKpiRuleModel> kpiList);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public IConfigDetailsForGridKpiRulesModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridKpiRulesModel configDetails);
  
  /*public Map<String,Object> getReferencedRoles();
  public void setReferencedRoles(Map<String,Object> referencedRoles);*/
}
