package com.cs.pim.runtime.articleinstance;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.abstrct.AbstractCreateInstanceService;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveCreatePermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleInstanceService
    extends AbstractCreateInstanceService<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateArticleInstanceService {
  
  @Autowired
  protected Long articleKlassCounter;
  
  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model)
      throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForArticle();
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new ArticleKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return articleKlassCounter++;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEARTIKLE;
  }
  
  @Override
  public BaseType getBaseType()
  {
    return BaseType.ARTICLE;
  }
}
