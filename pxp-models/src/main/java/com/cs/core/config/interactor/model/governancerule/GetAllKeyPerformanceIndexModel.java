package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridKpiRulesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllKeyPerformanceIndexModel implements IGetAllKeyPerformanceIndexModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected List<IBulkSaveKpiRuleModel>        kpiList;
  protected Long                               count;
  protected IConfigDetailsForGridKpiRulesModel configDetails;
  // protected Map<String, Object> referencedRoles;
  
  @Override
  public IConfigDetailsForGridKpiRulesModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGridKpiRulesModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGridKpiRulesModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<IBulkSaveKpiRuleModel> getKpiList()
  {
    return kpiList;
  }
  
  @JsonDeserialize(contentAs = BulkSaveKpiRuleModel.class)
  @Override
  public void setKpiList(List<IBulkSaveKpiRuleModel> kpiList)
  {
    this.kpiList = kpiList;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  /*@Override
  public Map<String, Object> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  public void setReferencedRoles(Map<String, Object> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }*/
  
}
