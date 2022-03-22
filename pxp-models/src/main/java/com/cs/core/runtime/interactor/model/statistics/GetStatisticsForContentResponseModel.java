package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.entity.propertyinstance.Statistics;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetStatisticsForContentResponseModel implements IGetStatisticsForContentResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected Map<String, IStatistics>       statistics;
  protected Map<String, IIdLabelCodeModel> referencedKpi;
  protected Long                           lastModified;
  
  @Override
  public Map<String, IStatistics> getStatistics()
  {
    return statistics;
  }
  
  @JsonDeserialize(contentAs = Statistics.class)
  @Override
  public void setStatistics(Map<String, IStatistics> statistics)
  {
    this.statistics = statistics;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedKpi()
  {
    return referencedKpi;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedKpi(Map<String, IIdLabelCodeModel> referencedKpi)
  {
    this.referencedKpi = referencedKpi;
  }
  
  @Override
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
}
