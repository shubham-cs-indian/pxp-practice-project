package com.cs.di.runtime.interactor.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetAllTalendJobs extends AbstractRuntimeInteractor<IModel, IListModel<IIdLabelModel>>
    implements IGetAllTalendJobs {

  @Value("${talend.jobs.path}") private String talendJobsLocation;

  @Override public IListModel<IIdLabelModel> executeInternal(IModel dataModel) throws Exception
  {
    IListModel<IIdLabelModel> iListModel = new ListModel<>();
    List<IIdLabelModel> listOfIdLabelModel = new ArrayList<>();

    File directory = new File(talendJobsLocation);
    File[] fList = directory.listFiles();

    if (fList == null) {
      return iListModel;
    }

    for (File file : fList) {
      try {
        if (!file.isDirectory()) {
          String extension = FilenameUtils.getExtension(file.getName());
          if (extension.equals("jar")) {
            IIdLabelModel idLabelModel = new IdLabelModel();
            idLabelModel.setId(file.getName());
            idLabelModel.setLabel(file.getName());
            listOfIdLabelModel.add(idLabelModel);
          }
        }
      }
      catch (Exception e) {
        continue;
      }
    }
    iListModel.setList(listOfIdLabelModel);

    return iListModel;
  }

}
