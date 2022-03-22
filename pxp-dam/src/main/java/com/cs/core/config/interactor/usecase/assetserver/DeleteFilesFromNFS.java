package com.cs.core.config.interactor.usecase.assetserver;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

/**
 * This service deletes the files from the given paths.
 * @author vannya.kalani
 *
 */
@Service
public class DeleteFilesFromNFS extends AbstractRuntimeInteractor<IIdsListParameterModel, IModel>
    implements IDeleteFilesFromNFS {
  
  @Override
  protected IModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    List<String> fileToDeletePaths = model.getIds();
    for (String pathString : fileToDeletePaths) {
      File directory = new File(FilenameUtils.getFullPathNoEndSeparator(pathString));
      FileUtils.forceDelete(directory);
    }
    return null;
  }
}
