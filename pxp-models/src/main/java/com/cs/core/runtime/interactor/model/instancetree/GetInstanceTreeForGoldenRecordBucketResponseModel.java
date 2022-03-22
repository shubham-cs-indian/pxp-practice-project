package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.goldenrecord.GoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GoldenRecordRuleBucketInstanceModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleBucketInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstanceTreeForGoldenRecordBucketResponseModel extends GetNewInstanceTreeResponseModel
    implements IGetInstanceTreeForGoldenRecordBucketResponseModel {
  
  private static final long                      serialVersionUID = 1L;
  protected Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;
  protected Map<String, IGoldenRecordRule>               referencedGoldenRecordRules;
  protected List<IGoldenRecordRuleBucketInstanceModel>   bucketInstances;
  protected List<String>                                 bucketIdsContainingGoldenRecord = new ArrayList<>();
  
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
  public void setReferencedGoldenRecordRules(Map<String, IGoldenRecordRule> referencedGoldenRecordRules)
  {
    this.referencedGoldenRecordRules = referencedGoldenRecordRules;
  }
  
  @Override
  public List<IGoldenRecordRuleBucketInstanceModel> getBucketInstances()
  {
    if (bucketInstances == null) {
      bucketInstances = new ArrayList<>();
    }
    return bucketInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = GoldenRecordRuleBucketInstanceModel.class)
  public void setBucketInstances(List<IGoldenRecordRuleBucketInstanceModel> bucketInstances)
  {
    this.bucketInstances = bucketInstances;
  }
  
  @Override
  public List<String> getBucketIdsContainingGoldenRecord()
  {
    return bucketIdsContainingGoldenRecord;
  }

  @Override
  public void setBucketIdsContainingGoldenRecord(List<String> bucketIdsContainingGoldenRecord)
  {
    this.bucketIdsContainingGoldenRecord = bucketIdsContainingGoldenRecord;
  }
}