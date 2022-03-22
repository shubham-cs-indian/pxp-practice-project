package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.goldenrecord.IGoldenRecordRuleBucketInstance;

public interface IGoldenRecordRuleBucketInstanceModel extends IGoldenRecordRuleBucketInstance {
  
  public static final String KLASS_INSTANCE_COUNT = "klassInstanceCount";
  
  public Long getklassInstanceCount();
  
  public void setKlassInstanceCount(Long klassInstanceCount);
}
