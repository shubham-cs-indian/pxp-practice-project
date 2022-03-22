package com.cs.core.runtime.notification;

import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class ChangeNotificationStatusService extends AbstractRuntimeService<IIdsListParameterModel, IIdsListParameterModel>
    implements IChangeNotificationStatusService {
  
  // @Autowired
  // protected IChangeNotificationStatusStrategy
  // changeNotificationStatusStrategy;
  
  @Override
  public IIdsListParameterModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    // return changeNotificationStatusStrategy.execute(dataModel);
    return null;
  }
}
