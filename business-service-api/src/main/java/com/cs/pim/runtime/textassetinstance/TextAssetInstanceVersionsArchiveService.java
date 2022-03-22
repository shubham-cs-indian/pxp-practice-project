package com.cs.pim.runtime.textassetinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.klassinstance.AbstractDeleteArticleInstanceVersionService;

@Service
public class TextAssetInstanceVersionsArchiveService extends
    AbstractDeleteArticleInstanceVersionService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements ITextAssetInstanceVersionsArchiveService {
  
}
