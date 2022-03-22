package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetConfigDetailsForGoldenRecordRuleResponseModel extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES          = "referencedAttributes";
  public static final String REFERENCED_TAGS                = "referencedTags";
  public static final String REFERENCED_GOLDEN_RECORD_RULES = "referencedGoldenRecordRules";
  public static final String REFRENCED_KLASSES              = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES          = "referencedTaxonomies";
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IGoldenRecordRule> getReferencedGoldenRecordRules();
  
  public void setReferencedGoldenRecordRules(
      Map<String, IGoldenRecordRule> referencedGoldenRecordRules);
}
