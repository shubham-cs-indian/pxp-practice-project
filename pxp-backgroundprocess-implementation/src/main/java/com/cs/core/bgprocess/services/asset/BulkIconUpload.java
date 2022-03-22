package com.cs.core.bgprocess.services.asset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.simple.JSONObject;

import com.cs.constants.DAMConstants;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.dto.BulkUploadIconsDTO;
import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IBulkUploadIconsDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.data.Text;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;

/**
 * THis is BGP task for bulk icon upload
 * @author pranav.huchche
 *
 */
public class BulkIconUpload extends AbstractBaseEntityProcessing {
  
  private IBulkUploadIconsDTO                iconUploadDTO                = new BulkUploadIconsDTO();
  private final HashMap<Long, IAssetFileDTO> sourceIconsMap               = new HashMap<>();
  
  private static final String                ORIENT_PLUGIN_FOT_SAVE_ICONS = "BulkCreateAndSaveIcons";
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    // Create fake base entity iids in order to benefit from
    // AbstractBaseEntityProcessing
    BGProcessDTO preJobData = (BGProcessDTO) initialProcessData;
    iconUploadDTO.fromJSON(preJobData.getEntryData().toString());
    IntStream.range(0, iconUploadDTO.getAssets().size())
        .forEach(iconIndex -> {
          sourceIconsMap.put(Long.valueOf(iconIndex + 1), iconUploadDTO.getAssets().get(iconIndex));
        });
    
    // inject the fetched base entity iids into the entry data before calling
    // the parent initialization
    preJobData.getEntryData().setLongArrayField(BaseEntityPlanDTO.BASE_ENTITY_IIDS, sourceIconsMap.keySet());
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs)
      throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    // Fill icons to upload
    Collection<IAssetFileDTO> iconsToUpload = new ArrayList<>();
    batchIIDs.forEach(batchIID -> {
      iconsToUpload.add(sourceIconsMap.get(batchIID));
    });
    List<Map<String, Object>> iconsList = new ArrayList<Map<String,Object>>();
    
    try {
      for (IAssetFileDTO icon : iconsToUpload) {
        String iconCode = icon.getCode();
        String iconName = icon.getName();
        Map<String, Object> uploadedIconAsset = null;
        try {
          uploadedIconAsset = createFileModelAndUploadAsset(icon);
        }
        catch (Exception e) {
          jobData.getLog().error("ICON UPLOAD ERROR" + e);
        }
        finally {
          AssetUtils.deleteFileAndDirectory(icon.getFilePath());
        }
        
        // Prepare data for icon entity to store in Orient
        if(uploadedIconAsset != null) {
          Map<String, Object> iconDataMap = new HashMap<>();
          String fileName = (String) uploadedIconAsset.get(DAMConstants.FILE_NAME);
          if (fileName.indexOf(".") > 0) 
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
          if(iconName == null || iconName.isBlank()) {
            iconName = fileName;
          }
          iconDataMap.put(IIconModel.LABEL, iconName);
          iconDataMap.put(IIconModel.CODE, iconCode);
          iconDataMap.put(IIconModel.ICON_KEY, uploadedIconAsset.get(DAMConstants.ASSET_OBJECT_KEY));
          iconsList.add(iconDataMap);
        }
      }
      
      if (!iconsList.isEmpty()) {
        // save in orintDB
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("icons", iconsList);
        JSONObject savedIcons = CSConfigServer.instance()
            .request(requestMap, ORIENT_PLUGIN_FOT_SAVE_ICONS, iconUploadDTO.getLocaleId());
        Map<String, Object> savedIconsResponse = ObjectMapperUtil.convertValue(savedIcons, HashMap.class);
        
        jobData.getLog().info("icons stored in OrientDB" + savedIconsResponse.get("ids"));
      }
    }
    catch (Exception e) {
      jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
      throw new RDBMSException(600, "ICON UPLOAD ERROR", e);
    }
    jobData.getLog()
        .info("%s Icon(s) uploaded with success: %s", iconsToUpload.size(),
            Text.join(",", iconsToUpload.stream()
                .map(iconFile -> iconFile.getFilePath())
                .collect(Collectors.toList())));
  }
  
  /**
   * Upload the asset file of icon into the Swift server
   * 
   * @param asset
   * @param sourcePathVsHash
   * @param storagePath
   * @param authTokenUrl
   * @return asset upload information
   * @throws Exception
   */
  private Map<String, Object> createFileModelAndUploadAsset(IAssetFileDTO asset) throws Exception
  {
    String fileSourcePath = asset.getFilePath();
    Map<String, Object> fileModelMap = new HashMap<>();
    fileModelMap.put(DAMConstants.FILE_MODEL_PATH, fileSourcePath);
    fileModelMap.put(DAMConstants.FILE_MODEL_IS_EXTRACTED, asset.getIsExtracted());
    fileModelMap.put(DAMConstants.FILE_MODEL_EXT_TYPE, asset.getExtensionType());
    fileModelMap.put(DAMConstants.FILE_MODEL_KLASS_ID, asset.getKlassID());
    fileModelMap.put(DAMConstants.FILE_MODEL_EXT_CONFIG, asset.getExtensionConfiguration());
    fileModelMap.put(DAMConstants.FILE_MODEL_PHYSICAL_CATALOG_ID, null);
    fileModelMap.put(DAMConstants.FILE_MODEL_ORGANIZATION_ID, iconUploadDTO.getOrganizationCode());
    fileModelMap.put(DAMConstants.FILE_MODEL_ENDPOINT_ID, null);
    fileModelMap.put(DAMConstants.FILE_MODEL_CONTAINER, DAMConstants.SWIFT_CONTAINER_ICONS);
//    fileModelMap.put(DAMConstants.ASSET_OBJECT_KEY, asset.getCode());
    
//    return AssetUtils.handleIconUpload(fileModelMap);
    return AssetUtils.handleFile(fileModelMap);
  }
}
