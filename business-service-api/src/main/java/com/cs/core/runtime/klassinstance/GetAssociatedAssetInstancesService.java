package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetAssociatedAssetInstancesModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetAssociatedAssetInstancesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.utils.BaseEntityUtils;

@Service
public class GetAssociatedAssetInstancesService extends AbstractRuntimeService<IIdParameterModel, IGetAssociatedAssetInstancesModel>
    implements IGetAssociatedAssetInstancesService {
  
  @Autowired
  RDBMSComponentUtils  rdbmsComponentUtils;
  
  @Override
  protected IGetAssociatedAssetInstancesModel executeInternal(IIdParameterModel model) throws Exception
  {
    IGetAssociatedAssetInstancesModel returnModel = new GetAssociatedAssetInstancesModel();
    String id = model.getId();
    Long iid = Long.valueOf(id);
    
    List<IAssetAttributeInstanceInformationModel> thumbKeyList = new ArrayList<>();
    returnModel.setReferencedAssets(thumbKeyList);
    
    
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    Set<Long> otherSideIIdsFromRelations = localeCatalogDao.getOtherSideIIdsFromRelations(iid);
    if(otherSideIIdsFromRelations.isEmpty()) {
      return returnModel;
    }
    
    Map<Long, IJSONContent> entityIIdEntityExtensionsMap = localeCatalogDao.getEntityExtensionByIIDs(otherSideIIdsFromRelations);
    
    Set<Long> otherSideEntityIIds = entityIIdEntityExtensionsMap.keySet();
    Map<Long, String> entityIIdVsNames = localeCatalogDao.getBaseEntityNamesByIID(new HashSet<>(otherSideEntityIIds));
    
    for (Long otherSideEntityIId : otherSideEntityIIds) {
      thumbKeyList.add(BaseEntityUtils.fillAssetInformationModel(otherSideEntityIId, entityIIdEntityExtensionsMap.get(otherSideEntityIId),
          entityIIdVsNames.get(otherSideEntityIId)));
    }
    
    return returnModel;
  }
}
