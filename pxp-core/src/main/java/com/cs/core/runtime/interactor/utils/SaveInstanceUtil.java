package com.cs.core.runtime.interactor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class SaveInstanceUtil {
  
  public static void addInModifiedTags(Map<String, List<String>> tagsToModify,
      List<IModifiedContentTagInstanceModel> modifiedTags, IContentTagInstance iContentTagInstance,
      String tagId) throws RDBMSException
  {
    List<String> childTagToModifyIds = tagsToModify.get(tagId);
    IModifiedContentTagInstanceModel modifiedTagInstanceModel = new ModifiedTagInstanceModel();
    
    List<ITagInstanceValue> addedTagValues = new ArrayList<>();
    List<String> deletedTagValues = new ArrayList<String>();
    
    for (ITagInstanceValue iTagInstanceValue : iContentTagInstance.getTagValues()) {
      deletedTagValues.add(iTagInstanceValue.getId());
    }
    for (String childTagToModifyId : childTagToModifyIds) {
      addInAddedTagValues(childTagToModifyId, addedTagValues);
    }
    
    fillModifiedTagInstanceModel(iContentTagInstance, modifiedTagInstanceModel, addedTagValues,
        deletedTagValues);
    modifiedTags.add(modifiedTagInstanceModel);
  }
  
  private static void addInAddedTagValues(String tagId, List<ITagInstanceValue> addedTagValues) throws RDBMSException
  {
    ITagInstanceValue valueTag = new TagInstanceValue();
    valueTag.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    valueTag.setRelevance(100);
    valueTag.setTagId(tagId);
    addedTagValues.add(valueTag);
  }
  
  private static void fillModifiedTagInstanceModel(IContentTagInstance iContentTagInstance,
      IModifiedContentTagInstanceModel modifiedTagInstanceModel,
      List<ITagInstanceValue> addedTagValues, List<String> deletedTagValues)
  {
    modifiedTagInstanceModel.setId(iContentTagInstance.getId());
    modifiedTagInstanceModel.setTagId(iContentTagInstance.getTagId());
    modifiedTagInstanceModel.setBaseType(iContentTagInstance.getBaseType());
    modifiedTagInstanceModel.setKlassInstanceId(iContentTagInstance.getKlassInstanceId());
    modifiedTagInstanceModel.setAddedTagValues(addedTagValues);
    modifiedTagInstanceModel.setDeletedTagValues(deletedTagValues);
  }
}
