package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.config.interactor.model.governancerule.IReferencedKPIModel;
import com.cs.core.config.interactor.model.governancerule.ReferencedKPIModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllKPIStatisticsModel implements IGetAllKPIStatisticsModel {
  
  private static final long                  serialVersionUID = 1L;
  protected List<IKPIStatisticsModel>        dataGovernance;
  protected Long                             totalCount;
  protected Map<String, IIdLabelCodeModel>   referencedTaxonomies;
  protected Map<String, IIdLabelCodeModel>   referencedKlasses;
  protected Map<String, IReferencedKPIModel> referencedKpi;
  protected List<String>                     entities;
  
  @Override
  public List<IKPIStatisticsModel> getDataGovernance()
  {
    if (dataGovernance == null) {
      dataGovernance = new ArrayList<>();
    }
    return dataGovernance;
  }
  
  @JsonDeserialize(contentAs = KPIStatisticsModel.class)
  @Override
  public void setDataGovernance(List<IKPIStatisticsModel> dataGovernance)
  {
    this.dataGovernance = dataGovernance;
  }
  
  @Override
  public Long getTotalCount()
  {
    if (totalCount == null) {
      totalCount = 0l;
    }
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedTaxonomies()
  {
    if (referencedTaxonomies == null) {
      referencedTaxonomies = new HashMap<>();
    }
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedTaxonomies(Map<String, IIdLabelCodeModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IIdLabelCodeModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IReferencedKPIModel> getReferencedKpi()
  {
    if (referencedKpi == null) {
      referencedKpi = new HashMap<>();
    }
    return referencedKpi;
  }
  
  @JsonDeserialize(contentAs = ReferencedKPIModel.class)
  @Override
  public void setReferencedKpi(Map<String, IReferencedKPIModel> referencedKpi)
  {
    this.referencedKpi = referencedKpi;
  }
  
  @Override
  public List<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
}
