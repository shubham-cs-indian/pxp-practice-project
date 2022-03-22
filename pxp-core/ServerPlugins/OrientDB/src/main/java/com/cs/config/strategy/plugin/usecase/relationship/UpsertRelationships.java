package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tauseef
 */
public class UpsertRelationships extends AbstractSaveRelationship {

  public UpsertRelationships(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception {
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) requestMap.get(
        IListModel.LIST);
    List<Map<String, Object>> successRelationshipList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    Map<String, Map<String, Object>> dataTransfer = new HashMap<>();
    List<Map<String, Object>> failedRelationshipList = new ArrayList<>();

    for (Map<String, Object> relationshipMap : relationships) {

      String relationshipCode = (String) relationshipMap.get(CommonConstants.CODE_PROPERTY);
      String relationshipLabel = (String) relationshipMap.get(CommonConstants.LABEL_PROPERTY);

      try {
        Vertex relationshipNode = ImportRelationshipPXONParser.prepareADMForNonNatureRelationshipImport(relationshipMap);
        String icon = (String) relationshipMap.get(CommonConstants.ICON_PROPERTY);
        UtilClass.validateIconExistsForImport(relationshipMap);
        //Set ICON property as empty as it comes from import
        if(icon.equals("")) {
          relationshipMap.put(CommonConstants.ICON_PROPERTY, icon);
        }
        
        //If Relationship Exists then update it
        if (relationshipNode == null) {
          relationshipNode = createNewRelationship(relationshipMap);
          RelationshipUtils.updateSideLabelsContextAndVisibility(UtilClass.getGraph(), relationshipMap, relationshipNode);
        }
        else {
          //Update Relationship
          setNonUpdatableProperty(relationshipMap, relationshipNode);
          relationshipNode = updateRelationshipFromNode(relationshipMap, dataTransfer, relationshipNode);
        }

        Map<String, Object> successRelationship = new HashMap<>();
        successRelationship.put(CommonConstants.CODE_PROPERTY, relationshipCode);
        successRelationship.put(ISummaryInformationModel.LABEL,relationshipLabel);
        successRelationshipList.add(successRelationship);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, relationshipCode,
            relationshipLabel);
        addToFailureIds(failedRelationshipList, relationshipMap);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, relationshipCode,
            relationshipLabel);
        addToFailureIds(failedRelationshipList, relationshipMap);
      }
    }
    
    UtilClass.getGraph().commit();

    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetAllRelationshipsResponseModel.LIST, successRelationshipList);

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkSaveRelationshipsResponseModel.SUCCESS, successMap);
    responseMap.put(IBulkSaveRelationshipsResponseModel.FAILURE, failure);
    responseMap.put(IPluginSummaryModel.FAILED_IDS, failedRelationshipList);

    return responseMap;
  }

  /**
   * set side1 and side2 non-updatable property
   * @param relationshipMap
   * @param relationshipNode
   */
  private void setNonUpdatableProperty(Map<String, Object> relationshipMap, Vertex relationshipNode)
  {
    setNonUpdtableProperty(relationshipMap, relationshipNode, IRelationship.SIDE1);
    setNonUpdtableProperty(relationshipMap, relationshipNode, IRelationship.SIDE2);
  }

  /**
   * even if user change the value of cardinality and side klassID don't allow the user
   * set the info from old relationship
   * @param relationshipMap map for update
   * @param relationshipNode old relationship node
   * @param sideLabel side label of the relationship
   */
  private void setNonUpdtableProperty(Map<String, Object> relationshipMap, Vertex relationshipNode, String sideLabel)
  {
    Map<String, Object> oldSide = relationshipNode.getProperty(sideLabel);
    String cardinality = (String) oldSide.get(IRelationshipSide.CARDINALITY);
    String klassId = (String) oldSide.get(IRelationshipSide.KLASS_ID);
    Map<String, Object> newSide = (Map<String, Object>) relationshipMap.get(sideLabel);
    newSide.put(IRelationshipSide.CARDINALITY, cardinality);
    newSide.put(IRelationshipSide.KLASS_ID, klassId);
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedRelationshipList, Map<String, Object> relationship)
  {
    Map<String, Object> failedRelationshipMap = new HashMap<>();
    failedRelationshipMap.put(ISummaryInformationModel.ID, relationship.get(IRelationship.CODE));
    failedRelationshipMap.put(ISummaryInformationModel.LABEL, relationship.get(IKlassModel.LABEL));
    failedRelationshipList.add(failedRelationshipMap);
  }

  @Override
  public String[] getNames() {
    return new String[] { "POST|UpsertRelationships/*" };
  }
}
