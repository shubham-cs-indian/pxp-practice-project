package com.cs.pim.runtime.interactor.usecase.textassetinstance;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTextAssetInstanceSingleClone extends
    IRuntimeInteractor<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel> {
  
}