package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;

public interface IGetGoldenRecordRuleBucketsInstancesResponseModel extends IModel {
  
  public static final String BUCKET_INTANCES                     = "bucketInstances";
  public static final String CONFIG_DETAILS                      = "configDetails";
  public static final String FROM                                = "from";
  public static final String TOTAL_BUCKETS                       = "totalBuckets";
  public static final String FILTER_INFO                         = "filterInfo";
  public static final String BUCKET_IDS_CONTAINING_GOLDEN_RECORD = "bucketIdsContainingGoldenRecord";
  
  public List<IGoldenRecordRuleBucketInstanceModel> getBucketInstances();
  
  public void setBucketInstances(List<IGoldenRecordRuleBucketInstanceModel> bucketInstances);
  
  public IGetConfigDetailsForGoldenRecordRuleResponseModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForGoldenRecordRuleResponseModel configDetails);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getTotalBuckets();
  
  public void setTotalBuckets(Integer totalBuckets);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public List<String> getBucketIdsContainingGoldenRecord();
  
  public void setBucketIdsContainingGoldenRecord(List<String> bucketIdsContainingGoldenRecord);
}
