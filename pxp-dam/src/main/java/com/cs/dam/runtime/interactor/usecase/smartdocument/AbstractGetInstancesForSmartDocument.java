package com.cs.dam.runtime.interactor.usecase.smartdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.core.config.strategy.usecase.smartdocument.IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy;
import com.cs.core.config.strategy.usecase.user.IGetAllUsersStrategy;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IMulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.MulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import com.cs.utils.dam.SmartDocumentUtils;

public abstract class AbstractGetInstancesForSmartDocument extends
    AbstractRuntimeInteractor<IGetEntityForSmartDocumentRequestModel, IListModel<ISmartDocumentKlassInstanceDataModel>> {
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy getConfigDetailsWithoutPermissionsForSmartDocumentStrategy;
  
  @Autowired
  protected IGetAllUsersStrategy                                        getAllUsersStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                         rdbmsComponentUtils;
  
  protected abstract IGetConfigDetailsToFetchDataForSmartDocumentResponseModel getConfigDetails(
      IGetEntityForSmartDocumentRequestModel dataModel) throws Exception;
  
  protected abstract IGetInstancesForSmartDocumentResponseModel getInstancesForSmartDocument(
      IGetEntityForSmartDocumentRequestModel dataModel,
      IGetConfigDetailsToFetchDataForSmartDocumentResponseModel configDetails) throws Exception;
  
  @Override
  public IListModel<ISmartDocumentKlassInstanceDataModel> executeInternal(IGetEntityForSmartDocumentRequestModel dataModel) throws Exception
  {
    IListModel<ISmartDocumentKlassInstanceDataModel> returnModel = new ListModel<>();
    List<ISmartDocumentKlassInstanceDataModel> smartDocumentKlassInstanceList = new ArrayList<>();
    
    IGetConfigDetailsToFetchDataForSmartDocumentResponseModel configDetails = getConfigDetails(dataModel);
    
    IGetInstancesForSmartDocumentResponseModel response = getInstancesForSmartDocument(dataModel, configDetails);
    
    getKlassInstances(smartDocumentKlassInstanceList, configDetails, response);
    returnModel.setList(smartDocumentKlassInstanceList);
    return returnModel;
  }
  
  protected void getKlassInstances(
      List<ISmartDocumentKlassInstanceDataModel> smartDocumentKlassInstanceList,
      IGetConfigDetailsToFetchDataForSmartDocumentResponseModel configDetails,
      IGetInstancesForSmartDocumentResponseModel response) throws Exception
  {
    Map<String, IAssetInformationModel> imageAttributeInstanceMap = response
        .getInstancesImageAttribute();
    String languageCode = configDetails.getPresetConfigDetails().getLanguageCode();
    String presetId = configDetails.getPresetConfigDetails().getId();
    for (IBaseEntityDTO baseEntityDTO : response.getKlassInstances()) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      IGetConfigDetailsForCustomTabModel configDetailsForInstance = getConfigDetailsForKlassInstance(languageCode, baseEntityDAO);
      IGetSmartDocumentPresetModel smartDocumentPreset = configDetails.getPresetConfigDetails();
      baseEntityDTO = SmartDocumentUtils.loadAttributesAndTagsPropertyForSmartDocument(baseEntityDAO, configDetailsForInstance, smartDocumentPreset);
     ISmartDocumentKlassInstanceDataModel smartDocumentKlassInstance = SmartDocumentUtils
          .getSmartDocumentKlassInstance(baseEntityDTO, imageAttributeInstanceMap,configDetailsForInstance, getUserCodeLabelMap());
      processSmartDocumentModel(smartDocumentKlassInstance, baseEntityDTO, configDetailsForInstance, response);
      smartDocumentKlassInstance.setPresetId(presetId);
      smartDocumentKlassInstanceList.add(smartDocumentKlassInstance);
    }
  }
  
  /**
   * Get config details of klass instance for smart document
   * @param languageCode
   * @param baseEntityDAO
   * @return
   * @throws Exception
   * @throws RDBMSException
   */
  protected IGetConfigDetailsForCustomTabModel getConfigDetailsForKlassInstance(String languageCode,
      IBaseEntityDAO baseEntityDAO) throws Exception, RDBMSException
  {
    List<String> taxonomy = BaseEntityUtils
        .getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
    List<String> types = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO);
    IMulticlassificationRequestModelForSmartDocument multiClassificationRequestModelForSmartDocument = new MulticlassificationRequestModelForSmartDocument();
    multiClassificationRequestModelForSmartDocument.setShouldUseTagIdTagValueIdsMap(false);
    multiClassificationRequestModelForSmartDocument.setKlassIds(types);
    multiClassificationRequestModelForSmartDocument.setSelectedTaxonomyIds(taxonomy);
    multiClassificationRequestModelForSmartDocument.setSelectedLanguage(languageCode);
    IGetConfigDetailsForCustomTabModel configDetailsForInstance = getConfigDetailsWithoutPermissionsForSmartDocumentStrategy
        .execute(multiClassificationRequestModelForSmartDocument);
    return configDetailsForInstance;
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, String> getUserCodeLabelMap() throws Exception
  {
    Map<String, String> userCodeLabelMap = new HashMap<>();
    IListModel<IUserInformationModel> usersInfo = getAllUsersStrategy.execute(new UserModel());
    List<IUserInformationModel> userInfoList = (List<IUserInformationModel>) usersInfo.getList();
    for (IUserInformationModel userInfo : userInfoList) {
      userCodeLabelMap.put(userInfo.getCode(), userInfo.getFirstName() + " " + userInfo.getLastName());
    }
    return userCodeLabelMap;
  }
  
  /**
   * Function to process every SmartDocument Model
   * 
   * @param smartDocumentKlassInstance
   * @param baseEntityDTO
   * @param configDetailsForInstance
   * @param response
   */
  protected void processSmartDocumentModel(ISmartDocumentKlassInstanceDataModel smartDocumentKlassInstance, IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForCustomTabModel configDetailsForInstance, IGetInstancesForSmartDocumentResponseModel response)
  {
    
  }
}
