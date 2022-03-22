package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.AbstractKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkSaveInstanceVariantsStrategyModel
    implements IBulkSaveInstanceVariantsStrategyModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected List<IKlassInstanceSaveModel> instancesToSave  = new ArrayList<>();
  protected IGetConfigDetailsModel        configDetails;
  
  @Override
  public List<IKlassInstanceSaveModel> getInstancesToSave()
  {
    return instancesToSave;
  }
  
  @JsonDeserialize(contentAs = AbstractKlassInstanceSaveModel.class)
  @Override
  public void setInstancesToSave(List<IKlassInstanceSaveModel> instancesToSave)
  {
    this.instancesToSave = instancesToSave;
  }
  
  @Override
  public IGetConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetConfigDetailsForCustomTabModel.class)
  @Override
  public void setConfigDetails(IGetConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
