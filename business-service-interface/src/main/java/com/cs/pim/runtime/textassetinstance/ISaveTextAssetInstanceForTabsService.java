package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;

public interface ISaveTextAssetInstanceForTabsService
    extends IRuntimeService<ITextAssetInstanceSaveModel, IGetKlassInstanceModel> {
  
}
