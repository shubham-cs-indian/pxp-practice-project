package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;
import java.util.Map;

public interface IDashboardInformationResponseModel extends IModel {
  
  public static final String TOTAL_CONTENTS     = "totalContents";
  public static final String FILTER_INFO        = "filterInfo";
  public static final String CHILDREN           = "children";
  public static final String REFERENCED_KLASSES = "referencedKlasses";
  
  public long getTotalContents();
  
  public void setTotalContents(long totalContents);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public List<IKlassInstanceInformationModel> getChildren();
  
  public void setChildren(List<IKlassInstanceInformationModel> childrens);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
}
