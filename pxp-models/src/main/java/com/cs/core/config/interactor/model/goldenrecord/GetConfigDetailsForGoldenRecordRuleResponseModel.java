package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.goldenrecord.GoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetConfigDetailsForGoldenRecordRuleResponseModel
    implements IGetConfigDetailsForGoldenRecordRuleResponseModel {
  
  private static final long                                  serialVersionUID = 1L;
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses;
  protected Map<String, IAttribute>                          referencedAttributes;
  protected Map<String, ITag>                                referencedTags;
  protected Map<String, IReferencedArticleTaxonomyModel>     referencedTaxonomies;
  protected Map<String, IGoldenRecordRule>                   referencedGoldenRecordRules;
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    if (referencedAttributes == null) {
      referencedAttributes = new HashMap<>();
    }
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
    }
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    if (referencedTaxonomies == null) {
      referencedTaxonomies = new HashMap<>();
    }
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IGoldenRecordRule> getReferencedGoldenRecordRules()
  {
    if (referencedGoldenRecordRules == null) {
      referencedGoldenRecordRules = new HashMap<>();
    }
    return referencedGoldenRecordRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = GoldenRecordRule.class)
  public void setReferencedGoldenRecordRules(
      Map<String, IGoldenRecordRule> referencedGoldenRecordRules)
  {
    this.referencedGoldenRecordRules = referencedGoldenRecordRules;
  }
}
