package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;
import java.util.Map;

public interface ICreateGoldenRecordStrategyModel extends IModel {
  
  public static final String BUCKET_ID            = "bucketId";
  public static final String ATTRIBUTES           = "attributes";
  public static final String DEPENDENT_ATTRIBUTES = "dependentAttributes";
  public static final String TAGS                 = "tags";
  public static final String KLASS_INSTANCE       = "klassInstance";
  public static final String LANGUAGE_INSTANCES   = "languageInstances";
  public static final String RELATIONSIP_INFO     = "relationshipInfo";
  public static final String CONFIG_DETAILS       = "configDetails";
  public static final String SOURCE_IDS           = "sourceIds";
  
  public String getBucketId();
  
  public void setBucketId(String bucketId);
  
  public IKlassInstance getKlassInstance();
  
  public void setKlassInstance(IKlassInstance klassInstance);
  
  public List<IRelationshipIdSourceModel> getRelationshipInfo();
  
  public void setRelationshipInfo(List<IRelationshipIdSourceModel> relationshipInfo);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
  
  public List<ILanguageKlassInstance> getLanguageInstances();
  
  public void setLanguageInstances(List<ILanguageKlassInstance> languageInstances);
  
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
  
  public List<String> getSourceIds();
  
  public void setSourceIds(List<String> sourceIds);
}
