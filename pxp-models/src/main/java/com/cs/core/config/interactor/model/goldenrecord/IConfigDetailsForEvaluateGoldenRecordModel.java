package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForEvaluateGoldenRecordModel extends IModel {
  
  public static final String REFERENCED_TAGS          = "referencedTags";
  public static final String GOLDEN_RECORD_RULES      = "goldenRecordRules";
  public static final String BOOLEAN_TAGS_TO_PRESERVE = "booleanTagsToPreserve";
  
  public List<IGoldenRecordRuleModel> getGoldenRecordRules();
  public void setGoldenRecordRules(List<IGoldenRecordRuleModel> goldenRecordRules);
  
  public Map<String, ITag> getReferencedTags();
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public List<String> getBooleanTagsToPreserve();
  public void setBooleanTagsToPreserve(List<String> booleanTagsToPreserve);
}
