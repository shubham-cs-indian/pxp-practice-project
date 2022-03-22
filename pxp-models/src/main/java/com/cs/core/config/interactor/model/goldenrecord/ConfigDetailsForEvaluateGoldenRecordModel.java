package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class ConfigDetailsForEvaluateGoldenRecordModel
    implements IConfigDetailsForEvaluateGoldenRecordModel {
  
  private static final long           serialVersionUID = 1L;
  
  public List<IGoldenRecordRuleModel> goldenRecordRules;
  public Map<String, ITag>            referencedTags;
  public List<String>                 booleanTagsToPreserve;
  
  @Override
  public List<IGoldenRecordRuleModel> getGoldenRecordRules()
  {
    return goldenRecordRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = GoldenRecordRuleModel.class)
  public void setGoldenRecordRules(List<IGoldenRecordRuleModel> goldenRecordRules)
  {
    this.goldenRecordRules = goldenRecordRules;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ITag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }

  @Override
  public List<String> getBooleanTagsToPreserve()
  {
    return booleanTagsToPreserve;
  }

  @Override
  public void setBooleanTagsToPreserve(List<String> booleanTagsToPreserve)
  {
    this.booleanTagsToPreserve = booleanTagsToPreserve;
  }
}
