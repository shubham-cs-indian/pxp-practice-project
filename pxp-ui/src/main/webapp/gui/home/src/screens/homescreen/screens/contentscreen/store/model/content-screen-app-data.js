import CS from '../../../../../../libraries/cs';
import { sortMenuList } from '../../tack/sortMenuList';
import ContentScreenConstants from './../model/content-screen-constants';
import ArticleAppData from './appdata/article-app-data';
import CollectionAppData from './appdata/collection-app-data';
import HomeScreenAppData from '../../../../store/model/home-screen-app-data';

var ContentScreenAppData = (function () {

  var Props = function () {

    const TREE_ROOT_ID = ContentScreenConstants.TREE_ROOT_ID;

    return {
      TREE_ROOT_ID:  TREE_ROOT_ID,

      aTagList: [],
      oTagListMap: {},
      aAttributeList: [],
      oAttributeListMap: {},
      aRoleList: [],
      oRoleListMap: {},

      aUserList: [],
      aTaxonomyList: [],

      oDefaultTypes: {        //Move to i-app-data
        children: [],
      },

      aAvailableEntities: [],
      aContentEventsList: [],

      jobList: [],
      oStaticCollectionMap: {},
      iLevel: 0,

      oDashboardReferencedTags: {}
    }
  };

  var Properties = new Props();

  return {

    getAttributeList: function () {
      return Properties.aAttributeList;
    },

    getAttributeListMap: function () {
      return Properties.oAttributeListMap;
    },

    getContentList: function () {
      return ArticleAppData.getEntityList();
    },

    getTagList: function () {
      return Properties.aTagList;
    },

    getTagListMap: function () {
      return Properties.oTagListMap;
    },

    getRoleList: function () {
      return Properties.aRoleList;
    },

    getUserList: function () {
      return HomeScreenAppData.getUserList();
      // return Properties.aUserList;
    },

    getTaxonomyList: function () {
      return Properties.aTaxonomyList;
    },

    setAttributeList: function (_aAttributeList, bAppend) {
      if (bAppend) {
        Properties.aAttributeList = Properties.aAttributeList.concat(_aAttributeList);
      } else {
      Properties.aAttributeList = _aAttributeList;
      Properties.oAttributeListMap = {};
      }
      CS.forEach(_aAttributeList, function (oAttribute) {
        Properties.oAttributeListMap[oAttribute.id] = oAttribute;
      });
    },

    setContentList: function (_aContentList) {
      ArticleAppData.setEntityList(_aContentList);
    },

    setTagList: function (_aTagList, bAppend) {
      if (bAppend) {
        Properties.aTagList = Properties.aTagList.concat(_aTagList);
      } else {
      Properties.aTagList = _aTagList;
      Properties.oTagListMap = {};
      }
      CS.forEach(_aTagList, function (oTag) {
        Properties.oTagListMap[oTag.id] = oTag;
      });
    },

    setRoleList: function (_aRoleList, bAppend) {
      if (bAppend) {
        Properties.aRoleList = Properties.aRoleList.concat(_aRoleList);
      } else {
        Properties.aRoleList = _aRoleList;
        Properties.oRoleListMap = {};
      }
      CS.forEach(_aRoleList, function (oRole) {
        Properties.oRoleListMap[oRole.id] = oRole;
      });
    },

    setUserList: function (_aUserList) {
      // Properties.aUserList = _aUserList;
      HomeScreenAppData.setUserList(_aUserList);
    },

    getDefaultTypes: function () {
      return Properties.oDefaultTypes;
    },

    setDefaultTypes: function (_oDefaultTypes) {
      Properties.oDefaultTypes = _oDefaultTypes;
    },

    getAvailableEntities: function () {
      return Properties.aAvailableEntities;
    },

    setAvailableEntities: function (_aAvailableEntities) {
      Properties.aAvailableEntities = _aAvailableEntities;
    },

    emptyAvailableEntities: function () {
      Properties.aAvailableEntities = [];
    },

    getDynamicCollectionList: function () {
      return CollectionAppData.getDynamicCollectionList();
    },

    setDynamicCollectionList: function (_aDynamicCollections) {
      CollectionAppData.setDynamicCollectionList(_aDynamicCollections);
    },

    getStaticCollectionList: function () {
      return CollectionAppData.getStaticCollectionList();
    },

    setStaticCollectionList: function (_aStaticCollections) {
      CollectionAppData.setStaticCollectionList(_aStaticCollections);
    },

    getStaticCollectionMap: function () {
      return Properties.oStaticCollectionMap;
    },

    setStaticCollectionMap: function (_oStaticCollectionMap) {
      Properties.oStaticCollectionMap = _oStaticCollectionMap;
    },

    getActiveStaticCollectionLevel: function () {
      return Properties.iLevel;
    },

    setActiveStaticCollectionLevel: function (_iLevel) {
      Properties.iLevel = _iLevel;
    },

    getSortMenuList : function(){
      return sortMenuList;
    },

    getJobList: function () {
      return Properties.jobList;
    },

    setJobList: function (aJobList) {
      Properties.jobList = aJobList;
    },

    reset: function () {
      Object.resetDataProperties(ArticleAppData);
      Object.resetDataProperties(CollectionAppData);
      Properties = new Props();
    },

    getDashboardReferencedTags: function () {
      return Properties.oDashboardReferencedTags ;
    },

    setDashboardReferencedTags: function (_oDashboardReferencedTags) {
      Properties.oDashboardReferencedTags = _oDashboardReferencedTags;
    },

    toJSON: function () {
      return {
      };
    },

  }

})();

export default ContentScreenAppData;
