package com.cs.core.config.configdata;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataTagValuesPaginationModel;
import com.cs.core.config.strategy.usecase.configdata.IGetConfigDataStrategy;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class GetConfigDataService extends AbstractGetConfigService<IGetConfigDataRequestModel, IGetConfigDataResponseModel>
    implements IGetConfigDataService {
  
  @Autowired
  protected IGetConfigDataStrategy getConfigDataStrategy;
  
  @Autowired
  protected RDBMSComponentUtils    rdbmsComponetUtils;
  
  @Override
  public IGetConfigDataResponseModel executeInternal(IGetConfigDataRequestModel dataModel) throws Exception
  {
    String klassInstanceId = getKlassInstanceId(dataModel);
    if (klassInstanceId != null && !klassInstanceId.isEmpty()) {
      dataModel = getTypeAndSelectedTaxonomyIds(dataModel, klassInstanceId, dataModel.getEntities().getTagValues().getBaseType());
    }
    return getConfigDataStrategy.execute(dataModel);
  }
  
  private IGetConfigDataRequestModel getTypeAndSelectedTaxonomyIds(IGetConfigDataRequestModel dataModel, String klassInstanceId,
      String baseType) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(klassInstanceId);
    IKlassInstanceTypeModel typeDetails = getTypesAccordingToBaseType(idParameterModel, baseType);
    if (typeDetails == null) {
      throw new NotFoundException();
    }
    IGetConfigDataEntityRequestModel entities = dataModel.getEntities();
    IGetConfigDataTagValuesPaginationModel tagValues = entities.getTagValues();
    Collection<String> types = typeDetails.getTypes();
    
    if (types instanceof List) {
      tagValues.setKlassTypes((List<String>) types);
    }
    else {
      tagValues.setKlassTypes(new ArrayList<String>(types));
    }
    
    tagValues.setSelectedTaxonomyIds(typeDetails.getSelectedTaxonomyIds());
    
    return dataModel;
  }
  
  private IKlassInstanceTypeModel getTypesAccordingToBaseType(IIdParameterModel idParameterModel, String baseType) throws Exception
  {
    
    IKlassInstanceTypeModel klassInstanceTypeModel = new KlassInstanceTypeModel();
    klassInstanceTypeModel.setBaseType(baseType);
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponetUtils.getBaseEntityDAO(rdbmsComponetUtils.getLocaleCatlogDAO().getBaseEntityDTOByIID(Long.parseLong(idParameterModel.getId())));

    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    List<String> classes = BaseEntityUtils.getReferenceTypeFromClassifierDTO(classifiers);
    classes.add(baseEntityDAO.getBaseEntityDTO()
        .getNatureClassifier()
        .getClassifierCode());
    List<String> allTaxonomy = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(classifiers);
    klassInstanceTypeModel.setTypes(classes);
    klassInstanceTypeModel.setSelectedTaxonomyIds(allTaxonomy);
    return klassInstanceTypeModel;
  }
  
  private String getKlassInstanceId(IGetConfigDataRequestModel dataModel)
  {
    IGetConfigDataEntityRequestModel entities = dataModel.getEntities();
    IGetConfigDataTagValuesPaginationModel tagValues = entities.getTagValues();
    if (tagValues != null) {
      return tagValues.getKlassInstanceId();
    }
    return null;
  }
}
