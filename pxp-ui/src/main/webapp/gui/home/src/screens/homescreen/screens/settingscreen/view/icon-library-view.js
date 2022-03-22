import React from "react";
import ReactPropTypes from "prop-types";
import CS from "../../../../../libraries/cs";
import {view as ConfigHeaderView} from "../../../../../viewlibraries/configheaderview/config-header-view";
import {view as ImageSimpleView} from "../../../../../viewlibraries/imagesimpleview/image-simple-view";
import {
  getTranslations as getTranslation,
  getTranslations as oTranslations
} from "../../../../../commonmodule/store/helper/translation-manager";
import TooltipView from "../../../../../viewlibraries/tooltipview/tooltip-view";
import ViewLibraryUtils from "../../../../../viewlibraries/utils/view-library-utils";
import copy from "copy-to-clipboard";
import alertify from "../../../../../commonmodule/store/custom-alertify-store";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {view as PaginationView} from "../../../../../viewlibraries/paginationview/pagination-view";
import Constants from "../../../../../commonmodule/tack/constants";
import {view as IconLibraryEditCodeDialogView} from './icon-library-edit-code-dialog-view';
import {view as FileDragAndDropView} from "../../../../../viewlibraries/filedraganddropview/file-drag-and-drop-view";
import CoverflowAssetTypeList from "../../contentscreen/tack/coverflow-asset-type-list";
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";
import {view as IconEditView} from "./icon-edit-view";

const oEvents = {
  ICON_ELEMENT_CHECKBOX_CLICKED: "icon_element_checkbox_clicked",
  ICON_LIBRARY_PAGINATION_CHANGED: "icon_library_pagination_changed",
  CONFIG_HEADER_VIEW_ACTION_BUTTON_CLICKED: "config_header_view_action_button_clicked",
  ICON_ELEMENT_ACTION_BUTTON_CLICKED: "icon_library_element_action_item_clicked",
}

const oPropTypes = {
  isShowSaveDiscardButtons: ReactPropTypes.bool,
  headerActionGroup: ReactPropTypes.array,
  iconLibraryData: ReactPropTypes.object,
  hideActionsOnElements: ReactPropTypes.bool,
  onSelectHandler: ReactPropTypes.func,
  dragAndDropViewRequired: ReactPropTypes.bool,
  isCopyCodeRequired: ReactPropTypes.bool,
  nothingToDisplayMessage: ReactPropTypes.string,
  isUsageDeleteDialog: ReactPropTypes.bool,
  activeIcons: ReactPropTypes.array,
  dialogExtraData: ReactPropTypes.object,
  isDragging: ReactPropTypes.bool,
};

class IconLibraryView extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      selectedId: ""
    };
  }

  static propTypes = oPropTypes;

  componentDidUpdate = () => {
    this.refs.onboardingFileUpload && (this.refs.onboardingFileUpload.value = null);
  };

  handleActionItemClicked = (sButtonId, sId, event) => {
    if (CS.isNotEmpty(event.target.files)) {
      EventBus.dispatch(oEvents.ICON_ELEMENT_ACTION_BUTTON_CLICKED, sButtonId, sId, event.target.files);
    }
    else {
      EventBus.dispatch(oEvents.ICON_ELEMENT_ACTION_BUTTON_CLICKED, sButtonId, sId);
    }

  };

  handleCopyToClipboardClick = (sValue) => {
    copy(sValue) ? alertify.success(oTranslations().CODE_COPIED) : alertify.error(oTranslations().COPY_TO_CLIPBOARD_FAILED);
  };

  handleIconElementSelectClicked = (oIconDetails, oEvent) => {
    if (CS.isFunction(this.props.onSelectHandler)) {
      this.props.onSelectHandler(oIconDetails, oEvent);
    }
  }

  handleIconElementCheckboxClicked = (oIconDetails, oEvent) => {
    if (!CS.isFunction(this.props.onSelectHandler)) {
      oEvent.nativeEvent.dontRaise = true;
      EventBus.dispatch(oEvents.ICON_ELEMENT_CHECKBOX_CLICKED, [oIconDetails.id], false);
    }
  };

  handleHeaderActionButtonClicked = (sButtonId, aFiles) => {
    if (CS.isFunction(this.props.eventsActionHandler)) {
      this.props.eventsActionHandler(sButtonId, aFiles);
    } else {
      EventBus.dispatch(oEvents.CONFIG_HEADER_VIEW_ACTION_BUTTON_CLICKED, sButtonId, aFiles);
    }
  };

  getSelectHeaderAction = (aSelectedIconIds, iTotalLength) => {
    let oSelectHeaderAction = {
      id: "select_icon_library",
      title: "SELECT_ALL"
    };
    let bSelectAll = true;
    let sIconClass = "selectIcon ";

    if (aSelectedIconIds.length && aSelectedIconIds.length < iTotalLength) {
      sIconClass += "partiallySelected";
    }
    if (iTotalLength && (aSelectedIconIds.length == iTotalLength)) {
      sIconClass += "isSelected";
      bSelectAll = false;
    }
    oSelectHeaderAction.className = sIconClass;

    let sId = oSelectHeaderAction.id;
    let sIconClassName = oSelectHeaderAction.className || "";
    let sTitle = oTranslations()[oSelectHeaderAction.title];
    let oSelectedItemsCount = null;
    if (CS.isNotEmpty(aSelectedIconIds)) {
      oSelectedItemsCount = <div className="selectedItemCount">
        {aSelectedIconIds.length}
      </div>;
    }

    let selectActionView = (<TooltipView label={sTitle} key={sId}>
      <div className="actionButton" onClick={this.handleHeaderActionButtonClicked.bind(this, sId)}>
        <div className={"actionButtonIcon " + sIconClassName}>
          {oSelectedItemsCount}
        </div>
      </div>
    </TooltipView>);

    oSelectHeaderAction.view = selectActionView;
    return oSelectHeaderAction;
  }

  getConditionalHeaderActions = (aHeaderActionGroup) => {
    let oManageEntityHeaderAction = {
      id: "manage_icon_library",
      className: "manageEntity",
      title: "MANAGE_ENTITY_USAGE"
    }
    let oDeleteHeaderAction = {
      id: 'delete_icon_library_items',
      className: 'actionItemDelete',
      title: 'DELETE'
    }
    aHeaderActionGroup.push(oManageEntityHeaderAction);
    aHeaderActionGroup.push(oDeleteHeaderAction);
  }

  getConfigHeaderView = () => {
    let oIconLibraryData = this.props.iconLibraryData;
    let aSelectedIconIds = oIconLibraryData.selectedIconIds || [];
    let iTotalLength = (oIconLibraryData.icons && oIconLibraryData.icons.length) || 0;
    let oSearchBarProps = {searchText: oIconLibraryData.searchText};
    let aHeaderActionGroup = this.props.headerActionGroup;

    if (CS.isNotEmpty(aHeaderActionGroup)) {
      if (iTotalLength !== 0)
        aHeaderActionGroup.unshift(this.getSelectHeaderAction(aSelectedIconIds, iTotalLength));

      if (aSelectedIconIds.length > 0) {
        this.getConditionalHeaderActions(aHeaderActionGroup);
      }
    }

    return (<ConfigHeaderView
        actionButtonList={aHeaderActionGroup}
        hideSearchBar={false}
        searchBarProps={oSearchBarProps}
        context={"iconLibrary"}
        filesUploadHandler={this.handleHeaderActionButtonClicked.bind(this, "upload_icon_library")}
        bMultiple={true}
    />)
  };

  getIconLibrarySectionHeader = (oIconDetails, bIsSelected) => {
    let sClassName = bIsSelected ? "iconElementCheckButton isSelected" : "iconElementCheckButton";
    let fOnCheckHandler = this.handleIconElementCheckboxClicked.bind(this, oIconDetails);
    let oHeaderDom = (
        <div className={sClassName}
             onClick={fOnCheckHandler}/>);
    return (<div className="iconElementHeaderView">
      {oHeaderDom}
    </div>)
  };

  handlePaginationChanged = (sContext, oPaginationData) => {
    oPaginationData.size = oPaginationData.pageSize;
    delete oPaginationData.pageSize;
    EventBus.dispatch(oEvents.ICON_LIBRARY_PAGINATION_CHANGED, oPaginationData, sContext);
  };

  getPaginationView = () => {
    let oPaginationView = null;
    let oIconLibraryData = this.props.iconLibraryData;
    let oNavigationData = oIconLibraryData.paginationData;
    let bIsSelectIconDialogView = !this.props.dragAndDropViewRequired;

    if (bIsSelectIconDialogView) {
      oPaginationView = (
          <div className="paginationSection">
            <PaginationView
                currentPageItems={oIconLibraryData.icons.length}
                from={oNavigationData.from}
                pageSize={oNavigationData.size}
                totalItems={oIconLibraryData.totalCount}
                onChange={this.handlePaginationChanged.bind(this, "selectDialog")}
                displayTheme={Constants.DARK_THEME}
                isMiniPaginationView={true}
                isMiniPaginationWithNextPreviousButtons={true}
            />
          </div>
      )
    } else {
      oPaginationView = (
          <div className="paginationSection">
            <PaginationView
                currentPageItems={oIconLibraryData.icons.length}
                from={oNavigationData.from}
                pageSize={oNavigationData.size}
                totalItems={oIconLibraryData.totalCount}
                onChange={this.handlePaginationChanged.bind(this, "iconLibrary")}
                displayTheme={Constants.DARK_THEME}
                pageSizes={[50, 100, 150]}
            />
          </div>
      )
    }
    return oPaginationView;
  };

  getIconLibrarySectionBody = () => {
    let oIconLibraryData = this.props.iconLibraryData;
    let aIconLists = oIconLibraryData.icons;
    let sNothingToDisplayMessage = this.props.nothingToDisplayMessage ?
                                   this.props.nothingToDisplayMessage : oTranslations().NOTHING_TO_DISPLAY_ICON_LIBRARY;
    if (CS.isEmpty(aIconLists)) {
      return (
          <div className="iconLibraryNothingToDisplayContainer">
            <div className="nothingToDisplay">{sNothingToDisplayMessage}</div>
          </div>
      );
    }
    return (<div className="iconLibrarySectionBody">
      {this.getIconLibraryElements()}
    </div>)
  };

  getIconLibrarySection = () => {
    let oProps = this.props;
    let bIsDragging = this.props.isDragging;
    let bDragAndDropViewRequired = oProps.dragAndDropViewRequired;
    let aAllowedFileTypes = CoverflowAssetTypeList.allTypes;

    if (bDragAndDropViewRequired) {
      return (
          <FileDragAndDropView context="iconLibrarySectionDrop"
                               id="iconLibrarySectionBody"
                               allowedFileTypes={aAllowedFileTypes}
                               isDragging={bIsDragging}>
            {this.getIconLibrarySectionBody()}
          </FileDragAndDropView>
      )
    }
    return this.getIconLibrarySectionBody()
  };

  getActionElementsView = (sId) => {

    if (this.props.hideActionsOnElements) {
      return null;
    }
    let FileImportAllowedTypes = CoverflowAssetTypeList.imageTypes.join(', ');
    return (
        <div className="iconLibrarySectionActions">
          <TooltipView placement={"top"} label={oTranslations().EDIT_ICON}>
            <div className="gridImageActionIconContainer">
              <div className="gridImageActionIcon edit"
                   onClick={this.handleActionItemClicked.bind(this, "edit", sId)}></div>
            </div>
          </TooltipView>
          <label for={"file-input"+ sId}>
            <TooltipView placement={"top"} label={oTranslations().REPLACE_ICON}>
              <div className="gridImageActionIconContainer">
                <div className="gridImageActionIcon replace">
                </div>
              </div>
            </TooltipView>
          </label>
          <input id={"file-input" + sId}
                 style={{"display":"none"}}
                 type="file"
                 onChange={this.handleActionItemClicked.bind(this, "replace", sId)}
                 accept={FileImportAllowedTypes}
                 multiple={false}/>


          <TooltipView placement={"top"} label={oTranslations().DELETE_ICON}>
            <div className="gridImageActionIconContainer">
              <div className="gridImageActionIcon delete"
                   onClick={this.handleActionItemClicked.bind(this, "delete", sId)}></div>
            </div>
          </TooltipView>
        </div>
    )
  }

  getIcon = (sIconKey) => {
    let sIcon = ViewLibraryUtils.getIconUrl(sIconKey, false);
    let sIconView = (<ImageSimpleView classLabel="icon" imageSrc={sIcon}/>);
    return (
        <div className="iconLibrarySectionElementIcon">
          {sIconView}
        </div>
    )
  }

  getCopyCodeView = (sCode) => {
    if (this.props.isCopyCodeRequired) {
      return (
          <TooltipView placement={"top"} label={oTranslations().COPY_TO_CLIPBOARD_TOOLTIP}>
            <div className="copyToClipboard"
                 onClick={this.handleCopyToClipboardClick.bind(this, sCode)}></div>
          </TooltipView>
      )
    }
  }

  getIconDetails = (sName, sCode) => {
    sName = CS.getLabelOrCode({label: sName, code: sCode});
    return (
        <div className="iconLibrarySectionElementDetails">
          <TooltipView placement={"top"} label={sName}>
            <div className="iconLibrarySectionElementName">{sName}</div>
          </TooltipView>
          <div className="iconLibrarySectionElementCodeContainer">
            <TooltipView placement={"top"} label={sCode}>
              <div className="iconLibrarySectionElementCode">{sCode}</div>
            </TooltipView>
            {this.getCopyCodeView(sCode)}
          </div>
        </div>
    )
  }

  getIconLibraryElements = () => {
    let aIconLibraryElements = [];
    let oIconLibraryData = this.props.iconLibraryData;
    let aIconLists = oIconLibraryData.icons;
    let aSelectedIconIds = oIconLibraryData.selectedIconIds;
    CS.forEach(aIconLists, (oIconDetails) => {
      let sId = oIconDetails.id;
      let sIconKey = oIconDetails.iconKey;
      let sName = oIconDetails.label;
      let sIconCode = oIconDetails.code;
      let bIsSelected = false;
      if (CS.includes(aSelectedIconIds, sId)) {
        bIsSelected = true;
      }
      let sClassName = bIsSelected ? "iconLibrarySectionElement selected" : "iconLibrarySectionElement";
      let fOnSelectHandler = this.handleIconElementSelectClicked.bind(this, oIconDetails);

      aIconLibraryElements.push(
          <div className={sClassName}
               onClick={fOnSelectHandler}>
            {this.getIconLibrarySectionHeader(oIconDetails, bIsSelected)}
            <div className="iconLibrarySectionElementBody">
              {this.getIcon(sIconKey)}
              {this.getIconDetails(sName, sIconCode)}
            </div>
            {this.getActionElementsView(sId)}
          </div>);
    });
    return aIconLibraryElements;
  };

  getDialogView = () => {
    let aGetDialogView = [];
    let aActiveIcons = this.props.activeIcons;
    let oDialogExtraData = this.props.dialogExtraData;
    let oIconLibraryData = this.props.iconLibraryData;
    let bIconLibraryUploadClicked = oDialogExtraData.isUploadClicked;
    let bIconElementEditClicked = oDialogExtraData.isIconEditClicked;

    if (bIconLibraryUploadClicked && CS.isNotEmpty(aActiveIcons)) {
      aGetDialogView.push(<IconLibraryEditCodeDialogView
          activeIcons={aActiveIcons}/>);
    }
    if (bIconElementEditClicked) {
      let oActiveIcon = oDialogExtraData.activeIconElement;
      let oOriginalIcon = CS.find(oIconLibraryData.icons, {id: oActiveIcon.id});

      aGetDialogView.push(<IconEditView
          oActiveIcon={oActiveIcon}
          oOriginalIcon={oOriginalIcon}
      />)
    }

    return aGetDialogView;
  };

  getEntityDialog = (aButtonData, sTitle = "", sMessage = "") => {
    let oEntityDataList = ManageEntityConfigProps.getDataForDialog();
    let bIsDelete = ManageEntityConfigProps.getIsDelete();
    return (
        <ManageEntityDialogView
            oEntityDatList={oEntityDataList}
            bIsDelete={bIsDelete}
            buttonData={aButtonData}
            entityType={"icon"}
            title={sTitle}
            message={sMessage}
        />
    );
  };

  getDeleteDialogWithUsage = () => {
    var aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        className: "iconLibraryDeleteDialogCancel"
      },
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        className: "iconLibraryDeleteDialogOk"
      }
    ];
    let sTitle = oTranslations().DELETE_CONFIRMATION;
    let sMessage = oTranslations().ICON_DELETE_WARNING;
    return this.getEntityDialog(aButtonData, sTitle, sMessage);
  };

  render () {
    let bIsUsageDeleteDialog = this.props.isUsageDeleteDialog;
    let oConfigHeaderView = this.getConfigHeaderView();
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? bIsUsageDeleteDialog ? this.getDeleteDialogWithUsage() : this.getEntityDialog([]) : null;
    let oDialogView = CS.isNotEmpty(this.props.dialogExtraData) ? this.getDialogView() : null;

    return (
        <div className={"iconLibraryViewContainer"}>
          {oConfigHeaderView}
          {oManageEntityDialog}
          <div className="iconLibrarySection">
            {this.getIconLibrarySection()}
            {oDialogView}
            {this.getPaginationView({})}
          </div>
        </div>);
  }
}

export const view = IconLibraryView;
export const events = oEvents;
