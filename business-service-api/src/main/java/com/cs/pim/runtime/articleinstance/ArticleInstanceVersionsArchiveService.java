package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.klassinstance.AbstractDeleteArticleInstanceVersionService;

@Service
public class ArticleInstanceVersionsArchiveService extends
    AbstractDeleteArticleInstanceVersionService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
      implements IArticleInstanceVersionsArchiveService{
        
}
