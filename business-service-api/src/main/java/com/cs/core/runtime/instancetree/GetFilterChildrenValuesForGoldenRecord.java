package com.cs.core.runtime.instancetree;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.goldenrecord.bucket.IGetFilterChildrenValuesForGoldenRecord;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGetGoldenRecordFilterChildrenStrategy;

@Service
public class GetFilterChildrenValuesForGoldenRecord extends
    AbstractGetFilterChildrenValues<IGetFilterChildrenRequestModel, /*IListModel<*/IGetFilterChildrenResponseModel/*>*/>
    implements IGetFilterChildrenValuesForGoldenRecord {
  
  /* @Autowired
  protected IGetFilterChildrenValuesForGoldenRecordStrategy           getFilterChildrenValuesForGoldenRecordStrategy;
  */
  
  @Autowired
  protected IGetConfigDetailsForGetGoldenRecordFilterChildrenStrategy getConfigDetailsForGetGoldenRecordFilterChildrenStrategy;
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
  }
  
  @Override
  protected IConfigDetailsForGetFilterChildrenResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetFilterChildrenRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetGoldenRecordFilterChildrenStrategy.execute(configRequestModel);
  }
  
  @Override
  protected ConfigDetailsForGetFilterChildrenRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetFilterChildrenRequestModel();
  }
  
  /* @Override
  protected IListModel<IGetFilterChildrenResponseModel> executeRuntimeStrategy(
      IGetFilterChildrenRequestModel model) throws Exception
  {
    return getFilterChildrenValuesForGoldenRecordStrategy.execute(model);
  }*/
  
  @Override
  protected List<String> getModuleEntities(IGetFilterChildrenRequestModel model) throws Exception
  {
    return Arrays.asList(CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY);
  }

  @Override
  protected List<IGetFilterChildrenModel> executeRuntimeStrategy(
      IGetFilterChildrenRequestModel model,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    return getFilterChildrenValues(model, configDetails);
  }
  
  @Override
  protected List<IGetFilterChildrenModel> getFilterChildrenValues(
      IGetFilterChildrenRequestModel dataModel,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    ISearchDTO searchDTO = fillSearchDTO(configDetails, dataModel, localeCatalogDao);
    ISearchDAO searchDAO = localeCatalogDao.openGoldenRecordBucketSearchDAO(searchDTO);
    
    return fillFilterChildren(dataModel, configDetails, localeCatalogDao, searchDAO);
  }
}
