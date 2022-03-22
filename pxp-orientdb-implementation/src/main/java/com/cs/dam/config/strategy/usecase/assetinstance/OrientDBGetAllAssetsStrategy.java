package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAllAssetsStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllAssetsStrategy extends OrientDBBaseStrategy
    implements IGetAllAssetsStrategy {
  
  @Override
  public IListModel<IKlassInformationModel> execute(IAssetModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<IAsset> assetList = super.execute(OrientDBBaseStrategy.GET_ASSETS, requestMap,
        new TypeReference<ListModel<IAsset>>()
        {
          
        });
    List<IKlassInformationModel> assetModelsList = new ArrayList<>();
    for (IAsset asset : assetList.getList()) {
      assetModelsList.add(new KlassInformationModel((IKlassBasic) asset));
    }
    IListModel<IKlassInformationModel> returnModel = new ListModel<>();
    returnModel.setList(assetModelsList);
    return returnModel;
  }
}
