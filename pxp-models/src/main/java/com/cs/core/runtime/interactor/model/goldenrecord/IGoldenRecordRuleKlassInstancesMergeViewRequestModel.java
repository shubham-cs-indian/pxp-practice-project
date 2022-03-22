package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IGoldenRecordRuleKlassInstancesMergeViewRequestModel extends IModel {
  
  public static final String GOLDEN_RECORD_ID                    = "goldenRecordId";
  public static final String BUCKET_INSTANCE_ID                  = "bucketInstanceId";
  public static final String LANGUAGE_CODE                       = "languageCode";
  public static final String MERGE_EFFECT                        = "mergeEffect";
  public static final String DEPENDEND_ATTRIBUTE_IDS             = "dependendAttributeIds";
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String FROM                                = "from";
  public static final String SIZE                                = "size";
  public static final String IS_AUTO_CREATE                      = "isAutoCreate";
  String                     ALL_LOCALES                         = "allLocales";
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
  
  public String getBucketInstanceId();
  
  public void setBucketInstanceId(String bucketInstanceId);
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
  
  public IMergeEffect getMergeEffect();
  
  public void setMergeEffect(IMergeEffect mergeEffect);
  
  public List<String> getDependentAttributeIds();
  
  public void setDependentAttributeIds(List<String> dependentAttributeIds);
  
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public Boolean getIsAutoCreate();
  
  public void setIsAutoCreate(Boolean isAutoCreate);
  
  List<String> getAllLocales();
  void setAllLocales(List<String> allLocales);
}
