package com.cs.core.runtime.instancetree;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.rdbms.entity.dto.CollectionFilterDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;

@Service
public class GetKlassTaxonomyTreeForCollectionService extends AbstractKlassTaxonomyTree<IGetKlassTaxonomyTreeRequestModel, IGetKlassTaxonomyTreeResponseModel> 
implements IGetKlassTaxonomyTreeForCollectionService {
  
  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeRuntimeStrategy(
      IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData) throws Exception
  {
    return getKlassTaxonomyTreeData(dataModel, configData);
  }

  @Override
  protected void fillUsecaseSpecificFilters(IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData, ISearchDTOBuilder searchBuilder) throws Exception
  {
    IGetKlassTaxonomyTreeForCollectionRequestModel klassTaxonomyTreeRequestModel = (IGetKlassTaxonomyTreeForCollectionRequestModel) dataModel;
    CollectionFilterDTO collectionFilterDto = new CollectionFilterDTO(klassTaxonomyTreeRequestModel.getIsQuicklist(),klassTaxonomyTreeRequestModel.getCollectionId());
    searchBuilder.addCollectionFilters(collectionFilterDto);
  }
  
  @Override
  protected List<String> getModuleEntities(IGetKlassTaxonomyTreeRequestModel dataModel) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(dataModel.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }

  @Override
  protected ConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetKlassTaxonomyTreeRequestModel();
  }

  @Override
  protected IConfigDetailsKlassTaxonomyTreeResponseModel executeConfigDetailsStrategy(IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel) throws Exception
  {
    return getConfigDetailForKlassTaxonomyTreeStrategy.execute(configRequsetModel);
  }

  @Override
  protected void additionalInformationForRelationshipFilter(IGetKlassTaxonomyTreeRequestModel model,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData)
  {
    // TODO Auto-generated method stub
    
  }
  
}
