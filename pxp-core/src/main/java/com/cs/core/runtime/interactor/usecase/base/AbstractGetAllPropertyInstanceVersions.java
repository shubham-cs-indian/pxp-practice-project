package com.cs.core.runtime.interactor.usecase.base;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancePropertyVersionModel;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetAllPropertyInstanceVersions<P extends IGetKlassInstancePropertyVersionModel, R extends IListModel<? extends IPropertyInstance>>
    extends AbstractRuntimeInteractor<P, R> {
  
  protected abstract R executeGetAllPropertyInstanceVersions(P getKlassInstancePropertyVersionModel)
      throws Exception;
  
  protected R executeInternal(P getKlassInstancePropertyVersionModel) throws Exception
  {
    return executeGetAllPropertyInstanceVersions(getKlassInstancePropertyVersionModel);
  }
}
