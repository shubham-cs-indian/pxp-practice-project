package com.cs.dam.runtime.assetinstance;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.Constants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.asset.AssetExportAPIRequestModel;
import com.cs.core.config.interactor.model.asset.AssetInstanceExportResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIResponseModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.physicalcatalog.IGetPhysicalCatalogIdsModel;
import com.cs.core.config.physicalcatalog.IGetPhysicalCatalogIdsService;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.asset.IGetConfigDetailsForAssetExportAPIStrategy;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.utils.dam.AssetUtils;

/**
 * This service exports DAM asset in binary.
 * @author rahul.sehrawat
 *
 */
@Service
public class GetAssetInstanceToExportService extends AbstractRuntimeService<IAssetInstanceExportRequestModel, IAssetInstanceExportResponseModel> 
  implements IGetAssetInstanceToExportService {
  
  @Autowired
  protected RDBMSComponentUtils                        rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsForAssetExportAPIStrategy getConfigDetailsForAssetExportAPIStrategy;
  
  @Autowired
  protected IGetPhysicalCatalogIdsService              getPhysicalCatalogIdsService;
  
  @Autowired
  protected TransactionThreadData                      transactionThread;
  
  @Override
  protected IAssetInstanceExportResponseModel executeInternal(IAssetInstanceExportRequestModel requestModel) 
      throws Exception
  {
    IBaseEntityDTO baseEntityDTO = null;
    String fileName = StringUtils.isBlank(requestModel.getFileName()) ? "" : requestModel.getFileName();
    String organizationCode = StringUtils.isBlank(requestModel.getOrganizationCode())
        ? IStandardConfig.STANDARD_ORGANIZATION_RCODE : requestModel.getOrganizationCode();
    String endpointCode = StringUtils.isBlank(requestModel.getEndpointCode()) ? "" : requestModel.getEndpointCode();
    String catalogCode = StringUtils.isBlank(requestModel.getCatalogCode())
        && StringUtils.isBlank(endpointCode) ? Constants.PIM_CATALOG_IDS : requestModel.getCatalogCode();
    Map<String,String> assetInfo = new HashMap<String, String>();
    String languageCode = requestModel.getLanguageCode();
    IAssetInstanceExportResponseModel responseModel = new AssetInstanceExportResponseModel();

    IAssetExportAPIRequestModel configRequestmodel = new AssetExportAPIRequestModel();
    configRequestmodel.setEndpointCode(endpointCode);
    configRequestmodel.setOrganizationCode(IStandardConfig.STANDARD_ORGANIZATION_RCODE.equals(organizationCode) ? "-1" : organizationCode);
    configRequestmodel.setLanguageCode(languageCode);
    IAssetExportAPIResponseModel configResponseModel = getConfigDetailsForAssetExportAPIStrategy.execute(configRequestmodel);

    try {
      String assetInstanceId = requestModel.getAssetInstanceId();
      validateInputParameters(assetInstanceId, catalogCode, organizationCode, endpointCode, configResponseModel);
      catalogCode = getValidCatalogCodeFromEndpoint(catalogCode, configResponseModel);
      
      //Get baseentityDTO 
      IAssetMiscDAO assetMiscDao = AssetMiscDAO.getInstance();
      Map<String, Object> baseentityiidMap = assetMiscDao.getIIdAndBaseLocaleId(assetInstanceId, catalogCode, organizationCode, endpointCode, languageCode);
      long baseEntityIID = (long) baseentityiidMap.get("entityiid");
      if (baseEntityIID != -1) {
        String language = (String) baseentityiidMap.get("languageCode");
        IUserSessionDAO userSessionDAO = RDBMSUtils.newUserSessionDAO();        
        ILocaleCatalogDTO localeCatalogDTO = userSessionDAO.newLocaleCatalogDTO(language, catalogCode, organizationCode);  
        ILocaleCatalogDAO localeCatalogDAO = userSessionDAO.openLocaleCatalog(context.getUserSessionDTO(), localeCatalogDTO);
        baseEntityDTO = localeCatalogDAO.getEntityByIID(baseEntityIID);
      }
      else {
        responseModel.setErrorMessage("Asset not available for the request with " + assetInstanceId);
        return responseModel;
      }
      
      //Get Asset Information
      if (baseEntityDTO.getBaseType().equals(BaseType.ASSET)) {
        IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
        if (entityExtension != null && !entityExtension.isEmpty()) {
          assetInfo = getAssetInfoFromEntityExtension(entityExtension);
        } 
        else {
          responseModel.setErrorMessage("Asset not available for the request with " + assetInstanceId);
          return responseModel;
        }
      } else {
        responseModel.setErrorMessage("Request couldn't be completed because of invalid " + assetInstanceId);
        return responseModel;
      }
      
      //Get file name
      if (fileName.isEmpty()) {
        boolean shouldExportAssetWithOriginalFilename = configResponseModel.getShouldDownloadAssetWithOriginalFilename();
        fileName = shouldExportAssetWithOriginalFilename ? IAssetInstanceExportRequestModel.ORIGINAL_FILENAME 
            : IAssetInstanceExportRequestModel.ASSETNAME_FILENAME;
      }
      switch (fileName) {
        case IAssetInstanceExportRequestModel.ORIGINAL_FILENAME:
          responseModel.setFileName(assetInfo.get("fileName"));
          break;
        case IAssetInstanceExportRequestModel.ASSETNAME_FILENAME:
          responseModel.setFileName(baseEntityDTO.getBaseEntityName() + "." +assetInfo.get("extension"));
          break;
        default: 
          responseModel.setErrorMessage("Request couldn't be completed because of invalid fileName.");
          return responseModel;
      }    
      
      Map<String, Object> assetServerResponseMap = AssetUtils.getAssetFromSwift(assetInfo);
      Integer responseCode = (Integer) assetServerResponseMap.get("responseCode");
      if (responseCode == 200 || responseCode == 206 || responseCode == 201) {
        responseModel.setInputStream((InputStream) assetServerResponseMap.get("inputStream"));
      } else if (responseCode == 401) {
        responseModel.setErrorMessage("Asset does not exist in the server for the request with " + assetInstanceId);
        return responseModel;
      }
    }
    catch (Exception e) {
      String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        responseModel.setErrorMessage(message);
      } else {
        responseModel.setErrorMessage("Request couldn't be completed because of invalid fileName/organiztaionCode/catalogCode/endpointCode.");
      }
    }
    
    return responseModel;
  }

  /**
   * Validate assetInstanceId,catalogCode, organizationCode and endpointCode
   * @param assetInstanceId
   * @param catalogCode
   * @param configResponseModel
   * @throws Exception
   */
  private void validateInputParameters(String assetInstanceId, String catalogCode, String organizationCode,
      String endpointCode, IAssetExportAPIResponseModel configResponseModel) throws Exception
  {
    if (assetInstanceId == null || assetInstanceId.isEmpty()) {
      throw new Exception("Asset does not exist in the server for the request with " + assetInstanceId);
    }
    
    StringBuilder errorMessage = new StringBuilder("Request couldn't be completed because of invalid ");
    String separator = ",";
    if (!configResponseModel.getIsOrganizationCodeValid()) {
      errorMessage.append( IAssetInstanceExportRequestModel.ORGANIZATION_CODE + separator);
    }
    IGetPhysicalCatalogIdsModel getPhysicalCatalogIdsModel = getPhysicalCatalogIdsService.execute(null);
    if (!StringUtils.isBlank(catalogCode) && !getPhysicalCatalogIdsModel.getAvailablePhysicalCatalogIds().contains(catalogCode)
        || (Constants.DATA_INTEGRATION_CATALOG_IDS.equals(catalogCode) && StringUtils.isBlank(endpointCode))) {
      errorMessage.append(IAssetInstanceExportRequestModel.CATALOG_CODE + separator);
    }
    if (!configResponseModel.getIsEndpointCodeValid()) {
      errorMessage.append(IAssetInstanceExportRequestModel.ENDPOINT_CODE + separator);
    }
    if (!configResponseModel.getIsLanguageCodeValid()) {
      errorMessage.append(IAssetInstanceExportRequestModel.LANGUAGE_CODE + separator);
    }
    if (errorMessage.indexOf(separator) != -1) {
      throw new Exception(errorMessage.toString().substring(0, errorMessage.lastIndexOf(separator)));
    }
  }

  /**
   * Returns error if endpoint is other than OutBound or catalogCode from
   * request is not same as of endpoint's catalog code, otherwise returns valid catalogCode.
   *
   * @param catalogCode
   * @param configResponseModel
   * @return catalogCode
   * @throws Exception
   */
  private String getValidCatalogCodeFromEndpoint(String catalogCode,
      IAssetExportAPIResponseModel configResponseModel) throws Exception
  {
    IEndpointModel endpoint = configResponseModel.getEndpoint();
    if (endpoint != null) {
      String endpointType = endpoint.getEndpointType();
      if (!Constants.OUTBOUND_ENDPOINT.equals(endpointType)) {
        throw new Exception("Incorrect request with " + IAssetInstanceExportRequestModel.ENDPOINT_CODE);
      }

      List<String> physicalCatalogs = endpoint.getPhysicalCatalogs();
      String catalogCodeFromEndpoint = physicalCatalogs.get(0);

      if (catalogCode.isEmpty()) {
        catalogCode = catalogCodeFromEndpoint;
      }
      else if (!catalogCodeFromEndpoint.equals(catalogCode)) {
        throw new Exception("Incorrect request with " + IAssetInstanceExportRequestModel.CATALOG_CODE + " and "
                + IAssetInstanceExportRequestModel.ENDPOINT_CODE);
      }
    }
    return catalogCode;
  }
  
  /**
   * This method extracts fileName,assetObjectKey,type from entityExtension and returns in new Map.
   * @param entityExtension
   * @return Map<String, String>
   */
  private Map<String, String> getAssetInfoFromEntityExtension(IJSONContent entityExtension) {
    String fileNameWithExtension = entityExtension.getInitField("fileName", "");
    String[] fileNameArray = fileNameWithExtension.split("[.]");
    Map<String, String> assetInfo = new HashMap<String, String>();
    assetInfo.put("type", entityExtension.getInitField("type", ""));
    assetInfo.put("fileName", fileNameWithExtension);
    assetInfo.put("extension", fileNameArray[fileNameArray.length - 1]);
    assetInfo.put("assetObjectKey", entityExtension.getInitField("assetObjectKey", ""));
    
    return assetInfo;
  }
  
}
