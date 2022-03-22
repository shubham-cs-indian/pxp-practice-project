package com.cs.core.config.interactor.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;

@Component
public abstract class AbstractGetSectionInfo<P extends IGetSectionInfoForTypeRequestModel, R extends IGetSectionInfoModel>
    extends AbstractGetConfigInteractor<P, R> {
  
  protected abstract R executeGetSectionInfo(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return executeGetSectionInfo(model);
  }
}
