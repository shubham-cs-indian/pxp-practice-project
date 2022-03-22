package com.cs.pim.runtime.dynamiccollection;

import java.util.List;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.runtime.interactor.model.instancetree.IGetBookmarkRequestModel;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.instancetree.AbstractBookmarkInstanceTree;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.instancetree.IGetInstanceTreeForBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
@Service
public class GetBaseEntityIidsByBookmarkIdService<R> extends
AbstractBookmarkInstanceTree<IGetInstanceTreeRequestModel, IIdsListParameterModel>
    implements IGetBaseEntityIidsByBookmarkIdService {

  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Autowired
  protected SessionContextCustomProxy   context;
  
  @Autowired
  protected TransactionThreadData transactionThread;

  @Autowired
  protected ModuleMappingUtil moduleMappingUtil;
 
  @Override
  public IIdsListParameterModel executeInternal(IGetInstanceTreeRequestModel model) throws Exception
  {
    IIdsListParameterModel responseModel = new IdsListParameterModel();
    IGetInstanceTreeForBookmarkRequestModel requestModel = ((IGetInstanceTreeForBookmarkRequestModel) model);
    IGetBookmarkRequestModel getRequestModel = getInstanceTreeRequestModelForBookmark(requestModel.getBookmarkId());
    getRequestModel.setIsFilterDataRequired(model.getIsFilterDataRequired());
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = prepareConfigRequestModel(getRequestModel, getModuleEntities(getRequestModel));
    IConfigDetailsForNewInstanceTreeModel configDetails = executeConfigDetailsStrategy(configRequestModel);
    prepareRuntimeRequestModel(getRequestModel, configDetails);
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();

    IIdsListParameterModel baseEntities = getBaseEntityIIdsAsPerSearchCriteria(
        getRequestModel, configDetails, localeCatalogDao);
    responseModel.setIds(baseEntities.getIds());
    Integer totalCount =((Long)baseEntities.getAdditionalProperty("totalCount")).intValue();
    if(totalCount> baseEntities.getIds().size()) {
      getRequestModel.setFrom(20);
      getRequestModel.setSize(totalCount);
      responseModel.getIds().addAll(getBaseEntityIIdsAsPerSearchCriteria(getRequestModel, 
      configDetails, localeCatalogDao).getIds());  
    }
    return responseModel;
  }
  
  @Override
  protected IConfigDetailsForNewInstanceTreeModel executeConfigDetailsStrategy(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetNewInstanceTreeStrategy.execute(configRequestModel);
  }
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(IGetInstanceTreeRequestModel model,
      IConfigDetailsForNewInstanceTreeModel configDetails) throws Exception
  {
    return null;
  }
  
  @Override
  protected List<String> getModuleEntities(IGetInstanceTreeRequestModel model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }


}
