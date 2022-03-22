/*package com.cs.core.runtime.interactor.usecase.klassinstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.exception.configuration.CSException;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.ISaveInstanceStrategyListModel;
import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.instance.SaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInfoModel;
import com.cs.core.runtime.strategy.db.DatabaseConnection;
import com.cs.core.runtime.strategy.db.PostgreConnectionDetails;
import com.cs.core.runtime.strategy.db.PostgreDBUtil;
import com.cs.core.runtime.strategy.db.PostgreSearchAPIUtil;
import com.cs.core.runtime.strategy.usecase.klassinstance.ISaveKlassInstanceStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class SaveArticleInstance implements ISaveKlassInstanceStrategy {

  @Override
  public ISaveStrategyInstanceResponseModel execute(ISaveInstanceStrategyListModel model)
      throws Exception
  {

    IKlassInstanceSaveModel klassInstance = model.getKlassInstance();

    try{
      saveModifiedAttributes(klassInstance);
      saveModifiedTags(klassInstance);
      saveArticle(klassInstance);
    }
    catch (Exception e) {
      throw new CSException(e);
    }

    ISaveStrategyInstanceResponseModel saveStrategyInstanceResponseModel = getKlassInstance(
        klassInstance);
    return saveStrategyInstanceResponseModel;
  }

  private ISaveStrategyInstanceResponseModel getKlassInstance(IKlassInstanceSaveModel klassInstance)
      throws Exception
  {
    ArticleInstance articleInstance = PostgreSearchAPIUtil.searchByItemId(klassInstance.getId());

    ISaveStrategyInstanceResponseModel saveStrategyInstanceResponseModel = new SaveStrategyInstanceResponseModel();
    IKlassInstanceInfoModel klassInstanceInfoModel = new KlassInstanceInfoModel();

    klassInstanceInfoModel.setBasetype(articleInstance.getBaseType());
    klassInstanceInfoModel.setEndpointId(articleInstance.getEndpointId());
    klassInstanceInfoModel.setKlassInstanceId(articleInstance.getKlassInstanceId());
    klassInstanceInfoModel.setOrganisationId(articleInstance.getOrganizationId());
    klassInstanceInfoModel.setPhysicalCatalogId(articleInstance.getPhysicalCatalogId());
    klassInstanceInfoModel.setSelectedTaxonomyIds(articleInstance.getSelectedTaxonomyIds());
    klassInstanceInfoModel.setTaxonomyIds(articleInstance.getTaxonomyIds());
    klassInstanceInfoModel.setTypes(articleInstance.getTypes());

    saveStrategyInstanceResponseModel.setKlassInstanceInfo(klassInstanceInfoModel);
    return saveStrategyInstanceResponseModel;
  }

  private void saveArticle(IKlassInstanceSaveModel klassInstance) throws Exception
  {

	    String updateLastModifiedQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
	        .getProperty(ArticleInstanceQueryConstants.SAVE_ARTICLE_LASTMODIFIED_QUERY);

    try (PreparedStatement preparedStmt = DatabaseConnection.getConnection().prepareStatement(updateLastModifiedQuery);) {

      preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      preparedStmt.setString(2, klassInstance.getId());
      preparedStmt.executeUpdate();
    }
    catch (Exception e) {
      throw new CSException(e);
    }
  }

  private void saveModifiedTags(IKlassInstanceSaveModel klassInstance) throws Exception
  {

    List<IModifiedContentTagInstanceModel> modifiedTags = klassInstance.getModifiedTags();

    String saveTagValuesQuery =PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(ArticleInstanceQueryConstants.SAVE_TAG_TAGVALUES_QUERY);
    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement tagUpdate = connection.prepareStatement(saveTagValuesQuery);

      for (IModifiedContentTagInstanceModel modifiedTag : modifiedTags) {

    	String tagId = modifiedTag.getId();
    	List<ITagInstanceValue> modifiedTagValue = modifiedTag.getModifiedTagValues();
        List<ITagInstanceValue> addedTagValues = modifiedTag.getAddedTagValues();
        List<String> deletedTagIds = modifiedTag.getDeletedTagValues();

        TagInstance tagInstance =   PostgreSearchAPIUtil.getTagInstance(tagId);
        List<ITagInstanceValue> tagValues = tagInstance.getTagValues();
        deleteTagValue(tagValues,deletedTagIds);
        addTagValue(tagValues,addedTagValues);
        modifyTagValue(tagValues,modifiedTagValue);

        PGobject jsonObject = PostgreDBUtil.convertPGJsonObject(tagValues);
        		
        tagUpdate.setObject(1, jsonObject);
        tagUpdate.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        tagUpdate.setString(3, tagInstance.getId());
        tagUpdate.setString(4, tagInstance.getKlassInstanceId());
        tagUpdate.addBatch();


      }
      tagUpdate.executeBatch();
  }
  private void deleteTagValue(List<ITagInstanceValue> tagValues, List<String> deleteTagHavingIds) {
	  List<ITagInstanceValue> deleteTags = new ArrayList<ITagInstanceValue>();
	  for(ITagInstanceValue tagValue: tagValues) {
		  String tagId = tagValue.getId();
		  if(deleteTagHavingIds.contains(tagId)) {
			  deleteTags.add(tagValue);
		  }
	   	
	}
	  tagValues.removeAll(deleteTags);
  }
  private void addTagValue(List<ITagInstanceValue> tagValues, List<ITagInstanceValue> addedTags) {
	     tagValues.addAll(addedTags);
  }

  private void modifyTagValue(List<ITagInstanceValue> tagValues, List<ITagInstanceValue> modifiedTagValues) {
	  for(ITagInstanceValue tagValue: tagValues) {
		
		  for(ITagInstanceValue modifiedTagValue: modifiedTagValues) {
	      	  if(tagValue.getId().equals(modifiedTagValue.getId())) {
	      		  tagValue.setRelevance(modifiedTagValue.getRelevance());
	      		  tagValue.setLastModifiedBy(modifiedTagValue.getLastModifiedBy());
	      		  tagValue.setTimestamp(modifiedTagValue.getTimestamp());
	      		  tagValue.setVersionId(modifiedTagValue.getVersionId());
	      		  tagValue.setVersionTimestamp(modifiedTagValue.getVersionTimestamp());
	      		
	      	  }
		   	
		}    	
	   	
	}
  }

  private void saveModifiedAttributes(IKlassInstanceSaveModel KlassInstance) throws Exception
  {
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = KlassInstance
        .getModifiedAttributes();

    String saveAttributeValueQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
            .getProperty(ArticleInstanceQueryConstants.SAVE_ATTRIBUTE_VALUE_QUERY);

    try (PreparedStatement preparedStmt = DatabaseConnection.getConnection().prepareStatement(saveAttributeValueQuery);) {

      for (IModifiedContentAttributeInstanceModel modifiedAttribute : modifiedAttributes) {
        AttributeInstance attributeInstance = (AttributeInstance) modifiedAttribute.getEntity();
        if (attributeInstance.getAttributeId()
            .equals("nameattribute")) {
          updateArticleName(attributeInstance);
        }
        preparedStmt.setString(1, attributeInstance.getValue());
        preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        preparedStmt.setString(3, attributeInstance.getId());
        preparedStmt.setString(4, attributeInstance.getKlassInstanceId());
        preparedStmt.addBatch();
      }
      preparedStmt.executeBatch();
    }
    catch (Exception e) {
      throw new CSException(e);
    }
  }

  private void updateArticleName(AttributeInstance attributeInstance) throws Exception
  {
	  String saveArticleNameQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
		        .getProperty(ArticleInstanceQueryConstants.SAVE_ARTICLE_NAME_QUERY);

    try (PreparedStatement preparedStmt = DatabaseConnection.getConnection().prepareStatement(saveArticleNameQuery);) {

      preparedStmt.setString(1, attributeInstance.getValue());
      preparedStmt.setString(2, attributeInstance.getKlassInstanceId());
      preparedStmt.setString(3, "en_US");

      preparedStmt.executeUpdate();
    }
    catch (Exception e) {
      throw new CSException(e);
    }
  }


   * public static String
   * UpdateDataIntoColumn(List<IModifiedContentAttributeInstanceModel>
   * modifiedAttributes) throws Exception { List<String> ignoredColumns=
   * Arrays.asList("duplicateStatus"); String insertData = null; for(int i = 0 ; i
   * < modifiedAttributes.size() ; i++){ Connection
   * connection=PostgreDBConnectionUtil.getConnection(); String
   * query="UPDATE attributes SET value =? Where id=?"; PreparedStatement
   * preparedStmt = connection.prepareStatement(query);
   * IModifiedContentAttributeInstanceModel model=new
   * ModifiedAttributeInstanceModel(); Map<String, Class> fieldsMapping =
   * getFieldsMapping(AttributeInstance.class);
   * for(IModifiedContentAttributeInstanceModel modifiedAttribute :
   * modifiedAttributes) { AttributeInstance attributeInstance =
   * (AttributeInstance) modifiedAttribute.getEntity(); } HashMap<String, Object>
   * attributeInstance= (HashMap<String, Object>) modifiedAttributes.get(i); for
   * (String column : PostgreDBUtil.getColumnNames("attributes")) {
   * preparedStmt.setNString(0, (String) attributeInstance.get(column) );
   * preparedStmt.setNString(1, (String) attributeInstance.get(column) );
   * preparedStmt.executeQuery(); } } return null;
   *
   * }
   *
   * public static String
   * UpdateDataIntoColumn1(List<IModifiedContentAttributeInstanceModel>
   * modifiedAttributes) throws Exception { List<String> ignoredColumns=
   * Arrays.asList("duplicateStatus"); String insertData = null; String
   * query="UPDATE attributes SET "; for (String column :
   * PostgreDBUtil.getColumnNames("attributes")) { query=
   * query.concat("\""+column+"\"" + "= ? ,"); } StringBuilder sb=new
   * StringBuilder(query); sb.deleteCharAt(query.length()-1); query=sb.toString();
   * InsertDataIntoQuery(modifiedAttributes, query); return query; }
   *
   * public static void
   * InsertDataIntoQuery(List<IModifiedContentAttributeInstanceModel>
   * modifiedAttributes , String query) throws Exception {
   * IModifiedContentAttributeInstanceModel model=new
   * ModifiedAttributeInstanceModel(); Map<String, Class> fieldsMapping =
   * getFieldsMapping(AttributeInstance.class);
   * for(IModifiedContentAttributeInstanceModel modifiedAttribute :
   * modifiedAttributes) { AttributeInstance attributeInstance =
   * (AttributeInstance) modifiedAttribute.getEntity();
   *
   *
   * for (String column : PostgreDBUtil.getColumnNames("attributes")) {
   * System.out.println(column + " ---> " + fieldsMapping.get(column).getName());
   * if(fieldsMapping.get(column).equals(String.class)) {
   * //System.out.println(column + " ---> " +
   * fieldsMapping.get(column).getName()); } else
   * if(fieldsMapping.get(column).equals(Long.class)) {
   *
   * }
   *
   * String methodName = column.substring(0, 1).toUpperCase() +
   * column.substring(1); java.beans.Statement stmt = new
   * java.beans.Statement(attributeInstance, "get" + methodName, new Object[] {});
   * stmt.execute(); } }
   *
   *
   * for(int i=0;i<modifiedAttributes.size();i++){
   * IModifiedContentAttributeInstanceModel attribute = modifiedAttributes.get(i);
   * IEntity entity = attribute.getEntity(); AttributeInstance attributeInstance
   * =(AttributeInstance) entity; FillDataIntoQuery(attributeInstance); for
   * (String column : PostgreDBUtil.getColumnNames("attributes")) { Field
   * declaredField = attributeInstance.getClass().getDeclaredField(column); String
   * name= declaredField.getType().getName(); } if(entity instanceof
   * IAttributeInstance) {
   *
   * IAttributeInstance atr = (IAttributeInstance) entity;
   * System.out.println("===>> \n\n"+new ObjectMapper().writeValueAsString(atr));
   * for (String column : PostgreDBUtil.getColumnNames("attributes")) {
   * atr.getClass().getDeclaredField(column).getType(); }
   *
   * }
   *
   * ObjectMapper m = new ObjectMapper(); Map<String,Object> props =
   * m.convertValue(attribute, Map.class); for (String column :
   * PostgreDBUtil.getColumnNames("attributes")) { int parameterIndex=0;
   * Connection connection = PostgreDBConnectionUtil.getConnection();
   * PreparedStatement stmt=connection.prepareStatement(query); if() {
   * stmt.setString(parameterIndex, (String) props.get(column)); }else if() {
   * stmt.setInt(parameterIndex, (int) props.get(column)); }else if() {
   * stmt.setTimestamp(parameterIndex, props.get(column), 1000); }else if() {
   * stmt.setArray(parameterIndex, (Array) props.get(column)); }else if() {
   * stmt.setObject(parameterIndex, props.get(column)) } parameterIndex++;
   *
   * //} //} }
   *
   * private static Map<String, Class> getFieldsMapping(Class cls) { Map<String,
   * Class> mappings = new HashMap<>();
   *
   * for(Class clsLoop = cls; clsLoop!=null; clsLoop = cls.getSuperclass()) {
   * for(Field field : clsLoop.getDeclaredFields()) {
   * mappings.put(field.getName(), field.getType()); } }
   *
   * return mappings; }
   *
   * private static void FillDataIntoQuery(AttributeInstance attributeInstance)
   * throws NoSuchFieldException, SecurityException, Exception { for (String
   * column : PostgreDBUtil.getColumnNames("attributes")) { Field declaredField =
   * attributeInstance.getClass().getDeclaredField(column); String name=
   * declaredField.getType().getName();
   *
   * }
   *
   * }



   * private static void updateAttributes(
   * List<IModifiedContentAttributeInstanceModel> modifiedAttributes, String
   * klassInstanceId) throws Exception {
   * for(IModifiedContentAttributeInstanceModel modifiedAttribute :
   * modifiedAttributes) { AttributeInstance attributeInstance =
   * (AttributeInstance) modifiedAttribute.getEntity();
   * if(attributeInstance.getAttributeId().equals("nameattribute")) {
   * updateArticleName(attributeInstance); } Connection
   * connection=PostgreDBConnectionUtil.getConnection(); String
   * query="UPDATE attributes " +
   * "SET value =? , \"lastModified\" = to_timestamp( extract(epoch from now())) "
   * + "Where id=?"; PreparedStatement preparedStmt =
   * connection.prepareStatement(query); preparedStmt.setString(1,
   * attributeInstance.getValue()); preparedStmt.setString(2,
   * attributeInstance.getId()); preparedStmt.executeUpdate(); } }



   * private void updateTags(List<IModifiedContentTagInstanceModel> modifiedTags)
   * throws Exception { for(IModifiedContentTagInstanceModel modifiedTag :
   * modifiedTags) { TagInstance tagsInstance=(TagInstance)
   * modifiedTag.getEntity(); Connection
   * connection=PostgreDBConnectionUtil.getConnection(); String
   * query="UPDATE Tags " + "SET tags =? " + "Where id=?"; PreparedStatement
   * preparedStmt = connection.prepareStatement(query);
   * //preparedStmt.setObject(1, tagsInstance.getTags());
   * preparedStmt.setObject(1, ObjectMapperUtil.writeValueAsString(tagsInstance));
   * preparedStmt.setString(2, tagsInstance.getId());
   * preparedStmt.executeUpdate(); } }

}*/
