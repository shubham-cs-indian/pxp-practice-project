package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetMatchColumnModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetMatchColumnModel;
import com.cs.core.runtime.interactor.model.attribute.AttributeMatchColumnModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeMatchColumnModel;
import com.cs.core.runtime.interactor.model.tag.ITagMatchColumnModel;
import com.cs.core.runtime.interactor.model.tag.TagMatchColumnModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetKlassInstancesForComparisonModel implements IGetKlassInstancesForComparisonModel {
  
  protected List<IKlassInstance>             klassInstances;
  protected List<IAttributeMatchColumnModel> matchColumn;
  protected List<ITagMatchColumnModel>       matchTagColumn;
  protected List<IAssetMatchColumnModel>     matchAssetColumn;
  
  @Override
  public List<IKlassInstance> getKlassInstances()
  {
    return klassInstances;
  }
  
  @Override
  public void setKlassInstances(List<IKlassInstance> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public List<IAttributeMatchColumnModel> getMatchColumn()
  {
    return matchColumn;
  }
  
  @JsonDeserialize(contentAs = AttributeMatchColumnModel.class)
  @Override
  public void setMatchColumn(List<IAttributeMatchColumnModel> matchColumn)
  {
    this.matchColumn = matchColumn;
  }
  
  @JsonDeserialize(contentAs = TagMatchColumnModel.class)
  @Override
  public List<ITagMatchColumnModel> getTagMatchColumn()
  {
    if (matchTagColumn == null) {
      matchTagColumn = new ArrayList<ITagMatchColumnModel>();
    }
    return matchTagColumn;
  }
  
  @Override
  public void setTagMatchColumn(List<ITagMatchColumnModel> tagMatchColumn)
  {
    this.matchTagColumn = tagMatchColumn;
  }
  
  @JsonDeserialize(contentAs = AssetMatchColumnModel.class)
  @Override
  public List<IAssetMatchColumnModel> getAssetMatchColumn()
  {
    if (matchAssetColumn == null) {
      matchAssetColumn = new ArrayList<IAssetMatchColumnModel>();
    }
    return matchAssetColumn;
  }
  
  @Override
  public void setAssetMatchColumn(List<IAssetMatchColumnModel> assetMatchColumn)
  {
    this.matchAssetColumn = assetMatchColumn;
  }
}
