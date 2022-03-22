package com.cs.pim.runtime.interactor.version;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstanceVersionsForComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstanceVersionsForComparison extends AbstractRuntimeInteractor<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetArticleInstanceVersionsForComparison {
  
  @Autowired
  protected IGetArticleInstanceVersionsForComparisonService getArticleInstanceVersionsForComparisonService;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    
    return getArticleInstanceVersionsForComparisonService.execute(dataModel);
  }
}
