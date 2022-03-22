package com.cs.dam.runtime.assetinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceDuplicateTabResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceDuplicateTabResponseModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.util.ConfigUtil;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceOverviewTabStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAssetInstancesForDuplicateTabService extends AbstractRuntimeService<IGetInstanceRequestModel, IAssetInstanceDuplicateTabResponseModel>
    implements IGetAssetInstancesForDuplicateTabService {
  
  @Autowired
  TransactionThreadData                                          transactionThreadData;
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                                           configUtil;
  
  @Autowired
  protected VariantInstanceUtils                                 variantInstanceUtils;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy                        postConfigDetailsForGetInstanceTreeStrategy;
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceOverviewTabStrategy getConfigDetailsForAssetInstanceOverviewTab;
  
  @Override
  protected IAssetInstanceDuplicateTabResponseModel executeInternal(IGetInstanceRequestModel model)
      throws Exception
  {
    IAssetInstanceDuplicateTabResponseModel assetInstanceDuplicateTabResponseModel = new AssetInstanceDuplicateTabResponseModel();
    List<IKlassInstanceInformationModel> klassInstanceList = new ArrayList<IKlassInstanceInformationModel>();
    List<String> types = new ArrayList<String>();
    long id = Long.parseLong(model.getId());
    
    List<String> languageInheritance = configUtil.getLanguageInheritance();
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils
        .getBaseEntityDAO(id, languageInheritance);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    //excluding the main asset, get duplicate asset instances using hashcode 
    List<IBaseEntityDTO> duplicateBaseEntities = baseEntityDAO.getDuplicateBaseEntities(
        baseEntityDTO.getHashCode(), id, baseEntityDTO.getLocaleCatalog()
            .getCatalogCode(),
        baseEntityDTO.getLocaleCatalog()
            .getOrganizationCode());
    //For each entity, prepare klass instance information model
    for (IBaseEntityDTO baseEntity : duplicateBaseEntities) {
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder
          .getKlassInstanceInformationModel(baseEntity, rdbmsComponentUtils);
      
      klassInstanceList.add(klassInstanceInformationModel);
      types.addAll(klassInstanceInformationModel.getTypes());
    }
    IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
    requestModel.setKlassIds(types);
    
    //get all attributes, tags and referenced klasses
    IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForGetInstanceTreeStrategy
        .execute(requestModel);
    //prepare model for response
    assetInstanceDuplicateTabResponseModel.setAssetInstances(klassInstanceList);
    assetInstanceDuplicateTabResponseModel
        .setReferencedKlasses(responseModel.getReferencedKlasses());
    assetInstanceDuplicateTabResponseModel.setFrom(model.getFrom());
    assetInstanceDuplicateTabResponseModel.setTotalContents(klassInstanceList.size());
    
    return assetInstanceDuplicateTabResponseModel;
  }
  
}
