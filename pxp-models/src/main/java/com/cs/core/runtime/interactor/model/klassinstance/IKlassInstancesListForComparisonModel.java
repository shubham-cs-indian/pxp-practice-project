package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;

public interface IKlassInstancesListForComparisonModel extends IModel {
  
  public static String KLASSINSTANCES_DETAILS = "klassInstancesDetails";
  public static String CONFIG_DETAILS         = "configDetails";
  
  public List<IKlassInstancesForComparisonModel> getKlassInstancesDetails();
  
  public void setKlassInstancesDetails(
      List<IKlassInstancesForComparisonModel> klassInstancesDetails);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
}
