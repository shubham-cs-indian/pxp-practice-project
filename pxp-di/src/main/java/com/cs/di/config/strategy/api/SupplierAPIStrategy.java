package com.cs.di.config.strategy.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.supplier.ISupplierKlassSaveModel;
import com.cs.core.config.interactor.usecase.supplier.IGetSupplierWithoutKP;
import com.cs.core.config.interactor.usecase.supplier.ISaveSupplier;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("supplierAPIStrategy")
public class SupplierAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  ISaveSupplier              saveSupplier;
  
  @Autowired
  IGetSupplierWithoutKP      getSupplierWithoutKP;
  
  private static final String CODE   = "code";
  private static final String ENTITY = "entity";
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(code);
    return getSupplierWithoutKP.execute(getEntityModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    // No provision to create supplier class through config
    return null;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    inputData.put(ISupplierKlassSaveModel.ID, inputData.get(CODE));
    Map<String, Object> entity = (Map<String, Object>) getData.get(ENTITY);
    inputData.put(ISupplierKlassSaveModel.PARENT, (Map<String, Object>) entity.get(ISupplierKlassSaveModel.PARENT));
    AbstractKlassSaveModel saveSupplierModel = mapper.convertValue(inputData, AbstractKlassSaveModel.class);
    saveSupplierModel.setIsNature((Boolean) entity.get(ISupplierKlassSaveModel.IS_NATURE));
    saveSupplierModel.setNatureType((String) entity.get(ISupplierKlassSaveModel.NATURE_TYPE));
    saveSupplierModel.setIsStandard((Boolean) entity.get(ISupplierKlassSaveModel.IS_STANDARD));
    saveSupplierModel.setType((String) entity.get(ISupplierKlassSaveModel.TYPE));
    saveSupplierModel.setClassifierIID((Long) entity.get(ISupplierKlassSaveModel.CLASSIFIER_IID));
    saveSupplierModel.setIsAllowedAtTopLevel((Boolean) entity.get(ISupplierKlassSaveModel.IS_ALLOWED_AT_TOP_LEVEL));
    saveSupplierModel.setIsDefaultFolder((Boolean) entity.get(ISupplierKlassSaveModel.IS_DEFAULT_FOLDER));
    saveSupplierModel.setIsDefaultChild((Boolean) entity.get(ISupplierKlassSaveModel.IS_DEFAULT_CHILD));
    saveSupplierModel.setIsAbstract((Boolean) entity.get(ISupplierKlassSaveModel.IS_ABSTRACT));
    if (DiValidationUtil.isBlank(saveSupplierModel.getLabel())) {
      saveSupplierModel.setLabel((String) entity.get(ISupplierKlassSaveModel.LABEL));
    }
    if (saveSupplierModel.getNumberOfVersionsToMaintain() == null) {
      saveSupplierModel.setNumberOfVersionsToMaintain((Long) entity.get(ISupplierKlassSaveModel.NUMBER_OF_VERSIONS_TO_MAINTAIN));
    }
    return saveSupplier.execute((ISupplierKlassSaveModel) saveSupplierModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
