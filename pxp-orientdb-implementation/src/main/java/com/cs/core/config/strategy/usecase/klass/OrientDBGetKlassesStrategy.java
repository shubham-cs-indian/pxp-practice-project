package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetKlassesStrategy extends OrientDBBaseStrategy
    implements IGetAllKlassesStrategy {
  
  public static final String useCase = "GetKlasses";
  
  @Override
  public IListModel<IKlassInformationModel> execute(IIdParameterModel model) throws Exception
  {
    String id = model.getId();
    String useCase = null;
    if (id.equals("market") || id.equals("collection_target")) {
      useCase = "GetAllTargets";
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("id", id);
      
      ListModel<ITarget> klassesList = execute(useCase, requestMap,
          new TypeReference<ListModel<ITarget>>()
          {
            
          });
      
      List<IKlassInformationModel> klassModelsList = new ArrayList<>();
      for (ITarget klass : klassesList.getList()) {
        klassModelsList.add(new KlassInformationModel((IKlassBasic) klass));
      }
      IListModel<IKlassInformationModel> returnModel = new ListModel<>();
      returnModel.setList(klassModelsList);
      return returnModel;
    }
    else if (id.equals("collection_asset") || id.equals("asset_asset") || id.equals("jpeg_asset")) {
      useCase = "GetAllAssets";
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("id", id);
      
      ListModel<IAsset> klassesList = execute(useCase, requestMap,
          new TypeReference<ListModel<IAsset>>()
          {
            
          });
      
      List<IKlassInformationModel> klassModelsList = new ArrayList<>();
      for (IAsset klass : klassesList.getList()) {
        klassModelsList.add(new KlassInformationModel((IKlassBasic) klass));
      }
      IListModel<IKlassInformationModel> returnModel = new ListModel<>();
      returnModel.setList(klassModelsList);
      return returnModel;
    }
    else if (id.equals("AllKlass")) {
      useCase = "AllKlass";
      Map<String, Object> requestMap = new HashMap<>();
      
      ListModel<IKlass> klassesList = execute(useCase, requestMap,
          new TypeReference<ListModel<IKlass>>()
          {
            
          });
      
      List<IKlassInformationModel> klassModelsList = new ArrayList<>();
      for (IKlass klass : klassesList.getList()) {
        klassModelsList.add(new KlassInformationModel((IKlassBasic) klass));
      }
      IListModel<IKlassInformationModel> returnModel = new ListModel<>();
      returnModel.setList(klassModelsList);
      return returnModel;
    }
    else {
      useCase = "GetKlasses";
    }
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    
    ListModel<IKlass> klassesList = execute(useCase, requestMap,
        new TypeReference<ListModel<IKlass>>()
        {
          
        });
    
    List<IKlassInformationModel> klassModelsList = new ArrayList<>();
    for (IKlass klass : klassesList.getList()) {
      klassModelsList.add(new KlassInformationModel((IKlassBasic) klass));
    }
    IListModel<IKlassInformationModel> returnModel = new ListModel<>();
    returnModel.setList(klassModelsList);
    return returnModel;
  }
}
