package com.cs.core.config.iconlibrary;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.config.interactor.model.asset.IIconResponseModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.IconModel;
import com.cs.core.config.interactor.model.iconlibrary.ISaveOrReplaceIconRequestModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.assetserver.UnknownAssetUploadException;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.dam.config.strategy.usecase.iconlibrary.ISaveOrReplaceIconStrategy;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;

/**
 * This is service class for updating icon information.
 * 
 * @author pranav.huchche
 */

@Service
public class SaveOrReplaceIconService extends AbstractSaveConfigService<ISaveOrReplaceIconRequestModel, IIconResponseModel>
    implements ISaveOrReplaceIconService {
  
  @Autowired
  protected IFetchAssetConfigurationDetails fetchAssetConfigurationDetails;
  
  @Autowired
  protected ISaveOrReplaceIconStrategy      saveOrReplaceIconStrategy;
  
  @Override
  protected IIconResponseModel executeInternal(ISaveOrReplaceIconRequestModel model) throws Exception
  {
    // Fetch data from request model
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = model.getMultiPartFileInfoList();
    String iconCode = model.getIconCode();
    String iconName = model.getIconName();
    List<IAssetFileModel> fileModelList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    IAssetFileModel fileModel = null;
    IAssetKeysModel assetKeyModel = null;
    // Fetch config details for asset type icon
    IIdParameterModel idParameterModel = new IdParameterModel();
    idParameterModel.setType(DAMConstants.ASSET_KLASS);
    IAssetConfigurationDetailsResponseModel assetModel = fetchAssetConfigurationDetails.execute(idParameterModel);
    
    if (assetModel.getNatureType() == null) {
      assetModel.setNatureType(VertexLabelConstants.ENTITY_TYPE_ICON);
    }
    
    // Get file model using AssetUtils
    if (!multiPartFileInfoModelList.isEmpty()) {
      IMultiPartFileInfoModel multiPartFileInfoModel = multiPartFileInfoModelList.get(0);
      IAssetFileModel assetFileModel = AssetUtils.getFileModel(multiPartFileInfoModel, DAMConstants.MODE_CONFIG, fileModelList, assetModel);
      assetFileModel.setKey(multiPartFileInfoModel.getKey());
      if (iconName != null) {
        assetFileModel.setName(iconName);
      }
      fileModelList.add(assetFileModel);
    }
    
    // Upload icon asset to swift server
    if (!fileModelList.isEmpty()) {
      fileModel = fileModelList.get(0);
      String fileNameWithExtension = fileModel.getName() + "." + fileModel.getExtension();
      try {
        assetKeyModel = AssetUtils.handleFile(fileModel);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, fileNameWithExtension);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new UnknownAssetUploadException(ex), null, fileNameWithExtension);
      }
      finally {
        AssetUtils.deleteFileAndDirectory(fileModel.getPath());
      }
    }
    
    // Prepare icon model
    IIconModel iconModel = new IconModel();
    iconModel.setCode(iconCode);
    iconModel.setLabel(iconName);
    if (assetKeyModel != null) {
      iconModel.setIconKey(assetKeyModel.getImageKey());
    }
    else {
      iconModel.setIconKey(StringUtils.EMPTY);
    }
    // Call Orient strategy to update icon node
    return saveOrReplaceIconStrategy.execute(iconModel);
  }
  
}
