package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetTargetInstanceCollectionModel extends IModel {
  
  public static final String KLASSINSTANCE_COLLECTION = "klassInstanceCollection";
  public static final String TYPE_KLASS               = "typeKlass";
  public static final String REFERENCED_ASSETS        = "referencedAssets";
  
  public IGetTargetInstanceCollectionStrategyModel getKlassInstanceCollection();
  
  public void setKlassInstanceCollection(IGetTargetInstanceCollectionStrategyModel targetInstance);
  
  public ITarget getTypeKlass();
  
  public void setTypeKlass(ITarget typeAsset);
  
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets();
  
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets);
}
