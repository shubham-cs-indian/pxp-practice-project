package com.cs.core.runtime.strategy.utils;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.role.ModifiedRoleInstanceModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.*;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.model.assetinstance.ModifiedAssetAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.*;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ADMUtils {
  
  public static void fillADM(IKlassInstance oldModel, IKlassInstance newModel,
      IKlassInstanceSaveModel klassInstanceADM, List<String> idsToIgnore) throws Exception
  {
    List<IAttributeInstance> attributesFromOld = (List<IAttributeInstance>) oldModel
        .getAttributes();
    List<IAttributeInstance> attributesFromNew = (List<IAttributeInstance>) newModel
        .getAttributes();
    manageAttributeInstances(attributesFromOld, attributesFromNew, klassInstanceADM, idsToIgnore);
    
    // TODO: MUST CREATE ADM FOR Relationships
    /*List<IAssetAttributeInstance> assetsFromOld = (List<IAssetAttributeInstance>) ((IContentInstance)oldModel)
        .getAssets();
    List<IAssetAttributeInstance> assetsFromNew = (List<IAssetAttributeInstance>) ((IContentInstance)newModel)
        .getAssets();*/
    
    // manageAssetInstances(assetsFromOld, assetsFromNew,
    // (IContentInstanceSaveModel)klassInstanceADM, idsToIgnore);
    
    List<ITagInstance> tagsFromOld = (List<ITagInstance>) oldModel.getTags();
    List<ITagInstance> tagsFromNew = (List<ITagInstance>) newModel.getTags();
    manageTagInstances(tagsFromOld, tagsFromNew, klassInstanceADM, idsToIgnore);
  }
  
  public static void manageAttributeInstances(List<IAttributeInstance> attributesFromOld,
      List<IAttributeInstance> attributesFromNew, IKlassInstanceSaveModel klassInstanceADM,
      List<String> idsToIgnore) throws Exception
  {
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = new ArrayList<>();
    List<IContentAttributeInstance> addedAttributes = new ArrayList<>();
    List<String> deletedAttributes = new ArrayList<>();
    
    List<IContentAttributeInstance> foundAttributes = new ArrayList<>();
    
    for (IAttributeInstance attributeFromNew : attributesFromNew) {
      boolean isFound = false;
      String newAttributeId = attributeFromNew.getAttributeId();
      if (idsToIgnore.contains(newAttributeId)) {
        continue;
      }
      for (IAttributeInstance attributeFromOld : attributesFromOld) {
        String newAttributeValue = attributeFromNew.getValue();
        String oldAttributeId = attributeFromOld.getAttributeId();
        String oldAttributeValue = attributeFromOld.getValue();
        if (oldAttributeId.equals(newAttributeId)) {
          isFound = true;
          if (!newAttributeValue.equals(oldAttributeValue) && !newAttributeValue.isEmpty()) {
            IModifiedAttributeInstanceModel modifiedAttributeInstance = new ModifiedAttributeInstanceModel(
                attributeFromOld);
            modifiedAttributeInstance.setValue(newAttributeValue.trim());
            modifiedAttributes.add(modifiedAttributeInstance);
            if (modifiedAttributeInstance.getAttributeId()
                .equals(IStandardConfig.StandardProperty.nameattribute.toString())) {
              klassInstanceADM.setName(attributeFromNew.getValue()
                  .trim());
            }
          }
          foundAttributes.add(attributeFromOld);
          break;
        }
      }
      if (!isFound) {
        attributeFromNew.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
        addedAttributes.add(attributeFromNew);
      }
    }
    List<IAttributeInstance> remainingAttributes = new ArrayList<IAttributeInstance>(
        attributesFromOld);
    remainingAttributes.removeAll(foundAttributes);
    for (IAttributeInstance attributeFromOld : remainingAttributes) {
      String attributeId = attributeFromOld.getId();
      if (!idsToIgnore.contains(attributeId)) {
        deletedAttributes.add(attributeFromOld.getId());
      }
    }
    
    klassInstanceADM.setModifiedAttributes(modifiedAttributes);
    klassInstanceADM.setAddedAttributes(addedAttributes);
    klassInstanceADM.setDeletedAttributes(deletedAttributes);
  }
  
  public static void manageTagInstances(List<ITagInstance> tagsFromOld,
      List<ITagInstance> tagsFromNew, IKlassInstanceSaveModel klassInstanceADM,
      List<String> idsToIgnore) throws Exception
  {
    List<IModifiedContentTagInstanceModel> modifiedTags = new ArrayList<>();
    List<ITagInstance> addedTags = new ArrayList<>();
    List<String> deletedTags = new ArrayList<>();
    
    List<ITagInstance> foundTags = new ArrayList<>();
    
    for (ITagInstance tagFromNew : tagsFromNew) {
      Boolean isTagGroupFound = false;
      String tagIdToModify = tagFromNew.getTagId();
      if (idsToIgnore.contains(tagIdToModify)) {
        continue;
      }
      for (ITagInstance tagFromOld : tagsFromOld) {
        String existingTagId = tagFromOld.getTagId();
        if (tagIdToModify.equals(existingTagId)) {
          IModifiedTagInstanceModel modifiedTagInstance = new ModifiedTagInstanceModel(
              (ITagInstance) tagFromOld);
          List<ITagInstanceValue> modifiedTagInstanceValues = new ArrayList<>();
          List<ITagInstanceValue> addedTagInstanceValues = new ArrayList<>();
          Boolean isTagValuesSet = false;
          for (ITagInstanceValue tagInstanceValue : tagFromNew.getTagValues()) {
            Boolean isModified = false;
            for (ITagInstanceValue existingTagValue : tagFromOld.getTagValues()) {
              String existingTagValueId = existingTagValue.getTagId();
              String tagValueIdToModify = tagInstanceValue.getTagId();
              if (tagValueIdToModify.equals(existingTagValueId)) {
                if (!tagInstanceValue.getRelevance()
                    .equals(existingTagValue.getRelevance())) {
                  modifiedTagInstanceValues.add(existingTagValue);
                  existingTagValue.setRelevance(tagInstanceValue.getRelevance());
                  if (!tagInstanceValue.getRelevance()
                      .equals(0)) {
                    isTagValuesSet = true;
                  }
                }
                isModified = true;
                break;
              }
            }
            if (!isModified) {
              tagInstanceValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
              addedTagInstanceValues.add(tagInstanceValue);
            }
          }
          modifiedTagInstance.setAddedTagValues(addedTagInstanceValues);
          if (isTagValuesSet) {
            modifiedTagInstance.setModifiedTagValues(modifiedTagInstanceValues);
          }
          modifiedTags.add(modifiedTagInstance);
          isTagGroupFound = true;
          foundTags.add(tagFromOld);
          break;
        }
      }
      if (!isTagGroupFound) {
        tagFromNew.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
        addedTags.add(tagFromNew);
        
        for (ITagInstanceValue tagInstanceValueToModify : tagFromNew.getTagValues()) {
          tagInstanceValueToModify.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
        }
      }
    }
    List<ITagInstance> remainingTags = new ArrayList<ITagInstance>(tagsFromOld);
    remainingTags.removeAll(foundTags);
    for (ITagInstance tagFromOld : remainingTags) {
      if (!idsToIgnore.contains(tagFromOld.getTagId())) {
        deletedTags.add(tagFromOld.getId());
      }
    }
    
    klassInstanceADM.setAddedTags(addedTags);
    klassInstanceADM.setModifiedTags(modifiedTags);
    klassInstanceADM.setDeletedTags(deletedTags);
  }
  
  private static void manageRoleInstances(List<IRoleInstance> rolesFromOld,
      List<IRoleInstance> rolesFromNew, IKlassInstanceSaveModel klassInstanceADM) throws Exception
  {
    List<IModifiedRoleInstanceModel> modifiedRoles = new ArrayList<>();
    List<IRoleInstance> addedRoles = new ArrayList<>();
    List<String> deletedRoles = new ArrayList<>();
    
    List<IRoleInstance> foundRoles = new ArrayList<>();
    
    Boolean isRoleFound = false;
    for (IRoleInstance roleFromNew : rolesFromNew) {
      String roleIdToModify = roleFromNew.getRoleId();
      
      for (IRoleInstance roleFromOld : rolesFromOld) {
        String existingRoleId = roleFromOld.getRoleId();
        if (roleIdToModify.equals(existingRoleId)) {
          isRoleFound = true;
          
          List<IRoleCandidate> addedRoleCandidates = new ArrayList<>();
          List<IRoleCandidate> foundCandidates = new ArrayList<>();
          List<String> deletedCandidates = new ArrayList<>();
          
          for (IRoleCandidate candidateFromNew : roleFromNew.getCandidates()) {
            String candidateIdToModify = candidateFromNew.getId();
            Boolean isFound = false;
            for (IRoleCandidate candidateFromOld : roleFromOld.getCandidates()) {
              String existingCandidateId = candidateFromOld.getId();
              if (candidateIdToModify.equals(existingCandidateId)) {
                isFound = true;
                foundCandidates.add(candidateFromOld);
                break;
              }
            }
            if (!isFound) {
              addedRoleCandidates.add(candidateFromNew);
            }
          }
          
          roleFromOld.getCandidates()
              .removeAll(foundCandidates);
          for (IRoleCandidate deletedCandidate : roleFromOld.getCandidates()) {
            deletedCandidates.add(deletedCandidate.getId());
          }
          if (addedRoleCandidates.size() > 0 || deletedCandidates.size() > 0) {
            IModifiedRoleInstanceModel modifiedRoleInstance = new ModifiedRoleInstanceModel(
                (IRoleInstance) roleFromOld);
            modifiedRoleInstance.setAddedCandidates(addedRoleCandidates);
            modifiedRoleInstance.setDeletedCandidates(deletedCandidates);
            
            modifiedRoles.add(modifiedRoleInstance);
          }
          
          foundRoles.add(roleFromOld);
          break;
        }
      }
      
      if (!isRoleFound) {
        roleFromNew.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ROLE.getPrefix()));
        addedRoles.add(roleFromNew);
      }
    }
    
    rolesFromOld.removeAll(foundRoles);
    for (IRoleInstance roleFromOld : rolesFromOld) {
      deletedRoles.add(roleFromOld.getId());
    }
    
    klassInstanceADM.setAddedRoles(addedRoles);
    klassInstanceADM.setModifiedRoles(modifiedRoles);
    klassInstanceADM.setDeletedRoles(deletedRoles);
  }
  
  public static void manageAssetInstances(List<IAssetAttributeInstance> assetsFromOld,
      List<IAssetAttributeInstance> assetsFromNew, IContentInstanceSaveModel klassInstanceADM,
      List<String> idsToIgnore) throws Exception
  {
    List<IModifiedAssetAttributeInstanceModel> modifiedAssets = new ArrayList<>();
    List<IAssetAttributeInstance> addedAssets = new ArrayList<>();
    List<String> deletedAssets = new ArrayList<>();
    
    List<IAssetAttributeInstance> foundAssets = new ArrayList<>();
    
    for (IAssetAttributeInstance assetFromNew : assetsFromNew) {
      boolean isFound = false;
      String newAssetAttributeId = assetFromNew.getAttributeId();
      if (idsToIgnore.contains(newAssetAttributeId)) {
        continue;
      }
      for (IAssetAttributeInstance assetFromOld : assetsFromOld) {
        String newAssetInstanceId = assetFromNew.getAssetInstanceId();
        String newAssetType = assetFromNew.getType();
        String oldAssetAttributeId = assetFromOld.getAttributeId();
        String oldAssetInstanceId = assetFromOld.getAssetInstanceId();
        if (oldAssetAttributeId.equals(newAssetAttributeId)) {
          isFound = true;
          if (!newAssetInstanceId.equals(oldAssetInstanceId) && !newAssetInstanceId.isEmpty()) {
            IModifiedAssetAttributeInstanceModel modifiedAttributeInstance = setModifiedAssetAttributeData(
                assetFromOld);
            modifiedAttributeInstance.setAssetInstanceId(newAssetInstanceId);
            modifiedAttributeInstance.setType(newAssetType);
            modifiedAttributeInstance.setFileName(assetFromNew.getFileName());
            modifiedAssets.add(modifiedAttributeInstance);
          }
          foundAssets.add(assetFromOld);
          break;
        }
      }
      if (!isFound) {
        assetFromNew.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
        addedAssets.add(assetFromNew);
      }
    }
    List<IAssetAttributeInstance> remainingAttributes = new ArrayList<IAssetAttributeInstance>(
        assetsFromOld);
    remainingAttributes.removeAll(foundAssets);
    for (IAssetAttributeInstance attributeFromOld : remainingAttributes) {
      String attributeId = attributeFromOld.getId();
      if (!idsToIgnore.contains(attributeId)) {
        deletedAssets.add(attributeFromOld.getId());
      }
    }
    
    klassInstanceADM.setModifiedAssets(modifiedAssets);
    klassInstanceADM.setAddedAssets(addedAssets);
    klassInstanceADM.setDeletedAssets(deletedAssets);
  }
  
  private static IModifiedAssetAttributeInstanceModel setModifiedAssetAttributeData(
      IAssetAttributeInstance assetFromOld)
  {
    IModifiedAssetAttributeInstanceModel dataToReturn = new ModifiedAssetAttributeInstanceModel();
    dataToReturn.setType(assetFromOld.getType());
    dataToReturn.setIsDefault(assetFromOld.getIsDefault());
    dataToReturn.setAttributeId(assetFromOld.getAttributeId());
    dataToReturn.setId(assetFromOld.getId());
    // dataToReturn.setMappingId(assetFromOld.getMappingId());
    dataToReturn.setBaseType(assetFromOld.getBaseType());
    return dataToReturn;
  }
}
