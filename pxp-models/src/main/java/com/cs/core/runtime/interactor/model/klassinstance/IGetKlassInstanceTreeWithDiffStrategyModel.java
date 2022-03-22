package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetKlassInstanceTreeWithDiffStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE      = "klassInstance";
  public static final String KLASS_INSTANCE_DIFF = "klassInstanceDiff";
  public static final String REFERENCED_ASSETS   = "referencedAssets";
  
  public IKlassInstance getKlassInstance();
  
  public void setKlassInstance(IKlassInstance klassInstance);
  
  public IKlassInstanceDiffModel getKlassInstanceDiff();
  
  public void setKlassInstanceDiff(IKlassInstanceDiffModel klassInstanceDiffModel);
  
  Map<String, ? extends IReferenceAssetModel> getReferencedAssets();
  
  void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets);
}
