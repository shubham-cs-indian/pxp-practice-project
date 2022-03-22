package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickList;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.IQuickListMarketInstances;

@Service
public class QuickListMarketInstances extends
    AbstractInstanceQuickList<IKlassInstanceQuickListModel, IListModel<IKlassInstanceInformationModel>>
    implements IQuickListMarketInstances {
  
  @Override
  protected IListModel<IKlassInstanceInformationModel> executeInternal(
      IKlassInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException();
    }
  }
  
  @Override
  protected String getMode()
  {
    return Constants.TARGET_MODE;
  }
  
  @Override
  protected IListModel<IKlassInstanceInformationModel> executeGetQuickListElements(
      IKlassInstanceQuickListModel klassInstanceQuickListModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
