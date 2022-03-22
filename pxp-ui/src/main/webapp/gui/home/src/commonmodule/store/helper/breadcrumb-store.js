import CS from '../../../libraries/cs';
import alertify from '../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import MethodTracker from '../../../libraries/methodtracker/method-tracker';
import { getTranslations as getTranslation } from './translation-manager';
import ContentScreenProps from '../../../screens/homescreen/screens/contentscreen/store/model/content-screen-props';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import HierarchyTypes from '../../../commonmodule/tack/hierarchy-types-dictionary';
import SharableURLStore from '../helper/sharable-url-store';
import { communicator as HomeScreenCommunicator } from '../../../screens/homescreen/store/home-screen-communicator';
import GlobalStore from '../../../screens/homescreen/store/global-store.js';
import FilterStoreFactory from '../../../screens/homescreen/screens/contentscreen/store/helper/filter-store-factory';
import FilterUtils from '../../../screens/homescreen/screens/contentscreen/store/helper/filter-utils';


let BreadCrumbStore = (function () {

  let oComponentProps = ContentScreenProps;

  let _triggerChange = function () {
    MethodTracker.emptyCallTrace();
    BreadCrumbStore.trigger('Breadcrumb-changed');
  };

  let _removeQuickListNodeFromBreadcrumbIfExists = function (aBreadcrumb) {
    let aTypesToRemove = [
      BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST,
      BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST,
      BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST
    ];
    CS.remove(aBreadcrumb, function (oBreadCrumb) {
      if (CS.includes(aTypesToRemove, oBreadCrumb.type)) {
        _resetBreadcrumbNodePropsAccordingToContext(oBreadCrumb.type);
        return true;
      }
    });
  };

  let _getBreadcrumbTypesToRemove = function (oBreadCrumbData, aBreadcrumb) {
    let aBreadcrumbTypesToRemove = [];
    let aTypesToReplace = [
      BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION,
      BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION,
      BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE,
      BreadCrumbModuleAndHelpScreenIdDictionary.CONFIG_CHILD_ENTITY,
      BreadCrumbModuleAndHelpScreenIdDictionary.MODULE
    ];
    if (CS.includes(aTypesToReplace, oBreadCrumbData.type)) {
      CS.forEach(aBreadcrumb, function (oBreadCrumb) {
        let sBreadcrumbType = "";
        if ((oBreadCrumb.type === BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION ||
            oBreadCrumb.type === BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION)) {
          _resetBreadcrumbNodePropsAccordingToContext(oBreadCrumb.type);

          /**
           * when static collection is opened from static collection/dynamic collection and vice versa.
           */
          sBreadcrumbType = oBreadCrumb.type;
        }
        else if (oBreadCrumb.type === oBreadCrumbData.type) {
          sBreadcrumbType = oBreadCrumbData.type;
        }

        sBreadcrumbType && aBreadcrumbTypesToRemove.push(sBreadcrumbType);
      });
    }

    return aBreadcrumbTypesToRemove;
  };

  var _removeAndReplaceBreadCrumbs = function (oBreadCrumbData) {
    var aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    _removeQuickListNodeFromBreadcrumbIfExists(aBreadcrumb);

    if(oBreadCrumbData) {
      /** for runtime **/
      let aToRemove = _getBreadcrumbTypesToRemove(oBreadCrumbData, aBreadcrumb);

      /** remove breadcrumb nodes from breadcrumb **/
      CS.remove(aBreadcrumb, function (oBreadCrumb) {
        return CS.includes(aToRemove, oBreadCrumb.type);
      });
    }
  };

  let _getPostData = oPayloadData => {
    let oPostData = {};
      CS.forEach(oPayloadData, data => {
        if(data.postData){
          oPostData = data.postData;
          return false;
        }
      });
      return oPostData;
  };

  var _removeTrailingBreadcrumbPath = function (sId, sType) {
    let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    let iExistingBreadcrumbIndex = CS.findIndex(aBreadcrumb, {id: sId});
    if(iExistingBreadcrumbIndex === -1 && (sType === "DIModule" || sType === "module")) {
      iExistingBreadcrumbIndex = CS.findIndex(aBreadcrumb, {type: sType});
    }

    /**Config Entity - remove all trailing breadcrumbs including current node**/
    if (CS.isNotEmpty(sType) && (sType === BreadCrumbModuleAndHelpScreenIdDictionary.CONFIG_TAB ||
        sType.includes(BreadCrumbModuleAndHelpScreenIdDictionary.CONFIG_PARENT_ENTITY))) {
      iExistingBreadcrumbIndex = CS.findIndex(aBreadcrumb, {type: sType});
      if(iExistingBreadcrumbIndex !== -1)
         CS.reverse(aBreadcrumb.splice(iExistingBreadcrumbIndex));
    }

    let aRemoved = [];
    if (iExistingBreadcrumbIndex !== -1) {
      aRemoved = CS.reverse(aBreadcrumb.splice(iExistingBreadcrumbIndex + 1));
      if (!CS.isEmpty(aRemoved)) {
        let aHierarchyTypes = Object.values(HierarchyTypes);
        let oScreenProps = oComponentProps.screen;
        let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
        CS.forEach(aRemoved, function (oRemoved) {
          let oPostData = _getPostData(oRemoved.payloadData);
          //when we return back to module from filter hierarchy, we need to update post data in modules breadCrumb data.
          _resetBreadcrumbNodePropsAccordingToContext(oRemoved.type, oRemoved.id, oPostData);
          delete oActiveEntitySelectedTabIdMap[oRemoved.id];
          /**
           * @Router Impl
           * @description: Required to reset state when back from any hierarchy.
           **/
          if (CS.includes(aHierarchyTypes, oRemoved.type)) {
            SharableURLStore.setIsEntityNavigation(false);
            let fCallback = () => {
              SharableURLStore.setIsPushHistoryState(true);
              SharableURLStore.addParamsInWindowURL(sId, sType);
            };
            CS.navigateBack(fCallback);
          }
        });
      }
    }

    return aRemoved;
  };

  var _updateExistingBreadcrumbWithNewBreadCrumbData = function (oOldBreadCrumb, oNewBreadCrumb) {
    CS.forEach(oNewBreadCrumb, (value, sKey) => {
      if ((CS.isNotEmpty(value) || CS.isFunction(value)) && sKey !== "extraData") {
        oOldBreadCrumb[sKey] = value;
      }
    });
  };

  /**
   * @Router Impl
   * @description: Update Forward Breadcrumb data and set state for history object
   **/
  let _addStateIntoHistoryObject = function (oNewBreadCrumbData) {
    let oForwardBreadCrumbData = ContentScreenProps.breadCrumbProps.getForwardBreadCrumbData();
    let aTypesToExclude = [
      BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST,
      BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST,
      BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST
    ];
    if (!CS.includes(aTypesToExclude, oNewBreadCrumbData.type)) {
      let sBaseType = oNewBreadCrumbData.baseType ? oNewBreadCrumbData.baseType : "";
      oForwardBreadCrumbData[oNewBreadCrumbData.id] = oNewBreadCrumbData;
      let aTypeToReplace = [
        BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION,
        BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION
      ];
      let oWindowHistoryState = CS.getHistoryState();
      let sHistoryStateType = oWindowHistoryState && oWindowHistoryState.type;
      let sBreadCrumbItemType = oNewBreadCrumbData.type;

      /**
       * @description: Required to replace collection in history.
       * @private
       **/
      let bIsPushHistoryState = SharableURLStore.getIsPushHistoryState();
      if (CS.includes(aTypeToReplace, sBreadCrumbItemType)
          && CS.includes(aTypeToReplace, sHistoryStateType)
          && bIsPushHistoryState) {
        let fCallBack = () => {
          SharableURLStore.addParamsInWindowURL(oNewBreadCrumbData.id, sBreadCrumbItemType, sBaseType);
        };
        SharableURLStore.setIsEntityNavigation(false);
        CS.navigateBack(fCallBack);
      } else {
        SharableURLStore.addParamsInWindowURL(oNewBreadCrumbData.id, sBreadCrumbItemType, sBaseType);
      }
    }
  };

//TODO: Callback data can be replace with breadcrumb item(pick in refactoring)
  let _addNewBreadCrumbItem = function (oNewBreadCrumbData, bDoTrigger) {
    let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    _removeTrailingBreadcrumbPath(oNewBreadCrumbData.id, oNewBreadCrumbData.type);

    let oAlreadyExistBreadcrumb = CS.find(aBreadcrumb, {id: oNewBreadCrumbData.id});

    /** To update breadcrumb data **/
    if (!CS.isEmpty(oAlreadyExistBreadcrumb)) {
      _updateExistingBreadcrumbWithNewBreadCrumbData(oAlreadyExistBreadcrumb, oNewBreadCrumbData);
    }
    else {
      /** To add new breadcrumb data **/
      _removeAndReplaceBreadCrumbs(oNewBreadCrumbData);

      let oSelectedMenu = GlobalStore.getSelectedMenu();
      if (oSelectedMenu.id === 'runtime') {
        _addStateIntoHistoryObject(oNewBreadCrumbData);
      }

      aBreadcrumb.push(oNewBreadCrumbData);
    }
    if (bDoTrigger) {
      _triggerChange();
    }
  };

  let _resetBreadcrumbNodePropsAccordingToContext = function (sContext, sContentId, oPostData) {
    HomeScreenCommunicator.handleResetBreadcrumbNodePropsAccordingToContext(sContext, sContentId, oPostData);
  };

  let _refreshCurrentBreadcrumbEntity = function () {
    let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    let iIndex = aBreadcrumb.length - 1;
    let oBreadCrumbData = aBreadcrumb[iIndex];
    let oExtraData = oBreadCrumbData.extraData;
    oExtraData.executeFunctionToSet();
  };

  /** clear all breadcrumb node's filter data **/
  let _resetAllBreadcrumbNodeFilterData = function () {
    let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();

    CS.forEach(aBreadcrumb, (oBreadcrumb) => {
      let aPayloadData = oBreadcrumb.payloadData;

      if (CS.isNotEmpty(oBreadcrumb.filterContext)) {
        CS.some(aPayloadData, (oPayloadData) => {
          let oPostData = oPayloadData.postData;
          if (oPostData) {
            let oFilterContext = oBreadcrumb.filterContext;
            let oFilterProps = FilterUtils.getFilterProps(oFilterContext);
            let oFilterInfo = oFilterProps.getFilterInfo();
            let aTranslatableAttributeIds = oFilterInfo.translatableAttributeIds;

            /**
             * Reset dependent attributes from breadcrumb node.
             */
            CS.remove(oPostData.attributes, (oAttribute) => {
              if (CS.includes(aTranslatableAttributeIds, oAttribute.id)) {
                return true;
              }
            });

            /** Reset dependent attributes from filter prop */
            let aAppliedFilterData = oFilterProps.getAppliedFilters();
            CS.remove(aAppliedFilterData, (oFilteredData) => {
              if (CS.includes(aTranslatableAttributeIds, oFilteredData.id)) {
                return true;
              }
            });
            return true;
          }
        });
      } else {
        ContentScreenProps.tableViewProps.resetLanguageDependentFilters(oBreadcrumb.id);
      }
    })
    alertify.message(getTranslation().SOME_LANGUAGE_DEPENDENT_FILTERS_HAVE_BEEN_RESET);
  };

  let _resetBreadcrumbNodeFilterDataById = function (sId) {
    let oBreadcrumb = ContentScreenProps.breadCrumbProps.getForwardBreadCrumbData()[sId];
    let aPayloadData = oBreadcrumb.payloadData;
    CS.some(aPayloadData, (oPayloadData) => {
      let oPostData = oPayloadData.postData;
      if (oPostData) {
        let oFilterContext = oBreadcrumb.filterContext;
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        let oFilterData = oFilterStore.createFilterPostData();
        CS.assign(oPostData, oFilterData);
        return true;
      }
    });
  };

  let _createBreadcrumbItem = function (id="", label="", type="", helpScreenId="", filterContext={}, extraData={}, baseType, payloadData=[], functionToSet) {

    // let _payLoadData = payloadData;

    const oDummyBreadCrumbItem = {
      id,
      label,
      type,
      helpScreenId,
      filterContext,
      extraData,
      baseType,
      payloadData,
      functionToSet
    };

    /** Constant Experiment (extra data should not get changed)**/
    Object.defineProperty(oDummyBreadCrumbItem, 'extraData', {
      get () {
        return extraData
      },
      set () {
        throw new Error("Can't change extraData");
      }
    });

    /*
    /!** Payload data's reference should not get change when it is empty so that this property is made constant**!/
    Object.defineProperty(oDummyBreadCrumbItem, 'payloadData', {
      get () {
        return _payLoadData
      },
      set (oData) {
        _payLoadData = oData;
      },
    });
*/

    oDummyBreadCrumbItem.extraData.executeFunctionToSet = function() {
      oDummyBreadCrumbItem.functionToSet.apply(this, oDummyBreadCrumbItem.payloadData);
    };

    return oDummyBreadCrumbItem;
  };

  let _updateBreadcrumbPostData = function(sBreadcrumbId, oNewPostData) {
    let oBreadcrumbData = ContentScreenProps.breadCrumbProps.getForwardBreadCrumbData()[sBreadcrumbId];
    CS.forEach(oBreadcrumbData.payloadData, oPayloadData => {
      if (oPayloadData.postData) {
        let oPostData = Object.assign(oPayloadData.postData, oNewPostData);
        oPayloadData.postData = oPostData;
      }
    })
  }

  /**************** Public API *********************/

  return {

    getData: function () {
      return {
        componentProps: oComponentProps
      }
    },

    removeTrailingBreadcrumbPath: function (sId, sType) {
      return _removeTrailingBreadcrumbPath(sId, sType);
    },

    addNewBreadCrumbItem: function (oBreadCrumbData, bDoTrigger) {
      return _addNewBreadCrumbItem(oBreadCrumbData, bDoTrigger);
    },

    removeQuickListBreadCrumbFromPath: function () {
      _removeAndReplaceBreadCrumbs();
    },

    refreshCurrentBreadcrumbEntity: function () {
      _refreshCurrentBreadcrumbEntity();
    },

    resetAllBreadcrumbNodeFilterData: function () {
      _resetAllBreadcrumbNodeFilterData();
    },

    resetBreadcrumbNodeFilterDataById: function (sId) {
      _resetBreadcrumbNodeFilterDataById(sId)
    },

    createBreadcrumbItem: function (sId, sLabel, sType, sHelpScreenId, oFilterContext, oExtraData, sBaseType, oPayloadData, functionToSet) {
      return _createBreadcrumbItem(sId, sLabel, sType, sHelpScreenId, oFilterContext, oExtraData, sBaseType, oPayloadData, functionToSet);
    },

    updateBreadcrumbPostData: function(sBreadCrumbId, oPostData) {
      _updateBreadcrumbPostData(sBreadCrumbId, oPostData)
    }

  };

})();

MicroEvent.mixin(BreadCrumbStore);

export default BreadCrumbStore;
