/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.exception.articleinstance.ArticleInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.klass.IDeleteKlassInstance;
import com.cs.core.runtime.interactor.usecase.klassinstance.AbstractDeleteKlassInstances;

@Service
public class DeleteKlassInstancePostGre
    extends AbstractDeleteKlassInstances<IDeleteKlassInstanceRequestModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteKlassInstance {

  @Resource(name = "articleException")
  private Properties                                          articleException;


  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IDeleteKlassInstanceRequestModel deleteModel) throws Exception
  {
    try
    {
      return super.executeInternal(deleteModel);
    }
    catch(PluginException e)
    {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(articleException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new ArticleInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }

}
*/
