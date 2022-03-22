package com.cs.dam.runtime.assetinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.MetadataUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceCustomTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;

@Service
public class SaveAssetInstanceForTabsService
    extends AbstractSaveInstanceForTabs<IAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveAssetInstanceForTabsService {
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceCustomTabStrategy getConfigDetailsForAssetInstanceCustomTab;
  
  @Autowired
  protected IFetchAssetConfigurationDetails                    fetchAssetConfigurationDetails;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IAssetInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEASSET;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails(IMulticlassificationRequestModel model,
      IKlassInstanceSaveModel saveModel) throws Exception
  {
    if (saveModel.getTabType().equals(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE)) {
      return getConfigDetailsForAssetInstanceCustomTab.execute(model);
    }
    return super.getConfigDetails(model, saveModel);
  }
  
  @Override
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails,
      IKlassInstanceSaveModel saveInstanceModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceModel executeGetKlassInstance = super.executeGetKlassInstance(configDetails,
        saveInstanceModel, baseEntityDAO);
    fillAssetCoverFlowAttributeInstance(executeGetKlassInstance, baseEntityDAO);
    return executeGetKlassInstance;
  }
  
  private void fillAssetCoverFlowAttributeInstance(IGetKlassInstanceModel executeGetKlassInstance,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    IAssetInstance assetInstance = (IAssetInstance) executeGetKlassInstance.getKlassInstance();
    AssetInstanceUtils.fillEntityExtensionInAssetCoverFlowAttribute(assetInstance, baseEntityDTO,
        entityExtension);

  }
  
  @Override
  protected List<IPropertyRecordDTO> manageAddedAttributes(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      PropertyRecordBuilder propertyRecordBuilder, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    if (klassInstancesModel.getAddedAttributes().isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IContentAttributeInstance> addedAttributes = klassInstancesModel.getAddedAttributes();
    List<IPropertyRecordDTO> valueRecords = new ArrayList<>();
    
    for (IContentAttributeInstance addedAttribute : addedAttributes) {
        IAttribute attributeConfig = configDetails.getReferencedAttributes().get(addedAttribute.getAttributeId());
        if(attributeConfig.getIsTranslatable()) {
          IAttributeInstance attributeInstance = (IAttributeInstance)addedAttribute;
          attributeInstance.setLanguage(controllerThread.getTransactionData().getDataLanguage());
          valueRecords.add(propertyRecordBuilder.updateValueRecord((IContentAttributeInstance)attributeInstance));
        }
        else {
          valueRecords.add(propertyRecordBuilder.updateValueRecord(addedAttribute));
        }
      if((addedAttribute.getBaseType()).equals(Constants.IMAGE_ATTRIBUTE_INSTANCE_TYPE)) {
        IAssetInstanceSaveModel assteInstanceSave = (IAssetInstanceSaveModel) klassInstancesModel;
        Map<String, Object> metadata = (Map<String, Object>) assteInstanceSave.getMetadata();
        if(!metadata.isEmpty()) {
          List<IPropertyRecordDTO> metaDataAttributes = MetadataUtils.addMetadataAttributesToAssetInstanceAttributes(
              metadata, baseEntityDAO, (IGetConfigDetailsForCustomTabModel)configDetails, rdbmsComponentUtils.getLocaleCatlogDAO());
          
          baseEntityDAO.createPropertyRecords(metaDataAttributes.toArray(new IPropertyRecordDTO[] {}));
        }
      }
    }
    return valueRecords;
  }
}
