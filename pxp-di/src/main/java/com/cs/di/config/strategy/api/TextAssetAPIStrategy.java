package com.cs.di.config.strategy.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetKlassSaveModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.interactor.usecase.textasset.ICreateTextAsset;
import com.cs.core.config.interactor.usecase.textasset.IGetTextAsset;
import com.cs.core.config.interactor.usecase.textasset.ISaveTextAsset;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("textAssetAPIStrategy")
public class TextAssetAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  private static final String       EMBEDDED     = "embedded";
  private static final String       CODE         = "code";
  private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Autowired
  ICreateTextAsset                  createTextAsset;
  
  @Autowired
  IGetTextAsset                     getTextAsset;
  
  @Autowired
  ISaveTextAsset                    saveTextAsset;
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    return getTextAsset.execute(new IdParameterModel(code));
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    AbstractKlassModel createTextAssetModel = objectMapper.readValue(configModel.getData(), AbstractKlassModel.class);
    // set only one value at a time to true for IsAbstract and IsDefaultChild
    if (createTextAssetModel.getIsAbstract() && createTextAssetModel.getIsDefaultChild()) {
      createTextAssetModel.setIsAbstract(false);
    }
    // restrict setting IsDefaultChild value to false for embedded text assets
    if (createTextAssetModel.getNatureType().equals(EMBEDDED)) {
      createTextAssetModel.setIsDefaultChild(false);
      createTextAssetModel.setIsAbstract(false);
      
    }
    // set NumberOfVersionsToMaintain = 0 and NaturType = "" for non nature text
    // assets
    if (!createTextAssetModel.getIsNature()) {
      createTextAssetModel.setNatureType("");
      createTextAssetModel.setNumberOfVersionsToMaintain(0L);
    }

    return createTextAsset.execute((ITextAssetModel) createTextAssetModel);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    inputData.put(ITextAssetKlassSaveModel.ID, inputData.get(CODE));
    inputData.put(ITextAssetKlassSaveModel.PARENT, (Map<String, Object>) getData.get(ITextAssetKlassSaveModel.PARENT));
    AbstractKlassSaveModel model = objectMapper.readValue(configModel.getData(), AbstractKlassSaveModel.class);
    model.setId((String) getData.get(ITextAssetKlassSaveModel.ID));
    model.setCode((String) getData.get(IConfigModel.CODE));
    model.setType((String) getData.get(ITextAssetKlassSaveModel.TYPE));
    model.setClassifierIID((Long) getData.get(ITextAssetKlassSaveModel.CLASSIFIER_IID));
    model.setIsAllowedAtTopLevel((Boolean) getData.get(ITextAssetKlassSaveModel.IS_ALLOWED_AT_TOP_LEVEL));
    model.setIsDefaultFolder((Boolean) getData.get(ITextAssetKlassSaveModel.IS_DEFAULT_FOLDER));
    model.setIsNature((Boolean) getData.get(ITextAssetKlassSaveModel.IS_NATURE));
    model.setIsStandard((Boolean) getData.get(ITextAssetKlassSaveModel.IS_STANDARD));
    model.setNatureType((String) getData.get(ITextAssetKlassSaveModel.NATURE_TYPE));
    model.setContextID((String) getData.get(ITextAssetKlassSaveModel.CONTEXT_ID));
    model.setContextIID((Long) getData.get(ITextAssetKlassSaveModel.CONTEXT_INTERNAL_ID));
    // restrict setting IsDefaultChild value to true for embedded text assets
    if (model.getNatureType().equals(EMBEDDED)) {
      model.setIsDefaultChild(false);
      model.setIsAbstract(false);
    }
    // restrict setting IsAbstract& IsDefaultChild to true.
    // only one value can be true
    if (model.getIsAbstract() && model.getIsDefaultChild()) {
      model.setIsAbstract(false);
    }
    // set NumberOfVersionsToMaintain = 0 for non nature textAssets
    if (!model.getIsNature()) {
      model.setNumberOfVersionsToMaintain(0L);
    }
    return saveTextAsset.execute((ITextAssetKlassSaveModel) model);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
