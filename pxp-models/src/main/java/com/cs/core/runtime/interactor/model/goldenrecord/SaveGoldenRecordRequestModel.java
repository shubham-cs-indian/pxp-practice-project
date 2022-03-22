package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.attribute.AttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.attribute.DependentAttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.tag.TagConflictMapCustomDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SaveGoldenRecordRequestModel implements ISaveGoldenRecordRequestModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected String                                            bucketId;
  protected List<IRelationshipIdSourceModel>                  relationships;
  protected String                                            ruleId;
  protected String                                            goldenRecordId;
  protected List<String>                                      selectedLanguageCodes;
  protected Map<String, List<IConflictingValue>>              attributes;
  protected Map<String, List<IConflictingValue>>              tags;
  protected Map<String, Map<String, List<IConflictingValue>>> dependentAttributes;
  protected String                                            creationLanguage;
  protected List<String>                                      klassIds;
  protected List<String>                                      taxonomyIds;
  protected List<String>                                      addedLanguageCodes;
  protected List<String>                                      sourceIds;
  
  @Override
  public String getBucketId()
  {
    return bucketId;
  }
  
  @Override
  public void setBucketId(String bucketId)
  {
    this.bucketId = bucketId;
  }
  
  @Override
  public List<IRelationshipIdSourceModel> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    return relationships;
  }
  
  @JsonDeserialize(contentAs = RelationshipIdSourceModel.class)
  @Override
  public void setRelationships(List<IRelationshipIdSourceModel> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public String getGoldenRecordId()
  {
    return goldenRecordId;
  }
  
  @Override
  public void setGoldenRecordId(String goldenRecordId)
  {
    this.goldenRecordId = goldenRecordId;
  }
  
  @Override
  public List<String> getSelectedLanguageCodes()
  {
    if (selectedLanguageCodes == null) {
      selectedLanguageCodes = new ArrayList<>();
    }
    return selectedLanguageCodes;
  }
  
  @Override
  public void setSelectedLanguageCodes(List<String> selectedLanguageCodes)
  {
    this.selectedLanguageCodes = selectedLanguageCodes;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getAttributes()
  {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentUsing = AttributesConflictMapCustomDeserializer.class)
  public void setAttributes(Map<String, List<IConflictingValue>> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, Map<String, List<IConflictingValue>>> getDependentAttributes()
  {
    return dependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentUsing = DependentAttributesConflictMapCustomDeserializer.class)
  public void setDependentAttributes(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes)
  {
    this.dependentAttributes = dependentAttributes;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentUsing = TagConflictMapCustomDeserializer.class)
  public void setTags(Map<String, List<IConflictingValue>> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getCreationLanguage()
  {
    return creationLanguage;
  }
  
  @Override
  public void setCreationLanguage(String creationLanguage)
  {
    this.creationLanguage = creationLanguage;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getAddedLanguageCodes()
  {
    if (addedLanguageCodes == null) {
      addedLanguageCodes = new ArrayList<>();
    }
    return addedLanguageCodes;
  }
  
  @Override
  public void setAddedLanguageCodes(List<String> addedLanguageCodes)
  {
    this.addedLanguageCodes = addedLanguageCodes;
  }
  
  @Override
  public List<String> getSourceIds()
  {
    if (sourceIds == null) {
      sourceIds = new ArrayList<>();
    }
    return sourceIds;
  }
  
  @Override
  public void setSourceIds(List<String> sourceIds)
  {
    this.sourceIds = sourceIds;
  }
}
