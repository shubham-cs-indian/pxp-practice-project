package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetInstanceDuplicateTabResponseModel
    implements IAssetInstanceDuplicateTabResponseModel {
  
  private static final long                                  serialVersionUID  = 1L;
  protected List<IKlassInstanceInformationModel>             assetInstances;
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = new HashMap<>();
  protected long                                             totalContents     = 0l;
  protected Integer                                          from;
  
  @Override
  public List<IKlassInstanceInformationModel> getAssetInstances()
  {
    return assetInstances;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  @Override
  public void setAssetInstances(List<IKlassInstanceInformationModel> assetInstances)
  {
    this.assetInstances = assetInstances;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public long getTotalContents()
  {
    return totalContents;
  }
  
  @Override
  public void setTotalContents(long totalContents)
  {
    this.totalContents = totalContents;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
}
