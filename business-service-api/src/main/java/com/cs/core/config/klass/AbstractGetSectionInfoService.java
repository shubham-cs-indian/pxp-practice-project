package com.cs.core.config.klass;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;

@Component
public abstract class AbstractGetSectionInfoService<P extends IGetSectionInfoForTypeRequestModel, R extends IGetSectionInfoModel>
    extends AbstractGetConfigService<P, R> {
  
  protected abstract R executeGetSectionInfo(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return executeGetSectionInfo(model);
  }
}
