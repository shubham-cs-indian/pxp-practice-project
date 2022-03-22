package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klasssupplier.SupplierKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;

@Service
public class SaveSupplierInstanceForTabsService extends AbstractSaveInstanceForTabs<ISupplierInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveSupplierInstanceForTabsService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISupplierInstanceSaveModel klassInstancesModel) throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new SupplierKlassNotFoundException(e);
    }
    return response;
  }
}
