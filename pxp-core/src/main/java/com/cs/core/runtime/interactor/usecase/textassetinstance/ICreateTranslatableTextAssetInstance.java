package com.cs.core.runtime.interactor.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTranslatableTextAssetInstance
    extends IRuntimeInteractor<ITextAssetInstanceSaveModel, IGetKlassInstanceModel> {
  
}
