package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IMatchPropertiesModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGoldenRecordRuleKlassInstancesMergeViewResponseModel extends IModel {
  
  public static final String CONFIG_DETAILS                       = "configDetails";
  public static final String RECOMMENDATION                       = "recommendation";
  public static final String KLASS_INSTANCE_IDS                   = "klassInstanceIds";
  public static final String KLASS_IDS                            = "klassIds";
  public static final String TAXONOMY_IDS                         = "taxonomyIds";
  public static final String GOLDEN_RECORD_INSTANCE               = "goldenRecordInstance";
  public static final String RULE_ID                              = "ruleId";
  public static final String MATCH_PROPERTIES_MODEL               = "matchPropertiesModel";
  public static final String KLASS_INSTANCES                      = "klassInstances";
  public static final String REFERENCED_ASSETS                    = "referencedAssets";
  public static final String CONTEXT_TAG_IDS                      = "contextTagIds";
  public static final String DEPENDENT_ATTRIBUTE_RECOMMENDATION   = "dependentAttributeRecommendation";
  public static final String GOLDEN_RECORD_RELATIONSHIP_INSTANCES = "goldenRecordRelationshipInstances";
  
  public IConfigDetailsForGetKlassInstancesToMergeModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGetKlassInstancesToMergeModel configDetails);
  
  public Map<String, IRecommendationModel> getRecommendation();
  
  public void setRecommendation(Map<String, IRecommendationModel> recommendation);
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public IContentInstance getGoldenRecordInstance();
  
  public void setGoldenRecordInstance(IContentInstance goldenRecordInstance);
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public IMatchPropertiesModel getMatchPropertiesModel();
  
  public void setMatchPropertiesModel(IMatchPropertiesModel matchPropetiesModel);
  
  // key:contentId
  public Map<String, IContentInfoWithAssetDetailsModel> getKlassInstances();
  
  public void setKlassInstances(
      Map<String, IContentInfoWithAssetDetailsModel> sourceIdRelationshipContentsMap);
  
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets);
  
  public List<String> getContextTagIds();
  
  public void setContextTagIds(List<String> contextTagIds);
  
  public Map<String, Map<String, IRecommendationModel>> getDependentAttributeRecommendation();
  
  public void setDependentAttributeRecommendation(
      Map<String, Map<String, IRecommendationModel>> dependentAttributeRecommendation);
  
  public Map<String, ITargetRelationshipInstancesModel> getGoldenRecordRelationshipInstances();
  
  public void setGoldenRecordRelationshipInstances(
      Map<String, ITargetRelationshipInstancesModel> goldenRecordRelationshipInstances);
}
