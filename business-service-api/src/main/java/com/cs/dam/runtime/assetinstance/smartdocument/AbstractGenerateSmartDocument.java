package com.cs.dam.runtime.assetinstance.smartdocument;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.DAMConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.assetserver.IUploadMultipleAssetsToServerService;
import com.cs.core.config.interactor.exception.smartdocument.ProductLanguageNotAvailableException;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.IUploadSmartDocumentModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.UploadSmartDocumentModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentInfoModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.config.strategy.usecase.user.IGetAllUsersStrategy;
import com.cs.core.exception.base.smartdocument.JsonConfigurationException;
import com.cs.core.exception.base.smartdocument.SmartDocumentTemplateNotFoundException;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.exception.asset.SDTemplateConfigurationException;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.smartdocument.GenerateSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.GenerateSmartDocumentSuccessModel;
import com.cs.core.runtime.interactor.model.smartdocument.GetInstancesForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentSuccessModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IMulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentInput;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentRendererRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentRendererResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTemplateEngineRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTemplateEngineResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.MulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentInput;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentRendererRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentTemplateEngineRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.dam.runtime.smartdocument.IFetchDataForSmartDocumentService;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.SmartDocumentUtils;

@Service
public abstract class AbstractGenerateSmartDocument<P extends IGenerateSmartDocumentRequestModel, R extends IGenerateSmartDocumentResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  private static final String                 INPUT_STREAM             = "inputStream";
  protected static final List<String>         CSS_FILE_EXTENTIONS      = Arrays.asList("css");
  protected static final List<String>         FONT_FILE_EXTENTIONS     = Arrays.asList("ttf",
      "otf");
  protected static final List<String>         TEMPLATE_FILE_EXTENTIONS = Arrays.asList("vm");
  protected static final List<String>         JSON_FILE_EXTENTIONS     = Arrays.asList("json");
  protected static final String               FETCH_DATA               = "fetchData";
  
  @Autowired
  protected IFetchAssetConfigurationDetails   fetchAssetConfigurationDetails;
  
  @Autowired
  protected IGetAllUsersStrategy              getAllUsersStrategy;
  
  @Autowired
  protected RDBMSComponentUtils               rdbmsComponentUtils;
  
  @Autowired
  IUploadMultipleAssetsToServerService        uploadMultipleSmartDocumentsToServerService;
  
  @Autowired
  protected IFetchDataForSmartDocumentService fetchDataForSmartDocumentService;
  
  @Autowired
  protected SearchAssembler                   searchAssembler;
  
  protected abstract IGetSmartDocumentInfoModel getSmartDocumentConfigInformation(
      String smartDocumentPresetId) throws Exception;
  
  protected abstract IGetInstancesForSmartDocumentResponseModel getInstancesForSmartDocument(
      IGetInstancesForSmartDocumentRequestModel instanceIdModel) throws Exception;
  
  protected abstract IGetConfigDetailsForCustomTabModel getConfigDetailsForInstance(
      IMulticlassificationRequestModelForSmartDocument articleInstance) throws Exception;
  
  protected abstract ISmartDocumentTemplateEngineResponseModel generateHtml(
      ISmartDocumentTemplateEngineRequestModel smartTemplateEngineRequestModel) throws Exception;
  
  protected abstract ISmartDocumentRendererResponseModel generatePdf(
      ISmartDocumentRendererRequestModel requestModel) throws Exception;
  
  @Override
  protected IGenerateSmartDocumentResponseModel executeInternal(
      IGenerateSmartDocumentRequestModel dataModel) throws Exception
  {
    
    IGenerateSmartDocumentResponseModel responseModel = new GenerateSmartDocumentResponseModel();
    IGenerateSmartDocumentSuccessModel successModel = new GenerateSmartDocumentSuccessModel();
    ISmartDocumentInput smartDocumentTemplateBinary = new SmartDocumentInput();
    
    // get config details of smart document from orientDB.
    IGetSmartDocumentInfoModel configDetails = getSmartDocumentConfigInformation(
        dataModel.getSmartDocumentPresetId());
    IGetSmartDocumentPresetModel presetModel = configDetails.getSmartDocumentPreset();
    String presetLanguageCode = presetModel.getLanguageCode();
    presetLanguageCode = presetLanguageCode.isEmpty() ? transactionThread.getTransactionData()
        .getDataLanguage() : presetLanguageCode;
    presetModel.setLanguageCode(presetLanguageCode);
    
    // get zip file from server and populate smartDocumentTemplateBinary.
    setBinaryFromZip(smartDocumentTemplateBinary, configDetails.getZipTemplateId());
    
    // get list of SmartDocumentKlassInstanceDataModel according to preset and
    // klass instance configuration.
    List<ISmartDocumentKlassInstanceDataModel> smartTemplateKlassInstanceModels = fillSmartTemplateInstanceModel(
        dataModel, presetModel, responseModel);
    
    // get file name suffix as currentDate_selectedLanguage
    String fileNameSuffix = getAssetInstanceFileNameSuffix(presetModel);
    
    List<IMultiPartFileInfoModel> creatAndLinkSmartDocumentModels = new ArrayList<>();
    if (!smartTemplateKlassInstanceModels.isEmpty()) {
      if (presetModel.getSplitDocument()) {
        // generate separate smart document pdf for all instances
        creatAndLinkSmartDocumentModels = createSplittedSmartDocuments(successModel,
            smartDocumentTemplateBinary, configDetails, smartTemplateKlassInstanceModels,
            fileNameSuffix);
      }
      else {
        // generate single smart document pdf for all the instances
        creatAndLinkSmartDocumentModels = createMergedSmartDocument(successModel,
            smartDocumentTemplateBinary, configDetails, smartTemplateKlassInstanceModels,
            fileNameSuffix);
      }
      // upload generated smart document to swift
      if (presetModel.getSaveDocument()) {
        IBulkUploadResponseAssetModel response = createAssetAndLinkDocument(
            creatAndLinkSmartDocumentModels, presetLanguageCode, dataModel.getBaseType());
        responseModel.getFailure()
            .getDevExceptionDetails()
            .addAll(response.getFailure()
                .getDevExceptionDetails());
        responseModel.getFailure()
            .getExceptionDetails()
            .addAll(response.getFailure()
                .getExceptionDetails());
      }
    }
    
    successModel.setShowPreview(presetModel.getShowPreview());
    responseModel.setSuccess(successModel);
    return responseModel;
  }
  
  private List<IMultiPartFileInfoModel> createMergedSmartDocument(
      IGenerateSmartDocumentSuccessModel successModel,
      ISmartDocumentInput smartDocumentTemplateBinary, IGetSmartDocumentInfoModel configDetails,
      List<ISmartDocumentKlassInstanceDataModel> smartTemplateKlassInstanceModels,
      String fileNameSuffix) throws Exception
  {
    List<IMultiPartFileInfoModel> creatAndLinkSmartDocumentModels = new ArrayList<>();
    IGetSmartDocumentPresetModel presetModel = configDetails.getSmartDocumentPreset();
    
    ISmartDocumentRendererResponseModel responseFromRenderer = generatePdfFromDataModel(
        smartDocumentTemplateBinary, smartTemplateKlassInstanceModels, configDetails);
    successModel.setPdfBytes(Arrays.asList(responseFromRenderer.getPdfBytes()));
    
    List<String> instanceIds = new ArrayList<>();
    for (ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel : smartTemplateKlassInstanceModels) {
      instanceIds.add(smartTemplateKlassInstanceModel.getId());
    }
    if (presetModel.getSaveDocument()) {
      IMultiPartFileInfoModel creatAndLinkSmartDocumentModel = new MultiPartFileInfoModel();
      String assetInstanceFileNamePrefix = presetModel.getLabel();
      if (instanceIds.size() == 1) {
        assetInstanceFileNamePrefix = smartTemplateKlassInstanceModels.get(0)
            .getLabel();
      }
      String assetInstanceFileName = assetInstanceFileNamePrefix + DAMConstants.FILE_NAME_SEPERATOR
          + fileNameSuffix;
      creatAndLinkSmartDocumentModel.setKey(UUID.randomUUID()
          .toString());
      creatAndLinkSmartDocumentModel.setOriginalFilename(assetInstanceFileName);
      creatAndLinkSmartDocumentModel.setBytes(responseFromRenderer.getPdfBytes());
      creatAndLinkSmartDocumentModel.setIds(instanceIds);
      creatAndLinkSmartDocumentModels.add(creatAndLinkSmartDocumentModel);
    }
    return creatAndLinkSmartDocumentModels;
  }
  
  private List<IMultiPartFileInfoModel> createSplittedSmartDocuments(
      IGenerateSmartDocumentSuccessModel successModel,
      ISmartDocumentInput smartDocumentTemplateBinary, IGetSmartDocumentInfoModel configDetails,
      List<ISmartDocumentKlassInstanceDataModel> smartTemplateKlassInstanceModels,
      String fileNameSuffix) throws Exception
  {
    List<IMultiPartFileInfoModel> creatAndLinkSmartDocumentModels = new ArrayList<>();
    IGetSmartDocumentPresetModel presetModel = configDetails.getSmartDocumentPreset();
    
    for (ISmartDocumentKlassInstanceDataModel iSmartDocumentKlassInstanceDataModel : smartTemplateKlassInstanceModels) {
      ISmartDocumentRendererResponseModel responseFromRenderer = generatePdfFromDataModel(
          smartDocumentTemplateBinary, Arrays.asList(iSmartDocumentKlassInstanceDataModel),
          configDetails);
      successModel.getPdfBytes()
          .add(responseFromRenderer.getPdfBytes());
      if (presetModel.getSaveDocument()) {
        IMultiPartFileInfoModel creatAndLinkSmartDocumentModel = new MultiPartFileInfoModel();
        String assetInstanceFilename = iSmartDocumentKlassInstanceDataModel.getLabel()
            + DAMConstants.FILE_NAME_SEPERATOR + fileNameSuffix;
        creatAndLinkSmartDocumentModel.setKey(UUID.randomUUID()
            .toString());
        creatAndLinkSmartDocumentModel.setOriginalFilename(assetInstanceFilename);
        creatAndLinkSmartDocumentModel.setBytes(responseFromRenderer.getPdfBytes());
        creatAndLinkSmartDocumentModel
            .setIds(Arrays.asList(iSmartDocumentKlassInstanceDataModel.getId()));
        creatAndLinkSmartDocumentModels.add(creatAndLinkSmartDocumentModel);
      }
    }
    return creatAndLinkSmartDocumentModels;
  }
  
  private ZipInputStream getZipFromServer(String assetKey)
      throws SmartDocumentTemplateNotFoundException
  {
    Map<String, Object> assetServerDetailsMap = new HashMap<>();
    IJSONContent authenticateSwiftServer;
    
    try {
      authenticateSwiftServer = CSDAMServer.instance()
          .getServerInformation();
      assetServerDetailsMap.put("storageUrl",
          authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
      assetServerDetailsMap.put("authToken",
          authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
      
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, assetKey);
      requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetailsMap);
      requestMap.put(IGetAssetDetailsRequestModel.CONTAINER,
          DAMConstants.SWIFT_CONTAINER_SD_TEMPLATES);
      requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, new HashMap<>());
      
      Map<String, Object> responseMap = CSDAMServer.instance()
          .getAsset(requestMap);
      return new ZipInputStream((InputStream) responseMap.get(INPUT_STREAM));
    }
    catch (Exception ex) {
      throw new SmartDocumentTemplateNotFoundException(ex.getMessage());
    }
  }
  
  private void setBinaryFromZip(ISmartDocumentInput smartDocumentInputStreams, String zipFileId)
      throws Exception
  {
    ZipInputStream zipInputStream = getZipFromServer(zipFileId);
    ZipEntry zipEntry;
    Integer jsonFileCount = 0;
    while ((zipEntry = zipInputStream.getNextEntry()) != null) {
      String zipEntryName = zipEntry.getName();
      String fileExtention = FilenameUtils.getExtension(zipEntryName);
      if (CSS_FILE_EXTENTIONS.contains(fileExtention)) {
        smartDocumentInputStreams.getCssFileBytes()
            .put(zipEntryName, IOUtils.toByteArray(new InputStreamReader(zipInputStream,
                Charset.forName(StandardCharsets.UTF_8.name())), Charset.defaultCharset()));
      }
      else if (FONT_FILE_EXTENTIONS.contains(fileExtention)) {
        smartDocumentInputStreams.getFontFileBytes()
            .put(zipEntryName, IOUtils.toByteArray(zipInputStream));
      }
      else if (TEMPLATE_FILE_EXTENTIONS.contains(fileExtention)) {
        smartDocumentInputStreams.getTemplateFileBytes()
            .put(zipEntryName, IOUtils.toByteArray(new InputStreamReader(zipInputStream,
                Charset.forName(StandardCharsets.UTF_8.name())), Charset.defaultCharset()));
      }
      else if (JSON_FILE_EXTENTIONS.contains(fileExtention)) {
        if (jsonFileCount > 0) {
          throw new SDTemplateConfigurationException();
        }
        jsonFileCount++;
        smartDocumentInputStreams.setJsonFileBytes(IOUtils.toByteArray(
            new InputStreamReader(zipInputStream, Charset.forName(StandardCharsets.UTF_8.name())),
            Charset.defaultCharset()));
      }
    }
    
    // validate Zip file using JSON file
    validateJsonFile(smartDocumentInputStreams);
    
    zipInputStream.close();
    if (smartDocumentInputStreams.getTemplateFileBytes()
        .size() == 0) {
      throw new SmartDocumentTemplateNotFoundException();
    }
  }
  
  /**
   * Validating Zip file using JSON file.
   * 
   * @param smartDocumentInputStreams
   * @throws Exception
   * @throws SmartDocumentTemplateNotFoundException
   */
  @SuppressWarnings("unchecked")
  private void validateJsonFile(ISmartDocumentInput smartDocumentInputStreams) throws Exception
  {
    byte[] jsonFileBytes = smartDocumentInputStreams.getJsonFileBytes();
    if (jsonFileBytes == null) {
      return;
    }
    Map<String, byte[]> templateFileBytesMap = smartDocumentInputStreams.getTemplateFileBytes();
    Map<String, Object> jsonMap = ObjectMapperUtil.readValue(new String(jsonFileBytes), Map.class);
    
    if (templateFileBytesMap != null && templateFileBytesMap.size() > 1 && jsonMap != null
        && !jsonMap.isEmpty()) {
      for (int index = 1; index <= jsonMap.size(); index++) {
        String templateFileName = (String) jsonMap.get(Integer.toString(index));
        if (StringUtils.isBlank(templateFileName)
            || !templateFileBytesMap.containsKey(templateFileName)) {
          throw new JsonConfigurationException();
        }
      }
    }
  }
  
  private IMulticlassificationRequestModelForSmartDocument getMulticlassificationConfigModel(
      IGetSmartDocumentPresetModel smartDocumentPreset, IBaseEntityDTO baseEntityDTO)
  {
    List<String> classifierCodes = new ArrayList<>();
    List<String> taxonomy = new ArrayList<>();
    classifierCodes.add(baseEntityDTO.getNatureClassifier()
        .getClassifierCode());
    for (IClassifierDTO classifier : baseEntityDTO.getOtherClassifiers()) {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        classifierCodes.add(classifier.getClassifierCode());
      }
      else if (classifierType.equals(ClassifierType.TAXONOMY)
          || classifierType.equals(ClassifierType.MINOR_TAXONOMY)) {
        taxonomy.add(classifier.getClassifierCode());
      }
    }
    
    IMulticlassificationRequestModelForSmartDocument multiClassificationRequestModelForSmartDocument = new MulticlassificationRequestModelForSmartDocument();
    multiClassificationRequestModelForSmartDocument.setShouldUseTagIdTagValueIdsMap(false);
    multiClassificationRequestModelForSmartDocument.setKlassIds(classifierCodes);
    multiClassificationRequestModelForSmartDocument.setSelectedTaxonomyIds(taxonomy);
    multiClassificationRequestModelForSmartDocument
        .setSelectedLanguage(smartDocumentPreset.getLanguageCode());
    return multiClassificationRequestModelForSmartDocument;
  }
  
  public List<ISmartDocumentKlassInstanceDataModel> fillSmartTemplateInstanceModel(
      IGenerateSmartDocumentRequestModel dataModel,
      IGetSmartDocumentPresetModel smartDocumentPreset,
      IGenerateSmartDocumentResponseModel responseModel) throws Exception
  {
    List<ISmartDocumentKlassInstanceDataModel> returnList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    IGetInstancesForSmartDocumentRequestModel getInstancesForSmartDocumentRequestModel = new GetInstancesForSmartDocumentRequestModel();
    getInstancesForSmartDocumentRequestModel.setKlassInstanceIds(dataModel.getKlassInstanceIds());
    getInstancesForSmartDocumentRequestModel.setSmartDocumentPresetConfigData(smartDocumentPreset);
    getInstancesForSmartDocumentRequestModel.setBaseType(dataModel.getBaseType());
    
    IGetInstancesForSmartDocumentResponseModel instances = getInstancesForSmartDocument(
        getInstancesForSmartDocumentRequestModel);
    Map<String, IAssetInformationModel> imageAttributeInstanceMap = instances
        .getInstancesImageAttribute();
    for (IBaseEntityDTO articleInstance : instances.getKlassInstances()) {
      IMulticlassificationRequestModelForSmartDocument multiClassificationRequestModelForSmartDocument = getMulticlassificationConfigModel(
          smartDocumentPreset, articleInstance);
      IGetConfigDetailsForCustomTabModel articleConfigDetails = getConfigDetailsForInstance(
          multiClassificationRequestModelForSmartDocument);
      
      // Load property records
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(articleInstance);
      IBaseEntityDTO baseEntityDTO = SmartDocumentUtils
          .loadAttributesAndTagsPropertyForSmartDocument(baseEntityDAO, articleConfigDetails,
              smartDocumentPreset);
      
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel = SmartDocumentUtils
          .getSmartDocumentKlassInstance(baseEntityDTO, imageAttributeInstanceMap,
              articleConfigDetails, getUserCodeLabelMap());
      smartTemplateKlassInstanceModel.setPresetId(smartDocumentPreset.getId());
      returnList.add(smartTemplateKlassInstanceModel);
    }
    for (String klassInstancesNotAvailableLable : instances.getKlassInstancesNotAvailableLabels()) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure,
          new ProductLanguageNotAvailableException(), null, klassInstancesNotAvailableLable);
    }
    responseModel.setFailure(failure);
    return returnList;
  }
  
  private String getAssetInstanceFileNameSuffix(IGetSmartDocumentPresetModel presetModel)
  {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    Date date = new Date();
    String dateString = formatter.format(date);
    
    String languageCode = presetModel.getLanguageCode();
    if (languageCode == null || languageCode.isEmpty()) {
      languageCode = transactionThread.getTransactionData()
          .getDataLanguage();
    }
    
    return dateString + DAMConstants.FILE_NAME_SEPERATOR + languageCode + ".pdf";
  }
  
  private ISmartDocumentRendererResponseModel generatePdfFromDataModel(
      ISmartDocumentInput smartDocumentTemplateBinary,
      List<ISmartDocumentKlassInstanceDataModel> smartTemplateKlassInstanceModels,
      IGetSmartDocumentInfoModel configDetails) throws Exception
  {
    ISmartDocumentTemplateEngineRequestModel smartTemplateEngineRequestModel = new SmartDocumentTemplateEngineRequestModel();
    smartTemplateEngineRequestModel.setSmartDocumentByteArrays(smartDocumentTemplateBinary);
    
    smartTemplateEngineRequestModel
        .setSmartDocumentKlassInstancesDataModel(smartTemplateKlassInstanceModels);
    
    Map<String, Object> fetchDataObject = new HashMap<>();
    fetchDataObject.put(FETCH_DATA, fetchDataForSmartDocumentService);
    smartTemplateEngineRequestModel.setFetchDataObject(fetchDataObject);
    
    ISmartDocumentTemplateEngineResponseModel responseFromTemplate = generateHtml(
        smartTemplateEngineRequestModel);
    
    ISmartDocumentRendererRequestModel smartRendererRequestModel = getRequestModelForRenderer(
        smartDocumentTemplateBinary, configDetails, responseFromTemplate);
    
    return generatePdf(smartRendererRequestModel);
  }
  
  private ISmartDocumentRendererRequestModel getRequestModelForRenderer(
      ISmartDocumentInput smartDocumentTemplateBinary, IGetSmartDocumentInfoModel configDetails,
      ISmartDocumentTemplateEngineResponseModel responseFromTemplate)
  {
    ISmartDocumentRendererRequestModel smartRendererRequestModel = new SmartDocumentRendererRequestModel();
    smartRendererRequestModel
        .setHtmlFromTemplateEngine(responseFromTemplate.getHtmlFromTemplateEngine());
    smartRendererRequestModel.setRendererLicenceKey(configDetails.getRendererLicenceKey());
    smartRendererRequestModel.setSmartDocumentPreset(configDetails.getSmartDocumentPreset());
    smartRendererRequestModel.setSmartDocumentByteArrays(smartDocumentTemplateBinary);
    return smartRendererRequestModel;
  }
  
  private IBulkUploadResponseAssetModel createAssetAndLinkDocument(
      List<IMultiPartFileInfoModel> creatAndLinkSmartDocumentModels, String presetLanguageCode,
      String baseType) throws Exception
  {
    IUploadSmartDocumentModel uploadSmartDocumentModel = new UploadSmartDocumentModel();
    uploadSmartDocumentModel.setPresetLanguageCode(presetLanguageCode);
    uploadSmartDocumentModel.setMultiPartFileInfoList(creatAndLinkSmartDocumentModels);
    uploadSmartDocumentModel.setKlassId(SystemLevelIds.SMARTDOCUMENT_ASSET);
    uploadSmartDocumentModel.setBaseType(baseType);
    uploadSmartDocumentModel.setMode(DAMConstants.MODE_BULK_UPLOAD);
    return uploadMultipleSmartDocumentsToServerService.execute(uploadSmartDocumentModel);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, String> getUserCodeLabelMap() throws Exception
  {
    Map<String, String> userCodeLabelMap = new HashMap<>();
    IListModel<IUserInformationModel> usersInfo = getAllUsersStrategy.execute(new UserModel());
    List<IUserInformationModel> userInfoList = (List<IUserInformationModel>) usersInfo.getList();
    for (IUserInformationModel userInfo : userInfoList) {
      userCodeLabelMap.put(userInfo.getCode(),
          userInfo.getFirstName() + " " + userInfo.getLastName());
    }
    return userCodeLabelMap;
  }
}
