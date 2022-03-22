package com.cs.core.runtime.interactor.usecase.contentgrid;

import com.cs.core.runtime.interactor.model.contentgrid.IGetContentGridKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetContentGridRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetContentGridView extends IRuntimeInteractor<IGetContentGridRequestModel, IGetContentGridKlassInstanceResponseModel> {
  
}
