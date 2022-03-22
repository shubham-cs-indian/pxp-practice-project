package com.cs.pim.config.interactor.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConflictSourcesInformation;
import com.cs.pim.config.klassinstance.IGetConflictSourcesInformationService;

@Service
public class GetConflictSourcesInformation extends
    AbstractRuntimeInteractor<IConflictSourcesRequestModel, IGetConflictSourcesInformationModel>
    implements IGetConflictSourcesInformation {
  
  @Autowired
  protected IGetConflictSourcesInformationService getConflictSourcesInformationService;
  
  @Override
  public IGetConflictSourcesInformationModel executeInternal(IConflictSourcesRequestModel dataModel)
      throws Exception
  {
    return getConflictSourcesInformationService.execute(dataModel);
  }

}
