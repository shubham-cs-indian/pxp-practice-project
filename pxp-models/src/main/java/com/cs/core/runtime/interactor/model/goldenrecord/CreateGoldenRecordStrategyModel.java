package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;
import com.cs.core.runtime.interactor.model.attribute.AttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.attribute.DependentAttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.tag.TagConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGoldenRecordStrategyModel implements ICreateGoldenRecordStrategyModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected String                                            bucketId;
  protected IKlassInstance                                    klassInstance;
  protected List<IRelationshipIdSourceModel>                  relationshipInfo;
  protected IGetConfigDetailsForCustomTabModel                configDetails;
  protected List<ILanguageKlassInstance>                      languageInstances;
  protected Map<String, List<IConflictingValue>>              attributes;
  protected Map<String, List<IConflictingValue>>              tags;
  protected Map<String, Map<String, List<IConflictingValue>>> dependentAttributes;
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
  public IKlassInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @JsonDeserialize(as = AbstractKlassInstance.class)
  @Override
  public void setKlassInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public List<IRelationshipIdSourceModel> getRelationshipInfo()
  {
    return relationshipInfo;
  }
  
  @JsonDeserialize(contentAs = RelationshipIdSourceModel.class)
  @Override
  public void setRelationshipInfo(List<IRelationshipIdSourceModel> relationshipInfo)
  {
    this.relationshipInfo = relationshipInfo;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetConfigDetailsForCustomTabModel.class)
  @Override
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<ILanguageKlassInstance> getLanguageInstances()
  {
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
