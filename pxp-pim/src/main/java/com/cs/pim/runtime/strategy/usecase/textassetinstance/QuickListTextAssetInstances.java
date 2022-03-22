package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.stereotype.Service;
import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickList;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IQuickListTextAssetInstances;


@Service
public class QuickListTextAssetInstances extends
    AbstractInstanceQuickList<IKlassInstanceQuickListModel, IListModel<IKlassInstanceInformationModel>>
    implements IQuickListTextAssetInstances {
  
  @Override
  protected IListModel<IKlassInstanceInformationModel> executeInternal(
      IKlassInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new TextAssetKlassNotFoundException();
    }
  }

  
  @Override
  protected String getMode()
  {
    return Constants.TEXT_ASSET_MODE;
  }


  @Override
  protected IListModel<IKlassInstanceInformationModel> executeGetQuickListElements(
      IKlassInstanceQuickListModel klassInstanceQuickListModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
