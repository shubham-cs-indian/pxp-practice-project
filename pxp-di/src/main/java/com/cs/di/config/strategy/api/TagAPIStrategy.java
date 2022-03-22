package com.cs.di.config.strategy.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.attributiontaxonomy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.SaveTagModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.interactor.usecase.tag.ICreateTag;
import com.cs.core.config.interactor.usecase.tag.IGetTag;
import com.cs.core.config.interactor.usecase.tag.ISaveTag;
import com.cs.core.config.strategy.usecase.tag.IGetTagsByTagIdsStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;

@Service
public class TagAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
   
  @Autowired
  protected IGetTag                  getTag;
  
  @Autowired
  protected ICreateTag               createTag;
  
  @Autowired
  protected ISaveTag                 saveTag;
  
  @Autowired
  protected IGetTagsByTagIdsStrategy getTagsByTagIdsStrategy;

    @Override
    public ServiceType getServiceType()
    {
        return null;
    }

    
    protected ITagModel read(String code, IConfigAPIRequestModel configModel) throws Exception {
        IIdsListParameterModel model = new IdsListParameterModel();
        List<String> ids = new ArrayList<String>();
        ids.add(code);
        model.setIds(ids);
        return  ((List<ITagModel>) getTagsByTagIdsStrategy.execute(model).getList()).get(0);
    }

    @Override
    protected IModel executeCreate(Map<String, Object> tagData, IConfigAPIRequestModel configModel) throws Exception
    {      
      if ((boolean) tagData.get(ITag.IS_STANDARD)) {
        throw new Exception("Can not create new Standard Tag");
      }
      ITagModel tagModel = new TagModel();
      tagModel.setId((String) tagData.get(ITag.CODE));
      tagModel.setCode((String) tagData.get(ITag.CODE));
      tagModel.setLabel((String) tagData.get(ITag.LABEL));
      tagModel.setTagType((String) tagData.get(ITag.TAG_TYPE));
      tagModel.setType(CoreConstant.TAG_TYPE);
      tagModel.setParent(getParent(tagData));
      tagModel.setIsMultiselect(true);
      tagModel.setIsVersionable(true);
      return createTag.execute(tagModel);
    }


    private ITreeEntity getParent(Map<String, Object> tagData)
    {
      ITreeEntity parent = new Tag();
      String parentId = (String) tagData.get(IConfigTaxonomyHierarchyInformationModel.PARENT_ID);
      if(parentId == null || parentId.equals("-1"))
      {
        return null;
      }
      parent.setId(parentId);
      return parent;
    }
    
    private ITag getDefaultValue(Map<String, Object> tagData, Map<String, Object> getTag)
    {
      Map<String, Object> defaultValue = new HashMap<String, Object>();
      if (tagData.containsKey(ITag.DEFAULT_VALUE)) {
        defaultValue = (Map<String, Object>) tagData.get(ITag.DEFAULT_VALUE);
      }
      else {
        defaultValue = (Map<String, Object>) getTag.get(ITag.DEFAULT_VALUE);
      }
      ITag tag = new Tag();
      tag.setId((String) defaultValue.get(ITag.ID));
      return tag;
    }

    @Override
    protected IModel executeUpdate(Map<String, Object> tagData, Map<String, Object> getTag, IConfigAPIRequestModel configModel)
        throws Exception
    {      
      ISaveTagModel tagModel = new SaveTagModel();
      tagModel.setId((String) getTag.get(ITag.CODE));
      tagModel.setCode((String) getTag.get(ITag.CODE));
      tagModel.setLabel((String) getUpdateData(tagData, getTag, ITag.LABEL));
      tagModel.setColor((String) getUpdateData(tagData, getTag, ITag.COLOR));
      tagModel.setLinkedMasterTagId((String) getUpdateData(tagData, getTag, ITag.LINKED_MASTER_TAG_ID));
      tagModel.setType((String) getTag.get(ITag.TYPE));
      if (getTag.get(ITag.PARENT) == null) {
        tagModel.setParent(null);
      }
      else {
        tagModel.setParent(setParentInUpdate(getTag));
      }
      String tagType = (String) getTag.get(ITag.TAG_TYPE);
      if (CommonConstants.YES_NEUTRAL_TAG_TYPE_ID.equals(tagType) || CommonConstants.YES_NEUTRAL_NO_TAG_TYPE_ID.equals(tagType)
          || CommonConstants.RULER_TAG_TYPE_ID.equals(tagType) || CommonConstants.RANGE_TAG_TYPE_ID.equals(tagType)) {
        tagModel.setDefaultValue(getDefaultValue(tagData, getTag));
        tagModel.setIsMultiselect((Boolean) getUpdateData(tagData, getTag, ITag.IS_MULTI_SELECT));
        tagModel.setTagType((String) getUpdateData(tagData, getTag, ITag.TAG_TYPE));
      }
      else {
        tagModel.setTagType(tagType);
      }
      tagModel.setDescription((String) getUpdateData(tagData, getTag, ITag.DESCRIPTION));
      tagModel.setTooltip((String) getUpdateData(tagData, getTag, ITag.TOOLTIP));
      tagModel.setAvailability((List<String>) getUpdateData(tagData, getTag, ITag.AVAILABILITY));
      tagModel.setIsDisabled((Boolean) tagData.get(ITag.IS_DISABLED));
      
      tagModel.setIsFilterable((Boolean) getUpdateData(tagData, getTag, ITag.IS_FILTERABLE));
      tagModel.setIsGridEditable((Boolean) getUpdateData(tagData, getTag, ITag.IS_GRID_EDITABLE));
      tagModel.setIcon((String) getUpdateData(tagData, getTag, ITag.ICON));
      tagModel.setIsVersionable((Boolean) getUpdateData(tagData, getTag, ITag.IS_VERSIONABLE));
      tagModel.setIsStandard((Boolean) getTag.get(ITag.IS_STANDARD));
      
      tagModel.setIsMandatory((Boolean) getTag.get(ITag.IS_MANDATORY));
      tagModel.setPlaceholder((String) getTag.get(ITag.PLACEHOLDER));
      tagModel.setChildren((List<? extends ITreeEntity>) getTag.get(ITag.CHILDREN));
      tagModel.setTagValues((List<ITagValue>) getTag.get(ITag.TAG_VALUES));
      tagModel.setTagValuesSequence((List<String>) getTag.get(ITag.TAG_VALUES_SEQUENCE));
      tagModel.setPropertyIID((long) getTag.get(ITag.PROPERTY_IID));
      
      IListModel<ISaveTagModel> dataModel = new ListModel<ISaveTagModel>();
      List<ISaveTagModel> list = new ArrayList<ISaveTagModel>();
      list.add((ISaveTagModel) tagModel);
      dataModel.setList(list);
      return saveTag.execute(dataModel);
    }
    
    Object getUpdateData(Map<String, Object> tagData, Map<String, Object> getTag, String key) {
      if(tagData.containsKey(key)) {
        return tagData.get(key);
      } else {
        return getTag.get(key);
      }
    }


    @Override
    protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
    {
      IIdsListParameterModel model = new IdsListParameterModel();
      List<String> ids = new ArrayList<String>();
      ids.add(code);
      model.setIds(ids);
      return  ((List<ITagModel>) getTagsByTagIdsStrategy.execute(model).getList()).get(0);
  }

  private ITreeEntity setParentInUpdate(Map<String, Object> getTag)
  {
    ITreeEntity parent = new Tag();
    Map<String, Object> parentMap = (Map<String, Object>) getTag.get(ITag.PARENT);
    parent.setId((String) parentMap.get(CommonConstants.ID_PROPERTY));
    return parent;
  }
}
