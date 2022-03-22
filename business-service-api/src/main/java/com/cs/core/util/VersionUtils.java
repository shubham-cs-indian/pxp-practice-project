package com.cs.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.revision.dto.RevisionTimelineBuilder;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionSummaryModel;
import com.cs.core.runtime.interactor.model.version.KlassInstanceVersionModel;
import com.cs.core.runtime.interactor.model.version.KlassInstanceVersionSummaryModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.definition.ICSEObject;

@Component
public class VersionUtils {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  public List<IKlassInstanceVersionModel> getObjectRevisions(long baseEntityIID,
      int versionNumber, int from, Boolean isArchived) throws Exception
  {
    IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();
    List<IKlassInstanceVersionModel> versions = new ArrayList<IKlassInstanceVersionModel>();
    IKlassInstanceVersionModel klassInstanceVersionModel = new KlassInstanceVersionModel();

    if(!isArchived) {
      IObjectRevisionDTO latestObjectRevision = revisionDAO.getLastObjectRevision(baseEntityIID);
      
      klassInstanceVersionModel.setCreatedBy(latestObjectRevision.getUserName());
      klassInstanceVersionModel.setLastModified(latestObjectRevision.getWhen());
      klassInstanceVersionModel.setLastModifiedBy(latestObjectRevision.getUserName());
      klassInstanceVersionModel.setVersionNumber(-1);
      
      versions.add(klassInstanceVersionModel);
    }
    
    List<IObjectRevisionDTO> objectRevisions = revisionDAO.getObjectRevisions(baseEntityIID, isArchived);
    for (IObjectRevisionDTO revision : objectRevisions) {
      List<IObjectTrackingDTO> objectTrackings = revisionDAO.getAllObjectTrackingsOfTransaction(revision.getTrackIID());
      klassInstanceVersionModel = prepareKlassInstanceVersionModelFromObjectRevision(revision, objectTrackings);
      versions.add(klassInstanceVersionModel);
    }
    return versions;
  }
  
  private IKlassInstanceVersionModel prepareKlassInstanceVersionModelFromObjectRevision(
      IObjectRevisionDTO revision, List<IObjectTrackingDTO> objectTrackings)
      throws CSFormatException
  {
    IKlassInstanceVersionModel klassInstanceVersionModel = new KlassInstanceVersionModel();
    klassInstanceVersionModel.setCreatedBy(revision.getUserName());
    klassInstanceVersionModel.setLastModified(revision.getWhen());
    klassInstanceVersionModel.setLastModifiedBy(revision.getUserName());
    Map<ChangeCategory, ICSEList> timelines = revision.getTimelines();
    klassInstanceVersionModel.setVersionNumber(revision.getRevisionNo());
    
    RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
    for (IObjectTrackingDTO change : objectTrackings) {
      rtBuilder.addTrackingTimeline((ObjectTrackingDTO) change);
    }
    IKlassInstanceVersionSummaryModel summary = getSummaryInformation(timelines, rtBuilder);
    klassInstanceVersionModel.setSummary(summary);
    return klassInstanceVersionModel;
  }
  
  private IKlassInstanceVersionSummaryModel getSummaryInformation(
      Map<ChangeCategory, ICSEList> timelines, RevisionTimelineBuilder trackingTimelines)
  {
    IKlassInstanceVersionSummaryModel summary = new KlassInstanceVersionSummaryModel();
    for (ChangeCategory category : timelines.keySet()) {
      ICSEList elements = timelines.get(category);
      List<ICSEElement> subElements = elements.getSubElements();
      for (ICSEElement subElement : subElements) {
        String type = subElement.getSpecification(Keyword.$type);
        switch (category) {
          case AddedClassifier:
            if (ClassifierType.CLASS.toString().equals(type))
              summary.setKlassAdded(summary.getKlassAdded() + 1);
            else
              summary.setTaxonomyAdded(summary.getTaxonomyAdded() + 1);
            break;
          
          case RemovedClassifier:
            if (ClassifierType.CLASS.toString().equals(type))
              summary.setKlassRemoved(summary.getKlassRemoved() + 1);
            else
              summary.setTaxonomyRemoved(summary.getTaxonomyRemoved() + 1);
            break;
          
          case CreatedRecord:
          case UpdatedRecord:
            if(trackingTimelines.getDelta().get(category) != null && trackingTimelines.getDelta().get(category).getSubElements().contains(subElement)) {
              trackingTimelines.getDelta().get(category).getSubElements().remove(subElement);
              SuperType superType = PropertyType.valueOf(type).getSuperType();
              if (SuperType.ATTRIBUTE.equals(superType)) {
                String localeID = subElement.getSpecification(Keyword.$locale);
                
                if (localeID.isEmpty()) {
                  summary.setAttributeChanged(summary.getAttributeChanged() + 1);
                }
                else {
                  Map<String, Integer> dependentAttributeIdsCountMap = summary.getDependentAttributeIdsCountMap();
                  Integer integer = dependentAttributeIdsCountMap.get(localeID);
                  if (integer == null) {
                    dependentAttributeIdsCountMap.put(localeID, 1);
                  }
                  else {
                    dependentAttributeIdsCountMap.put(localeID,integer++);
                  }
                }
              }
              else if (SuperType.TAGS.equals(superType)) {
                String tagCode = ((ICSEObject) subElement).getCode();
                if (Constants.LIFE_SATUS_TAG.equals(tagCode)) {
                  summary.setLifeCycleStatusChanged(summary.getLifeCycleStatusChanged() + 1);
                }
                if (Constants.LISTING_STATUS_TAG.equals(tagCode)) {
                  summary.setListStatusChanged(summary.getListStatusChanged() + 1);
                }
                summary.setTagChanged(summary.getTagChanged() + 1);
              }
            }
            break;
            
          case AddedRelation:
          case CreatedRelation:
          case RemovedRelation:
            if (type.equals(PropertyType.RELATIONSHIP.toString()))
              summary.setRelationshipChanged(summary.getRelationshipChanged() + 1);
            else
              summary.setNatureRelationshipChanged(summary.getNatureRelationshipChanged() + 1);
              
            break;
          
          case NewDefaultImageIID:
            summary.setIsDefaultAssetInstanceIdChanged(true);
            break;
            
          case CreatedTranslation:
            summary.setLanguageAdded(summary.getLanguageAdded() + 1);
            break;
            
          case DeletedTranslation:
            summary.setLanguageRemoved(summary.getLanguageRemoved() + 1);
            break;
          
          default:
            break;
        }
      }
    }
    return summary;
  }
  
  
}
