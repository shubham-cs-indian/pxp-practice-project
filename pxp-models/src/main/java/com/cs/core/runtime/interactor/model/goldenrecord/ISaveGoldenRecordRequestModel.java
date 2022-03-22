package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ISaveGoldenRecordRequestModel extends IModel {
  
  public static final String BUCKET_ID               = "bucketId";
  public static final String RELATIONSHIPS           = "relationships";
  public static final String RULE_ID                 = "ruleId";
  public static final String GOLDEN_RECORD_ID        = "goldenRecordId";
  public static final String SELECTED_LANGUAGE_CODES = "selectedLanguageCodes";
  public static final String ATTRIBUTES              = "attributes";
  public static final String DEPENDENT_ATTRIBUTES    = "dependentAttributes";
  public static final String TAGS                    = "tags";
  public static final String KLASS_IDS               = "klassIds";
  public static final String TAXONOMY_IDS            = "taxonomyIds";
  public static final String ADDED_LANGUAGE_CODES    = "addedLanguageCodes";
  public static final String SOURCE_IDS              = "sourceIds";
  
  public String getBucketId();
  
  public void setBucketId(String bucketId);
  
  public List<IRelationshipIdSourceModel> getRelationships();
  
  public void setRelationships(List<IRelationshipIdSourceModel> relationships);
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
  
  public List<String> getSelectedLanguageCodes();
  
  public void setSelectedLanguageCodes(List<String> selectedLanguageCodes);
  
  // key : attributeId
  public Map<String, List<IConflictingValue>> getAttributes();
  
  public void setAttributes(Map<String, List<IConflictingValue>> attributes);
  
  // key:languageCode
  public Map<String, Map<String, List<IConflictingValue>>> getDependentAttributes();
  
  public void setDependentAttributes(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes);
  
  // Key : tagId
  public Map<String, List<IConflictingValue>> getTags();
  
  public void setTags(Map<String, List<IConflictingValue>> tags);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getAddedLanguageCodes();
  
  public void setAddedLanguageCodes(List<String> addedLanguageCodes);
  
  public List<String> getSourceIds();
  
  public void setSourceIds(List<String> sourceIds);
}
