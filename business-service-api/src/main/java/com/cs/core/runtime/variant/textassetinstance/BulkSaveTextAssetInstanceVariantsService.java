package com.cs.core.runtime.variant.textassetinstance;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.AbstractBulkSaveInstanceVariants;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveTextAssetInstanceVariantsService
    extends AbstractBulkSaveInstanceVariants<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveTextAssetInstanceVariantsService {

  @Autowired
  protected IGetTextAssetVariantInstancesInTableViewService getTextAssetVariantInstancesInTableViewService;

  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel execute(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    IBulkSaveKlassInstanceVariantsResponseModel response = null;
    try {
      response = super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new TextAssetKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.TEXTASSET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel)
      throws Exception
  {
    return getTextAssetVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }

}
