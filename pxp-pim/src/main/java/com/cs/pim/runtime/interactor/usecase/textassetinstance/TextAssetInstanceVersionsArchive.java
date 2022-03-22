package com.cs.pim.runtime.interactor.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractDeleteArticleInstanceVersion;
import com.cs.pim.runtime.textassetinstance.ITextAssetInstanceVersionsArchiveService;

@Service
public class TextAssetInstanceVersionsArchive
    extends AbstractDeleteArticleInstanceVersion<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements ITextAssetInstanceVersionsArchive {
 
  @Autowired 
  protected ITextAssetInstanceVersionsArchiveService textAssetInstanceVersionsArchiveService; 
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel execute(IMoveKlassInstanceVersionsModel dataModel)
      throws Exception
  {
    return textAssetInstanceVersionsArchiveService.execute(dataModel);
  }
  
}
