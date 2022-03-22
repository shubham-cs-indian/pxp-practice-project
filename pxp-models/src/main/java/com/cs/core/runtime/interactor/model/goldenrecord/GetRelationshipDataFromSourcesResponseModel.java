package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRelationshipDataFromSourcesResponseModel
    implements IGetRelationshipDataFromSourcesResponseModel {
  
  private static final long                                                serialVersionUID = 1L;
  protected Map<String, ITargetRelationshipInstancesModel>                 sourceIdRelationshipInstancesMap;
  protected List<String>                                                   sourceIds;
  protected Map<String, IContentInfoWithAssetDetailsModel>                 klassInstances;
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets;
  protected IConfigDetailsForGetRelationshipDataModel                      configDetails;
  protected List<String>                                                   contextTagIds;
  
  @Override
  public Map<String, ITargetRelationshipInstancesModel> getSourceIdRelationshipInstancesMap()
  {
    if (sourceIdRelationshipInstancesMap == null) {
      sourceIdRelationshipInstancesMap = new HashMap<>();
    }
    return sourceIdRelationshipInstancesMap;
  }
  
  @JsonDeserialize(contentAs = TargetRelationshipInstancesModel.class)
  @Override
  public void setSourceIdRelationshipInstancesMap(
      Map<String, ITargetRelationshipInstancesModel> sourceIdRelationshipInstancesMap)
  {
    this.sourceIdRelationshipInstancesMap = sourceIdRelationshipInstancesMap;
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
  
  @Override
  public Map<String, IContentInfoWithAssetDetailsModel> getKlassInstances()
  {
    if (klassInstances == null) {
      klassInstances = new HashMap<>();
    }
    return klassInstances;
  }
  
  @JsonDeserialize(contentAs = ContentInfoWithAssetDetailsModel.class)
  @Override
  public void setKlassInstances(Map<String, IContentInfoWithAssetDetailsModel> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    if (referencedAssets == null) {
      referencedAssets = new HashMap<>();
    }
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  @Override
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
  
  @Override
  public IConfigDetailsForGetRelationshipDataModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGetRelationshipDataModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGetRelationshipDataModel configDetails)
  {
    this.configDetails = configDetails;
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
}
