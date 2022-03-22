package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.builder.BuilderFactory;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;
import com.cs.core.runtime.interactor.entity.language.LanguageKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.model.languageinstance.LanguageComparisonReponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.util.ConfigUtil;
import com.cs.utils.BaseEntityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractGetInstanceForLanguageComparisonService<P extends IGetInstanceForLanguageComparisonRequestModel, R extends ILanguageComparisonResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy getConfigDetailsForCustomTab;
  
  @Autowired
  protected PermissionUtils                       permissionUtils;
  
  @Autowired
  protected TransactionThreadData                 controllerThread;
  
  @Autowired
  protected ConfigUtil                            configUtil;
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;
  
  @Override
  protected R executeInternal(P instanceRequestModel) throws Exception
  {
    List<String> languageInheritance = configUtil.getLanguageInheritance();
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils
        .getBaseEntityDAO(Long.parseLong(instanceRequestModel.getId()),languageInheritance);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = configUtil
        .getConfigRequestModelForCustomTab(instanceRequestModel, baseEntityDAO);
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetail(
        multiclassificationRequestModel);
    
    ILanguageComparisonResponseModel responseModel = new LanguageComparisonReponseModel();
    
    List<String> comparisonLanguages = instanceRequestModel.getLanguagesToBeCompared();
    comparisonLanguages.add(controllerThread.getTransactionData().getDataLanguage());
    List<ILanguageKlassInstance> languageKlassInstance = new ArrayList<>();
    responseModel.setLanguageInstances(languageKlassInstance);
    
    for(String comparisonLanguage : comparisonLanguages) {
      IBaseEntityDAO comparsionDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(instanceRequestModel.getId()), comparisonLanguage);
      ILanguageKlassInstance comparedInstance = getLanguageComparisonRequestModel(instanceRequestModel, configDetails, comparsionDAO);
      comparedInstance.setLanguage(comparisonLanguage);
      languageKlassInstance.add(comparedInstance);
    }
    
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, this.rdbmsComponentUtils);
    responseModel.setKlassInstance((IContentInstance) klassInstanceBuilder.getKlassInstance());
    responseModel.setConfigDetails(configDetails);
    manageKlassInstancePermissions(responseModel);
    
    manageContentRelationshipInstance(responseModel, baseEntityDAO, configDetails);
    return (R) responseModel;
  }
  
  protected IGetConfigDetailsForCustomTabModel getConfigDetail(
      IMulticlassificationRequestModel multiclassificationRequestModel) throws Exception
  {
    return getConfigDetailsForCustomTab.execute(multiclassificationRequestModel);
  }
  
  protected void manageKlassInstancePermissions(ILanguageComparisonResponseModel responseModel)
  {
    permissionUtils.manageKlassInstancePermissions(responseModel);
  }
  
  protected ILanguageKlassInstance getLanguageComparisonRequestModel(
      IGetInstanceForLanguageComparisonRequestModel instanceRequestModel,
      IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    
    List<IPropertyDTO> referenceAttributesTags = BaseEntityUtils
        .getReferenceAttributesTagsProperties(configDetails.getReferencedAttributes(),
            configDetails.getReferencedTags(), baseEntityDAO);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(
        referenceAttributesTags.toArray(new IPropertyDTO[referenceAttributesTags.size()]));
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    ILanguageKlassInstance languageKlassInstance = fillLanguageklassInstances(propertyRecords,
        baseEntityDTO);
    return languageKlassInstance;
  }
  
  protected ILanguageKlassInstance fillLanguageklassInstances(
      Set<IPropertyRecordDTO> propertyRecords, IBaseEntityDTO baseEntityDTO)
  {
    
    ILanguageKlassInstance languageKlassInstance = new LanguageKlassInstance();
    List<IContentAttributeInstance> dependentAttributes = new ArrayList<>();
    
    languageKlassInstance.setCommonInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    languageKlassInstance.setName(baseEntityDTO.getBaseEntityName());
    languageKlassInstance.setLanguage(baseEntityDTO.getBaseLocaleID());
    
    for (IPropertyRecordDTO propertyRecordDto : propertyRecords) {
      if (propertyRecordDto instanceof IValueRecordDTO) {
        IValueRecordDTO valueRecordDto = (IValueRecordDTO) propertyRecordDto;
        if (StringUtils.isNotEmpty(valueRecordDto.getLocaleID())) {
          IContentAttributeInstance fillDependentAttributesInfo = fillDependentAttributesInfo(
              valueRecordDto, baseEntityDTO);
          dependentAttributes.add(fillDependentAttributesInfo);
        }
      }
    }
    languageKlassInstance.setDependentAttributes(dependentAttributes);
    
    return languageKlassInstance;
  }
  
  public IContentAttributeInstance fillDependentAttributesInfo(IValueRecordDTO valueRecordDto,
      IBaseEntityDTO baseEntityDTO)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    
    attributeInstance.setAttributeId(valueRecordDto.getProperty()
        .getCode());
    attributeInstance.setId(KlassInstanceBuilder.getAttributeID(valueRecordDto));
    attributeInstance.setBaseType(AttributeInstance.class.getName());
    attributeInstance.setLanguage(valueRecordDto.getLocaleID());
    attributeInstance.setValue(valueRecordDto.getValue());
    attributeInstance.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    attributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    attributeInstance.setValueAsHtml(valueRecordDto.getAsHTML());
    attributeInstance.setCode(valueRecordDto.getProperty()
        .getCode());
    
    return attributeInstance;
  }
  
  public void manageContentRelationshipInstance(ILanguageComparisonResponseModel responseModel, IBaseEntityDAO baseEntityDAO, 
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception {
    
    IRelationshipInstanceModel relationshipInstanceModel = BuilderFactory
        .newRelationshipInstanceBuilder(rdbmsComponentUtils, configDetails)
        .baseEntityDAO(baseEntityDAO)
        .build();
    
    responseModel.setContentRelationships(relationshipInstanceModel.getContentRelationships());
  }
  
}

