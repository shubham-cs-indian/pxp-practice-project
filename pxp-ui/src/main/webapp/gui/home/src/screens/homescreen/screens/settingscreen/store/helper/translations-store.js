import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import alertify from '../../../.././../../commonmodule/store/custom-alertify-store';
import oTranslationsProps from './../model/translations-config-view-props';
import SettingScreenProps from './../model/setting-screen-props';
import { SettingsRequestMapping as oSettingsRequestMapping, TranslationsRequestMapping as oTranslationsRequestMapping }
  from '../../tack/setting-screen-request-mapping';
import GlobalStore from '../../../../store/global-store.js';
import SettingUtils from './../helper/setting-utils';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import TagTypeConstants from './../../../../../../commonmodule/tack/tag-type-constants';
import TranslationsModuleList from './../../tack/translations-module-list';
import SettingScreenModuleList from '../.././../../../../commonmodule/tack/setting-screen-module-dictionary';
import AttributeTypeDisctionary from '../.././../../../../commonmodule/tack/attribute-type-dictionary-new';
import TranslationsConfigGridViewSkeleton from './../../tack/translations-config-grid-view-skeleton';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../../viewlibraries/tack/view-library-constants';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import assetTypes from '../../tack/coverflow-asset-type-list';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';

var TranslationsStore = (function () {

  var _triggerChange = function () {
    TranslationsStore.trigger('translations-changed');
  };

  var _setTranslationProps = function (sModuleId, bIsTreeItemClicked) {
    var aLanguages = [];
    let oLanguages = _getLanguages(sModuleId);
    CS.forEach(oLanguages, function (oLang) {
      aLanguages.push(oLang.code);
    });
    oTranslationsProps.setDisplayLanguages(aLanguages);
    oTranslationsProps.setAvailableLanguages(aLanguages);
    oTranslationsProps.reset();
    bIsTreeItemClicked && SettingScreenProps.screen.resetTranslationGridProps();
  };

  let _getLanguages = function(sModuleId) {
    return sModuleId === SettingScreenModuleList.TAG_VALUES? GlobalStore.getLanguageInfo().dataLanguages :
        GlobalStore.getLanguageInfo().userInterfaceLanguages;
  };

  var _fetchTranslations = function (sModuleId) {
    if (!sModuleId) {
      sModuleId = oTranslationsProps.getSelectedModule();
    }
    SettingScreenProps.screen.setGridViewContext(GridViewContexts.TRANSLATIONS);
    SettingScreenProps.screen.setGridViewVisualData({});
    _modifyGridViewSkeletonAccordingToLanguagesSelected(sModuleId);
    var oPaginationData = SettingScreenProps.screen.getGridViewPaginationData();
    var aLanguages = CS.clone(oTranslationsProps.getDisplayLanguages());
    let sGridViewSortLanguage = SettingScreenProps.screen.getGridViewSortLanguage();
    let sSearchLanguage = (sGridViewSortLanguage === "defaultLanguage" || sGridViewSortLanguage === "") ?
        GlobalStore.getLanguageInfo().defaultLanguage : sGridViewSortLanguage;
    aLanguages.push("#default#");
    var oReqData = {
      entityType: sModuleId == SettingScreenModuleList.CLASS || sModuleId == SettingScreenModuleList.TABS || sModuleId == SettingScreenModuleList.SMART_DOCUMENT ? oTranslationsProps.getSelectedChildModule() : sModuleId,
      languages: aLanguages,
      from: oPaginationData.from,
      size: oPaginationData.pageSize,
      searchLanguage: sSearchLanguage,
      searchText: SettingScreenProps.screen.getGridViewSearchText(),
      searchField: SettingScreenProps.screen.getGridViewSearchBy(),
      sortBy: SettingScreenProps.screen.getGridViewSortBy(),
      sortLanguage: SettingScreenProps.screen.getGridViewSortLanguage() == "defaultLanguage" ?
          GlobalStore.getLanguageInfo().defaultLanguage : SettingScreenProps.screen.getGridViewSortLanguage(),
      sortOrder: SettingScreenProps.screen.getGridViewSortOrder()
    };

    var sUrl = oTranslationsRequestMapping.GetTranslations;
    if (sModuleId == SettingScreenModuleList.TAG || sModuleId == SettingScreenModuleList.MASTER_LIST) {
      sUrl = oTranslationsRequestMapping.GetTranslationsForTag;
    } else if (sModuleId == SettingScreenModuleList.RELATIONSHIP) {
      sUrl = oTranslationsRequestMapping.GetTranslationsForRelationship;
    } else if (sModuleId == SettingScreenModuleList.STATIC_TRANSLATION) {
      sUrl = oTranslationsRequestMapping.GetStaticTranslations;
      delete oReqData.entityType;
    } else if(sModuleId === SettingScreenModuleList.TAG_VALUES){

      oReqData = {
        "searchColumn": "label",
        "searchText": SettingScreenProps.screen.getGridViewSearchText(),
        "entities": {
          "tags": {
            "from": oPaginationData.from,
            "size": oPaginationData.pageSize,
            "sortBy": "label",
            "sortOrder": SettingScreenProps.screen.getGridViewSortOrder(),
            "types": [],
            "typesToExclude": [TagTypeConstants.TAG_TYPE_BOOLEAN]
          }
        }
      };
      sUrl = oSettingsRequestMapping.GetConfigData;
    }

    if(oReqData.sortLanguage === ""){
        oReqData.sortLanguage = oReqData.searchLanguage;
    }
    if(oReqData.sortOrder === ""){
      oReqData.sortOrder = "asc";
    }
    SettingUtils.csPostRequest(sUrl, {}, oReqData, successFetchTranslationsCallback.bind(this, oReqData, sModuleId), failureFetchTranslationsCallback).then();
  };

  var _getTranslationsScreenLockStatus = function () {
    return oTranslationsProps.getTranslationsScreenLockedStatus();
  };

  var _modifyGridViewSkeletonAccordingToLanguagesSelected = function (sModuleId) {
    var oTranslationsConfigGridViewSkeleton = new TranslationsConfigGridViewSkeleton();
    var aSelectedLanguages = oTranslationsProps.getDisplayLanguages();
    var sDefaultLanguage = GlobalStore.getLanguageInfo().defaultLanguage;
    let oLanguages = _getLanguages(sModuleId);
    let oLanguageMap = CS.keyBy(oLanguages, 'code');

    CS.forEach(aSelectedLanguages, function (sLanguage) {
      /*if (sLanguage == sDefaultLanguage) {
        var oColumn = CS.find(oTranslationsConfigGridViewSkeleton.scrollableColumns, {id: "defaultLanguage"});
        if(oColumn){
        oColumn.label = getTranslation()[sDefaultLanguage] + " [" + getTranslation().DEFAULT_LANGUAGE + "]";
        }
        return;
      }*/
      if(sLanguage != sDefaultLanguage) {
        let oLanguageDetails = oLanguageMap[sLanguage];
        var oColumn = {
          id: sLanguage,
          label: CS.getLabelOrCode(oLanguageDetails),
          type: oGridViewPropertyTypes.TEXT,
          width: 300,
          isSortable: true
        };
        if(sModuleId === SettingScreenModuleList.TAG_VALUES){
          oColumn.isSortable = false;
        }
        oTranslationsConfigGridViewSkeleton.scrollableColumns.push(oColumn)
      }
    });
    CS.forEach(oTranslationsConfigGridViewSkeleton.fixedColumns, function (oColumn) {
      switch (oColumn.id) {
        case "defaultLanguage":
          let oLanguageDetails = oLanguageMap[sDefaultLanguage];
          oColumn.label = oLanguageDetails.label +" (" + oColumn.label + ")" ;
          break;
      }
    });

    SettingScreenProps.screen.setGridViewSkeleton(oTranslationsConfigGridViewSkeleton);
  };

  var _handleTranslationsModuleListItemClicked = function (sModuleId) {
    SettingScreenProps.screen.setGridViewSearchText('');
    _fetchTranslations(sModuleId);
    oTranslationsProps.reset();
  };

  var _handleTranslationPropertyChanged = function (sSelectedProperty) {
    oTranslationsProps.setSelectedProperty(sSelectedProperty);
    SettingScreenProps.screen.setGridViewSortBy(sSelectedProperty);
    //SettingScreenProps.screen.setGridViewSearchBy(sSelectedProperty);
    oTranslationsProps.setSortByField("label");
    if (SettingScreenProps.screen.getGridViewSearchText() != '') {
      SettingScreenProps.screen.setGridViewSearchText('');
      _fetchTranslations();
    } else {
      var oGridData = oTranslationsProps.getTranslationsGridData();
      var sModuleId = oTranslationsProps.getSelectedModule();
      var sSelectedClassModule = oTranslationsProps.getSelectedChildModule();
      var aProcessedGridViewData = _preProcessTranslationDataForGridView(oGridData, sModuleId, sSelectedClassModule);
      SettingScreenProps.screen.setGridViewData(aProcessedGridViewData);
      _triggerChange();
    }
  };

  var _handleTranslationLanguagesChanged = function (aLanguages) {
    var sDefaultLanguage = GlobalStore.getLanguageInfo().defaultLanguage;
    aLanguages.push(sDefaultLanguage);
    oTranslationsProps.setDisplayLanguages(aLanguages);
    let aAvailableLanguages = CS.clone(aLanguages);
    let sModuleId = oTranslationsProps.getSelectedModule();
    let oLanguages = _getLanguages(sModuleId);
    CS.forEach(oLanguages, function (oLanguage) {
      if (aAvailableLanguages.indexOf(oLanguage.code) == -1) {
        aAvailableLanguages.push(oLanguage.code);
      }
    });
    oTranslationsProps.setAvailableLanguages(aAvailableLanguages);
    _fetchTranslations();
  };

  var successFetchTranslationsCallback = function (oReqData, sModuleId, oResponse) {
    var oResponseData = oResponse.success;
    let iCount = oResponseData.count;
    if(sModuleId === SettingScreenModuleList.TAG_VALUES){
      oResponseData.data = oResponseData.tags.list;
      iCount = oResponseData.tags.totalCount;
    }
    oTranslationsProps.setTranslationsGridData(oResponseData.data);
    var iNestedCount = SettingUtils.calculateNestedContentCount(oResponseData.data);
    SettingScreenProps.screen.setGridViewTotalNestedItems(iNestedCount);
    var aProcessedGridViewData = _preProcessTranslationDataForGridView(oResponseData.data, sModuleId, oReqData.entityType);
    SettingScreenProps.screen.setGridViewData(aProcessedGridViewData);
    SettingScreenProps.screen.setGridViewTotalItems(iCount);
    SettingScreenProps.screen.setIsGridDataDirty(false);
    oTranslationsProps.setSelectedModule(sModuleId);
    SettingScreenProps.screen.setGridViewSortBy(oTranslationsProps.getSelectedProperty());
    //SettingScreenProps.screen.setGridViewSearchBy(oTranslationsProps.getSelectedProperty());
    _triggerChange();
  };

  var failureFetchTranslationsCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTranslationsCallback", getTranslation());
  };

  var _preProcessTranslationDataForGridView = function (aList, sModuleId, sChildModuleId, sPath) {
    var oGridSkeleton = SettingScreenProps.screen.getGridViewSkeleton();
    var oGridViewVisualData = SettingScreenProps.screen.getGridViewVisualData();
    var aGridViewData = [];
    var sSelectedProperty = oTranslationsProps.getSelectedProperty();
    var sDefaultLanguage = GlobalStore.getLanguageInfo().defaultLanguage;
    let oTranslationsModuleList = new TranslationsModuleList();
    _modifyTranslationsSkeletonBasedOnSelectedProperty(oGridSkeleton, sSelectedProperty);
    let oLabelSkeletonData = CS.find(oGridSkeleton.fixedColumns, {id: "label"});
    oLabelSkeletonData.hideColumn = !(sSelectedProperty == "description" || sSelectedProperty == "placeholder" || sSelectedProperty == "tooltip");
    CS.forEach(aList, function (oItem) {
      var oProcessedGridItem = {};
      oProcessedGridItem.id = oItem.id;
      oProcessedGridItem.code = oItem.code;
      if (oGridViewVisualData[oItem.id]) {

      } else {
        oGridViewVisualData[oItem.id] = {
          isExpanded: false
        }
      }
      let bDisableRow = sSelectedProperty == "placeholder" && (oItem.type === AttributeTypeDisctionary.CALCULATED || oItem.type === AttributeTypeDisctionary.CONCATENATED);
      var sPathToAdd = sPath;
      if (!sPathToAdd && oItem.parent) {
        sPathToAdd = oItem.parent.id;
      }
      oProcessedGridItem.pathToRoot = sPathToAdd ? sPathToAdd + SettingUtils.getSplitter() + oItem.id : oItem.id;

      oProcessedGridItem.children = [];
      oProcessedGridItem.actionItemsToShow = [];
      oProcessedGridItem.properties = {};
      var isExpandable = (sModuleId == SettingScreenModuleList.TAG_VALUES && oItem.tagType != TagTypeConstants.TAG_TYPE_BOOLEAN && !sPath);

      CS.forEach(oGridSkeleton.fixedColumns, function (oColumn) {
        switch (oColumn.id) {
          case "code":
            if (sModuleId == SettingScreenModuleList.STATIC_TRANSLATION) {
              if (oItem.hasOwnProperty("id")) {
                oProcessedGridItem.properties[oColumn.id] = {
                  value: oItem.id,
                  isDisabled: true,
                  bIsMultiLine: false,
                };
              }

              oColumn.label = "KEY";
            } else {
              if (oItem.hasOwnProperty(oColumn.id)) {
                oProcessedGridItem.properties[oColumn.id] = {
                  value: oItem[oColumn.id],
                  isDisabled: true,
                  bIsMultiLine: true,
                  isExpandable: isExpandable
                };
              }
              if (sModuleId == SettingScreenModuleList.CLASS) {
                oColumn.label = CS.find(oTranslationsModuleList.ClassModules, {id: "translations"+SettingUtils.getSplitter()+sChildModuleId}).label;
              } else if (sModuleId == SettingScreenModuleList.TABS) {
                oColumn.label = CS.find(oTranslationsModuleList.TabsModules, {id: "translations"+SettingUtils.getSplitter()+sChildModuleId}).label;
              } else {
                oColumn.label = CS.find(oTranslationsModuleList.SettingScreenModules, {id: "translations"+SettingUtils.getSplitter()+sModuleId}).label;
              }
            }
            break;
          case "languageKey" :
            if(sSelectedProperty === "label" || sSelectedProperty === "side1Label" || sSelectedProperty === "side2Label") {
              oProcessedGridItem.properties[oColumn.id] = {
                value: oItem.code,
                isDisabled: true,
                bIsMultiLine: false,
                isExpandable: isExpandable
              };
            }
            break;
          case "#default#" :
            //bIsDisabled is used for TagValues handling.
            if(sSelectedProperty === "label" || sSelectedProperty === "side1Label" || sSelectedProperty === "side2Label") {
              let bIsDisabled = oItem.hasOwnProperty('translations');
              let oDefaultTranslationData = bIsDisabled ? oItem.translations["#default#"] : oItem;
              oProcessedGridItem.properties[oColumn.id] = {
                value: bIsDisabled ? oDefaultTranslationData[sSelectedProperty] : "",
                isDisabled: !bIsDisabled,
                bIsMultiLine: false,
                isExpandable: bIsDisabled ? isExpandable : false
              };
            }
            break;
          case "defaultLanguage":
            if (sModuleId === SettingScreenModuleList.TAG_VALUES){
              oProcessedGridItem.properties[oColumn.id] = {
                value: oItem[sSelectedProperty] || getLabelOrCode(oItem),
                isExpandable: isExpandable,
                isDisabled: true
              };
            }
            break;
        }
      });

      CS.forEach(oItem.translations, function (oTranslations, sLanguage) {
        if (sLanguage == sDefaultLanguage) {
          oProcessedGridItem.properties['defaultLanguage'] = {
            value: oTranslations[sSelectedProperty],
            placeholder: oTranslations['placeholder'] || !oTranslations['label'] ? getLabelOrCode(oProcessedGridItem) : null,
            bIsMultiLine: false,
            showTooltip: true,
            isDisabled: bDisableRow
          };
          if(sSelectedProperty == "description" || sSelectedProperty == "placeholder" || sSelectedProperty == "tooltip") {
            oProcessedGridItem.properties['label'] = {
              value: oTranslations['label'],
              placeholder: oTranslations['placeholder'] || !oTranslations['label'] ? getLabelOrCode(oProcessedGridItem) : null,
              bIsMultiLine: true,
              isDisabled: bDisableRow
            };
          }
          oProcessedGridItem.properties[sLanguage] = {
            value: oTranslations[sSelectedProperty],
            bIsMultiLine: true,
            isDisabled: bDisableRow
          };
        } else if (oTranslations.hasOwnProperty(sSelectedProperty) && sLanguage !== "#default#") {
          oProcessedGridItem.properties[sLanguage] = {
            value: oTranslations[sSelectedProperty],
            bIsMultiLine: true,
            isDisabled: bDisableRow
          };
        }
      });

      if (!CS.isEmpty(oItem.children)) {
        oProcessedGridItem.children = _preProcessTranslationDataForGridView(oItem.children, sModuleId, sChildModuleId, oProcessedGridItem.pathToRoot);
      }

      aGridViewData.push(oProcessedGridItem);
    });

    return aGridViewData;
  };

  let _modifyTranslationsSkeletonBasedOnSelectedProperty = function (oGridViewSkeleton, sSelectedProperty) {
    let oFoundKeyColumn = CS.find(oGridViewSkeleton.fixedColumns, {id: "languageKey"});
    if (sSelectedProperty === "label" || sSelectedProperty === "side1Label" || sSelectedProperty === "side2Label") {
      if (CS.isEmpty(oFoundKeyColumn)) {
        var oTranslationsConfigGridViewSkeleton = new TranslationsConfigGridViewSkeleton();
        oGridViewSkeleton.fixedColumns = oTranslationsConfigGridViewSkeleton.fixedColumns;
      }
    } else {
      CS.remove(oGridViewSkeleton.fixedColumns, function (oColumnData) {
        if (oColumnData.id == "languageKey" || oColumnData.id == "#default#") {
          return oColumnData.id;
        }
      });
    }
  };

  let getLabelOrCode = function (oItem) {
    return oItem.code ? "["+ oItem.code +"]" : null;
  };

  var _safeToSave = function (oReqData, sDefaultLanguage, sSelectedProperty) {
    var bSafeToSave = true;
    let bIsDefaultLanguageOrTranslationEmpty = false;

    if (CS.isEmpty(oReqData.data)) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return false;
    }

    CS.forEach(oReqData.data, function (oTranslation) {
      let oTranslations = oTranslation.translations;
      let oDefaultLangModifiedData = oTranslations[sDefaultLanguage];
      if (CS.isEmpty(oDefaultLangModifiedData[sSelectedProperty]) ||
          CS.isEmpty(oDefaultLangModifiedData[sSelectedProperty].trim())) {
        bIsDefaultLanguageOrTranslationEmpty = true;
        return false;
      }
      if (oTranslations.hasOwnProperty("#default#")) {
        let oDefaultKeyModifiedData = oTranslations["#default#"];
        let sLabel = oDefaultKeyModifiedData[sSelectedProperty];
        if (CS.isEmpty(sLabel) || CS.isEmpty(sLabel.trim())) {
          bIsDefaultLanguageOrTranslationEmpty = true;
          return false;
        }
      }
    });

    if(bIsDefaultLanguageOrTranslationEmpty){
      alertify.message(getTranslation().DEFAULT_LANGUAGE_OR_DEFAULT_TRANSLATION_SHOULD_NOT_EMPTY);
      return false;
    }
    return bSafeToSave;
  };

  var _recursivelyProcessTranslations = function (aGridData, sDefaultLanguage, sSelectedProperty, aTranslationsListToSave) {
    CS.forEach(aGridData, function (oProcessedGridItem) {
      if (oProcessedGridItem.isDirty) {
        var oTranslation = {
          id: oProcessedGridItem.id,
          translations: {}
        };

        CS.forEach(oProcessedGridItem.properties, function (oData, sProperty) {
          if (sProperty == "label" || sProperty == "languageKey") {
            return;
          } else {
            var oTranslationFields = {
              label: null,
            };
            if (oTranslationsProps.getSelectedModule() == SettingScreenModuleList.RELATIONSHIP) {
              oTranslationFields.side1Label = null;
              oTranslationFields.side2Label = null;
            } else if (oTranslationsProps.getSelectedModule() != SettingScreenModuleList.STATIC_TRANSLATION) {
              oTranslationFields.placeHolder = null;
              oTranslationFields.description = null;
              oTranslationFields.tooltip = null;
            }
            if (sProperty == "defaultLanguage") {
              oTranslation.translations[sDefaultLanguage] = oTranslationFields;
              oTranslation.translations[sDefaultLanguage][sSelectedProperty] = oData.value;
            } else if(sProperty != sDefaultLanguage) {
              oTranslation.translations[sProperty] = oTranslationFields;
              oTranslation.translations[sProperty][sSelectedProperty] = oData.value;
            }
          }
        });

        aTranslationsListToSave.push(oTranslation);
      }
      if (!CS.isEmpty(oProcessedGridItem.children)) {
        _recursivelyProcessTranslations(oProcessedGridItem.children, sDefaultLanguage, sSelectedProperty, aTranslationsListToSave);
      }
    });
  };

  let _getEntityType = function (sModuleId) {
    if(sModuleId === SettingScreenModuleList.CLASS || sModuleId === SettingScreenModuleList.TABS || sModuleId === SettingScreenModuleList.SMART_DOCUMENT){
      return oTranslationsProps.getSelectedChildModule();
    } else if(sModuleId === SettingScreenModuleList.TAG_VALUES) {
      return SettingScreenModuleList.TAG;
    }
    return sModuleId;
  };

  var _postProcessTranslationsAndSave = function (oCallbackData) {
    var aGridData = SettingScreenProps.screen.getGridViewData();
    var sSelectedProperty = oTranslationsProps.getSelectedProperty();
    var sSelectedModuleId = oTranslationsProps.getSelectedModule();
    var sDefaultLanguage = GlobalStore.getLanguageInfo().defaultLanguage;
    var aLanguages = CS.clone(oTranslationsProps.getDisplayLanguages());
    var aTranslationsListToSave = [];
    if (sSelectedProperty === "label" || sSelectedProperty === "side1Label" || sSelectedProperty === "side2Label") {
      aLanguages.push("#default#");
    }

    _recursivelyProcessTranslations(aGridData, sDefaultLanguage, sSelectedProperty, aTranslationsListToSave);
    var oReqData = {
      entityType: _getEntityType(sSelectedModuleId),
      languages: aLanguages,
      data: aTranslationsListToSave
    };
    _saveTranslationsInBulk(oReqData, sSelectedModuleId, sSelectedProperty, oCallbackData, sDefaultLanguage);
  };

  var _saveTranslationsInBulk = function (oReqData, sSelectedModuleId, sSelectedProperty, oCallbackData, sDefaultLanguage) {
    if (_safeToSave(oReqData, sDefaultLanguage, sSelectedProperty)) {

      var sUrl = oTranslationsRequestMapping.SaveTranslations;
      if (sSelectedModuleId == SettingScreenModuleList.STATIC_TRANSLATION) {
        sUrl = oTranslationsRequestMapping.SaveStaticTranslations;
        //delete oReqData.entityType;
      } else if (sSelectedModuleId == SettingScreenModuleList.RELATIONSHIP) {
        sUrl = oTranslationsRequestMapping.SaveRelationshipTranslations;
      }

      SettingUtils.csPostRequest(sUrl, {}, oReqData, successSaveTranslationsInBulk.bind(this, oCallbackData, oReqData, sSelectedModuleId), failureSaveTranslationsInBulk);
    }
  };

  var successSaveTranslationsInBulk = function (oCallbackData, oReqData, sSelectedModuleId, oResponse) {
    let aTranslationsList = oResponse.success;
    var aGridData = oTranslationsProps.getTranslationsGridData();
    CS.forEach(aTranslationsList, function (oTranslation) {
      _updateTranslationsRecursively(aGridData, oTranslation);
    });
    var aProcessedGridViewData = _preProcessTranslationDataForGridView(aGridData, sSelectedModuleId, oReqData.entityType);
    SettingScreenProps.screen.setGridViewData(aProcessedGridViewData);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TRANSLATIONS}));
    SettingScreenProps.screen.setIsGridDataDirty(false);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var failureSaveTranslationsInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveTranslationsInBulk", getTranslation());
  };

  var _updateTranslationsRecursively = function (aGridDataList, oTranslation) {
    CS.forEach(aGridDataList, function (oData) {

      if (oData.id == oTranslation.id) {
        CS.assign(oData, oTranslation);
      } else {
        if (oData.children) {
         _updateTranslationsRecursively(oData.children, oTranslation);
        }
      }
    });
  };

  var _discardTranslationsGridViewChanges = function (oCallbackData) {
    var aTranslationsGridData = oTranslationsProps.getTranslationsGridData();
    var aGridViewData = SettingScreenProps.screen.getGridViewData(); //saved Processed data
    var sSelectedModule = oTranslationsProps.getSelectedModule();
    var sSelectedClassModule = oTranslationsProps.getSelectedChildModule();
    let bShowDiscardMessage = _recursivelyDiscardDirtyItems(aGridViewData, aTranslationsGridData, sSelectedModule, sSelectedClassModule);

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    SettingScreenProps.screen.setIsGridDataDirty(false);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    _triggerChange();
  };

  var _recursivelyDiscardDirtyItems = function (aGridViewData, aTranslationsGridData, sSelectedModule, sSelectedClassModule) {
    var bShowDiscardMessage = false;
    CS.forEach(aGridViewData, function (oOldProcessedItem, iIndex) {
      var oItem = CS.find(aTranslationsGridData, {id: oOldProcessedItem.id});
      if (oOldProcessedItem.isDirty) {
        let sPathToRoot = "";
        if (oOldProcessedItem.pathToRoot && oOldProcessedItem.pathToRoot !== oItem.id) {
          sPathToRoot = oOldProcessedItem.pathToRoot;
          let aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
          aPath.splice(-1);
          sPathToRoot = aPath.join(SettingUtils.getSplitter());
        }
        aGridViewData[iIndex] = _preProcessTranslationDataForGridView([oItem], sSelectedModule, sSelectedClassModule, sPathToRoot)[0];
        bShowDiscardMessage = true;
      }
      if (!CS.isEmpty(oOldProcessedItem.children)) {
        bShowDiscardMessage = bShowDiscardMessage || _recursivelyDiscardDirtyItems(oOldProcessedItem.children, oItem.children, sSelectedModule, sSelectedClassModule);
      }
    });
    return bShowDiscardMessage;
  };

  var _handleSelectedChildModuleChanged = function (sModuleId) {
    SettingScreenProps.screen.setGridViewSearchText('');
    var sSplitter = SettingUtils.getSplitter();
    oTranslationsProps.setSelectedChildModule(sModuleId.split(sSplitter)[1]);
    let sModule = SettingScreenModuleList.CLASS;
    if (sModuleId.split(sSplitter)[1] == SettingScreenModuleList.CONTENT_TAB || sModuleId.split(sSplitter)[1] == SettingScreenModuleList.DASHBOARD_TAB) {
      sModule = SettingScreenModuleList.TABS;
    }
    if (sModuleId.split(sSplitter)[1] == SettingScreenModuleList.SMART_DOCUMENT_TEMPLATE || sModuleId.split(sSplitter)[1] == SettingScreenModuleList.SMART_DOCUMENT_PRESET) {
      sModule = SettingScreenModuleList.SMART_DOCUMENT;
    }
    _fetchTranslations(sModule);
  };

  var _handleGridParentExpandToggled = function (sContentId) {
    var oGridData = oTranslationsProps.getTranslationsGridData();
    var oContent = CS.find(oGridData, {id: sContentId});
    var oGridViewVisualData = SettingScreenProps.screen.getGridViewVisualData();
    if (oGridViewVisualData[sContentId].isExpanded || (oContent.children && oContent.children.length)) {
      oGridViewVisualData[sContentId].isExpanded = !oGridViewVisualData[sContentId].isExpanded;
      _triggerChange();
    } else {
      var sSelectedModule = oTranslationsProps.getSelectedModule();
      _fetchChildTranslations(sSelectedModule, sContentId);
    }
  };

  var _fetchChildTranslations = function (sModuleId, sContentId) {
    // ToDo: abcd
    var oPaginationData = SettingScreenProps.screen.getGridViewPaginationData();
    var aLanguages = CS.clone(oTranslationsProps.getDisplayLanguages());
    aLanguages.push("#default#");
    var oReqData = {
      entityType: _getEntityType(sModuleId),
      languages: aLanguages,
      from: 0,
      searchLanguage: GlobalStore.getLanguageInfo().defaultLanguage,
      searchText: SettingScreenProps.screen.getGridViewSearchText(),
      searchField: SettingScreenProps.screen.getGridViewSearchBy(),
      sortBy: SettingScreenProps.screen.getGridViewSortBy(),
      sortLanguage: SettingScreenProps.screen.getGridViewSortLanguage() == "defaultLanguage" ?
          GlobalStore.getLanguageInfo().defaultLanguage : SettingScreenProps.screen.getGridViewSortLanguage(),
      sortOrder: SettingScreenProps.screen.getGridViewSortOrder(),
      parentId: sContentId
    };

    SettingUtils.csPostRequest(oTranslationsRequestMapping.GetTranslationsForTag, {}, oReqData,
        successFetchChildTranslationsCallback.bind(this, oReqData, sModuleId, sContentId), failureFetchChildTranslationsCallback);
  };

  var successFetchChildTranslationsCallback = function (oReqData, sModuleId, sContentId, oResponse) {
    var oSuccessResponse = oResponse.success;
    var oGridData = oTranslationsProps.getTranslationsGridData();
    var oContent = CS.find(oGridData, {id: sContentId});
    let aChildTags = oSuccessResponse.data;
    oContent.children = aChildTags;
    var iNestedCount = SettingUtils.calculateNestedContentCount(oGridData);
    SettingScreenProps.screen.setGridViewTotalNestedItems(iNestedCount);
    let aOriginalGridData = SettingScreenProps.screen.getGridViewData();
    let oProcessedContent = CS.find(aOriginalGridData, {id: sContentId});
    oProcessedContent.children = _preProcessTranslationDataForGridView(aChildTags, sModuleId, oReqData.entityType, oProcessedContent.pathToRoot);
    var oGridViewVisualData = SettingScreenProps.screen.getGridViewVisualData();
    oGridViewVisualData[sContentId].isExpanded = !oGridViewVisualData[sContentId].isExpanded;
    _triggerChange();
  };

  var failureFetchChildTranslationsCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchChildTranslationsCallback", getTranslation());
  };

  var _handleColumnHeaderClicked = function (sColumnId) {
    var sCurrentSortByColumn = oTranslationsProps.getSortByField();
    oTranslationsProps.setSortByField(sColumnId);
    if (sColumnId == sCurrentSortByColumn) {
      (SettingScreenProps.screen.getGridViewSortOrder() == 'asc') ?
      SettingScreenProps.screen.setGridViewSortOrder('desc') : SettingScreenProps.screen.setGridViewSortOrder('asc');
    } else if (sColumnId == "label" || sColumnId == "code")  {
      SettingScreenProps.screen.setGridViewSortBy(sColumnId);
      SettingScreenProps.screen.setGridViewSearchBy(sColumnId);
      SettingScreenProps.screen.setGridViewSortLanguage("defaultLanguage");
      SettingScreenProps.screen.setGridViewSortOrder('asc')
    } else {
      SettingScreenProps.screen.setGridViewSortLanguage(sColumnId);
      SettingScreenProps.screen.setGridViewSortOrder('asc');
      SettingScreenProps.screen.setGridViewSearchBy(oTranslationsProps.getSelectedProperty());
    }
    var sModuleId = oTranslationsProps.getSelectedModule();
    _fetchTranslations(sModuleId);
  };

  var _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data, successUploadFileImport.bind(this, oCallback),
        failureUploadFileImport, false);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var _handleTranslationFileUploaded = function (aFiles,oImportExcel) {
    var bIsAnyValidImage = false;
    var bIsAnyInvalidImage = false;
    if (aFiles.length) {

      var iFilesInProcessCount = 0;
      var count = 0;

      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {
        if (_getIsValidFileTypes(file)) {
          bIsAnyValidImage = true;
          var reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {
            return function (event) {
              count += 1;
              var filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              data.append("entityType", oImportExcel.entityType);
              oImportExcel.data = data;
              uploadFileImport(oImportExcel, {});
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });

    }
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
  };

  var _handleExportTranslation = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {}, oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
  };

  return {

    fetchTranslations: function (sModuleId) {
      _fetchTranslations(sModuleId);
    },

    getTranslationsScreenLockStatus: function () {
      return _getTranslationsScreenLockStatus();
    },

    handleTranslationsModuleListItemClicked: function (sModuleId) {
      _handleTranslationsModuleListItemClicked(sModuleId);
    },

    handleTranslationPropertyChanged: function (sSelectedProperty) {
      _handleTranslationPropertyChanged(sSelectedProperty);
    },

    handleTranslationLanguagesChanged: function (aLanguages) {
      _handleTranslationLanguagesChanged(aLanguages);
    },

    handleSelectedChildModuleChanged: function (sModuleId) {
      _handleSelectedChildModuleChanged(sModuleId);
    },

    postProcessTranslationsAndSave: function (oCallbackData) {
      _postProcessTranslationsAndSave(oCallbackData);
    },

    discardTranslationsGridViewChanges: function (oCallbackData) {
      _discardTranslationsGridViewChanges(oCallbackData);
    },

    setTranslationProps: function (sModuleId, bIsTreeItemClicked) {
      _setTranslationProps(sModuleId, bIsTreeItemClicked);
    },

    handleColumnHeaderClicked: function (sColumnId, sContext) {
      _handleColumnHeaderClicked(sColumnId, sContext);
    },

    handleGridParentExpandToggled: function (sContentId) {
      _handleGridParentExpandToggled(sContentId);
    },

    handleTranslationFileUploaded: function (aFiles,oImportExcel) {
      _handleTranslationFileUploaded(aFiles,oImportExcel);
    },

    handleExportTranslation: function (oSelectiveExportDetails) {
      _handleExportTranslation(oSelectiveExportDetails);
    },
  }


})();

MicroEvent.mixin(TranslationsStore);

export default TranslationsStore;
