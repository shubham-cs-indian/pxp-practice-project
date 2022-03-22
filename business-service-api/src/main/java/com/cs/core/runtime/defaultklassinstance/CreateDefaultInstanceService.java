package com.cs.core.runtime.defaultklassinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.defaultinstance.IGetConfigDetailsForDefaultInstanceStrategy;
import com.cs.core.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateDefaultInstanceService implements ICreateDefaultInstanceService {
  
  
  @Autowired
  protected IGetConfigDetailsForDefaultInstanceStrategy getConfigDetailsForTaxonomyDefaultInstanceStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                 rdbmsComponentUtils;
  
  @Autowired
  protected ISessionContext                                     context;
  
  @Autowired
  protected ConfigUtil                                          configUtil;
  
  @Override
  public IVoidModel execute(ITypesTaxonomiesModel dataModel) throws Exception
  {
    ILocaleCatalogDAO localeCatlogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    for (String type : dataModel.getTypes()) {
      List<String> types = new ArrayList<String>();
      types.add(type);
      IMulticlassificationRequestModel multiclassificationRequestModel = configUtil.getConfigRequestModelForGivenTypesTaxonomies(types,
          new ArrayList<String>());
      multiclassificationRequestModel.setUserId(CommonConstants.ADMIN_USER_ID);
      multiclassificationRequestModel.setOrganizationId(IStandardConfig.STANDARD_ORGANIZATION_CODE);
      IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForTaxonomyDefaultInstanceStrategy
          .execute(multiclassificationRequestModel);
      
      createDefaultInstance(localeCatlogDAO, type, configDetails);
    }
    
    for (String taxonomy : dataModel.getTaxonomyIds()) {
      List<String> types = new ArrayList<String>();
      types.add(taxonomy);
      IMulticlassificationRequestModel multiclassificationRequestModel = configUtil
          .getConfigRequestModelForGivenTypesTaxonomies(new ArrayList<String>(), types);
      multiclassificationRequestModel.setUserId(CommonConstants.ADMIN_USER_ID);
      multiclassificationRequestModel.setOrganizationId(IStandardConfig.STANDARD_ORGANIZATION_CODE);
      IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForTaxonomyDefaultInstanceStrategy
          .execute(multiclassificationRequestModel);
      createDefaultInstance(localeCatlogDAO, taxonomy, configDetails);
    }
    return null;
  }
  
  private void createDefaultInstance(ILocaleCatalogDAO localeCatlogDAO, String type, IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(type);
    
   
    IBaseEntityDTO baseEntityDTO = localeCatlogDAO.newBaseEntityDTOBuilder(type, BaseType.UNDEFINED, classifierDTO).build();
    
    IBaseEntityDAO baseEntityDAO = localeCatlogDAO.openBaseEntity(baseEntityDTO);
    IPropertyRecordDTO[] propertyRecords = createPropertyRecordInstance(baseEntityDAO, localeCatlogDAO, configDetails);
    baseEntityDAO.createPropertyRecords(propertyRecords);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  protected IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,ILocaleCatalogDAO localeCatalogDAO,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails,
        PropertyRecordType.DEFAULT_COUPLED, localeCatalogDAO);
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = this.createAttributePropertyRecordInstance(propertyRecordBuilder, configDetails);
    // Create tag
    propertyRecords.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder, configDetails));
    return propertyRecords.toArray(new IPropertyRecordDTO[propertyRecords.size()]);
  }
  
  protected List<IPropertyRecordDTO> createAttributePropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    configDetails.getReferencedAttributes().values().forEach(referencedAttribute -> {
      try {
        IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
        if (dto != null) {
          attributePropertyRecords.add(dto);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return attributePropertyRecords;
  }
  
  protected List<IPropertyRecordDTO> createTagPropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    List<IPropertyRecordDTO> tagPropertyRecords = new ArrayList<>();
    configDetails.getReferencedTags().values().forEach(referencedTag -> {
      try {
        IPropertyRecordDTO dto = propertyRecordBuilder.createTagsRecordForDefaultInstance(referencedTag);
        if (dto != null) {
          tagPropertyRecords.add(dto);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return tagPropertyRecords;
  }
  
}
