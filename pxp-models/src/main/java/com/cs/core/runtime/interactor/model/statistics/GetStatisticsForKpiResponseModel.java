package com.cs.core.runtime.interactor.model.statistics;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeMapModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetStatisticsForKpiResponseModel implements IGetStatisticsForKpiResponseModel{
  
  private static final long serialVersionUID = 1L;
  protected Map<String, String> kpiBlocks;
  protected Map<String, IIdLabelCodeMapModel> referencedKpi;
  
  @Override
  public Map<String, String> getKpiBlocks()
  {
    return kpiBlocks;
  }

  @Override
  public void setKpiBlocks(Map<String, String> kpiBlocks)
  {
    this.kpiBlocks = kpiBlocks;
  }

  @Override
  public Map<String, IIdLabelCodeMapModel> getReferencedKpi()
  {
    return referencedKpi;
  }

  @Override
  @JsonDeserialize(contentAs = IdLabelCodeMapModel.class)
  public void setReferencedKpi(Map<String, IIdLabelCodeMapModel> referencedKpi)
  {
    this.referencedKpi = referencedKpi;
  }
  
}
