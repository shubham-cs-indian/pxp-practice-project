package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardInformationResponseModel implements IDashboardInformationResponseModel {
  
  private static final long                                  serialVersionUID  = 1L;
  protected long                                             totalContents     = 0l;
  protected IGetFilterInfoModel                              filterInfo;
  protected List<IKlassInstanceInformationModel>             children;
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = new HashMap<>();
  
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
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @JsonDeserialize(as = GetFilterInfoModel.class)
  @Override
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public List<IKlassInstanceInformationModel> getChildren()
  {
    if (children == null) {
      children = new ArrayList<>();
    }
    return this.children;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  @Override
  public void setChildren(List<IKlassInstanceInformationModel> children)
  {
    this.children = children;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = (Map<String, IReferencedKlassDetailStrategyModel>) referencedKlasses;
  }
}
