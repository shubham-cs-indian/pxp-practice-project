package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.klasssupplier.SupplierKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.interactor.model.summary.SummaryInformationModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.exception.supplierinstance.UserNotHaveCreatePermissionForSupplier;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstance;
import com.cs.core.runtime.interactor.usecase.supplierinstance.IBulkCreateSupplierInstance;
import com.cs.core.runtime.interactor.usecase.supplierinstance.ICreateSupplierInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulkCreateSupplierInstance extends AbstractRuntimeInteractor<IBulkCreateInstanceModel, IPluginSummaryModel>
    implements IBulkCreateSupplierInstance {

  @Autowired
  protected ICreateSupplierInstance createSupplierInstance;

  @Override
  protected IPluginSummaryModel executeInternal(IBulkCreateInstanceModel model) throws Exception
  {
    IPluginSummaryModel returnModel = new PluginSummaryModel();

    List<ISummaryInformationModel> success = new ArrayList<>();
    List<ISummaryInformationModel> failure = new ArrayList<>();
    returnModel.setFailedIds(failure);
    returnModel.setSuccess(success);

    for (ICreateInstanceModel createModel : model.getCreationList()) {
      try{
        createSupplierInstance.execute(createModel);
      }
      catch(Exception e){
        failure.add(new SummaryInformationModel(createModel.getId(), createModel.getName()));
      }
      success.add(new SummaryInformationModel(createModel.getId(), createModel.getName()));
    }
    return returnModel;
  }
}
