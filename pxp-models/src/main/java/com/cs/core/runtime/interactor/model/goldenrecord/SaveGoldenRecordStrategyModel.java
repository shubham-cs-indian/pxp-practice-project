package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;
import com.cs.core.runtime.interactor.model.attribute.AttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.attribute.DependentAttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.tag.TagConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaveGoldenRecordStrategyModel implements ISaveGoldenRecordStrategyModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected String                                            bucketId;
  protected String                                            goldenRecordId;
  protected List<IRelationshipIdSourceModel>                  relationshipInfo;
  protected IGetConfigDetailsForCustomTabModel                configDetails;
  protected List<ILanguageKlassInstance>                      languageInstances;
  
  protected Map<String, List<IConflictingValue>>              attributes;
  protected Map<String, List<IConflictingValue>>              tags;
  protected Map<String, Map<String, List<IConflictingValue>>> dependentAttributes;
  
  protected List<String>                                      klassIds;
  protected List<String>                                      taxonomyIds;
  protected List<String>                                      selectedTaxonomyIds;
  protected List<String>                                      sourceIds;
  
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
  public List<IRelationshipIdSourceModel> getRelationshipInfo()
  {
    if (relationshipInfo == null) {
      relationshipInfo = new ArrayList<>();
    }
    return relationshipInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipIdSourceModel.class)
  public void setRelationshipInfo(List<IRelationshipIdSourceModel> relationshipInfo)
  {
    this.relationshipInfo = relationshipInfo;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsForCustomTabModel.class)
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<ILanguageKlassInstance> getLanguageInstances()
  {
    if (languageInstances == null) {
      languageInstances = new ArrayList<>();
    }
    return languageInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipIdSourceModel.class)
  public void setLanguageInstances(List<ILanguageKlassInstance> languageInstances)
  {
    this.languageInstances = languageInstances;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentUsing = AttributesConflictMapCustomDeserializer.class)
  public void setAttributes(Map<String, List<IConflictingValue>> attributes)
  {
    this.attributes = attributes;
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
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
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
