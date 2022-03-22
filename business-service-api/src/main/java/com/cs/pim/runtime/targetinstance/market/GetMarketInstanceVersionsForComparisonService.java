package com.cs.pim.runtime.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.MarketInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForTimelineTabStrategy;
import com.cs.core.runtime.klassinstance.AbstractGetKlassInstanceVersionsForComparisonService;

@Service
public class GetMarketInstanceVersionsForComparisonService extends AbstractGetKlassInstanceVersionsForComparisonService<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetMarketInstanceVersionsForComparisonService {
  
  @Autowired
  protected IGetConfigDetailsForTimelineTabStrategy getConfigDetailsForTimelineTabStrategy;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception
  {
    
    return getConfigDetailsForTimelineTabStrategy.execute(idParameterModel);
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.MARKET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IContentInstance getContentInstance()
  {
    return new MarketInstance();
  }
  
}
