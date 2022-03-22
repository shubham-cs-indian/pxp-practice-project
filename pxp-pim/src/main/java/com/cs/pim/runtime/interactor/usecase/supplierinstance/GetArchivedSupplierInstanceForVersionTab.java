package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.IGetArchivedSupplierInstanceForVersionTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArchivedSupplierInstanceForVersionTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArchivedSupplierInstanceForVersionTab {
  
  @Autowired
  protected IGetArchivedSupplierInstanceForVersionTabService getArchivedSupplierInstanceForVersionTabService;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(
      IGetInstanceRequestModel dataModel) throws Exception
  {
    return getArchivedSupplierInstanceForVersionTabService.execute(dataModel);
  }

}
