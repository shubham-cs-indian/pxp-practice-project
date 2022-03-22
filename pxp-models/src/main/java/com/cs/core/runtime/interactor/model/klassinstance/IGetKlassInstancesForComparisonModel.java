package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetMatchColumnModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeMatchColumnModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.tag.ITagMatchColumnModel;

import java.util.List;

public interface IGetKlassInstancesForComparisonModel extends IModel {
  
  public static final String KLASS_INSTANCES    = "klassInstances";
  public static final String MATCH_COLUMN       = "matchColumn";
  public static final String TAG_MATCH_COLUMN   = "tagMatchColumn";
  public static final String ASSET_MATCH_COLUMN = "assetMatchColumn";
  
  public List<IKlassInstance> getKlassInstances();
  
  public void setKlassInstances(List<IKlassInstance> klassInstances);
  
  public List<IAttributeMatchColumnModel> getMatchColumn();
  
  public void setMatchColumn(List<IAttributeMatchColumnModel> attributeMatchColumn);
  
  public List<ITagMatchColumnModel> getTagMatchColumn();
  
  public void setTagMatchColumn(List<ITagMatchColumnModel> tagMatchColumn);
  
  public List<IAssetMatchColumnModel> getAssetMatchColumn();
  
  public void setAssetMatchColumn(List<IAssetMatchColumnModel> assetMatchColumn);
}
