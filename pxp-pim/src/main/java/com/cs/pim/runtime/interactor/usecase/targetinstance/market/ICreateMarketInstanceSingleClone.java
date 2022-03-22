package com.cs.pim.runtime.interactor.usecase.targetinstance.market;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateMarketInstanceSingleClone extends
    IRuntimeInteractor<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel> {
  
}