package com.cs.dam.runtime.assetinstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.config.strategy.usecase.asset.IGetAllRenditionKlassesWithPermissionStrategy;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.assetinstance.AssetDownloadInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.AssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.assetinstance.BulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IIdLabelCodeDownloadPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListWithUserModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;

@Service
public class GetAssetTechnicalTypeIDsService extends AbstractService<IIdsListParameterModel, IBulkAssetDownloadWithVariantsModel> 
    implements IGetAssetTechnicalTypeIDsService {
  
  private static final String                             CONTENT_LENGTH = "Content-Length";
  private static final String                             CONTAINER      = "container";
  private static final String                             AUTH_TOKEN     = "authToken";
  private static final String                             STORAGE_URL    = "storageUrl";
  private static final String                             ASSET_KEY      = "assetKey";

  @Autowired
  protected IGetAllRenditionKlassesWithPermissionStrategy getAllRenditionKlassesWithPermissionStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                           rdbmsComponentUtils;
  
  @Override
  public IBulkAssetDownloadWithVariantsModel executeInternal(
      IIdsListParameterModel dataModel) throws Exception
  {
    IBulkAssetDownloadWithVariantsModel returnModel = new BulkAssetDownloadWithVariantsModel();
    Set<String> allTypesByIds = getAllTypesByIdsStrategy(dataModel, returnModel);
    IIdsListWithUserModel idsListWithUserModel = new IdsListWithUserModel();
    idsListWithUserModel.setIds(new ArrayList<>(allTypesByIds));
    idsListWithUserModel.setUserId(rdbmsComponentUtils.getUserID());
    IBulkDownloadConfigInformationResponseModel configResponse = getAllRenditionKlassesWithPermissionStrategy.execute(idsListWithUserModel);
    prepareResponseModel(configResponse, returnModel);
    returnModel.setShouldDownloadAssetWithOriginalFilename(configResponse.getShouldDownloadAssetWithOriginalFilename());
    return returnModel;
  }
  
  /***
   * Prepares the return model for master asset and its TIVs
   * 
   * @param configResponse
   * @param returnModel
   * @throws Exception
   */
  private void prepareResponseModel(IBulkDownloadConfigInformationResponseModel configResponse,
      IBulkAssetDownloadWithVariantsModel returnModel) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getEmptyBaseEntityDAO();
    
    for(IAssetDownloadInformationWithTIVModel model : returnModel.getDownloadInformation()) {
      Map<String, List<IAssetDownloadInformationModel>> tivDownloadInformation = new HashMap<>();
      String classifierCode = model.getClassifierCode();
      IIdLabelCodeDownloadPermissionModel masterKlassConfigInfo = configResponse.getMasterAssetKlassInformation().get(classifierCode);
      List<String> tivKlasses = configResponse.getMasterAssetTivKlassMap().get(classifierCode);
      long totalSize = 0; //sum of size of instances having download permission
      long totalContent = 0; //count of instances having download permission
      boolean allAssetsHaveDownloadPermission = true; //false if any of the klass(master or its TIVs) doesn't have download permission
      
      // for each tivKlass, prepare list of tiv instances information
      for(String tivKlassCode : tivKlasses) {
        List<IAssetDownloadInformationModel> tivModel = new ArrayList<>();
        long masterAssetBaseEntityIId = model.getAssetInstanceId();
        List<Long> tivIIds = baseEntityDAO.getIIdsByParentIIdAndClassifierCode(masterAssetBaseEntityIId, tivKlassCode);
        IIdLabelCodeDownloadPermissionModel tivConfigInfo = configResponse.getTivAssetKlassInformation().get(tivKlassCode);
        boolean tivDownloadPermission = tivConfigInfo.getCanDownload();
        
        //for each tiv instance prepare return model
        for(long tivIId : tivIIds){
          IAssetDownloadInformationModel tivInformation = new AssetDownloadInformationModel();
          IBaseEntityDAO tivInstance = rdbmsComponentUtils.getBaseEntityDAO(tivIId);
          IBaseEntityDTO tivBaseEntityDTO = tivInstance.getBaseEntityDTO();
          IJSONContent entityExtension = tivBaseEntityDTO.getEntityExtension();
          String tivFullFileName = entityExtension.getInitField(IAssetInformationModel.FILENAME, "");
          String tivFileName = FilenameUtils.getBaseName(tivFullFileName);
          String tivExtension = FilenameUtils.getExtension(tivFullFileName);
          
          //get size and extension only if download permission exists for the tiv klass
          if(tivDownloadPermission && !tivFullFileName.isEmpty()) {
            String assetObjectKey = entityExtension.getInitField(IAssetInformationModel.ASSET_OBJECT_KEY, "");
            String type = entityExtension.getInitField(IAssetInformationModel.TYPE, "");
            long tivSize = getAssetSizeFromSwift(assetObjectKey, type);
            totalSize += tivSize;
            totalContent++;
            
            tivInformation.setExtension(tivExtension);
            tivInformation.setSize(tivSize);
            tivInformation.setAssetObjectKey(assetObjectKey);
            tivInformation.setContainer(type);
          }
          else if(!tivDownloadPermission) {
            allAssetsHaveDownloadPermission = false;
          }
          
          String baseEntityName = tivBaseEntityDTO.getBaseEntityName();
          tivInformation.setAssetFileName(tivFileName);
          tivInformation.setAssetInstanceId(tivBaseEntityDTO.getBaseEntityIID());
          tivInformation.setAssetInstanceName(baseEntityName);
          tivInformation.setCanDownload(tivDownloadPermission);
          tivInformation.setAssetKlassName(tivConfigInfo.getLabel());
          tivModel.add(tivInformation);
        }
        tivDownloadInformation.put(tivKlassCode, tivModel);
      }
      
      //put size and extension in return model only if download permission exists for the klass
      boolean canDownload = masterKlassConfigInfo.getCanDownload();
      String assetFileName = model.getAssetFileName();
      if(canDownload && !assetFileName.isEmpty()) {
        long size = getAssetSizeFromSwift(model.getAssetObjectKey(), model.getContainer());
        totalSize += size;
        model.setSize(size);
        totalContent++;
      }
      else if(!canDownload) {
        model.setExtension(null);
        allAssetsHaveDownloadPermission = false;
      }

      model.setAssetFileName(assetFileName);
      model.setAssetKlassName(masterKlassConfigInfo.getLabel());
      model.setCanDownload(canDownload);
      model.setTotalSize(totalSize);
      model.setTotalContent(totalContent);
      model.setAllDownloadPermission(allAssetsHaveDownloadPermission);
      model.setTIVDownloadInformation(tivDownloadInformation);
    }
  }

  /***
   * Returns the list of unique classifier codes and prepares the return model
   * for master assets.
   * 
   * @param dataModel list of master asset IIds.
   * @param returnModel model in which master asset information needs to be filled.
   * @return
   * @throws Exception
   */
  private Set<String> getAllTypesByIdsStrategy(IIdsListParameterModel dataModel, 
      IBulkAssetDownloadWithVariantsModel returnModel) throws Exception
  {
    Set<String> assetClassifiers = new HashSet<>();
    List<String> ids = dataModel.getIds();
    
    for (String id : ids) {
      IAssetDownloadInformationWithTIVModel model = new AssetDownloadInformationWithTIVModel();
      IBaseEntityDAO assetInstance = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(id));
      IBaseEntityDTO baseEntityDTO = assetInstance.getBaseEntityDTO();
      IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
      String baseEntityName = baseEntityDTO.getBaseEntityName();
      String classifierCode = baseEntityDTO.getNatureClassifier().getClassifierCode();
      
      assetClassifiers.add(classifierCode);

      //Prepare master asset model
      String assetObjectKey = entityExtension.getInitField(IAssetInformationModel.ASSET_OBJECT_KEY, "");
      String type = entityExtension.getInitField(IAssetInformationModel.TYPE, "");
      String fullFileName = entityExtension.getInitField(IAssetInformationModel.FILENAME, "");
      String extension = FilenameUtils.getExtension(fullFileName);
      String fileName = FilenameUtils.getBaseName(fullFileName);

      model.setAssetObjectKey(assetObjectKey);
      model.setContainer(type);
      model.setAssetInstanceId(baseEntityDTO.getBaseEntityIID());
      model.setAssetInstanceName(baseEntityName);
      model.setAssetFileName(fileName);
      model.setExtension(extension);
      model.setThumbKey(entityExtension.getInitField(IAssetInformationModel.THUMB_KEY, ""));
      model.setClassifierCode(classifierCode);
      returnModel.getDownloadInformation().add(model);
    }
    return assetClassifiers;
  }

  /***
   * Returns the size of asset in bytes from swift server using "Content-Length" header information.
   * Returns 0 if any exception occurs while fetching the size.
   * 
   * @param assetObjectKey key of asset for which size needs to be fetched
   * @param container swift container
   * @return
   */
  private long getAssetSizeFromSwift(String assetObjectKey, String container)
  {
    try {
      CSDAMServer instanceCSDAMServer = CSDAMServer.instance();
      IJSONContent authenticateSwiftServer = instanceCSDAMServer.getServerInformation();
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put(ASSET_KEY, assetObjectKey);
      requestMap.put(CONTAINER, container);
      requestMap.put(STORAGE_URL, authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
      requestMap.put(AUTH_TOKEN, authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
      Map<String, List<String>> headerInformation = instanceCSDAMServer.getHeaderInformationForAsset(requestMap);
      List<String> contentLength = headerInformation.get(CONTENT_LENGTH);
      return Long.parseLong(contentLength.get(0));
    }
    catch (IOException | PluginException | CSInitializationException e) {
      return 0;
    }
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
