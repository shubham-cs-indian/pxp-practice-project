package com.cs.pim.runtime.interactor.version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractDeleteArticleInstanceVersion;
import com.cs.pim.runtime.articleinstance.IArticleInstanceVersionsArchiveService;

@Service
public class ArticleInstanceVersionsArchive extends
    AbstractDeleteArticleInstanceVersion<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IArticleInstanceVersionsArchive {
  
  @Autowired 
  protected IArticleInstanceVersionsArchiveService articleInstanceVersionsArchiveService; 
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel execute(IMoveKlassInstanceVersionsModel dataModel)
      throws Exception
  {
    return articleInstanceVersionsArchiveService.execute(dataModel);
  }
  
}