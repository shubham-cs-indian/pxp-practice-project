package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.config.strategy.usecase.variantcontext.IBulkSaveVariantContextsStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.utils.ExceptionUtil;
import coms.cs.core.config.businessapi.variantcontext.IBulkSaveVariantContextsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulkSaveVariantContextsService extends AbstractSaveConfigService<IListModel<IGridEditVariantContextInformationModel>,IBulkSaveVariantContextsResponseModel>
    implements IBulkSaveVariantContextsService {
  
  @Autowired
  protected IBulkSaveVariantContextsStrategy bulkSaveVariantContextsStrategy;

  @Override
  public IBulkSaveVariantContextsResponseModel executeInternal(IListModel<IGridEditVariantContextInformationModel> dataModel)
      throws Exception
  {
    IExceptionModel exception = new ExceptionModel();
    List<IGridEditVariantContextInformationModel> bulkSave = new ArrayList<>();
    for (IGridEditVariantContextInformationModel gridEditInfo : dataModel.getList()) {
      try {
        ContextValidations.validateContext(gridEditInfo);
        bulkSave.add(gridEditInfo);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(exception, e, null, null);
      }
    }

    IListModel<IGridEditVariantContextInformationModel>  bulkModel = new ListModel<>();
    bulkModel.setList(bulkSave);

    IBulkSaveVariantContextsResponseModel bulkResponse = bulkSaveVariantContextsStrategy.execute(bulkModel);
    bulkResponse.getFailure().getDevExceptionDetails().addAll(exception.getDevExceptionDetails());
    bulkResponse.getFailure().getExceptionDetails().addAll(exception.getExceptionDetails());

    return bulkResponse;
  }

}
