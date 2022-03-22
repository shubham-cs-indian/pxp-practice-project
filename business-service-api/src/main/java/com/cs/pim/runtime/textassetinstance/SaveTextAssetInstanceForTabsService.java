package com.cs.pim.runtime.textassetinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;

@Service
public class SaveTextAssetInstanceForTabsService extends AbstractSaveInstanceForTabs<ITextAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveTextAssetInstanceForTabsService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ITextAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new TextAssetKlassNotFoundException(e);
    }
    return response;
  }
}