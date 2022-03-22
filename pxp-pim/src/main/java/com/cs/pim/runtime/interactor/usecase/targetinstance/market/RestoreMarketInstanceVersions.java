package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractRestoreInstanceVersions;
import com.cs.pim.runtime.targetinstance.market.IRestoreMarketInstanceVersionsService;

@Service
public class RestoreMarketInstanceVersions
    extends AbstractRestoreInstanceVersions<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IRestoreMarketInstanceVersions {
  
  @Autowired
  IRestoreMarketInstanceVersionsService restoreMarketInstanceVersionsService;
  
  @Override
  protected IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel model) throws Exception
  {
    return restoreMarketInstanceVersionsService.execute(model);
  }
  
}
