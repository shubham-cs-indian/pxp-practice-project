package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetStatisticsResponseModel extends IModel {
  
  public static final String KPI_STATISTICS        = "kpiStatistics";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String ENTITIES              = "entities";
  public static final String RULE_BLOCK_VS_CODE    = "ruleBlockVsCode";
  
  public IKPIStatisticsModel getKpiStatistics();
  
  public void setKpiStatistics(IKPIStatisticsModel kpiStatistics);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IIdLabelCodeModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, IIdLabelCodeModel> referencedTaxonomies);
  
  public Map<String, IIdLabelCodeModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IIdLabelCodeModel> referencedKlasses);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public Map<String, String> getRuleBlockVsCode();
  
  public void setRuleBlockVsCode(Map<String, String> ruleBlockVsCode);
}
