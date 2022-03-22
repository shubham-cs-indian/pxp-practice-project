package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveDeletePermission;
import com.cs.core.runtime.interactor.exception.supplierinstance.SupplierInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.supplierinstance.UserNotHaveDeletePermissionForSupplier;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractDeleteInstances;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.IDeleteSupplierInstance;

@Service
public class DeleteSupplierInstance
    extends AbstractDeleteInstances<IIdsListParameterModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteSupplierInstance {
  
  @Resource(name = "supplierException")
  private Properties supplierException;
  
  @Override
  protected UserNotHaveDeletePermission getUserNotHaveDeletePermissionException()
  {
    return new UserNotHaveDeletePermissionForSupplier();
  }
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IIdsListParameterModel deleteModel)
      throws Exception
  {
    try {
      IDeleteKlassInstanceResponseModel response = super.executeInternal(deleteModel);
      IExceptionModel failure = response.getFailure();
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(supplierException,
          failure.getExceptionDetails(), failure.getDevExceptionDetails());
      return response;
    }
    // TODO - Replace the plugin exception with DeleteInstanceFailedException
    // after implementing bulk delete for content relationship.
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(supplierException,
          e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new SupplierInstanceDeleteFailedException(e.getExceptionDetails(),
          e.getDevExceptionDetails());
    }
  }
  
  @Override
  protected IListModel<IKlassInstance> getKlassInstanceByIdsForPermissionCheck(
      IIdsListParameterModel idsListParameterModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  protected IDeleteInstancesResponseModel executeDeleteInstances(
      IIdsListParameterModel idsToDeleteListParameterModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
}
