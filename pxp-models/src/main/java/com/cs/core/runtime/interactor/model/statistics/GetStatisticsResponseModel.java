package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStatisticsResponseModel implements IGetStatisticsResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected IKPIStatisticsModel            kpiStatistics;
  protected Map<String, ITag>              referencedTags;
  protected Map<String, IIdLabelCodeModel> referencedTaxonomies;
  protected Map<String, IIdLabelCodeModel> referencedKlasses;
  protected List<String>                   entities;
  protected Map<String,String>			       ruleBlockVsCode;
  
  @Override
  public IKPIStatisticsModel getKpiStatistics()
  {
    return kpiStatistics;
  }
  
  @JsonDeserialize(as = KPIStatisticsModel.class)
  @Override
  public void setKpiStatistics(IKPIStatisticsModel kpiStatistics)
  {
    this.kpiStatistics = kpiStatistics;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedTaxonomies()
  {
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
  public List<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public Map<String, String> getRuleBlockVsCode()
  {
    if (ruleBlockVsCode == null) {
      ruleBlockVsCode = new HashMap<>();
    }
    return ruleBlockVsCode;
  }
  
  @Override
  public void setRuleBlockVsCode(Map<String, String> ruleBlockVsCode)
  {
    this.ruleBlockVsCode = ruleBlockVsCode;
  }
}
