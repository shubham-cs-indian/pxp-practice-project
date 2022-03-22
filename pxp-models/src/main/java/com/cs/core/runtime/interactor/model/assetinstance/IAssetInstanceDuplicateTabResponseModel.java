package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

import java.util.List;
import java.util.Map;

public interface IAssetInstanceDuplicateTabResponseModel extends IModel {
  
  public static final String ASSET_INSTANCES    = "assetInstances";
  public static final String REFERENCED_KLASSES = "referencedKlasses";
  public static final String TOTAL_CONTENTS     = "totalContents";
  public static final String FROM               = "from";
  
  public List<IKlassInstanceInformationModel> getAssetInstances();
  
  public void setAssetInstances(List<IKlassInstanceInformationModel> assetInstances);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  public long getTotalContents();
  
  public void setTotalContents(long totalContents);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
}
