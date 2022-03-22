package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleBucketInstanceModel;

public interface IGetInstanceTreeForGoldenRecordBucketResponseModel
    extends IGetNewInstanceTreeResponseModel {
  
  public static final String BUCKET_INSTANCES                    = "bucketInstances";
  public static final String REFERENCED_TAXONOMIES               = "referencedTaxonomies";
  public static final String BUCKET_IDS_CONTAINING_GOLDEN_RECORD = "bucketIdsContainingGoldenRecord";
  public static final String REFERENCED_GOLDEN_RECORD_RULES      = "referencedGoldenRecordRules";
  
  public List<IGoldenRecordRuleBucketInstanceModel> getBucketInstances();
  public void setBucketInstances(List<IGoldenRecordRuleBucketInstanceModel> bucketInstances);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  public void setReferencedTaxonomies(Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public List<String> getBucketIdsContainingGoldenRecord();
  public void setBucketIdsContainingGoldenRecord(List<String> bucketIdsContainingGoldenRecord);
  
  public Map<String, IGoldenRecordRule> getReferencedGoldenRecordRules();
  public void setReferencedGoldenRecordRules(Map<String, IGoldenRecordRule> referencedGoldenRecordRules);
}
