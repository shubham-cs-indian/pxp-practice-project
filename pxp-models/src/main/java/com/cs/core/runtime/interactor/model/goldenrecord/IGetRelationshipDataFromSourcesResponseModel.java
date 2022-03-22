package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetRelationshipDataFromSourcesResponseModel extends IModel {
  
  public static final String SOURCE_ID_RELATIONSHIP_INSTANCES_MAP = "sourceIdRelationshipInstancesMap";
  public static final String SOURCE_IDS                           = "sourceIds";
  public static final String KLASS_INSTANCES                      = "klassInstances";
  public static final String REFERENCED_ASSETS                    = "referencedAssets";
  public static final String CONFIG_DETAILS                       = "configDetails";
  public static final String CONTEXT_TAG_IDS                      = "contextTagIds";
  
  // key: sourceContentId from golden record bucket
  public Map<String, ITargetRelationshipInstancesModel> getSourceIdRelationshipInstancesMap();
  
  public void setSourceIdRelationshipInstancesMap(
      Map<String, ITargetRelationshipInstancesModel> sourceIdRelationshipInstancesMap);
  
  public List<String> getSourceIds();
  
  public void setSourceIds(List<String> sourceIds);
  
  // key:contentId
  public Map<String, IContentInfoWithAssetDetailsModel> getKlassInstances();
  
  public void setKlassInstances(
      Map<String, IContentInfoWithAssetDetailsModel> sourceIdRelationshipContentsMap);
  
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets);
  
  public IConfigDetailsForGetRelationshipDataModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGetRelationshipDataModel configDetails);
  
  public List<String> getContextTagIds();
  
  public void setContextTagIds(List<String> contextTagIds);
}
