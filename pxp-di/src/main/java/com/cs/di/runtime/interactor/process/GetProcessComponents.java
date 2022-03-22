package com.cs.di.runtime.interactor.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.cammunda.IProcessComponent;

@Component
public class GetProcessComponents extends
    AbstractRuntimeInteractor<IModel, IListModel<IIdLabelModel>> implements IGetProcessComponents {

  @Autowired protected ApplicationContext context;

  @Override public IListModel<IIdLabelModel> executeInternal(IModel dataModel) throws Exception
  {
    IListModel<IIdLabelModel> iListModel = new ListModel<>();
    List<IIdLabelModel> listOfIdLabelModel = new ArrayList<>();

    Map<String, ? extends IProcessComponent> customComponentsMap = context.getBeansOfType(
        IProcessComponent.class);

    for (IProcessComponent customComponent : customComponentsMap.values()) {
      String componentId = customComponent.getComponentControllerBeanName();
      String componentlabel = customComponent.getComponentLabel();
      if (componentId != null && componentlabel != null) {
        IIdLabelModel idLabelModel = new IdLabelModel();
        idLabelModel.setId(componentId);
        idLabelModel.setLabel(componentlabel);

        listOfIdLabelModel.add(idLabelModel);
      }

    }

    iListModel.setList(listOfIdLabelModel);
    return iListModel;
  }

}
