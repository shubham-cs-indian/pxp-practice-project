package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.marketinstance.MarketInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveDeletePermissionForMarket;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveDeletePermission;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractDeleteInstances;

@Service
public class DeleteMarketInstance
    extends AbstractDeleteInstances<IIdsListParameterModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteMarketInstance {
  
  @Resource(name = "marketException")
  private Properties marketException;
  
  @Override
  protected UserNotHaveDeletePermission getUserNotHaveDeletePermissionException()
  {
    return new UserNotHaveDeletePermissionForMarket();
  }
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IIdsListParameterModel deleteModel)
      throws Exception
  {
    try {
      IDeleteKlassInstanceResponseModel response = super.executeInternal(deleteModel);
      IExceptionModel failure = response.getFailure();
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(marketException,
          failure.getExceptionDetails(), failure.getDevExceptionDetails());
      return response;
    }
    // TODO - Replace the plugin exception with DeleteInstanceFailedException
    // after implementing bulk delete for content relationship.
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(marketException,
          e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new MarketInstanceDeleteFailedException(e.getExceptionDetails(),
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
