package com.cs.core.config.interactor.model.goldenrecord;

import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.goldenrecord.GoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelNatureTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetGoldenRecordRuleModel extends ConfigResponseWithAuditLogModel
    implements IGetGoldenRecordRuleModel {
  
  private static final long                              serialVersionUID = 1L;
  protected IGoldenRecordRule                            goldenRecordRule;
  protected Map<String, IIdLabelCodeModel>               referencedAttributes;
  protected Map<String, IIdLabelCodeModel>               referencedTags;
  protected Map<String, IIdLabelNatureTypeModel>         referencedKlasses;
  protected Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;
  protected Map<String, IIdLabelCodeModel>               referencedOrganizations;
  protected Map<String, IIdLabelCodeModel>               referencedEndpoints;
  protected Map<String, IIdLabelCodeModel>               referencedRelationships;
  protected Map<String, IIdLabelCodeModel>               referencedNatureRelationships;
  protected Boolean                                      needToEvaluateGoldenRecordRuleBucket;
  protected List<String>                                 physicalCatalogIds;
  
  @Override
  public IGoldenRecordRule getGoldenRecordRule()
  {
    return goldenRecordRule;
  }
  
  @Override
  @JsonDeserialize(as = GoldenRecordRule.class)
  public void setGoldenRecordRule(IGoldenRecordRule goldenRecordRule)
  {
    this.goldenRecordRule = goldenRecordRule;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedAttributes(Map<String, IIdLabelCodeModel> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedTags(Map<String, IIdLabelCodeModel> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  public Map<String, IIdLabelNatureTypeModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelNatureTypeModel.class)
  public void setReferencedKlasses(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  public Map<String, IIdLabelCodeModel> getReferencedOrganizations()
  {
    return referencedOrganizations;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedOrganizations(Map<String, IIdLabelCodeModel> referencedOrganizations)
  {
    this.referencedOrganizations = referencedOrganizations;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedEndpoints()
  {
    return referencedEndpoints;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedEndpoints(Map<String, IIdLabelCodeModel> referencedEndpoints)
  {
    this.referencedEndpoints = referencedEndpoints;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedRelationships(Map<String, IIdLabelCodeModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IIdLabelCodeModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return this.physicalCatalogIds;
  }

  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }

  @Override
  public Boolean getNeedToEvaluateGoldenRecordRuleBucket()
  {
    return this.needToEvaluateGoldenRecordRuleBucket;
  }

  @Override
  public void setNeedToEvaulateGoldenRecordRuleBucket(Boolean needToEvaluateGoldenRecordRuleBucket)
  {
    this.needToEvaluateGoldenRecordRuleBucket = needToEvaluateGoldenRecordRuleBucket;
  }
}
