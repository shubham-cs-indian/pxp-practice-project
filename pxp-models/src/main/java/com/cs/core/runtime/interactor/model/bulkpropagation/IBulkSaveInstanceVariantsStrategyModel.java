package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;

import java.util.List;

public interface IBulkSaveInstanceVariantsStrategyModel extends IModel {
  
  public static final String INSTANCES_TO_SAVE = "instancesToSave";
  public static final String CONFIG_DETAILS    = "configDetails";
  
  public List<IKlassInstanceSaveModel> getInstancesToSave();
  
  public void setInstancesToSave(List<IKlassInstanceSaveModel> instancesToSave);
  
  public IGetConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsModel configDetails);
}
