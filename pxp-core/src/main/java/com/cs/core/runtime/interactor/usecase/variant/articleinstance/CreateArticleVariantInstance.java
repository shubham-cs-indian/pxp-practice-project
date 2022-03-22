package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.AbstractCreateInstanceVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleVariantInstance
    extends AbstractCreateInstanceVariant<ICreateVariantModel, IGetKlassInstanceModel>
    implements ICreateArticleVariantInstance {
  
  @Autowired
  protected Long articleKlassCounter;
  
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
  public BaseType getBaseType()
  {
    return BaseType.ARTICLE;
  }
}
