package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetAllSide2NatureKlassesFromNatureRelationship extends AbstractOrientPlugin {
  
  public GetAllSide2NatureKlassesFromNatureRelationship(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllSide2NatureKlassesFromNatureRelationship/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> side2NatureKlasses = new HashMap<>();
    List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.CODE,
        IConfigEntityInformationModel.ICON, IConfigEntityInformationModel.ID,
        IKlassModel.NATURE_TYPE, IConfigEntityInformationModel.LABEL, IKlassModel.IS_NATURE);
    
    String natureRelationshipId = (String) requestMap.get(IIdParameterModel.ID);
    String side2KlassId = RelationshipUtils
        .getSide2KlassIdFromNatureRelationshipId(natureRelationshipId);
    
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole);
    Boolean shouldCheckReadPermission = !klassIdsHavingReadPermission.isEmpty();
    
    Iterable<Vertex> klassNodes = RelationshipUtils.getAllNatureKlassNodesFromKlassId(side2KlassId);
    Map<String, Object> klassMap = new HashMap<String, Object>();
    for (Vertex klassNode : klassNodes) {
      String klassCode = UtilClass.getCode(klassNode);
      if (shouldCheckReadPermission && !klassIdsHavingReadPermission.contains(klassCode)) {
        continue;
      }
      Map<String, Object> klassAndTaxonomyPermission = GlobalPermissionUtils.getKlassAndTaxonomyPermission(UtilClass.getCId(klassNode),
          UtilClass.getCode(userInRole));
      if ((Boolean) klassAndTaxonomyPermission.get(IGlobalPermission.CAN_CREATE)) {
        klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
        klassMap.put(IConfigEntityInformationModel.TYPE, klassMap.remove(IKlassModel.NATURE_TYPE));
        klassMap.remove(IKlass.IS_NATURE);
        side2NatureKlasses.put(klassCode, klassMap);
      }
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel.SIDE2_NATURE_KLASSES,
        side2NatureKlasses);
    return returnMap;
  }
}
