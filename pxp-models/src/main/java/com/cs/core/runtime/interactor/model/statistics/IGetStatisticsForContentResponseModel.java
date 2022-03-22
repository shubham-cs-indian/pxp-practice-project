package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetStatisticsForContentResponseModel extends IModel {
  
  public static final String STATISTICS     = "statistics";
  public static final String REFERENCED_KPI = "referencedKpi";
  public static final String LAST_MODIFIED  = "lastModified";
  
  public Map<String, IStatistics> getStatistics();
  
  public void setStatistics(Map<String, IStatistics> statistics);
  
  public Map<String, IIdLabelCodeModel> getReferencedKpi();
  
  public void setReferencedKpi(Map<String, IIdLabelCodeModel> referencedKpi);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
}
