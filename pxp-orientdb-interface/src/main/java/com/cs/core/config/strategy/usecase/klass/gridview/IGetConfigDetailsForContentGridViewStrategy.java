package com.cs.core.config.strategy.usecase.klass.gridview;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.gridcontent.IConfigDetailsForContentGridViewModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetKlassConfigRequestModel;

public interface IGetConfigDetailsForContentGridViewStrategy
    extends IConfigStrategy<IGetKlassConfigRequestModel, IConfigDetailsForContentGridViewModel> {
  
}
