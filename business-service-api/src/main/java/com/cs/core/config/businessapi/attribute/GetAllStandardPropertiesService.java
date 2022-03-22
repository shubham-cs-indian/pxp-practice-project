package com.cs.core.config.businessapi.attribute;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.datarule.IMandatoryProperty;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.GetAllMandatoryAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.IGetAllMandatoryAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.standard.properties.IGetAllStandardPropertiesStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;

@Service
public class GetAllStandardPropertiesService extends AbstractGetConfigService<IMandatoryAttributeModel, IGetAllMandatoryAttributeModel>
    implements IGetAllStandardAttributesService {
  
  // TODO: move to constants class
  public static final String        PROJECT_KLASS = "com.cs.config.interactor.entity.concrete.klass.ProjectKlass";
  public static final String        TASK_KLASS    = "com.cs.config.interactor.entity.concrete.klass.Task";
  public static final String        ASSET_KLASS   = "com.cs.config.interactor.entity.concrete.klass.Asset";
  public static final String        MARKET_KLASS  = "com.cs.config.interactor.entity.concrete.klass.Market";
  
  @Autowired
  IGetAllStandardPropertiesStrategy neo4jGetAllStandardPropertiesStrategy;
  
  @Override
  public IGetAllMandatoryAttributeModel executeInternal(IMandatoryAttributeModel dataModel) throws Exception
  {
    IListModel<IMandatoryProperty> propertyList = neo4jGetAllStandardPropertiesStrategy.execute(dataModel);
    
    List<IMandatoryProperty> projectKlassAttributes = new ArrayList<>();
    List<IMandatoryProperty> taskKlassAttributes = new ArrayList<>();
    List<IMandatoryProperty> assetKlassAttributes = new ArrayList<>();
    List<IMandatoryProperty> targetKlassAttributes = new ArrayList<>();
    
    for (IMandatoryProperty mandatoryProperty : propertyList.getList()) {
      if (mandatoryProperty.getKlassType().contains(TASK_KLASS)) {
        taskKlassAttributes.add(mandatoryProperty);
      }
      
      if (mandatoryProperty.getKlassType().contains(PROJECT_KLASS)) {
        projectKlassAttributes.add(mandatoryProperty);
      }
      
      if (mandatoryProperty.getKlassType().contains(ASSET_KLASS)) {
        assetKlassAttributes.add(mandatoryProperty);
      }
      
      if (mandatoryProperty.getKlassType().contains(MARKET_KLASS)) {
        targetKlassAttributes.add(mandatoryProperty);
      }
    }
    
    GetAllMandatoryAttributeModel response = new GetAllMandatoryAttributeModel();
    response.setProjectKlassAttributes(projectKlassAttributes);
    response.setTaskKlassAttributes(taskKlassAttributes);
    response.setAssetKlassAttributes(assetKlassAttributes);
    response.setTargetKlassAttributes(targetKlassAttributes);
    
    return response;
  }
  
  @Override
  public IGetAllMandatoryAttributeModel execute(IMandatoryAttributeModel dataModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
