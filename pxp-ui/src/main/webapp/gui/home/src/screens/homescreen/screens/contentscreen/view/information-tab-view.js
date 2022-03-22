import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from './utils/view-utils';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { view as ListItemView } from '../../../../../viewlibraries/detailedlistview/detailed-list-item-view';
import ListItemModel from '../../../../../viewlibraries/detailedlistview/model/detailed-list-item-model';
import MarkerClassTypeDictionary from '../../../../../commonmodule/tack/marker-class-type-dictionary';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as DashboardTileView } from '../../../../../viewlibraries/dashboardview/dashboard-tile-view';
import { view as FileDragAndDropView } from '../../../../../viewlibraries/filedraganddropview/file-drag-and-drop-view';
import BillboardChart from 'react-billboardjs';
import DashboardTabDictionary from '../screens/dashboardscreen/tack/dashboard-tab-dictionary';
import { view as UploadAssetDialogView } from './upload-asset-dialog-view';

const oEvents = {
  HANDLE_INFORMATION_TAB_RULE_VIOLATION_ITEM_CLICKED: "handle_information_tab_rule_violation_item_clicked",
  HANDLE_INFORMATION_TAB_UPLOAD_DIALOG_CLOSE_CLICKED: "handle_information_tab_upload_dialog_close_clicked",
  HANDLE_INFORMATION_TAB_UPLOAD_DIALOG_OK_CLICKED: "handle_information_tab_upload_dialog_ok_clicked"
};

const oPropTypes = {
  dashboardTabData: ReactPropTypes.object,
  dataRuleFilterData: ReactPropTypes.object,
  dashboardRuleViolationTileData: ReactPropTypes.array,
  showBulkUploadDialog: ReactPropTypes.bool,
  assetClassList: ReactPropTypes.array,
  context: ReactPropTypes.string.isRequired,
  requestURLForAssetDefaultTypes: ReactPropTypes.string
};

// @CS.SafeComponent
class InformationTabView extends React.Component {
  state = {
    selectedKlassId: "",
  };

  static propTypes = oPropTypes;

  getImageSrcForThumbnail = (oContent) => {
    let aList = oContent.referencedAssets;
    let oImage = {};
    if (oContent.baseType === BaseTypeDictionary.assetBaseType) {
      oImage = oContent.assetInformation
    } else {
      oImage = CS.find(aList, {isDefault: true});
    }

    return ViewUtils.getImageSrcUrlFromImageObject(oImage);
  };

  getThumbnailClassNameByScreen = () => {
    let sScreenMode = this.props.screenMode;
    switch (sScreenMode) {

      default:
        return "articleThumb";
    }
  };

  handleInformationTabRuleViolationItemClicked = (sParentId, sSelectedRuleViolationId) => {
    let { context: sContext } = this.props;
    EventBus.dispatch(oEvents.HANDLE_INFORMATION_TAB_RULE_VIOLATION_ITEM_CLICKED, sParentId, sSelectedRuleViolationId, sContext);
  };

  handleButtonClick = (sButtonId) => {
    if (sButtonId === "ok") {
      this.handleOkButtonClicked();
    }
    else {
      this.closeUploadDialogDialog();
    }
  };

  closeUploadDialogDialog = () => {
    this.setState({
      selectedKlassId: "",
    });
    let { context: sContext } = this.props;
    EventBus.dispatch(oEvents.HANDLE_INFORMATION_TAB_UPLOAD_DIALOG_CLOSE_CLICKED, sContext);
  };

  handleOkButtonClicked = () => {
    this.setState({
      selectedKlassId: "",
    });
    let sSelectedKlassId = this.state.selectedKlassId;
    let { context: sContext } = this.props;
    EventBus.dispatch(oEvents.HANDLE_INFORMATION_TAB_UPLOAD_DIALOG_OK_CLICKED, "assetBulkUpload", sSelectedKlassId, sContext);
  };

  getViolationTilesView = () => {
    let aDashboardRuleViolationTileData = this.props.dashboardRuleViolationTileData;
    let aChildrenView = [];
    let _this = this;
    let { context: sContext } = _this.props;
    CS.forEach(aDashboardRuleViolationTileData, function (oChartData) {
      let sLabel = "";
      if (oChartData.id === "green") {
        switch (sContext) {
          case DashboardTabDictionary.INFORMATION_TAB:
            sLabel = getTranslation().CONTENTS_WITHOUT_ANY_VIOLATIONS
            break;

          case DashboardTabDictionary.DAM_TAB:
            sLabel = getTranslation().NO_VIOLATIONS
            break;
        }
      } else {
        switch (sContext) {
          case DashboardTabDictionary.INFORMATION_TAB:
            sLabel = ViewUtils.getDecodedTranslation(
              getTranslation().CONTENTS_WITH_LABEL_VIOLATIONS,
              { label: getTranslation()[oChartData.label] });
            break;

          case DashboardTabDictionary.DAM_TAB:
            sLabel = ViewUtils.getDecodedTranslation(
              getTranslation().LABEL_VIOLATIONS,
              { label: CS.upperCase(CS.getLabelOrCode(oChartData)) });
            break;
        }

      }
      let aBreadCrumbsData = [{
        id: -1,
        label: sLabel,
        levelId: 0,
        path: []
      }];
      let oStyle = {
        "marginTop": "20px"
      };
      let oChartInfo = oChartData.chartData;
      aChildrenView.push(
        <div className="dashboardRuleViolationItemsView" key={oChartData.id}
          onClick={_this.handleInformationTabRuleViolationItemClicked.bind(_this, "colorVoilation", oChartData.id)}>
          <DashboardTileView headerLabel={CS.getLabelOrCode(oChartData)}
            showKpiContentExplorerButton={false}
            hideButtons={true}
            showPreviousButton={false}
            showNextButton={false}
            breadCrumbData={aBreadCrumbsData}
            isLoading={oChartData.isLoading}
           >
            <BillboardChart data={oChartInfo.data} color={oChartInfo.color} size={oChartInfo.size} style={oStyle}
                            legend={{show: false}}/>
            <div className="contentCount">
              {oChartData.contentCount + "/" + oChartData.totalContentCount}
            </div>
          </DashboardTileView>
        </div>
      );
    });
    return (
      <div className="dashboardRuleViolationItemsContainer">
        {sContext === DashboardTabDictionary.INFORMATION_TAB && <div className="dashboardViewContainerLabel">{getTranslation().RULE_VIOLATIONS}</div>}
        {aChildrenView}
      </div>
    )
  };

  getDashboardContentView = (aContentList, sLabel, isLoading = false) => {
    let __props = this.props;
    let aDashboardContentThumbnails = [];
    let oFilterContext = {
      filterType: "",
      screenContext: sLabel
    };

    CS.forEach(aContentList, function (oContent) {

      let bIsActive = false,
        bIsSelected = false,
        sIdKey = "id";
      let sContentName = ViewUtils.getContentName(oContent);

      if (oContent.uuid) {
        sIdKey = "uuid";
      }
      let sContentId = oContent[sIdKey];
      let sClassName = "";
      let sClassIcon = "";
      let sKlassId = ViewUtils.getEntityClassType(oContent);
      let oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
      let sThumbnailImageSrc = "";
      let sPreviewImageSrc = "";
      if (oClass) {
        sClassName = CS.getLabelOrCode(oClass);
        sClassIcon = oClass.icon;
        sPreviewImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      }
      let aIcons = [];
      let sBranchNumber = "";
      if (oContent.variantOf != "-1") {
        aIcons.push("variantIcon");
      }
      if (oContent.branchOf != "-1") {
        aIcons.push("branchIcon");
        sBranchNumber = oContent.branchNo || "";
      }

      let oDataForThumbnail = this.getImageSrcForThumbnail(oContent);
      sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc || sPreviewImageSrc;
      let sThumbnailType = oDataForThumbnail.thumbnailType;
      let oImageAttribute = CS.find(oContent.attributes, { baseType: BaseTypeDictionary.imageAttributeInstanceBaseType });
      let sThumbnailClass = this.getThumbnailClassNameByScreen();
      let sClassNameForIcon = oContent.isFolder ? sThumbnailClass + ' folderThumb' : sThumbnailClass;
      let sVersionText = sBranchNumber ? ("Version " + sBranchNumber) : "";

      let aKlassIds = oContent.types;
      let aKlassLabel = [];
      let bIsGoldenArticle = false;
      CS.forEach(aKlassIds, function (sKlassId) {
        if (sKlassId === MarkerClassTypeDictionary.GOLDEN_RECORD) {
          bIsGoldenArticle = true;
        }
        let oKlass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
        aKlassLabel.push(CS.getLabelOrCode(oKlass));
      });

      let oThumbnailProperties = {
        entity: oContent,
        entityType: ViewUtils.getEntityType(oContent),
        isActive: bIsActive,
        isSelected: bIsSelected,
        thumbnailType: ViewUtils.getThumbnailType() || sThumbnailType,
        lastModifiedBy: "",
        isDirty: Boolean(oContent.isDirty) || Boolean(oContent.isCreated) || false,
        className: sClassNameForIcon,
        classIcon: sClassIcon,
        imageAttribute: oImageAttribute,
        versionText: sVersionText,
        icons: aIcons,
        searchFilterData: [],
        thumbnailMode: "basic",
        disableDelete: true,
        disableClone: true,
        disableView: false,
        disableCheck: true,
        disableDownload: true,
        disableAddToCollection: true,
        activeXRayPropertyGroup: __props.activeXRayPropertyGroup,
        xRayData: __props.xRayData,
        classNames: aKlassLabel,
        containerClass: oClass.natureType || "",
        isEntityRemovable: false,
        allowRightClick: false,
        contextMenuList: [],
        isToCutEntity: false,
        relevanceValue: oContent.relevance,
        showPreview: false,
        isGoldenArticle: bIsGoldenArticle,
        disableRestore: true,
        previewImageSrc: sPreviewImageSrc
      };

      let oThumbnailModel = new ListItemModel(
        sContentId,
        sContentName,
        sThumbnailImageSrc,
        oContent.tags,
        sThumbnailType,
        sClassName,
        oThumbnailProperties
      );

      let oThumbnailView = (
        <ListItemView model={oThumbnailModel} key={sContentId} currentZoom={2} filterContext={oFilterContext}/>);

      aDashboardContentThumbnails.push(oThumbnailView);

    }.bind(this));
    let aBreadCrumbsData = [{
      id: -1,
      label: sLabel,
      levelId: 0,
      path: []
    }];
    return (
      <DashboardTileView headerLabel={sLabel}
        showKpiContentExplorerButton={false}
        hideButtons={true}
        showPreviousButton={false}
        showNextButton={false}
        breadCrumbData={aBreadCrumbsData}
        isLoading={isLoading}
      >
        <div className="dashboardContentList">
          {aDashboardContentThumbnails}
        </div>
      </DashboardTileView>
    );
  };

  getInformationTabContentView = () => {
    let oDashboardTabData = this.props.dashboardTabData;
    let { context: sContext } = this.props;
    let oContent = null;
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        oContent = <React.Fragment>
          {this.getDashboardContentView(oDashboardTabData.lastModifiedContentsList, getTranslation().RECENTLY_MODIFIED_CONTENTS, oDashboardTabData.isLastModifiedListLoading)}
          {this.getDashboardContentView(oDashboardTabData.latestCreatedContentsList, getTranslation().RECENTLY_CREATED_CONTENTS, oDashboardTabData.isLatestCreatedListLoading)}
        </React.Fragment>
        break;

      case DashboardTabDictionary.DAM_TAB:
        oContent = <React.Fragment>
          {!CS.isEmpty(oDashboardTabData.lastModifiedAssetList) && this.getDashboardContentView(oDashboardTabData.lastModifiedAssetList, getTranslation().RECENTLY_MODIFIED)}
          {!CS.isEmpty(oDashboardTabData.latestCreatedAssetList) && this.getDashboardContentView(oDashboardTabData.latestCreatedAssetList, getTranslation().RECENTLY_CREATED)}
          {!CS.isEmpty(oDashboardTabData.expiredAssetList) && this.getDashboardContentView(oDashboardTabData.expiredAssetList, getTranslation().EXPIRED)}
          {!CS.isEmpty(oDashboardTabData.assetAboutToExpire) && this.getDashboardContentView(oDashboardTabData.assetAboutToExpire, getTranslation().ASSETS_ABOUT_TO_EXPIRE)}
          {!CS.isEmpty(oDashboardTabData.duplicateAssetsList) && this.getDashboardContentView(oDashboardTabData.duplicateAssetsList, getTranslation().DUPLICATE_ASSETS)}
        </React.Fragment>
        break;
    }
    return (
      <div className="dashboardContentListContainer">
        <div className="dashboardViewContainerLabel">{getTranslation().LATEST_CONTENTS}</div>
        {oContent}
      </div>
    );
  };

  getDragAndDropView = () => {
    let bIsUserReadonly = ViewUtils.getIsCurrentUserReadOnly();
    if (bIsUserReadonly) {
      return null;
    }
    else {
      return (
          <div className="dashboardFileDragAndDrop">
            <FileDragAndDropView
                id="dashboardBulkAssetUpload"
                context="dashboardBulkUpload">
              <div className="dashboardDragView">
                <div className="dashboardDragText">{getTranslation().DROP_ASSET_FILES_HERE}</div>
              </div>
            </FileDragAndDropView>
          </div>)
    }
  };

  render () {
    let oViolationTilesView = this.getViolationTilesView();
    let oCollectionsDialog = (<UploadAssetDialogView assetClassList={this.props.assetClassList}
                                                     context={this.props.context}
                                                     showBulkUploadDialog={this.props.showBulkUploadDialog}
                                                     requestURL={this.props.requestURLForAssetDefaultTypes}/>);
    return (
        <div className="dashboardTabViewContainer">
          {oViolationTilesView}
          {this.getInformationTabContentView()}
          {this.getDragAndDropView()}
          {oCollectionsDialog}
        </div>
    );
  }
}

export const view = InformationTabView;
export const events = oEvents;
