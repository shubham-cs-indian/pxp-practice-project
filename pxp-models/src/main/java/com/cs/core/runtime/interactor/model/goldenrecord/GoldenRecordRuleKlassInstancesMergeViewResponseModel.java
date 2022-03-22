package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.ConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IMatchPropertiesModel;
import com.cs.core.config.interactor.model.goldenrecord.MatchPropertiesModel;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.attribute.DependentAttributesRecommendedCustomDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldenRecordRuleKlassInstancesMergeViewResponseModel
    implements IGoldenRecordRuleKlassInstancesMergeViewResponseModel {
  
  private static final long                                                serialVersionUID = 1L;
  
  protected IConfigDetailsForGetKlassInstancesToMergeModel                 configDetails;
  protected Map<String, IRecommendationModel>                              recommendation;
  protected List<String>                                                   klassInstanceIds;
  protected List<String>                                                   klassIds;
  protected List<String>                                                   taxonomyIds;
  protected String                                                         ruleId;
  protected IContentInstance                                               goldenRecordInstance;
  protected IMatchPropertiesModel                                          matchPropertiesModel;
  protected Map<String, IContentInfoWithAssetDetailsModel>                 sourceIdRelationshipContentsMap;
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets;
  protected List<String>                                                   contextTagIds;
  protected Map<String, Map<String, IRecommendationModel>>                 dependentAttributeRecommendation;
  protected Map<String, ITargetRelationshipInstancesModel>                 goldenRecordRelationshipInstances;
  
  @Override
  public IConfigDetailsForGetKlassInstancesToMergeModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ConfigDetailsForGetKlassInstancesToMergeModel.class)
  public void setConfigDetails(IConfigDetailsForGetKlassInstancesToMergeModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Map<String, IRecommendationModel> getRecommendation()
  {
    if(this.recommendation == null) {
      this.recommendation = new HashMap<>();
    }
    return recommendation;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractRecommendationModel.class)
  public void setRecommendation(Map<String, IRecommendationModel> recommendation)
  {
    this.recommendation = recommendation;
  }
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
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
  public IContentInstance getGoldenRecordInstance()
  {
    return goldenRecordInstance;
  }
  
  @Override
  @JsonDeserialize(as = AbstractContentInstance.class)
  public void setGoldenRecordInstance(IContentInstance goldenRecordInstance)
  {
    this.goldenRecordInstance = goldenRecordInstance;
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
  public IMatchPropertiesModel getMatchPropertiesModel()
  {
    return matchPropertiesModel;
  }
  
  @Override
  @JsonDeserialize(as = MatchPropertiesModel.class)
  public void setMatchPropertiesModel(IMatchPropertiesModel matchPropetiesModel)
  {
    this.matchPropertiesModel = matchPropetiesModel;
  }
  
  @Override
  public Map<String, IContentInfoWithAssetDetailsModel> getKlassInstances()
  {
    return sourceIdRelationshipContentsMap;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentInfoWithAssetDetailsModel.class)
  public void setKlassInstances(
      Map<String, IContentInfoWithAssetDetailsModel> sourceIdRelationshipContentsMap)
  {
    this.sourceIdRelationshipContentsMap = sourceIdRelationshipContentsMap;
  }
  
  @Override
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    if (referencedAssets == null) {
      referencedAssets = new HashMap<>();
    }
    return referencedAssets;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
  
  @Override
  public List<String> getContextTagIds()
  {
    if (contextTagIds == null) {
      contextTagIds = new ArrayList<>();
    }
    return contextTagIds;
  }
  
  @Override
  public void setContextTagIds(List<String> contextTagIds)
  {
    this.contextTagIds = contextTagIds;
  }
  
  @Override
  public Map<String, Map<String, IRecommendationModel>> getDependentAttributeRecommendation()
  {
    if (dependentAttributeRecommendation == null) {
      dependentAttributeRecommendation = new HashMap<>();
    }
    return dependentAttributeRecommendation;
  }
  
  @Override
  @JsonDeserialize(contentUsing = DependentAttributesRecommendedCustomDeserializer.class)
  public void setDependentAttributeRecommendation(
      Map<String, Map<String, IRecommendationModel>> dependentAttributeRecommendation)
  {
    this.dependentAttributeRecommendation = dependentAttributeRecommendation;
  }
  
  @Override
  public Map<String, ITargetRelationshipInstancesModel> getGoldenRecordRelationshipInstances()
  {
    if (goldenRecordRelationshipInstances == null) {
      goldenRecordRelationshipInstances = new HashMap<>();
    }
    return goldenRecordRelationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = TargetRelationshipInstancesModel.class)
  public void setGoldenRecordRelationshipInstances(
      Map<String, ITargetRelationshipInstancesModel> goldenRecordRelationshipInstances)
  {
    this.goldenRecordRelationshipInstances = goldenRecordRelationshipInstances;
  }
}
