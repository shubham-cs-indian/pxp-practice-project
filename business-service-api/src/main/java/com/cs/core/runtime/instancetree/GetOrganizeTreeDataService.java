package com.cs.core.runtime.instancetree;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IOrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetOrganizeTreeDataResponseModel;

@Service
public class GetOrganizeTreeDataService
    extends AbstractOrganizeTreeData<IOrganizeTreeDataRequestModel, IGetOrganizeTreeDataResponseModel>
    implements IGetOrganizeTreeDataService {
  
  @Override
  protected ConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetKlassTaxonomyTreeRequestModel();
  }
  
  @Override
  protected IConfigDetailsOrganizeTreeDataResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailForOrganizeScreenTreeDataStrategy.execute(configRequestModel);
  }
  
  @Override
  protected IGetOrganizeTreeDataResponseModel executeRuntimeStrategy(IOrganizeTreeDataRequestModel dataModel,
      IConfigDetailsOrganizeTreeDataResponseModel configData) throws Exception
  {
    return getKlassTaxonomyTreeData(dataModel, configData);
  }
}
