/*package com.cs.core.runtime.strategy.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.postgresql.jdbc.PgArray;
import org.postgresql.jdbc4.Jdbc4Array;
import org.postgresql.util.PGobject;

import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.exception.configuration.CSException;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.ResultSetUtils;
import com.fasterxml.jackson.core.type.TypeReference;

public class PostgreSearchAPIUtil {

  public static List<ArticleInstance> searchAllItems(IGetKlassInstanceTreeStrategyModel klassInstanceTreeModel)
			throws Exception {
		return searchAllItems(PostgreConstants.ItemType.ARTICLE, klassInstanceTreeModel);
	}

	public static List<ArticleInstance> searchAllItems(PostgreConstants.ItemType itemType,
			IGetKlassInstanceTreeStrategyModel klassInstanceTreeModel) throws Exception {
		List<ArticleInstance> articleInstancesList = new ArrayList<>();

		String allItemsByNameQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ALL_ARTICLE_QUERY);
		Map<String, ArticleInstance> articleInstances = getArticleInstance(allItemsByNameQuery, klassInstanceTreeModel);

		Set<String> articleIds = articleInstances.keySet();

		String attributesByItemIdsQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ALL_ATTRIBUTES_BY_ITEM_ID_QUERY);
		attributesByItemIdsQuery = attributesByItemIdsQuery.concat(PostgreDBUtil.getInClause(articleIds));
		Map<String, List<AttributeInstance>> attributeInstances = getAttributeInstances(attributesByItemIdsQuery,
				itemType);

		String tagsByItemIdsQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ALL_TAGS_BY_ITEM_ID_QUERY);
		tagsByItemIdsQuery = tagsByItemIdsQuery.concat(PostgreDBUtil.getInClause(articleIds));
		Map<String, List<TagInstance>> tagInstances = getTagInstances(tagsByItemIdsQuery, itemType);

		for (String articleId : articleIds) {
			ArticleInstance articleInstance = articleInstances.get(articleId);
			articleInstance.setAttributes(attributeInstances.get(articleId));
			articleInstance.setTags(tagInstances.get(articleId));
			articleInstancesList.add(articleInstance);
		}

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static ArticleInstance searchByItemId(String itemId) throws Exception {
		return searchByItemId(PostgreConstants.ItemType.ARTICLE, itemId);
	}

	public static ArticleInstance searchByItemId(PostgreConstants.ItemType itemType, String itemId) throws Exception {
		// String itemByIdQuery = PostgreDBUtil.getItemByIdQuery(itemType, itemId);
		// TODO: Have to change the query process for getQuery with prepare statement

		String itemByIdQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.ARTICLE_GET_QUERY_KEY);
		Map<String, ArticleInstance> articleInstances = getArticleInstance(itemByIdQuery, itemId);
		ArticleInstance articleInstance = articleInstances.get(itemId);

		return articleInstance;
	}

	public static List<ArticleInstance> searchByItemName(String name) throws Exception {
		return searchByItemName(PostgreConstants.ItemType.ARTICLE, name);
	}

	public static List<ArticleInstance> searchByItemName(PostgreConstants.ItemType itemType, String name)
			throws Exception {
		List<ArticleInstance> articleInstancesList = new ArrayList<>();
		String itemByNameQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ITEM_BY_NAME_QUERY);
		Map<String, ArticleInstance> articleInstances = getArticleInstanceByName(itemByNameQuery, name);
		Set<String> articleIds = articleInstances.keySet();

		String attributesByItemIdsQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ALL_ATTRIBUTES_BY_ITEM_ID_QUERY);
		attributesByItemIdsQuery = attributesByItemIdsQuery.concat(PostgreDBUtil.getInClause(articleIds));
		Map<String, List<AttributeInstance>> attributeInstances = getAttributeInstances(attributesByItemIdsQuery,
				itemType);

		String tagsByItemIdsQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ALL_TAGS_BY_ITEM_ID_QUERY);
		tagsByItemIdsQuery = tagsByItemIdsQuery.concat(PostgreDBUtil.getInClause(articleIds));
		Map<String, List<TagInstance>> tagInstances = getTagInstances(tagsByItemIdsQuery, itemType);

		for (String articleId : articleIds) {
			ArticleInstance articleInstance = articleInstances.get(articleId);
			articleInstance.setAttributes(attributeInstances.get(articleId));
			articleInstance.setTags(tagInstances.get(articleId));

			articleInstancesList.add(articleInstance);
		}

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static List<ArticleInstance> searchByAttributeValueExact(String value, String attributeId) throws Exception {
		return searchByAttributeValueExact(PostgreConstants.ItemType.ARTICLE, value, attributeId);
	}

	public static List<ArticleInstance> searchByAttributeValueExact(PostgreConstants.ItemType itemType, String value,
			String attributeId) throws Exception {
		String allItemAttributesQuery = PostgreDBUtil.getAttributesByValueExactQuery(itemType, value, attributeId);
		List<ArticleInstance> articleInstancesList = searchByAttributeValue(itemType, allItemAttributesQuery, value);

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static List<ArticleInstance> searchByAttributeValueNotEquals(String value, String attributeId)
			throws Exception {
		return searchByAttributeValueNotEquals(PostgreConstants.ItemType.ARTICLE, value, attributeId);
	}

	public static List<ArticleInstance> searchByAttributeValueNotEquals(PostgreConstants.ItemType itemType,
			String value, String attributeId) throws Exception {
		String allItemAttributesQuery = PostgreDBUtil.getAttributesByValueNotEqualsQuery(itemType, value);
		List<ArticleInstance> articleInstancesList = searchByAttributeValue(itemType, allItemAttributesQuery, value);

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static List<ArticleInstance> searchByAttributeValueStartsWith(String value, String attributeId)
			throws Exception {
		return searchByAttributeValueStartsWith(PostgreConstants.ItemType.ARTICLE, value, attributeId);
	}

	public static List<ArticleInstance> searchByAttributeValueStartsWith(PostgreConstants.ItemType itemType,
			String value, String attributeId) throws Exception {
		String allItemAttributesQuery = PostgreDBUtil.getAttributesByValueStartsWithQuery(itemType, value, attributeId);
		List<ArticleInstance> articleInstancesList = searchByAttributeValue(itemType, allItemAttributesQuery, value);

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static List<ArticleInstance> searchByAttributeValueEndsWith(String value, String attributeId)
			throws Exception {
		return searchByAttributeValueEndsWith(PostgreConstants.ItemType.ARTICLE, value, attributeId);
	}

	public static List<ArticleInstance> searchByAttributeValueEndsWith(PostgreConstants.ItemType itemType, String value,
			String attributeId) throws Exception {
		String allItemAttributesQuery = PostgreDBUtil.getAttributesByValueEndsWithQuery(itemType, value, attributeId);
		List<ArticleInstance> articleInstancesList = searchByAttributeValue(itemType, allItemAttributesQuery, value);

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static List<ArticleInstance> searchByAttributeValueContains(String value, String attributeId)
			throws Exception {
		return searchByAttributeValueContains(PostgreConstants.ItemType.ARTICLE, value, attributeId);
	}

	public static List<ArticleInstance> searchByAttributeValueContains(PostgreConstants.ItemType itemType, String value,
			String attributeId) throws Exception {
		String allItemAttributesQuery = PostgreDBUtil.getAttributesByValueContainsQuery(itemType, value, attributeId);
		List<ArticleInstance> articleInstancesList = searchByAttributeValue(itemType, allItemAttributesQuery, value);

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	public static List<ArticleInstance> searchByAttributeValueNotContains(String value, String attributeId)
			throws Exception {
		return searchByAttributeValueNotContains(PostgreConstants.ItemType.ARTICLE, value, attributeId);
	}

	public static List<ArticleInstance> searchByAttributeValueNotContains(PostgreConstants.ItemType itemType,
			String value, String attributeId) throws Exception {
		String allItemAttributesQuery = PostgreDBUtil.getAttributesByValueNotContainsQuery(itemType, value,
				attributeId);
		List<ArticleInstance> articleInstancesList = searchByAttributeValue(itemType, allItemAttributesQuery, value);

		// System.out.println(ObjectMapperUtil.writeValueAsString(articleInstancesList));

		return articleInstancesList;
	}

	private static List<ArticleInstance> searchByAttributeValue(PostgreConstants.ItemType itemType, String query,
			String value) throws Exception {
		List<ArticleInstance> articleInstancesList = new ArrayList<>();

		Map<String, List<AttributeInstance>> attributeInstances = getAttributeInstances(query, itemType);

		Set<String> articleIds = new HashSet<>(attributeInstances.keySet());

		// String tagsByItemIdsQuery = PostgreDBUtil.getTagsByItemIdsQuery(itemType,
		// articleIds);
		// Map<String, List<TagInstance>> tagInstances =
		// getTagInstances(tagsByItemIdsQuery, itemType);

		String itemsByIdsQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES
				.getProperty(ArticleInstanceQueryConstants.GET_ALL_ARTICLE_QUERY);
		itemsByIdsQuery = itemsByIdsQuery.concat(PostgreDBUtil.getInClause(articleIds));

		Map<String, ArticleInstance> articleInstances = getArticleInstance(itemsByIdsQuery, itemType);

		for (String articleId : articleIds) {
			ArticleInstance articleInstance = articleInstances.get(articleId);
			articleInstance.setAttributes(attributeInstances.get(articleId));
			// articleInstance.setTags(tagInstances.get(articleId));

			articleInstancesList.add(articleInstance);
		}

		return articleInstancesList;
	}

  private static Map<String, List<AttributeInstance>> getAttributeInstances(String query,
      PostgreConstants.ItemType itemType) throws Exception
  {
    Map<String, List<AttributeInstance>> attributeInstances = new HashMap<>();

    try (PreparedStatement preparedStat = DatabaseConnection.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStat.executeQuery();) {

      fillAttributeInstance(resultSet, attributeInstances);
    }
    catch (Exception e) {
      throw new CSException(e);
    }

		return attributeInstances;
	}



	public static Map<String, List<TagInstance>> getTagInstances(String query) throws Exception {
		return getTagInstances(query, PostgreConstants.ItemType.ARTICLE);
	}

	private static Map<String, List<TagInstance>> getTagInstances(String query, PostgreConstants.ItemType itemType)
			throws Exception {
		Map<String, List<TagInstance>> tagInstances = new HashMap<>();
		try (PreparedStatement preparedStat = DatabaseConnection.getConnection().prepareStatement(query);
		        ResultSet resultSet = preparedStat.executeQuery();) {
		
		      fillTagInstance(resultSet, tagInstances);
		} catch (Exception e) {
		  throw new CSException(e);
		}

		return tagInstances;
	}

	private static Map<String, ArticleInstance> getArticleInstance(String query, PostgreConstants.ItemType itemType)
			throws SQLException, Exception {
		Map<String, ArticleInstance> articleInstances = new HashMap<>();
		try (PreparedStatement preparedStat = DatabaseConnection.getConnection().prepareStatement(query);
				ResultSet resultSet = preparedStat.executeQuery();) {

			fillArticleInstance(resultSet, articleInstances);
		} catch (Exception e) {
		  throw new CSException(e);
		}
		return articleInstances;
	}

	private static Map<String, ArticleInstance> getArticleInstance(String query, String itemId)
			throws SQLException, Exception {
		Map<String, ArticleInstance> articleInstances = new HashMap<>();

		try (PreparedStatement prepareStatement = DatabaseConnection.getConnection().prepareStatement(query , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			prepareStatement.setString(1, itemId);
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			IArticleInstance articleInstance = ResultSetUtils.mapArticleInstance(resultSet, 1);
			List<AttributeInstance> attributes = new ArrayList<>();
      List<TagInstance> tags = new ArrayList<>();
			resultSet.beforeFirst();
		
			List <String> processedAttributes = new ArrayList<String>();
			List <String> processedTags = new ArrayList<String>();
			
			while(resultSet.next()) {
        if (!processedAttributes.contains(resultSet.getString(38))) {
          attributes.add((AttributeInstance) ResultSetUtils.mapAttributeInstance(resultSet, 38));
          processedAttributes.add(resultSet.getString(38));
        }

        if (!processedTags.contains(resultSet.getString(65))) {
          tags.add((TagInstance) ResultSetUtils.mapTagInstance(resultSet, 65));
          processedTags.add(resultSet.getString(65));
        }
      }
			
			articleInstance.setAttributes(attributes);
			articleInstance.setTags(tags);

      articleInstances.put(articleInstance.getId(), (ArticleInstance) articleInstance);
			
			resultSet.close();
		} catch (Exception e) {
		  throw new CSException(e);
		}
		return articleInstances;
	}

	public static long getTotalArticleCount() throws Exception{
		long totalCount = 0;
		String query = PostgreConnectionDetails.SCHEMA_AND_QUERIES
		        .getProperty(ArticleInstanceQueryConstants.GET_TOTAL_COUNT_QUERY);
		try (Statement statement = DatabaseConnection.getConnection().createStatement();
				ResultSet resultSet = statement.executeQuery(query);) {
			resultSet.next();
			totalCount = resultSet.getInt(PostgreConstants.COUNT);

		} catch (Exception e) {
		  throw new CSException(e);
		}
		return totalCount;
	}
	
	// TODO: need TO DELETE ABOVE METHOD BUT WHICH CALL FROM SEVERAL SO COPY IT
	  // OVERLOADING IT BY ID BECAUSE OF NEW QUERY IN SCHEMA FILE
	
	  private static Map<String, List<AttributeInstance>> getAttributeInstances(String query,
	      String itemId) throws Exception
	  {
	    Map<String, List<AttributeInstance>> attributeInstances = new HashMap<>();
	
	    try (PreparedStatement prepareStatement = DatabaseConnection.getConnection().prepareStatement(query);) {
	
	      prepareStatement.setString(1, itemId);
	      ResultSet resultSet = prepareStatement.executeQuery();
	
	      fillAttributeInstance(resultSet, attributeInstances);
	     resultSet.close();
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	
	    return attributeInstances;
	  }
	
	  // TODO: need TO DELETE ABOVE METHOD BUT WHICH CALL FROM SEVERAL SO COPY IT
	  // OVERLOADING IT BY ID BECAUSE OF NEW QUERY IN SCHEMA FILE
	  private static Map<String, List<TagInstance>> getTagInstances(String query, String itemId)
	      throws Exception
	  {
	    Map<String, List<TagInstance>> tagInstances = new HashMap<>();
	    try (PreparedStatement prepareStatement = DatabaseConnection.getConnection().prepareStatement(query);) {
	
	      prepareStatement.setString(1, itemId);
	      ResultSet resultSet = prepareStatement.executeQuery();
	
	      fillTagInstance(resultSet, tagInstances);
	      resultSet.close();
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	
	    return tagInstances;
	  }
	
	  // TODO : WE overloading THIS METHOD BECAUSE OF QUERY CHANGES IN SCHEMA FILE
	  private static Map<String, ArticleInstance> getArticleInstance(String query,
	      IGetKlassInstanceTreeStrategyModel klassInstanceTreeModel) throws SQLException, Exception
	  {
	    Map<String, ArticleInstance> articleInstances = new HashMap<>();
	    try (PreparedStatement preparedStat = DatabaseConnection.getConnection().prepareStatement(query);) {
	
	      preparedStat.setInt(1, klassInstanceTreeModel.getSize());
	      preparedStat.setInt(2, klassInstanceTreeModel.getFrom());
	      ResultSet resultSet = preparedStat.executeQuery();
	
	      fillArticleInstance(resultSet, articleInstances);
	      resultSet.close();
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	    return articleInstances;
	  }
	
	  private static Map<String, ArticleInstance> getArticleInstanceByName(String query,
	      String valueOfItem) throws SQLException, Exception
	  {
	    Map<String, ArticleInstance> articleInstances = new HashMap<>();
	
	    try (PreparedStatement prepareStatement = DatabaseConnection.getConnection().prepareStatement(query);) {
	
	      prepareStatement.setString(1, PostgreConstants.STRING_LIKE_CONSTANT + valueOfItem + PostgreConstants.STRING_LIKE_CONSTANT);
	
	      ResultSet resultSet = prepareStatement.executeQuery();
	      fillArticleInstance(resultSet, articleInstances);
	      resultSet.close();
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	    return articleInstances;
	  }
	
	  private static void fillArticleInstance(ResultSet resultSet,
	      Map<String, ArticleInstance> articleInstances) throws Exception
	  {
	    List<String> ignoredColumns = PostgreDBUtil.getAllIgnoredCoumns();
	
	    // FIXME
	    try {
	      while (resultSet.next()) {
	        String articleInstanceId = resultSet.getString(PostgreConstants.ID);
	        ArticleInstance articleInstance = new ArticleInstance();
	        for (String column : PostgreConstants.STANDARD_KLASS_TABLE_COLUMNS) {
	          Object object = resultSet.getObject(column);
	          if (object == null) {
	            continue;
	          }
	
	          if (object.getClass()
                .equals(BigDecimal.class)) {
              if (column.equals(PostgreConstants.COLUMN_NAME_VALUE_AS_NUMBER)) {
                object = Double.parseDouble(object.toString());
              }
              else if (column.equals(PostgreConstants.COLUMN_NAME_IS_UNIQUE)) {
                object = Integer.valueOf(object.toString());
              }
              else if (column.equals(PostgreConstants.COLUMN_NAME_VERSION_ID)) {
                object = Long.parseLong(object.toString());
              }
              else if (column.equals(PostgreConstants.COLUMN_NAME_KLASS_INSTANCE_VERSION)) {
                object = Long.parseLong(object.toString());
              }
            }
	
	          else if (object.getClass()
	              .equals(Integer.class)) {
	            object = Long.parseLong(object.toString());
	          } else if (object.getClass()
		           .equals(BigDecimal.class)) {
		        object = Long.parseLong(object.toString());
		      }
	          else if (object.getClass()
	              .equals(Timestamp.class)) {
	            Timestamp timestamp = (Timestamp) object;
	            object = new Long(timestamp.getTime());
	          }
	          else if (ignoredColumns.contains(column)) {
	            continue;
	          }
	          else if (object.getClass()
	              .equals(PgArray.class)) {
	            PgArray a = (PgArray) object;
	            String[] javaArray = (String[]) a.getArray();
	            object = new ArrayList<>(Arrays.asList(javaArray));
	          }
	          else if (object.getClass()
	              .equals(Jdbc4Array.class)) {
	            Jdbc4Array a = (Jdbc4Array) object;
	            String[] javaArray = (String[]) a.getArray();
	            object = new ArrayList<>(Arrays.asList(javaArray));
	          }
	          else if (object.getClass()
	              .equals(PGobject.class)) {
	            continue;
	          }
	
	          String methodName = column.substring(0, 1)
	              .toUpperCase() + column.substring(1);
					 String methodName = PostgreConstants.ARTICLE_TABLE_PROPERTIES.get(column);
	          java.beans.Statement stmt = new java.beans.Statement(articleInstance, PostgreConstants.SET + methodName,
	              new Object[] { object });
	          stmt.execute();
	        }
	
	        articleInstances.put(articleInstanceId, articleInstance);
	      }
	      resultSet.close();
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	  }
	
	  private static void fillTagInstance(ResultSet resultSet,
	      Map<String, List<TagInstance>> tagInstances) throws Exception
	  {
	    try {
	      while (resultSet.next()) {
	        String articleInstanceId = resultSet.getString(PostgreConstants.COLUMN_NAME_KLASS_INSTANCE_ID);
	        List<String> ignoredColumns = PostgreDBUtil.getAllIgnoredCoumns();
	        TagInstance tagInstance = new TagInstance();
	        for (String column : PostgreConstants.STANDARD_TAG_TABLE_COLUMNS) {
	          Object object = resultSet.getObject(column);
	          if (object == null) {
	            continue;
	          }
	          if (column.equals(PostgreConstants.COLUMN_NAME_NOTIFICATION)) {
	            object = ObjectMapperUtil.readValue((String) object, HashMap.class);
	          }
	          else if (object.getClass()
	              .equals(Integer.class)
	              || object.getClass()
	                  .equals(BigDecimal.class)) {
	            String string = object.toString();
	            object = Long.parseLong(string);
	          }
	          else if (object.getClass()
	              .equals(Timestamp.class)) {
	            Timestamp timestamp = (Timestamp) object;
	            object = new Long(timestamp.getTime());
	          }
	          else if (ignoredColumns.contains(column)) {
	            continue;
	          }
	          else if (object.getClass()
	              .equals(PgArray.class)) {
	            PgArray a = (PgArray) object;
	            String[] javaArray = (String[]) a.getArray();
	            object = new ArrayList<>(Arrays.asList(javaArray));
	          }
	          else if (object.getClass()
	              .equals(PGobject.class)) {
	            if (column.equals(PostgreConstants.COLUMN_NAME_TAG_VALUES)) {
	              PGobject jsonObject = (PGobject) object;
	              object = ObjectMapperUtil.readValue(jsonObject.getValue(),
	                  new TypeReference<List<TagInstanceValue>>()
	                  {
	                  });
	            }
	            else
	              continue;
	          }
	
	          String methodName = column.substring(0, 1)
	              .toUpperCase() + column.substring(1);
					
					  String methodName =PostgreConstants.TAG_TABLE_PROPERTIES.get(column);
					 	          java.beans.Statement stmt = new java.beans.Statement(tagInstance, PostgreConstants.SET + methodName,
	              new Object[] { object });
	          stmt.execute();
	        }
	
	        if (tagInstances.containsKey(articleInstanceId)) {
	          tagInstances.get(articleInstanceId)
	              .add(tagInstance);
	        }
	        else {
	          List<TagInstance> list = new ArrayList<>();
	          list.add(tagInstance);
	          tagInstances.put(articleInstanceId, list);
	        }
	      }
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	  }
	
	  private static void fillAttributeInstance(ResultSet resultSet,
	      Map<String, List<AttributeInstance>> attributeInstances) throws Exception
	  {
	    try {
	      while (resultSet.next()) {
	        String articleInstanceId = resultSet.getString(PostgreConstants.COLUMN_NAME_KLASS_INSTANCE_ID);
	        List<String> ignoredColumns = PostgreDBUtil.getAllIgnoredCoumns();
	        AttributeInstance attributeInstance = new AttributeInstance();
	        for (String column : PostgreConstants.STANDARD_ATTRIBUTE_TABLE_COLUMNS) {
	          Object object = resultSet.getObject(column);
	          if (object == null) {
	            continue;
	          }
	          //// System.out.println(column + "--->" + object);
	          if (object.getClass()
	              .equals(BigDecimal.class)) {
	            if (column.equals(PostgreConstants.COLUMN_NAME_VALUE_AS_NUMBER)) {
	              object = Double.parseDouble(object.toString());
	            }
	            else if (column.equals(PostgreConstants.COLUMN_NAME_IS_UNIQUE)) {
	              object = Integer.valueOf(object.toString());
	            }
	            else if (column.equals(PostgreConstants.COLUMN_NAME_VERSION_ID)) {
	              object = Long.parseLong(object.toString());
	            }
	            else if (column.equals(PostgreConstants.COLUMN_NAME_KLASS_INSTANCE_VERSION)) {
	              object = Long.parseLong(object.toString());
	            }
	          }
	          else if (object.getClass()
	              .equals(Timestamp.class)) {
	            Timestamp timestamp = (Timestamp) object;
	            object = new Long(timestamp.getTime());
	          }
	          else if (ignoredColumns.contains(column)) {
	            continue;
	          }
	          else if (object.getClass()
	              .equals(PgArray.class)) {
	            PgArray a = (PgArray) object;
	            String[] javaArray = (String[]) a.getArray();
	            object = new ArrayList<>(Arrays.asList(javaArray));
	          }
	          else if (object.getClass()
	              .equals(PGobject.class)) {
	            continue;
	          }
	
	          String methodName =column.substring(0, 1)
	              .toUpperCase() + column.substring(1);
					
					  String methodName = PostgreConstants.ATTRIBUTE_TABLE_PROPERTIES.get(column);
					
	          java.beans.Statement stmt = new java.beans.Statement(attributeInstance,
	              PostgreConstants.SET + methodName, new Object[] { object });
	          stmt.execute();
	        }
	
	        if (attributeInstances.containsKey(articleInstanceId)) {
	          attributeInstances.get(articleInstanceId)
	              .add(attributeInstance);
	        }
	        else {
	          List<AttributeInstance> list = new ArrayList<>();
	          list.add(attributeInstance);
	          attributeInstances.put(articleInstanceId, list);
	        }
	      }
	    }
	    catch (Exception e) {
	      throw new CSException(e);
	    }
	  }
	  public static TagInstance getTagInstance(String tagId) throws Exception {
		
	      String tagsByTagIdsQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(ArticleInstanceQueryConstants.GET_TAGS_BY_ID_QUERY);
	
		  PreparedStatement getTagById = DatabaseConnection.getConnection().prepareStatement(tagsByTagIdsQuery);
		  getTagById.setString(1, tagId);
		  ResultSet resultSet = getTagById.executeQuery();
		  resultSet.next();
		
		  TagInstance tagInstance =  (TagInstance) ResultSetUtils.mapTagInstance(resultSet,1);
		
		  return tagInstance;
		}
	  }*/
