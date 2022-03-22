package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.versions.AbstractRestoreInstanceVersionsService;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;

@Service
public class RestoreMarketInstanceVersionsService
    extends AbstractRestoreInstanceVersionsService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IRestoreMarketInstanceVersionsService {
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
