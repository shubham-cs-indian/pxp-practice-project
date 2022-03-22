package com.cs.di.runtime.interactor.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public class GetProcessCustomComponents
    extends AbstractRuntimeInteractor<IModel, IListModel<IIdLabelModel>>
    implements IGetProcessCustomComponents {

  @Autowired protected ApplicationContext context;

  @Override public IListModel<IIdLabelModel> executeInternal(IModel dataModel) throws Exception
  {/*

   IListModel<IIdLabelModel> iListModel = new ListModel<>();
   List<IIdLabelModel> listOfIdLabelModel = new ArrayList<>();
   
   Map<String, ? extends IDiCustomComponent> customComponentsMap = context.getBeansOfType(IDiCustomComponent.class);
   
   for (IDiCustomComponent customComponent : customComponentsMap.values()) {
     String componentId = customComponent.getComponentName().getKey();
     String componentlabel = customComponent.getComponentName().getValue();
     
     IIdLabelModel idLabelModel = new IdLabelModel();
     idLabelModel.setId(componentId);
     idLabelModel.setLabel(componentlabel);
     
     listOfIdLabelModel.add(idLabelModel);
   }
   
   iListModel.setList(listOfIdLabelModel);
   return iListModel;
   */
    return null;
  }

}
