package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.klassinstance.AbstractGetKlassInstanceForVersionTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstanceForVersionTabService extends AbstractGetKlassInstanceForVersionTabService<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArticleInstanceForVersionTabService {
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy getConfigDetailsForCustomTabStrategy;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(
      IGetInstanceRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ARTICLE_INSTANCE_BASE_TYPE;
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception
  {
    return getConfigDetailsForCustomTabStrategy.execute(idParameterModel);
  }
  
  @Override
  protected Boolean getIsArchive() throws Exception
  {
    return false;
  }
  
}
