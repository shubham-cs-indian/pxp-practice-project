package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class KlassInstancesListForComparisonModel implements IKlassInstancesListForComparisonModel {
  
  private static final long                         serialVersionUID = 1L;
  
  protected List<IKlassInstancesForComparisonModel> klassInstancesDetails;
  protected IGetConfigDetailsForCustomTabModel      configDetails;
  
  @Override
  public List<IKlassInstancesForComparisonModel> getKlassInstancesDetails()
  {
    return klassInstancesDetails;
  }
  
  @JsonDeserialize(contentAs = KlassInstancesForComparisonModel.class)
  @Override
  public void setKlassInstancesDetails(
      List<IKlassInstancesForComparisonModel> klassInstancesDetails)
  {
    this.klassInstancesDetails = klassInstancesDetails;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetConfigDetailsForCustomTabModel.class)
  @Override
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
