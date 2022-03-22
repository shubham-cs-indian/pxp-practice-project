package com.cs.core.runtime.instancetree;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;

@Service
public class GetKlassTaxonomyTreeService
    extends AbstractKlassTaxonomyTree<IGetKlassTaxonomyTreeRequestModel, IGetKlassTaxonomyTreeResponseModel>
    implements IGetKlassTaxonomyTreeService {
  
  @Override
  protected ConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetKlassTaxonomyTreeRequestModel();
  }
  
  @Override
  protected IConfigDetailsKlassTaxonomyTreeResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailForKlassTaxonomyTreeStrategy.execute(configRequestModel);
  }
  
  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeRuntimeStrategy(IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData) throws Exception
  {
    return getKlassTaxonomyTreeData(dataModel, configData);
  }
  
  @Override
  protected void additionalInformationForRelationshipFilter(IGetKlassTaxonomyTreeRequestModel model,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData)
  {
    // TODO Auto-generated method stub
    
  }
  
}