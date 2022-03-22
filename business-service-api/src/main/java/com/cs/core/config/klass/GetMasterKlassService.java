package com.cs.core.config.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassPermission;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionPermission;
import com.cs.core.config.interactor.entity.propertycollection.Section;
import com.cs.core.config.interactor.entity.role.IRolePermission;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.config.interactor.model.configdetails.GetMasterKlassModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetMasterKlassModel;
import com.cs.core.config.klass.IGetMasterKlassService;
import com.cs.core.config.strategy.usecase.attribute.IGetAttributesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithReferencedKlassesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesStrategy;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;

@Service
public class GetMasterKlassService
    extends AbstractGetConfigService<IIdParameterModel, IGetMasterKlassModel>
    implements IGetMasterKlassService {
  
  @Autowired
  IGetKlassesStrategy                    neo4jGetKlassesByIdsStrategy;
  
  @Autowired
  IGetKlassWithReferencedKlassesStrategy neo4jGetKlassWithReferencedKlassesStrategy;
  
  @Autowired
  IGetAttributesStrategy                 neo4jGetAttributesByIdsStrategy;
  
  @Autowired
  ISessionContext                        context;
  
  @Autowired
  KlassInstanceUtils                     klassInstanceUtils;
  
  @Override
  public IGetMasterKlassModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    IGetMasterKlassModel returnModel = new GetMasterKlassModel();
    List<IKlass> referencedKlasses = new ArrayList<IKlass>();
    
    // get Klass
    IGetKlassModel masterKlassModel = neo4jGetKlassWithReferencedKlassesStrategy.execute(idModel);
    List<IProjectKlass> fetchedKlasses = (List<IProjectKlass>) masterKlassModel
        .getReferencedKlasses();
    Map<String, ? extends IStructure> referencedStructures = masterKlassModel
        .getReferencedStructures();
    
    IProjectKlass iProjectKlassModel = (IProjectKlass) masterKlassModel.getKlass();
    manageKlassPermissionAndViewSetting(iProjectKlassModel, returnModel, referencedStructures);
    
    for (IProjectKlass iKlassModel : fetchedKlasses) {
      manageSectionsOfKlassForOwner(iKlassModel);
      referencedKlasses.add(iKlassModel);
    }
    
    returnModel.setKlass(iProjectKlassModel);
    returnModel.setReferencedKlasses(referencedKlasses);
    returnModel.setReferencedStructures(referencedStructures);
    
    return returnModel;
  }
  
  private void manageKlassPermissionAndViewSetting(IProjectKlass iKlassModel,
      IGetMasterKlassModel returnModel, Map<String, ? extends IStructure> referencedStructures)
      throws Exception
  {
  }
  
  private void manageSectionsOfKlassForOwner(IKlass klassModel)
  {
    List<ISection> sections = null;
    
    sections = getSectionsForSingleRoleUser(klassModel, SystemLevelIds.OWNER_ROLE);
    klassModel.setSections(sections);
  }
  
  private List<String> getRoleIdsExistInKlassPermission(List<String> roleIds, IKlass klassModel)
  {
    List<String> roleIdsClone = new ArrayList<String>();
    roleIdsClone.addAll(roleIds);
    for (String roleId : roleIds) {
      IKlassPermission klassPermission = klassModel.getPermission();
      Map<String, IRolePermission> rolePermissionMap = klassPermission.getRolePermission();
      IRolePermission rolePermission = rolePermissionMap.get(roleId);
      // case: when role is deleted from klass
      if (rolePermission == null) {
        roleIdsClone.remove(roleId);
      }
    }
    return roleIdsClone;
  }
  
  private List<String> getRoleIdsForUserFromKlassInstances(String userId,
      List<? extends IRoleInstance> roleInstances)
  {
    List<String> roleIds = new ArrayList<String>();
    for (IRoleInstance roleInstance : roleInstances) {
      List<? extends IRoleCandidate> roleCandidates = roleInstance.getCandidates();
      Boolean isUserExist = checkUserExistInARole(userId, roleCandidates);
      if (isUserExist) {
        roleIds.add(roleInstance.getRoleId());
      }
    }
    
    return roleIds;
  }
  
  private List<ISection> getSectionsForSingleRoleUser(IKlass klassModel, String roleId)
  {
    List<ISection> sections = new ArrayList<ISection>();
    sections.addAll(klassModel.getSections());
    
    IKlassPermission klassPermission = klassModel.getPermission();
    Map<String, IRolePermission> rolePermissionMap = klassPermission.getRolePermission();
    IRolePermission rolePermission = rolePermissionMap.get(roleId);
    Map<String, ISectionPermission> sectionPermissionMap = rolePermission.getSectionPermission();
    for (Map.Entry<String, ISectionPermission> sectionPermissionEntry : sectionPermissionMap
        .entrySet()) {
      String sectionId = sectionPermissionEntry.getKey();
      ISectionPermission sectionPermission = sectionPermissionEntry.getValue();
      manageSectionPermission(sections, sectionId, sectionPermission);
    }
    
    return sections;
  }
  
  private void manageSectionPermission(List<ISection> sections, String sectionId,
      ISectionPermission sectionPermission)
  {
    ISection tempSection = new Section();
    tempSection.setId(sectionId);
    int sectionIndex = sections.indexOf(tempSection);
    if (sectionPermission.getIsHidden()) {
      sections.remove(sectionIndex);
    }
    else {
      ISection section = sections.get(sectionIndex);
      section.setIsCollapsed(sectionPermission.getIsCollapsed());
      List<String> disabledElements = sectionPermission.getDisabledElements();
      for (ISectionElement sectionElement : section.getElements()) {
        if (disabledElements.contains(sectionElement.getId())) {
          sectionElement.setIsDisabled(true);
        }
      }
    }
  }
  
  private List<ISection> getSectionsForMultiRoleUser(IKlass klassModel, List<String> roleIds)
  {
    List<ISection> filteredSections = new ArrayList<ISection>();
    filteredSections.addAll(klassModel.getSections());
    IKlassPermission klassPermission = klassModel.getPermission();
    
    List<ISection> sectionToBeRemoved = new ArrayList<ISection>();
    for (int iSectionCount = 0; iSectionCount < klassModel.getSections()
        .size(); iSectionCount++) {
      ISection sectionFromKlass = klassModel.getSections()
          .get(iSectionCount);
      Map<String, Object> sectionPermissionMap = getSectionPermission(klassPermission,
          sectionFromKlass.getId(), roleIds);
      ISection section = filteredSections.get(iSectionCount);
      if ((Boolean) sectionPermissionMap.get("isHidden")) {
        sectionToBeRemoved.add(section);
        // filteredSections.remove(section);
      }
      else {
        section.setIsCollapsed((Boolean) sectionPermissionMap.get("isCollapsed"));
        List<String> disabledElements = (List<String>) sectionPermissionMap.get("disabledElements");
        for (ISectionElement sectionElement : section.getElements()) {
          // set sections elements isDesabled true if it is there in
          // disabledElements
          if (disabledElements.contains(sectionElement.getId())) {
            sectionElement.setIsDisabled(true);
          }
        }
      }
    }
    
    filteredSections.removeAll(sectionToBeRemoved);
    
    return filteredSections;
  }
  
  private Map<String, Object> getSectionPermission(IKlassPermission klassPermission,
      String sectionId, List<String> roleIds)
  {
    Map<String, Object> sectionPermissionMap = new HashMap<String, Object>();
    sectionPermissionMap.put("isHidden", true);
    sectionPermissionMap.put("isCollapsed", true);
    for (Map.Entry<String, IRolePermission> entry : klassPermission.getRolePermission()
        .entrySet()) {
      String roleId = entry.getKey();
      IRolePermission rolePermission = entry.getValue();
      if (!roleIds.contains(roleId)) {
        continue;
      }
      
      ISectionPermission sectionPermission = rolePermission.getSectionPermission()
          .get(sectionId);
      Boolean isHidden = sectionPermission.getIsHidden();
      if (!isHidden) {
        sectionPermissionMap.put("isHidden", false);
        if (!sectionPermission.getIsCollapsed()) {
          sectionPermissionMap.put("isCollapsed", false);
        }
        List<String> disabledElements = (List<String>) sectionPermissionMap.get("disabledElements");
        if (disabledElements != null) {
          disabledElements.retainAll(sectionPermission.getDisabledElements());
        }
        else {
          sectionPermissionMap.put("disabledElements", sectionPermission.getDisabledElements());
        }
      }
    }
    return sectionPermissionMap;
  }
  
  private List<String> getIntersectedSectionIds(List<String> roleIds,
      IKlassPermission klassPermission)
  {
    Map<String, IRolePermission> rolePermissionMap = klassPermission.getRolePermission();
    IRolePermission rolePermission = rolePermissionMap.get(roleIds.get(0));
    List<String> intersectedSectionIds = new ArrayList<String>();
    intersectedSectionIds.addAll(rolePermission.getSectionPermission()
        .keySet());
    for (int iRoleCount = 1; iRoleCount < roleIds.size(); iRoleCount++) {
      IRolePermission otherRolePermissions = rolePermissionMap.get(roleIds.get(iRoleCount));
      intersectedSectionIds.retainAll(otherRolePermissions.getSectionPermission()
          .keySet());
      if (intersectedSectionIds.size() == 0) {
        break;
      }
    }
    return intersectedSectionIds;
  }
  
  private Boolean checkUserExistInARole(String userId,
      List<? extends IRoleCandidate> roleCandidates)
  {
    for (IRoleCandidate roleCandidate : roleCandidates) {
      if (roleCandidate instanceof User) {
        if (roleCandidate.getId()
            .equals(userId)) {
          return true;
        }
      }
    }
    return false;
  }
}
