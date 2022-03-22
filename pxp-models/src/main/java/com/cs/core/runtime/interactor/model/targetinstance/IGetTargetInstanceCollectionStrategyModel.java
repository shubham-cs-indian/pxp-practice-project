package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.ITargetInstanceCollection;
import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceDiffModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

import java.util.List;
import java.util.Map;

public interface IGetTargetInstanceCollectionStrategyModel
    extends IRuntimeModel, ITargetInstanceCollection {
  
  public static final String CHILDREN            = "children";
  public static final String MARKET_INSTANCES    = "marketInstances";
  public static final String KLASS_INSTANCE_DIFF = "klassInstanceDiff";
  public static final String REFERENCED_ASSETS   = "referencedAssets";
  
  public List<IKlassInstanceInformationModel> getChildren();
  
  public void setChildren(List<IKlassInstanceInformationModel> childrens);
  
  public List<IKlassInstanceInformationModel> getMarketInstances();
  
  public void setMarketInstances(List<IKlassInstanceInformationModel> markets);
  
  public IKlassInstanceDiffModel getKlassInstanceDiff();
  
  public void setKlassInstanceDiff(IKlassInstanceDiffModel klassInstanceDiff);
  
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets();
  
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets);
}
