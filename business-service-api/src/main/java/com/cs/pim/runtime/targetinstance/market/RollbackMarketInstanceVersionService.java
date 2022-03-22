package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.abstrct.AbstractRollbackInstanceVersionService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

@Component
public class RollbackMarketInstanceVersionService
    extends AbstractRollbackInstanceVersionService<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel>
    implements IRollbackMarketInstanceVersionService {
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel model) throws Exception
  {
    return super.executeInternal(model);
  }
  
}
