package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.AbstractRollbackInstanceVersionService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

@Service
public class RollbackSupplierInstanceVersionService extends AbstractRollbackInstanceVersionService<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel>
  implements IRollbackSupplierInstanceVersionService
{
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
