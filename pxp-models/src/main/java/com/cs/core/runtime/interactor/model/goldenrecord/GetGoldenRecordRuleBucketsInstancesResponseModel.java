package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.GetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetGoldenRecordRuleBucketsInstancesResponseModel
    implements IGetGoldenRecordRuleBucketsInstancesResponseModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected List<IGoldenRecordRuleBucketInstanceModel>        bucketInstances;
  protected IGetConfigDetailsForGoldenRecordRuleResponseModel configDetails;
  protected Integer                                           from;
  protected Integer                                           totalBuckets;
  protected IGetFilterInfoModel                               filterInfo;
  protected List<String>                                      bucketIdsContainingGoldenRecord;
  
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
  public IGetConfigDetailsForGoldenRecordRuleResponseModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsForGoldenRecordRuleResponseModel.class)
  public void setConfigDetails(IGetConfigDetailsForGoldenRecordRuleResponseModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getTotalBuckets()
  {
    return totalBuckets;
  }
  
  @Override
  public void setTotalBuckets(Integer totalBuckets)
  {
    this.totalBuckets = totalBuckets;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @JsonDeserialize(as = GetFilterInfoModel.class)
  @Override
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
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
