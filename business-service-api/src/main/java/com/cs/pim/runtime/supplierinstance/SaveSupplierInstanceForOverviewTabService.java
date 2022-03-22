package com.cs.pim.runtime.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.klasssupplier.SupplierKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;

@Service
public class SaveSupplierInstanceForOverviewTabService extends AbstractSaveInstanceForTabs<ISupplierInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveSupplierInstanceForOverviewTabService {
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy getConfigDetailsForOverviewTabStrategy;
  
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
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails(IMulticlassificationRequestModel model, IKlassInstanceSaveModel saveModel)
      throws Exception
  {
    switch (saveModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        return getConfigDetailsForOverviewTabStrategy.execute(model);
      default:
        return null;
    }
  }
  
  @Override
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails, IKlassInstanceSaveModel saveInstanceModel,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    return variantInstanceUtils.executeGetKlassInstanceForOverviewTab(configDetails, baseEntityDAO);
  }
}
