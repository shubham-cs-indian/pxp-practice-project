package com.cs.core.runtime.interactor.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IDeleteKlassInstance;
import com.cs.core.runtime.klassinstance.IDeleteKlassInstanceService;

@Service
public class DeleteKlassInstance extends
    AbstractRuntimeInteractor<IDeleteKlassInstanceRequestModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteKlassInstance {
  
  @Autowired
  protected IDeleteKlassInstanceService deleteKlassInstanceService;
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(
      IDeleteKlassInstanceRequestModel deleteModel) throws Exception
  {
    return deleteKlassInstanceService.execute(deleteModel);
  }
}
