package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.collection.StaticCollection;
import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.model.datarule.ApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkAssetInstanceInformationStrategyModel extends BulkAssetInstanceInformationModel
    implements IBulkAssetInstanceInformationStrategyModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected List<IStaticCollection>              collectionsToAdd;
  protected List<IStaticCollection>              collectionsToSave;
  protected List<IAssetInstance>                 assetInstancesToAdd;
  protected List<IApplyEffectModel>              applyEffect;
  protected List<IUpdateSearchableInstanceModel> updateSearchableDocumentData;
  
  @Override
  public List<IStaticCollection> getCollectionsToAdd()
  {
    return collectionsToAdd;
  }
  
  @JsonDeserialize(contentAs = StaticCollection.class)
  public void setCollectionsToAdd(List<IStaticCollection> collectionsToAdd)
  {
    this.collectionsToAdd = collectionsToAdd;
  }
  
  public List<IStaticCollection> getCollectionsToSave()
  {
    return collectionsToSave;
  }
  
  @JsonDeserialize(contentAs = StaticCollection.class)
  public void setCollectionsToSave(List<IStaticCollection> collectionsToSave)
  {
    this.collectionsToSave = collectionsToSave;
  }
  
  public List<IAssetInstance> getAssetInstancesToAdd()
  {
    return assetInstancesToAdd;
  }
  
  @JsonDeserialize(contentAs = AssetInstance.class)
  public void setAssetInstancesToAdd(List<IAssetInstance> assetInstancesToAdd)
  {
    this.assetInstancesToAdd = assetInstancesToAdd;
  }
  
  @Override
  public List<IApplyEffectModel> getApplyEffect()
  {
    return applyEffect;
  }
  
  @JsonDeserialize(contentAs = ApplyEffectModel.class)
  @Override
  public void setApplyEffect(List<IApplyEffectModel> applyEffect)
  {
    this.applyEffect = applyEffect;
  }
  
  @Override
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableDocumentData()
  {
    return updateSearchableDocumentData;
  }
  
  @Override
  @JsonDeserialize(contentAs = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableDocumentData(
      List<IUpdateSearchableInstanceModel> updateSearchableDocumentData)
  {
    this.updateSearchableDocumentData = updateSearchableDocumentData;
  }
}
