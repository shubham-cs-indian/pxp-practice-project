package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;

public interface IGetSectionInfoForKlassService
    extends IGetConfigService<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel> {
  
}
