package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveDeletePermission;
import com.cs.core.runtime.interactor.exception.textassetinstance.TextAssetInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveDeletePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractDeleteInstances;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IDeleteTextAssetInstance;

@Service
public class DeleteTextAssetInstance
    extends AbstractDeleteInstances<IIdsListParameterModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteTextAssetInstance {

  @Resource(name="textAssetException")
  private Properties textAssetException;
 
  @Override
  protected UserNotHaveDeletePermission getUserNotHaveDeletePermissionException()
  {
    return new UserNotHaveDeletePermissionForTextAsset();
  }
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IIdsListParameterModel deleteModel) throws Exception
  {
    try
    {
      IDeleteKlassInstanceResponseModel response = super.executeInternal(deleteModel);
      IExceptionModel failure = response.getFailure();
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(textAssetException, failure.getExceptionDetails(), failure.getDevExceptionDetails());
      return response;
    }
    //TODO - Replace the plugin exception with DeleteInstanceFailedException after implementing bulk delete for content relationship.
    catch(PluginException e)
    {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(textAssetException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new TextAssetInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }

  @Override
  protected IDeleteInstancesResponseModel executeDeleteInstances(
      IIdsListParameterModel idsToDeleteListParameterModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected IListModel<IKlassInstance> getKlassInstanceByIdsForPermissionCheck(
      IIdsListParameterModel idsListParameterModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  } 
}
