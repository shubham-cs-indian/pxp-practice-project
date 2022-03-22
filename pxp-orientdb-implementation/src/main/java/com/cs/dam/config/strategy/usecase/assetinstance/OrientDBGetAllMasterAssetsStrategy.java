package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.model.asset.AssetModel;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAllMasterAssetsStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllMasterAssetsStrategy extends OrientDBBaseStrategy
    implements IGetAllMasterAssetsStrategy {
  
  @Override
  public IListModel<IAssetModel> execute(IAssetModel model) throws Exception
  {
    List<IAssetModel> assetModelsList = new ArrayList<IAssetModel>();
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<IAsset> assetList = super.execute(OrientDBBaseStrategy.GET_ALL_MASTER_ASSETS,
        requestMap, new TypeReference<ListModel<IAsset>>()
        {
          
        });
    for (IAsset asset : assetList.getList()) {
      assetModelsList.add(new AssetModel(asset));
    }
    IListModel<IAssetModel> returnModel = new ListModel<>();
    returnModel.setList(assetModelsList);
    
    return returnModel;
  }
}
