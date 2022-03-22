package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldenRecordRuleKlassInstancesMergeViewRequestModel
    implements IGoldenRecordRuleKlassInstancesMergeViewRequestModel {
  
  private static final long                                     serialVersionUID = 1L;
  protected String                                              goldenRecordId;
  protected String                                              bucketInstanceId;
  protected String                                              languageCode;
  protected IMergeEffect                                        mergeEffect;
  protected List<String>                                        dependentAttributeIds;
  protected Map<String, List<String>>                           klassIdSideIdsMap;
  protected Map<String, List<String>>                           relaitonshipIdSideIdsMap;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected Integer                                             from;
  protected Integer                                             size;
  protected Boolean                                             isAutoCreate     = false;
  protected List<String>                                        allLocales       = new ArrayList<>();
  
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
  public String getBucketInstanceId()
  {
    return bucketInstanceId;
  }
  
  @Override
  public void setBucketInstanceId(String bucketInstanceId)
  {
    this.bucketInstanceId = bucketInstanceId;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }
  
  @Override
  public IMergeEffect getMergeEffect()
  {
    return mergeEffect;
  }
  
  @Override
  public void setMergeEffect(IMergeEffect mergeEffect)
  {
    this.mergeEffect = mergeEffect;
  }
  
  @Override
  public List<String> getDependentAttributeIds()
  {
    if (dependentAttributeIds == null) {
      dependentAttributeIds = new ArrayList<>();
    }
    return dependentAttributeIds;
  }
  
  @Override
  public void setDependentAttributeIds(List<String> dependentAttributeIds)
  {
    this.dependentAttributeIds = dependentAttributeIds;
  }
  
  @Override
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
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
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
  
  @Override
  public List<String> getAllLocales()
  {
    return this.allLocales;
  }
  
  @Override
  public void setAllLocales(List<String> allLocales)
  {
    this.allLocales = allLocales;
  }
}
