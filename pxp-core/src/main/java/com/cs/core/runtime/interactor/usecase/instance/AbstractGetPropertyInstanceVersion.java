package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.core.runtime.interactor.model.datarule.IPropertyInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancePropertyVersionModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetPropertyInstanceVersion<P extends IGetKlassInstancePropertyVersionModel, R extends IPropertyInstanceModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  protected abstract R executeGetPropertyInstanceVersion(P getKlassInstancePropertyVersionModel)
      throws Exception;
  
  protected R executeInternal(P getKlassInstancePropertyVersionModel) throws Exception
  {
    return executeGetPropertyInstanceVersion(getKlassInstancePropertyVersionModel);
  }
}
