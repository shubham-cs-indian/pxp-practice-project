package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

import java.util.List;

public interface IBulkAssetInstanceInformationStrategyModel
    extends IBulkAssetInstanceInformationModel {
  
  public static final String COLLECTIONSTOADD                = "collectionsToAdd";
  public static final String COLLECTIONSTOSAVE               = "collectionsToSave";
  public static final String ASSETINSTANCESTOADD             = "assetInstancesToAdd";
  public static final String APPLY_EFFECT                    = "applyEffect";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA = "updateSearchableDocumentData";
  
  public List<IStaticCollection> getCollectionsToAdd();
  
  public void setCollectionsToAdd(List<IStaticCollection> collectionsToAdd);
  
  public List<IStaticCollection> getCollectionsToSave();
  
  public void setCollectionsToSave(List<IStaticCollection> collectionsToSave);
  
  public List<IAssetInstance> getAssetInstancesToAdd();
  
  public void setAssetInstancesToAdd(List<IAssetInstance> assetInstancesToAdd);
  
  public List<IApplyEffectModel> getApplyEffect();
  
  public void setApplyEffect(List<IApplyEffectModel> applyEffect);
  
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      List<IUpdateSearchableInstanceModel> updateSearchableDocumentData);
}
