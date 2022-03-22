package com.cs.di.runtime.interactor.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetAllCustomComponents
    extends AbstractRuntimeInteractor<IModel, IListModel<IIdLabelModel>>
    implements IGetAllCustomComponents {

  @Autowired protected ApplicationContext context;

  @Autowired IGetProcessCustomComponents getProcessCustomComponents;

  @Override public IListModel<IIdLabelModel> executeInternal(IModel dataModel) throws Exception
  {

    return getProcessCustomComponents.execute(dataModel);
  }

}
