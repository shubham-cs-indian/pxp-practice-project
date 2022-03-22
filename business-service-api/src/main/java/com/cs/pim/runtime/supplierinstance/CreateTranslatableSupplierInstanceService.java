package com.cs.pim.runtime.supplierinstance;

import com.cs.core.config.interactor.exception.klasssupplier.SupplierKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.abstrct.AbstractCreateTranslatableInstanceService;
import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTranslatableSupplierInstanceService
    extends AbstractCreateTranslatableInstanceService<ISupplierInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableSupplierInstanceService {
  
  @Autowired
  protected Long supplierKlassCounter;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISupplierInstanceSaveModel klassInstancesModel)
      throws Exception
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
 
  
  @Override
  protected Long getCounter()
  {
    return supplierKlassCounter++;
  }
}
