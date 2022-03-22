package com.cs.core.config.businessapi.tag;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.tag.ICreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.ICreateTagStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateTagService extends AbstractCreateConfigService<ITagModel, ICreateTagResponseModel> implements ICreateTagService {
  
  @Autowired
  ICreateTagStrategy createTagStrategy;
  
  @Override
  public ICreateTagResponseModel executeInternal(ITagModel tag) throws Exception
  {
    if (StringUtils.isEmpty(tag.getId())) {
      String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix());
      tag.setId(id);
    }
    Validations.validateLabel(tag.getLabel());
    String code = createRDBMSConfig(tag);
    tag.setCode(code);
    ICreateTagResponseModel response = createTagStrategy.execute(tag);
    List<ITagModel> tags = response.getTagResponseModel();
    for (ITagModel tagModel : tags) {
      if (tagModel.getTagType() != null && tagModel.getTagType().equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
        syncBooleanTagValue(tagModel);
      }
    }
    
    return response;
  }
  
  private void syncBooleanTagValue(ITagModel tag) throws Exception
  {
    List<ITag> tagChildrens = (List<ITag>) tag.getChildren();
    
    for (ITag childTag : tagChildrens) {
      RDBMSUtils.createTagValue(childTag.getCode(), tag.getId());
    }
  }
  
  public String createRDBMSConfig(ITagModel tag) throws Exception
  {
    
    String code = tag.getCode();
    if (StringUtils.isEmpty(code)) {
      code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix());
    }
    
    tag.setCode(code);
    tag.setId(code);
    
    if (tag.getParent() == null) {
      IPropertyDTO property = RDBMSUtils.createProperty(code, code, IConfigMap.getPropertyType(tag.getTagType()));
      tag.setPropertyIID(property.getPropertyIID());
      return property.getCode();
      
    }
    else {
      ITagValueDTO tagValue = RDBMSUtils.createTagValue(code, tag.getParent().getId());
      tag.setPropertyIID(tagValue.getPropertyIID());
      return tagValue.getCode();
    }
  }
  
}
