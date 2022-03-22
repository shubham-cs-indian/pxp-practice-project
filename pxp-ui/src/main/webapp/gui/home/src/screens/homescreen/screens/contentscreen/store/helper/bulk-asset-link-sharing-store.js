import oBulkAssetLinkSharingProps from '../model/bulk-asset-link-sharing-props';
import ContentUtils from '../helper/content-utils';
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import BulkAssetDownloadGridViewSkeleton from '../../tack/bulk-asset-download-grid-view-skeleton';
import ScreenModeUtils from './screen-mode-utils';
import BulkAssetDownloadDictionary from '../../tack/bulk-asset-download-dictionary';
import ContentScreenProps from "../model/content-screen-props";
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

const BulkAssetLinkSharingStore = (function () {

  var _triggerChange = function () {
    BulkAssetLinkSharingStore.trigger('bulkAssetLinkSharing-change');
  };

  let _setUpAssetLinkSharingGridView = function () {
    oBulkAssetLinkSharingProps.setGridViewVisualData({});
    //set skeleton
    let oBulkAssetDownloadGridViewSkeleton = BulkAssetDownloadGridViewSkeleton();
    oBulkAssetLinkSharingProps.setGridViewSkeleton(oBulkAssetDownloadGridViewSkeleton);
  };

  let _handleGridExpandToggled = function (sContentId) {
    let oGridVisualData = oBulkAssetLinkSharingProps.getGridViewVisualData();
    oGridVisualData[sContentId].isExpanded = !oGridVisualData[sContentId].isExpanded;
  };

  let _handleGridViewSelectButtonClicked = function (aSelectedContentIds, bSelectAllClicked) {
    let oGridSkeleton = oBulkAssetLinkSharingProps.getGridViewSkeleton();
    let aGridData = oBulkAssetLinkSharingProps.getGridViewData();
    let sContentId = aSelectedContentIds[0] || "";
    let oContent = CS.find(aGridData, {id: sContentId});
    if (bSelectAllClicked) {
      oGridSkeleton.selectedContentIds = aSelectedContentIds;
    } else {
      if (oContent && !CS.isEmpty(oContent.children)) {
        let aChildrenIds = CS.map(oContent.children, "id");
        if (CS.includes(oGridSkeleton.selectedContentIds, sContentId)) {
          CS.pull(oGridSkeleton.selectedContentIds, sContentId);
          oGridSkeleton.selectedContentIds = CS.difference(oGridSkeleton.selectedContentIds, aChildrenIds);
        } else {
          oGridSkeleton.selectedContentIds.push(sContentId);
          oGridSkeleton.selectedContentIds = CS.union(oGridSkeleton.selectedContentIds, aChildrenIds);
        }
      } else {
        let bAllTechnicalVariantsSelected = true;
        let aTechnicalVariantTypeId = oBulkAssetLinkSharingProps.getTechnicalVariantTypeIdsList();
        oGridSkeleton.selectedContentIds = CS.xor(oGridSkeleton.selectedContentIds, aSelectedContentIds);
        CS.forEach(aTechnicalVariantTypeId, function (oTechnicalVariant) {
          if (!CS.includes(oGridSkeleton.selectedContentIds, oTechnicalVariant.id)) {
            bAllTechnicalVariantsSelected = false;
          }
        });
        if (CS.isEmpty(aTechnicalVariantTypeId)) {
          bAllTechnicalVariantsSelected = false;
        }
        if (bAllTechnicalVariantsSelected && !CS.includes(oGridSkeleton.selectedContentIds, BulkAssetDownloadDictionary.VARIANT_ASSETS)) {
          oGridSkeleton.selectedContentIds.push(BulkAssetDownloadDictionary.VARIANT_ASSETS);
        }
        else if (!bAllTechnicalVariantsSelected) {
          CS.pull(oGridSkeleton.selectedContentIds, BulkAssetDownloadDictionary.VARIANT_ASSETS);
        }
      }
    }
  };


  let _preProcessAssetDataForGridView = function (aAssetList, sPath) {
    let oGridSkeleton = oBulkAssetLinkSharingProps.getGridViewSkeleton();
    let oGridViewVisualData = oBulkAssetLinkSharingProps.getGridViewVisualData();
    let aGridViewData = [];
    let bIsExpandable = false;
    CS.forEach(aAssetList, function (oAsset) {
      let oProcessedAsset = {};
      oProcessedAsset.id = oAsset.id;
      oProcessedAsset.children = [];

      if (oAsset.id === BulkAssetDownloadDictionary.VARIANT_ASSETS) {
        oGridViewVisualData[oAsset.id] = {
          isExpanded: false
        };
        bIsExpandable = true;

      }
      let sPathToAdd = sPath;
      if (!sPathToAdd && oAsset.parent) {
        sPathToAdd = oAsset.parent.id;
      }
      oProcessedAsset.pathToRoot = sPathToAdd ? sPathToAdd + ContentUtils.getSplitter() + oAsset.id : oAsset.id;
      //setting the properties
      oProcessedAsset.properties = {};
      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {

        oProcessedAsset.properties[oColumn.id] = {
          value: CS.getLabelOrCode(oAsset),
          isDisabled: true,
          isExpandable: bIsExpandable
        };
      });

      if (oAsset.id === BulkAssetDownloadDictionary.VARIANT_ASSETS) {
        let aAssetChildren = oBulkAssetLinkSharingProps.getTechnicalVariantTypeIdsList();
        let aDownloadAssetChildren = [];
        CS.forEach(aAssetChildren, function (oAsset) {
          if (oAsset.canDownload) {
            aDownloadAssetChildren.push(oAsset);
          }
        });
        oProcessedAsset.children = _preProcessAssetDataForGridView(aDownloadAssetChildren, oProcessedAsset.pathToRoot);
      }
      aGridViewData.push(oProcessedAsset);
    });

    return aGridViewData;
  };

  let _handleLinkSharingDialogClicked = function (oAssetDownloadData) {
    let aMasterAssetIds = oBulkAssetLinkSharingProps.getMasterAssetID();
    let aSelectedContentIds = oBulkAssetLinkSharingProps.getGridViewSkeleton().selectedContentIds;
    oAssetDownloadData.bIsTechnicalImage && aSelectedContentIds.push(BulkAssetDownloadDictionary.MAIN_ASSETS);
    let aTechnicalVariantTypeIds = [];
    let bMasterAssetDownload = false;
    let bTechnicalVariantDownload = false;
    if (!CS.isEmpty(aSelectedContentIds)) {
      CS.forEach(aSelectedContentIds, function (sContentId) {
            switch (sContentId) {
              case BulkAssetDownloadDictionary.MAIN_ASSETS:
                bMasterAssetDownload = true;
                break;
              case BulkAssetDownloadDictionary.VARIANT_ASSETS:
                bTechnicalVariantDownload = true;
                break;
              default:
                bTechnicalVariantDownload = true;
                aTechnicalVariantTypeIds.push(sContentId);
            }
          }
      );

      if (oAssetDownloadData.bIsShareSelected) {
        let oPostDataForShare = {
          "masterAssetIds": aMasterAssetIds,
          "technicalVariantTypeIds": aTechnicalVariantTypeIds,
          "masterAssetShare": bMasterAssetDownload,
        };
        CS.postRequest(getRequestMapping().ShareDownload, {}, oPostDataForShare, successBulkAssetsShareCallback, failureBulkAssetsShareCallback);
      }
    }
    else {
      alertify.error("Please Select Something");
    }
    _triggerChange();
  };

  let successBulkAssetsShareCallback = function () {
    oBulkAssetLinkSharingProps.reset();
    alertify.success(getTranslation().GENERATING_LINK_FOR_SHARING);
    _triggerChange();
  };

  let failureBulkAssetsShareCallback = function (oResponse) {
    oBulkAssetLinkSharingProps.reset();
    ContentUtils.failureCallback(oResponse, 'failureBulkAssetsShareCallback', getTranslation());
    let aExceptionDetails = oResponse.failure.exceptionDetails;
    CS.forEach(aExceptionDetails, function (oData) {
      let sKey = oData.key;
      if (sKey === "UserNotHaveSharePermission") {
        let oScreenProps = ContentScreenProps.screen;
        let oFunctionalPermission = oScreenProps.getFunctionalPermission();
        oFunctionalPermission.canShare = false;
      }
    });
    _triggerChange();
  };

  let _handleLinkSharingCancelButtonClicked = function () {
    oBulkAssetLinkSharingProps.setShowAssetLinkSharingDialog(false);
    oBulkAssetLinkSharingProps.reset();
  };


  //-------------------------------------------------  Public Methods  -----------------------------------------------//

  return {

    preProcessAssetDataForGridView: function (aAssetList) {
      return _preProcessAssetDataForGridView(aAssetList);
    },
    setUpAssetLinkSharingGridView: function () {
      _setUpAssetLinkSharingGridView();
      _triggerChange();
    },
    handleGridExpandToggled: function (sContentId) {
      _handleGridExpandToggled(sContentId);
      _triggerChange();
    },
    handleGridViewSelectButtonClicked: function (aSelectedContentIds, bSelectAllClicked) {
      _handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      _triggerChange();
    },
    handleAssetShareButtonClicked: function (oAssetDownloadData) {
      _handleLinkSharingDialogClicked(oAssetDownloadData);
    },
    handleLinkSharingCancelButtonClicked: function () {
      _handleLinkSharingCancelButtonClicked();
      _triggerChange();
    },

  }

})();

MicroEvent.mixin(BulkAssetLinkSharingStore);

export default BulkAssetLinkSharingStore;



