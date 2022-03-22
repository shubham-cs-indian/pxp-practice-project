package com.cs.core.runtime.contentgrid;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.contentgrid.IGetContentGridKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetContentGridRequestModel;

public interface IGetContentGridViewService
    extends IRuntimeService<IGetContentGridRequestModel, IGetContentGridKlassInstanceResponseModel> {
  
}
