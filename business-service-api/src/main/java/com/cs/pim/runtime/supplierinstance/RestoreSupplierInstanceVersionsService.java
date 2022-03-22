package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.versions.AbstractRestoreInstanceVersionsService;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;

@Service
public class RestoreSupplierInstanceVersionsService 
extends AbstractRestoreInstanceVersionsService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel> 
implements IRestoreSupplierInstanceVersionsService {

  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
