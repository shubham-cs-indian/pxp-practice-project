package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.attributiontaxonomy.ISaveMasterTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;

@Service
public class SaveMasterTaxonomy extends AbstractSaveConfigInteractor<ISaveMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel>
    implements ISaveMasterTaxonomy {
  
  @Autowired
  protected ISaveMasterTaxonomyService                       saveMasterTaxonomyService;
  
   @Override
  protected IGetMasterTaxonomyWithoutKPModel executeInternal(ISaveMasterTaxonomyModel model) throws Exception
  {
    return saveMasterTaxonomyService.execute(model);
  }
  
}
