package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.variantconfiguration.IVariantConfigurationModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.ISide2NatureKlassFromNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetSide2NatureKlassFromNatureRelationship extends AbstractOrientPlugin {
  
  public GetSide2NatureKlassFromNatureRelationship(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSide2NatureKlassFromNatureRelationship/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(ISide2NatureKlassFromNatureRelationshipModel.CODE,
        ISide2NatureKlassFromNatureRelationshipModel.ICON, ISide2NatureKlassFromNatureRelationshipModel.ID,
        IKlassModel.NATURE_TYPE, ISide2NatureKlassFromNatureRelationshipModel.LABEL, IKlassModel.IS_NATURE);
    
    String natureRelationshipId = (String) requestMap.get(IIdParameterModel.ID);
    String side2KlassId = RelationshipUtils
        .getSide2KlassIdFromNatureRelationshipId(natureRelationshipId);
    
    Iterable<Vertex> klassNodes = RelationshipUtils.getAllNatureKlassNodesFromKlassId(side2KlassId);
    Map<String, Object> klassMap = new HashMap<String, Object>();
    
    Vertex variantConfigurationVertex = UtilClass.getVertexByIndexedId(
        SystemLevelIds.STANDARD_VARIANT_CONFIGURATION,
        VertexLabelConstants.VARIANT_CONFIGURATION);
    Boolean isSelectVariant = variantConfigurationVertex.getProperty(IVariantConfigurationModel.IS_SELECT_VARIANT);
    
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole);
    Boolean shouldCheckReadPermission = !klassIdsHavingReadPermission.isEmpty();
    
    for (Vertex klassNode : klassNodes) {
      String klassCode = UtilClass.getCode(klassNode);
      if (shouldCheckReadPermission && !klassIdsHavingReadPermission.contains(klassCode)) {
        continue;
      }
      
      Map<String, Object> klassAndTaxonomyPermission = GlobalPermissionUtils.getKlassAndTaxonomyPermission(UtilClass.getCId(klassNode),
              UtilClass.getCode(userInRole));
      
      if ((Boolean) klassAndTaxonomyPermission.get(IGlobalPermission.CAN_CREATE)) {
        klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
        klassMap.put(ISide2NatureKlassFromNatureRelationshipModel.TYPE,klassMap.remove(IKlassModel.NATURE_TYPE));
        klassMap.remove(IKlass.IS_NATURE);
        klassMap.put(ISide2NatureKlassFromNatureRelationshipModel.SELECT_VARIANT, isSelectVariant);
        break;
      }
      
    }
    return klassMap;
  }
}
