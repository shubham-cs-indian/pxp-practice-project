package com.cs.core.runtime.klassinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.exception.articleinstance.ArticleInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;

@Service
public class DeleteKlassInstanceService
    extends AbstractDeleteKlassInstancesService<IDeleteKlassInstanceRequestModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteKlassInstanceService {
  
  @Resource(name = "articleException")
  private Properties articleException;
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IDeleteKlassInstanceRequestModel deleteModel) throws Exception
  {
    try {
      return super.executeInternal(deleteModel);
    }
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(articleException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new ArticleInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }
}
