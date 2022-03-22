package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.config.interactor.entity.textasset.ITextAsset;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllTextAssetsStrategy extends OrientDBBaseStrategy
    implements IGetAllTextAssetsStrategy {
  
  public static final String useCase = "GetTextAssets";
  
  @Override
  public IListModel<IKlassInformationModel> execute(ITextAssetModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<ITextAsset> targetList = execute(useCase, requestMap,
        new TypeReference<ListModel<ITextAsset>>()
        {
          
        });
    List<IKlassInformationModel> targetModelsList = new ArrayList<>();
    for (ITextAsset target : targetList.getList()) {
      targetModelsList.add(new KlassInformationModel((IKlassBasic) target));
    }
    IListModel<IKlassInformationModel> returnModel = new ListModel<>();
    returnModel.setList(targetModelsList);
    return returnModel;
  }
}
