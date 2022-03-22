package com.cs.config.strategy.plugin.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlassTagValues;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.itextpdf.text.log.SysoCounter;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class MigrationForTypeChange extends AbstractOrientPlugin {
  
  public MigrationForTypeChange(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrationForTypeChange/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    String query = "rebuild index *";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "Update Attribute  set type = type.replace('com.cs.config.interactor.entity.concrete.attribute.standard.',"
        + " 'com.cs.core.config.interactor.entity.standard.attribute.')  where type like "
        + "'com.cs.config.interactor.entity.concrete.attribute.standard.%'";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();

    query = "Update Attribute set type = type.replace('com.cs.config.interactor.entity.concrete.attribute.',"
        + " 'com.cs.core.config.interactor.entity.attribute.')" + " where type like 'com.cs.config.interactor.entity.concrete.attribute.%'";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
  
    query = "update Tag set type = 'com.cs.core.config.interactor.entity.tag.Tag' where type = 'com.cs.config.interactor.entity.Tag'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = " update Role set type = 'com.cs.core.config.interactor.entity.role.Role' where type = 'com.cs.config.interactor.entity.Role'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Role set type = type.replace('com.cs.config.interactor.entity.concrete.role.standard.',"
        + " 'com.cs.core.config.interactor.entity.standard.role.')"
        + " where type like 'com.cs.config.interactor.entity.concrete.role.standard.%'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update User set type = 'com.cs.core.config.interactor.entity.user.User' where type = 'com.cs.config.interactor.entity.User'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass set type = 'com.cs.core.config.interactor.entity.klass.ProjectKlass' "
        + "where type = 'com.cs.config.interactor.entity.concrete.klass.ProjectKlass'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass set parent.type = 'com.cs.core.config.interactor.entity.klass.ProjectKlass'"
        + " where parent.type = 'com.cs.config.interactor.entity.concrete.klass.ProjectKlass'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass set type = 'com.cs.core.config.interactor.entity.klass.Asset' "
        + "where type = 'com.cs.config.interactor.entity.concrete.klass.Asset'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass set parent.type = 'com.cs.core.config.interactor.entity.klass.Asset' "
        + "where parent.type = 'com.cs.config.interactor.entity.concrete.klass.Asset'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = " update RootKlass  set type = 'com.cs.core.config.interactor.entity.klass.Market'"
        + "  where type = 'com.cs.config.interactor.entity.concrete.klass.Market'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = " update RootKlass  set parent.type = 'com.cs.core.config.interactor.entity.klass.Market' "
        + "where parent.type = 'com.cs.config.interactor.entity.concrete.klass.Market'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass set type = 'com.cs.core.config.interactor.entity.textasset.TextAsset'"
        + "  where type = 'com.cs.config.interactor.entity.concrete.klass.textasset.TextAsset'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass  set parent.type = 'com.cs.core.config.interactor.entity.textasset.TextAsset' "
        + " where parent.type = 'com.cs.config.interactor.entity.concrete.klass.textasset.TextAsset'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass  set type = 'com.cs.core.config.interactor.entity.supplier.Supplier'  "
        + "where type = 'com.cs.config.interactor.entity.concrete.klass.supplier.Supplier'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update RootKlass  set parent.type = 'com.cs.core.config.interactor.entity.supplier.Supplier' "
        + " where parent.type = 'com.cs.config.interactor.entity.concrete.klass.supplier.Supplier'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = " update Relationship  set type = 'com.cs.core.config.interactor.entity.relationship.Relationship'"
        + "  where type = 'com.cs.config.interactor.entity.Relationship'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Normalization  set baseType = 'com.cs.core.config.interactor.model.datarule.FindReplaceNormalization' "
        + " where baseType = 'com.cs.config.interactor.model.FindReplaceNormalization'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Normalization  set baseType = 'com.cs.core.config.interactor.model.datarule.Normalization'"
        + "  where baseType = 'com.cs.config.interactor.model.Normalization'";
    
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "update Normalization  set baseType = 'com.cs.core.config.interactor.model.attribute.AttributeValueNormalization' "
        + "where baseType = 'com.cs.config.interactor.model.AttributeValueNormalization'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    // TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
    /*query = "update Klass_Taxonomy set baseType = 'com.cs.core.config.interactor.entity.attributiontaxonomy.HierarchyTaxonomy'"
        + " where baseType = 'com.cs.config.interactor.entity.attributiontaxonomy.HierarchyTaxonomy'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();*/
    
    query = "update Attribution_Taxonomy  set baseType = 'com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy'"
        + " where baseType = 'com.cs.config.interactor.entity.attributiontaxonomy.MasterTaxonomy'";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Role  set klassType = ['com.cs.core.config.interactor.entity.klass.ProjectKlass', 'task',"
        + " 'com.cs.core.config.interactor.entity.klass.Asset', 'com.cs.core.config.interactor.entity.klass.Market'] "
        + "where klassType contains('com.cs.config.interactor.entity.concrete.klass.ProjectKlass')";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Klass_Property set propertyId = first(out('Has_Property').code)";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Klass_Property set code = cid";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    query = "update Value set code = cid";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    /*query = "update Language  set code = code.substring(0,3) + code.substring(6,100) where code.length() > 5 ";
    
    UtilClass.getGraph().command(new OCommandSQL(query)).execute();*/
    
    query = "update Property_Collection_Can_Read_Permission  set roleId = first(in('Has_Role_Property_Collection_Permission').code), "
        + "entityId = first(in('Has_Property_Collection_Permission').code), propertyCollectionId = "
        + "first(out('Is_Property_Collection_Permission_Of').code)";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "update Relationship_Can_Read_Permission  set roleId = first(in('Has_Role_Relationship_Permission').code),"
        + "  entityId = first(in('Has_Relationship_Permission').code), relationshipId = first(out('Is_Relationship_Permission_Of').code)";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = " update Rule_Violation set entityId = first(out('Entity_Rule_Violation_Link').code)";
    
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "update Property_Can_Read_Permission  set roleId = first(in('Has_Role_Property_Permission').code), "
        + "entityId = first(in('Has_Property_Permission').code), propertyId = first(out('Is_Property_Permission_Of').code)";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "update Property_Can_Edit_Permission  set roleId = first(in('Has_Role_Property_Permission').code), "
        + "entityId = first(in('Has_Property_Permission').code), propertyId = first(out('Is_Property_Permission_Of').code)";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "update Header_Permission  set roleId = first(in('Has_Role_Header_Permission').code), "
        + " entityId = first(in('Has_Header_Permission').code)";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "update Property_Collection_Can_Edit_Permission  set roleId = first(in('Has_Role_Property_Collection_Permission').code), "
        + "entityId = first(in('Has_Property_Collection_Permission').code), propertyCollectionId = first(out('Is_Property_Collection_Permission_Of').code)";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "Update Smart_Document_Preset SET smartDocumentTemplateId = (SELECT code FROM Smart_Document_Template where cid = $parent.$current.smartDocumentTemplateId),"
        + "smartDocumentTemplateId = smartDocumentTemplateId.replace(\"[\",\"\").replace(\"]\",\"\")";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "select  from Tab_Sequence";
    
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query)).execute();
    
    Iterator<Vertex> iterator = vertices.iterator();
    Vertex vertex = iterator.next();
    
    List<String> updatedSequenceList = new ArrayList<String>();
    List<String> sequenceList = vertex.getProperty(CommonConstants.SEQUENCE_LIST);
    
    for (String sequence : sequenceList) {
      
      query = "SELECT cid, code FROM Tab where cid = '" + sequence + "'";
      
      Iterable<Vertex> tabVertices = graph.command(new OCommandSQL(query)).execute();
      
      Iterator<Vertex> tabIterator = tabVertices.iterator();
      Vertex tabVertex = tabIterator.next();
      updatedSequenceList.add(tabVertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    vertex.setProperty(CommonConstants.SEQUENCE_LIST, updatedSequenceList);
    graph.commit();
    
    
    query = "select from " + VertexLabelConstants.ENTITY_TAG;
    Iterable<Vertex> tagVertices = graph.command(new OCommandSQL(query)).execute();
    for (Vertex tag : tagVertices) {
      
      List<String> allowedTags = tag.getProperty(ITag.ALLOWED_TAGS);
      List<String> tagValuesSequence = tag.getProperty(ITag.TAG_VALUES_SEQUENCE);
      if (allowedTags != null && !allowedTags.isEmpty()) {
        List<String> modifiedAllowedTags = new ArrayList<>();
        for (String allowedTag : allowedTags) {
          query = "select from Tag where cid = '" + allowedTag + "'";
          Iterable<Vertex> tVertices = graph.command(new OCommandSQL(query)).execute();
          Iterator<Vertex> tIterator = tVertices.iterator();
          Vertex tVertex = tIterator.next();
          modifiedAllowedTags.add(tVertex.getProperty(CommonConstants.CODE_PROPERTY));
        }
        tag.setProperty(ITag.ALLOWED_TAGS, modifiedAllowedTags);
      }
      
      if (tagValuesSequence != null && !tagValuesSequence.isEmpty()) {
        List<String> modifiedTagValueSequence = new ArrayList<>();
        
        for (String tagValue : tagValuesSequence) {
          if (tagValue == null || tagValue.isEmpty()) {
            continue;
          }
          query = "select from Tag where cid = '" + tagValue + "'";
          Iterable<Vertex> tVertices = graph.command(new OCommandSQL(query)).execute();
          Iterator<Vertex> tIterator = tVertices.iterator();
          Vertex tVertex = tIterator.next();
          modifiedTagValueSequence.add(tVertex.getProperty(CommonConstants.CODE_PROPERTY));
          
        }
        tag.setProperty(ITag.TAG_VALUES_SEQUENCE, modifiedTagValueSequence);
      }
      
    }
    graph.commit();
    
    query = "select from Klass_Tag";
    
    Iterable<Vertex> klassTagVertices = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex tag : klassTagVertices) {
      
      List<String> defaultValues = tag.getProperty(CommonConstants.DEFAULT_VALUE_PROPERTY);
      if (!defaultValues.isEmpty()) {
        List<String> modifiedDefaultValue = new ArrayList<>();
        for (String defaultValue : defaultValues) {
          query = "select from Tag where cid = '" + defaultValue + "'";
          Iterable<Vertex> tVertices = graph.command(new OCommandSQL(query)).execute();
          Iterator<Vertex> tIterator = tVertices.iterator();
          Vertex tVertex = tIterator.next();
          modifiedDefaultValue.add(tVertex.getProperty(CommonConstants.CODE_PROPERTY));
        }
        tag.setProperty(CommonConstants.DEFAULT_VALUE_PROPERTY, modifiedDefaultValue);
      }
    }
    graph.commit();
    
    query = "select from Root_Relationship";
    Iterable<Vertex> relationshipVertices = graph.command(new OCommandSQL(query)).execute();
    for (Vertex relationship : relationshipVertices) {
      
      String code = relationship.getProperty(CommonConstants.CODE_PROPERTY);
      
      Map<String, Object> side1 = (Map<String, Object>) relationship.getProperty(IRelationship.SIDE1);
      Map<String, Object> side2 = (Map<String, Object>) relationship.getProperty(IRelationship.SIDE2);
      String side1KlassId = (String) side1.get("klassId");
      String side2KlassId = (String) side2.get("klassId");
      
        query = "SELECT FROM RootKlass where cid = '" + side1KlassId + "'";
        
        Iterable<Vertex> klassVertices = graph.command(new OCommandSQL(query)).execute();
        
        Iterator<Vertex> klassIterator = klassVertices.iterator();
        Vertex klassVertex = klassIterator.next();
        String side1Code = klassVertex.getProperty(CommonConstants.CODE_PROPERTY);
        
        query = "SELECT FROM RootKlass where cid = '" + side2KlassId + "'";
        
        klassVertices = graph.command(new OCommandSQL(query)).execute();
        
        klassIterator = klassVertices.iterator();
        klassVertex = klassIterator.next();
        String side2Code = klassVertex.getProperty(CommonConstants.CODE_PROPERTY);
        
        query = "update Root_Relationship set side1.klassId = '" + side1Code + "', side2.klassId = '" + side2Code + "' where code = '" + code
            + "'";
        
        graph.command(new OCommandSQL(query)).execute();
     
      }
    graph.commit();
    
    handleCodeChangeForRelations("Klass_Relationship");
    handleCodeChangeForRelations("Klass_Relationship_Nature");
    
    query = "select  from Tab";
    
    Iterable<Vertex> tabVertices = graph.command(new OCommandSQL(query)).execute();
    Map<String, Object> returnMap = new HashMap<>();
    List<String> cids = new ArrayList<String>();
    returnMap.put("IgnoredCIDsForPropertyCollectionListOfTabs", cids);
    for (Vertex tab : tabVertices) {
      List<String> propertySequenceList = tab.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
      
      if (propertySequenceList != null && !propertySequenceList.isEmpty()) {
        List<String> modifiedPropertySequenceList = new ArrayList<>();
        
        for (String property : propertySequenceList) {
         try {
           // In case of refereces cid doesn't exist thats why skipping that one from propertysequence list
           query = "select from V where cid = '" + property + "'";
           Iterable<Vertex> tVertices = graph.command(new OCommandSQL(query)).execute();
           Iterator<Vertex> tIterator = tVertices.iterator();
           Vertex tVertex = tIterator.next();
           modifiedPropertySequenceList.add(tVertex.getProperty(CommonConstants.CODE_PROPERTY));
         }catch(Exception e) {
           cids.add(property);
         }
        }
        tab.setProperty(ITab.PROPERTY_SEQUENCE_LIST, modifiedPropertySequenceList);
      }
    }
    graph.commit();

    
    // reindex of code instead of ID
    removeIndexOnCidAndAddOnCodeAndReIndex();
    
    
    return returnMap;
  }
  
  private void removeIndexOnCidAndAddOnCodeAndReIndex() throws CSInitializationException
  {
    UtilClass.getDatabase().commit();
    OrientGraph graph = UtilClass.getGraph();
    Collection<OClass> classes = UtilClass.getDatabase().getMetadata().getSchema().getClasses();
    for (OClass class1 : classes) {
      String name = class1.getName();
      try {
        if (class1.isVertexType()) {
          graph.dropIndex(name + ".cid");
          
          OrientVertexType vertexType = graph.getVertexType(name);
          
          graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class, new Parameter("type", "UNIQUE"),
              new Parameter("class", vertexType));
          
        }
      }
      catch (RuntimeException exception) {
        System.out.println("--------Exception occured while droping index or creating new index for vertex " + name
            + " message " + exception.getMessage());
      }
      
    }
  }
  
  public void handleCodeChangeForRelations(String vertexLable)
  {
    
    String query = "select from " + vertexLable;
    
    Iterable<Vertex> relationshipVertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    for (Vertex relationship : relationshipVertices) {
      
      String code = relationship.getProperty(CommonConstants.CODE_PROPERTY);
      
      Map<String, Object> side = (Map<String, Object>) relationship.getProperty("relationshipSide");
      String klassId = (String) side.get("klassId");
      
      query = "SELECT FROM RootKlass where cid = '" + klassId + "'";
      
      Iterable<Vertex> klassVertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      
      Iterator<Vertex> klassIterator = klassVertices.iterator();
      Vertex klassVertex = klassIterator.next();
      String sideCode = klassVertex.getProperty(CommonConstants.CODE_PROPERTY);
      
      query = "update " + vertexLable + " set relationshipSide.klassId = '" + sideCode + "' where code = '" + code + "'";
      
      UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      
    }
    UtilClass.getGraph().commit();
    
  }
}
