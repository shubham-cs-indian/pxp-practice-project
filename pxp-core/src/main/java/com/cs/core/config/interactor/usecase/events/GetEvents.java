package com.cs.core.config.interactor.usecase.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.EventInformationModel;
import com.cs.core.runtime.interactor.model.configuration.EventModel;
import com.cs.core.runtime.interactor.model.configuration.IEventInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IEventModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.base.IInterceptableRuntimeInteractor;

@Service
public class GetEvents extends AbstractRuntimeInteractor<IIdParameterModel, IEventModel>
    implements IGetEvents {
  
  @Autowired
  ApplicationContext appContext;
  
  @Override
  public IEventModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    
    IEventModel returnModel = new EventModel();
    IEventInformationModel infoModel = new EventInformationModel();
    List<IEventInformationModel> response = new ArrayList<>();
    Map<String, IInterceptableRuntimeInteractor> beansOfType = appContext
        .getBeansOfType(IInterceptableRuntimeInteractor.class);
    for (IInterceptableRuntimeInteractor implementations : beansOfType.values()) {
      infoModel = new EventInformationModel();
      infoModel.setLabel(implementations.getLabel());
      infoModel.setEventClass(implementations.getClass()
          .getName());
      response.add(infoModel);
    }
    
    returnModel.setEventList(response);
    return returnModel;
  }
}
