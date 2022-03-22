package com.cs.pim.runtime.interactor.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateArticleInstanceSingleClone extends
    IRuntimeInteractor<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel> {
  
}