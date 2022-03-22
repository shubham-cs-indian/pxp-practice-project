package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.config.interactor.model.governancerule.IReferencedKPIModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetAllKPIStatisticsModel extends IModel {
  
  public static final String DATA_GOVERNANCE       = "dataGovernance";
  public static final String TOTAL_COUNT           = "totalCount";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_KPI        = "referencedKpi";
  public static final String ENTITIES              = "entities";
  
  public List<IKPIStatisticsModel> getDataGovernance();
  
  public void setDataGovernance(List<IKPIStatisticsModel> dataGovernance);
  
  public Long getTotalCount();
  
  public void setTotalCount(Long totalCount);
  
  public Map<String, IIdLabelCodeModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, IIdLabelCodeModel> referencedTaxonomies);
  
  public Map<String, IIdLabelCodeModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IIdLabelCodeModel> referencedKlasses);
  
  public Map<String, IReferencedKPIModel> getReferencedKpi();
  
  public void setReferencedKpi(Map<String, IReferencedKPIModel> referencedKpi);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
}
