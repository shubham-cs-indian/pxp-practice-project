package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridKpiRulesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForGridKpiRulesModel implements IConfigDetailsForGridKpiRulesModel {
  
  private static final long                serialVersionUID = 1L;
  protected Map<String, IIdLabelCodeModel> referencedDashboardTabs;
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedDashboardTabs()
  {
    return referencedDashboardTabs;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedDashboardTabs(Map<String, IIdLabelCodeModel> referencedDashboardTabs)
  {
    this.referencedDashboardTabs = referencedDashboardTabs;
  }
}
