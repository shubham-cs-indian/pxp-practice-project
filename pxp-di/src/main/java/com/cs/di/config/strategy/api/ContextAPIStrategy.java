package com.cs.di.config.strategy.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.variantcontext.DefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext.View;
import com.cs.core.config.interactor.entity.variantcontext.VariantContext;
import com.cs.core.config.interactor.model.variantcontext.CreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.SaveVariantContextModel;
import com.cs.core.config.interactor.usecase.variantcontext.ICreateVariantContext;
import com.cs.core.config.interactor.usecase.variantcontext.IGetVariantContext;
import com.cs.core.config.interactor.usecase.variantcontext.ISaveVariantContext;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("contextAPIStrategy")
public class ContextAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  IGetVariantContext                getVariantContext;
  
  @Autowired
  ICreateVariantContext             createVariantContext;
  
  @Autowired
  ISaveVariantContext               saveVariantContext;
  
  private static final ObjectMapper objectMapper          = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
      false);
  
  private static final List<String> CONTEXT_VARIANT_TYPES = Arrays.asList("contextualVariant", "gtinVariant");
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    return getVariantContext.execute(new IdParameterModel(code));
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    CreateVariantContextModel contextVariantCreateModel = objectMapper.readValue(configModel.getData(), CreateVariantContextModel.class);
    VariantContext entity = (VariantContext) contextVariantCreateModel.getEntity();
    if (CONTEXT_VARIANT_TYPES.contains(entity.getType())) {
      throw new Exception();
    }
    return createVariantContext.execute(contextVariantCreateModel);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    ISaveVariantContextModel model = objectMapper.readValue(configModel.getData(), SaveVariantContextModel.class);
    // below values can not be updated
    model.setType((String) getData.get(ISaveVariantContextModel.TYPE));
    model.setId((String) getData.get(ISaveVariantContextModel.ID));
    model.setCode((String) getData.get(ISaveVariantContextModel.CODE));
    model.setDefaultView(View.valueOf((String) getData.get(ISaveVariantContextModel.DEFAULT_VIEW)));
    // set default time range
    Map<String, Object> inputTimeRange = (Map<String, Object>) inputData.get(ISaveVariantContextModel.DEFAULT_TIME_RANGE);
    IDefaultTimeRange defaultTimeRange = new DefaultTimeRange();
    if ((boolean) inputData.get(ISaveVariantContextModel.IS_TIME_ENABLED)) {
      Object isCurrentTime = inputTimeRange.get(IDefaultTimeRange.IS_CURRENT_TIME);
      if (isCurrentTime != null && (boolean) isCurrentTime) {
        defaultTimeRange.setIsCurrentTime(true);
      }
      else {
        defaultTimeRange.setIsCurrentTime(false);
        defaultTimeRange.setFrom((Long) inputTimeRange.get(IDefaultTimeRange.FROM));
        defaultTimeRange.setTo((Long) inputTimeRange.get(IDefaultTimeRange.TO));
      }
    }
    model.setDefaultTimeRange(defaultTimeRange);
    return saveVariantContext.execute(model);
  }
  
}
