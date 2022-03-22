package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.GoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class EvaluateGoldenRecordRuleBucketRequestModel
    implements IEvaluateGoldenRecordRuleBucketRequestModel {
  
  private static final long            serialVersionUID = 1L;
  
  private String                       klassInstanceId;
  private List<IGoldenRecordRuleModel> goldenRecordRules;
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public List<IGoldenRecordRuleModel> getGoldenRecordRules()
  {
    if (goldenRecordRules == null) {
      goldenRecordRules = new ArrayList<>();
    }
    return goldenRecordRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = GoldenRecordRuleModel.class)
  public void setGoldenRecordRules(List<IGoldenRecordRuleModel> goldenRecordRules)
  {
    this.goldenRecordRules = goldenRecordRules;
  }
}
