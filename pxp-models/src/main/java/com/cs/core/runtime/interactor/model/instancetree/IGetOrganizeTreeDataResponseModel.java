package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;

public interface IGetOrganizeTreeDataResponseModel extends IModel {
  
  public static final String KLASS_TAXONOMY_INFO = "klassTaxonomyInfo";
  
  public List<ICategoryInformationModel> getKlassTaxonomyInfo();
  
  public void setKlassTaxonomyInfo(List<ICategoryInformationModel> klassTaxonomyInfo);
}
