package com.cs.di.runtime.interactor.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;


@Service
public class GetAllProcessComponents
    extends AbstractRuntimeInteractor<IModel, IListModel<IIdLabelModel>>
    implements IGetAllProcessComponents {
  
  @Autowired
  IGetProcessComponents getProcessComponents;
  
  @Override
  public IListModel<IIdLabelModel> executeInternal(IModel dataModel) throws Exception
  {

    return getProcessComponents.execute(dataModel);
  }

}
