package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IEvaluateGoldenRecordRuleBucketRequestModel extends IModel {
  
  public static final String KLASS_INSTANCE_ID   = "klassInstanceId";
  public static final String GOLDEN_RECORD_RULES = "goldenRecordRules";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public List<IGoldenRecordRuleModel> getGoldenRecordRules();
  
  public void setGoldenRecordRules(List<IGoldenRecordRuleModel> goldenRecordRules);
}
