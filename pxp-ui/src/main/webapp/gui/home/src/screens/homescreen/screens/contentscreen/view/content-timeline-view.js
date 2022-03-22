import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from './../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import { view as ToolbarView } from '../../../../../viewlibraries/toolbarview/toolbar-view';
import ViewUtils from './utils/view-utils';
import TimelineConstantMapping from './../tack/mock/time-line-constant-mapping';
import oFilterPropType from '../tack/filter-prop-type-constants';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  TIMELINE_VIEW_VERSION_SELECT_BUTTON_CLICKED: "timeline_view_version_select_button_clicked",
  TIMELINE_VIEW_VERSION_DELETE_BUTTON_CLICKED: "timeline_view_version_delete_button_clicked",
  TIMELINE_VIEW_COMPARE_BUTTON_CLICKED: "timeline_view_compare_button_clicked",
  TIMELINE_VIEW_VERSION_LOAD_MODE_BUTTON_CLICKED: "timeline_view_version_load_mode_button_clicked",
  TIMELINE_VIEW_SHOW_ARCHIVE_BUTTON_CLICKED : "timeline_view_show_archive_button_clicked",
  TIMELINE_VIEW_VERSION_SELECT_ALL_BUTTON_CLICKED : "timeline_view_version_select_all_button_clicked",
  TIMELINE_VIEW_VERSION_RESTORE_BUTTON_CLICKED : "timeline_view_version_restore_button_clicked",
  TIMELINE_VIEW_VERSION_ROLLBACK_BUTTON_CLICKED : "timeline_view_version_rollback_button_clicked"
};

const oPropTypes = {
  timelineData: ReactPropTypes.object
};

// @CS.SafeComponent
class ContentTimelineView extends React.Component {

  handleSelectAllClicked = () => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_VERSION_SELECT_ALL_BUTTON_CLICKED);
  };

  handleCompareClicked = (iVersionNumber) => {
    var aSelectedVersions = [];
    if (CS.isInteger(iVersionNumber)) {
      aSelectedVersions.push(this.props.timelineData.versions[0].versionNumber);
      aSelectedVersions.push(iVersionNumber);
    }
    EventBus.dispatch(oEvents.TIMELINE_VIEW_COMPARE_BUTTON_CLICKED, true, aSelectedVersions);
  };

  handleVersionDeleteButtonClicked = (sVersionNumber) => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_VERSION_DELETE_BUTTON_CLICKED, sVersionNumber);
  };

  handleVersionRestoreButtonClicked = (sVersionNumber) => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_VERSION_RESTORE_BUTTON_CLICKED, sVersionNumber);
  };

  handleVersionSelectButtonClicked = (aVersionNumber) => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_VERSION_SELECT_BUTTON_CLICKED, aVersionNumber);
  };

  handleVersionRollbackButtonClicked = (aVersionNumber) => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_VERSION_ROLLBACK_BUTTON_CLICKED, aVersionNumber);
  }

  handleLoadMoreButtonClicked = () => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_VERSION_LOAD_MODE_BUTTON_CLICKED);
  };

  handleShowArchiveClicked = (bArchiveFlag) => {
    EventBus.dispatch(oEvents.TIMELINE_VIEW_SHOW_ARCHIVE_BUTTON_CLICKED,bArchiveFlag, {
      screenContext: "timeline",
      filterType: oFilterPropType.MODULE
    });
  };

  getBadgeView = (sProperty) => {
    var sClassName = "versionBadge";

    switch (sProperty) {

      case TimelineConstantMapping.attributeChanged:
        sClassName = sClassName + " attributeBadge";
        break;

      case TimelineConstantMapping.tagChanged:
        sClassName = sClassName + " tagBadge";
        break;

      case TimelineConstantMapping.relationshipChanged:
        sClassName = sClassName + " relationshipBadge";
        break;

      case TimelineConstantMapping.natureRelationshipChanged:
        sClassName = sClassName + " natureRelationshipBadge";
        break;

      case TimelineConstantMapping.lifeCycleStatusChanged:
        sClassName = sClassName + " lifeCycleBadge";
        break;

      case TimelineConstantMapping.listStatusChanged:
        sClassName = sClassName + " listingBadge";
        break;

      case TimelineConstantMapping.eventScheduleChanged:
        sClassName = sClassName + " eventScheduleBadge";
        break;

      case TimelineConstantMapping.klassAdded:
        sClassName = sClassName + " klassAddedBadge";
        break;

      case TimelineConstantMapping.taxonomyAdded:
        sClassName = sClassName + " taxonomyAddedBadge";
        break;

      case TimelineConstantMapping.klassRemoved:
        sClassName = sClassName + " klassRemovedBadge";
        break;

      case TimelineConstantMapping.taxonomyRemoved:
        sClassName = sClassName + " taxonomyRemovedBadge";
        break;

      case TimelineConstantMapping.roleChanged:
        sClassName = sClassName + " roleBadge";
        break;

      case TimelineConstantMapping.mamValidityChanged:
        sClassName = sClassName + " mamValidityBadge";
        break;

      case TimelineConstantMapping.isDefaultAssetInstanceIdChanged:
        sClassName = sClassName + " defaultImageBadge";
        break;

      case TimelineConstantMapping.languageAdded:
        sClassName = sClassName + " languageAddedBadge";
        break;

      case TimelineConstantMapping.languageRemoved:
        sClassName = sClassName + " languageRemovedBadge";
        break;
    }

    return (<div className="versionBadgeContainer" key={sClassName}>
      <TooltipView label={getTranslation()[sProperty]}>
        <div className={sClassName}></div>
      </TooltipView>
    </div>);
  };


  getBadgesView = (oVersion) => {
    let oSummaryData = oVersion.summary;
    let aBadgeView = [];
    let bShowAttributeBadge = false;
    CS.forEach(oSummaryData, (iValue, sKey) => {
      if (iValue) {
        /** for dependent/independent attribute*/
        if ((CS.isObject(iValue) && CS.isNotEmpty(iValue)) || sKey === "attributeChanged") {
          /** To show attribute badge only once for dependent/independent attribute change **/
          if(!bShowAttributeBadge) {
            aBadgeView.push(this.getBadgeView(TimelineConstantMapping["attributeChanged"]));
            bShowAttributeBadge = true;
          }
        }
        else if (CS.isNumber(iValue)){
          /** for tags, relationships, images etc. **/
          aBadgeView.push(this.getBadgeView(TimelineConstantMapping[sKey]));
        }
      }
    });

    return aBadgeView;
  } ;

  getChangesView = (sHeader, oSummaryData, sVersionNumber, bShowCount = true) => {
    let aChangesView = [];
    let iTotalCount = 0;

    CS.forEach(oSummaryData, (iValue, sKey) => {
      if(iValue) {
        let sDescription;
        if(CS.isNumber(iValue) && bShowCount){
          iTotalCount += iValue;
          sDescription = iValue + " " + getTranslation()[TimelineConstantMapping[sKey]];
        } else {
          sDescription = getTranslation()[TimelineConstantMapping[sKey]];
        }
        aChangesView.push(<div className="changeElementContainer" key={sVersionNumber + sKey}>
          <div className="changeElementVal">{sDescription}</div>
        </div>);
      }
    });

      iTotalCount && aChangesView.unshift(<div className="mainInfoChangesHeader">{sHeader +" (" + iTotalCount + ")"}</div>);

    return aChangesView ;
  };

  getAddedRemovedLanguageView = (oVersion) => {
    let sVersionNumber = oVersion.versionNumber;
    let oSummary = oVersion.summary;

    let oData = {
      languageAdded : oSummary.languageAdded,
      languageRemoved : oSummary.languageRemoved
    };

    return this.getChangesView("", oData, sVersionNumber, false);
  };

  getLanguageIndependentChangesView = (oVersion) => {
    let sVersionNumber = oVersion.versionNumber;
    let oSummary = oVersion.summary;
    let oData = {};

    let aItemsToExcludeFromSummary = ['dependentAttributeIdsCountMap', 'languageAdded', 'languageRemoved'];
    CS.forEach(oSummary, (sValue, sKey) => {
      !CS.includes(aItemsToExcludeFromSummary, sKey) && (oData[sKey] = sValue);
    });

    let sHeader = getTranslation().LANGUAGE_INDEPENDENT;
    return this.getChangesView(sHeader, oData, sVersionNumber);
  };

  getLanguageDependentChangesView = (oVersion) => {
    let sVersionNumber = oVersion.versionNumber;
    let oSummary = oVersion.summary;
    let oDependentAttributesChange = oSummary.dependentAttributeIdsCountMap;
    let oData = {};
    let sHeader = "";
    let aChangesView = [];
    let oTimelineData = this.props.timelineData;
    let oReferencedLanguages = oTimelineData.referencedLanguages;
    CS.forEach(oDependentAttributesChange, (sValue, sKey) => {
      oData.attributeChanged = sValue;
      let oLanguageData = oReferencedLanguages[sKey];
      sHeader = CS.getLabelOrCode(oLanguageData);
      let aLanguageChangesView = this.getChangesView(sHeader, oData, sVersionNumber);
      aChangesView.push.apply(aChangesView, aLanguageChangesView);
    });

    return aChangesView;
  };

  getCompareView = (oVersion) => {
    return (
        <TooltipView placement="top" label={getTranslation().COMPARE_WITH_CURRENT_VERSION}>
          <div className="mainInfoUpperTool toolCompare"
               onClick={this.handleCompareClicked.bind(this, oVersion.versionNumber)}>
          </div>
        </TooltipView>
    );
  };

  getVersionView = (oVersion, bLeft) => {
    var oTimelineData = this.props.timelineData;
    var aSelectedVersionIds = oTimelineData.selectedVersionIds;
    var sCurrentVersionId = oTimelineData.currentVersion;

    var sContainerClassName = bLeft ? "versionContainer alignedLeft " : "versionContainer ";
    let aBadgeViews = this.getBadgesView(oVersion);
    let aLanguageIndependentChangesView = this.getLanguageIndependentChangesView(oVersion);
    let aLanguageDependentChangesView = this.getLanguageDependentChangesView(oVersion);
    let aAddedRemovedLanguagesView = this.getAddedRemovedLanguageView(oVersion);

    if(CS.includes(aSelectedVersionIds, oVersion.versionNumber)) {
      sContainerClassName += "selectedVersion ";
    }

    let oUser = ViewUtils.getUserByUserName(oVersion.lastModifiedBy);
    var sVersionNumber = oVersion.versionNumber;
    var oImageDom = oUser && oUser.icon ?
                    (<ImageFitToContainerView imageSrc={ViewUtils.getIconUrl(oUser.icon)} />) :
                    (<div className="userDefaultImage"></div>);

    var bArchiveVisibleStatus = oTimelineData.isArchiveVisible;
    var oDeleteButtonView = null;
    let oRollbackButtonView = null;
    let oCompareView = null;
    let oRestoreButtonView = null;
    let sDeleteLabel = '';
    if (oVersion.versionNumber !== sCurrentVersionId &&
        (
            !oTimelineData.isContentDisabled &&
            oTimelineData.globalPermission &&
            oTimelineData.globalPermission.canDelete
        )
    ) {

      if(!bArchiveVisibleStatus) {
        sDeleteLabel = getTranslation().ARCHIVE;
        oCompareView = this.getCompareView(oVersion);
      }
      else {
        sDeleteLabel = getTranslation().DELETE;
        oRestoreButtonView = (
            <TooltipView placement="top" label={getTranslation().RESTORE}>
              <div className="mainInfoUpperTool toolRestore"
                   onClick={this.handleVersionRestoreButtonClicked.bind(this, oVersion.versionNumber)}>
              </div>
            </TooltipView>
        );
      }

      oDeleteButtonView = (
          <TooltipView placement="top" label={sDeleteLabel}>
            <div className="mainInfoUpperTool toolDelete"
                 onClick={this.handleVersionDeleteButtonClicked.bind(this, oVersion.versionNumber)}>
            </div>
          </TooltipView>
      );

      oRollbackButtonView = (oTimelineData.isArchiveVisible || oTimelineData.hideRollback) ? null : (<TooltipView placement="top" label={getTranslation().ROLLBACK}>
        <div className="mainInfoUpperTool toolRollback"
             onClick={this.handleVersionRollbackButtonClicked.bind(this, oVersion.versionNumber)}>
        </div>
      </TooltipView>);
    }

    let sCurrentVersion = "";
    if (oVersion.versionNumber === sCurrentVersionId) {
      sCurrentVersion = " (" + getTranslation().CRNT_VRSN + ")";
    }

    let oSelectView = (<TooltipView placement="top" label={getTranslation().SELECT}>
      <div className="mainInfoUpperTool toolSelect"
           onClick={this.handleVersionSelectButtonClicked.bind(this, oVersion.versionNumber)}>
      </div>
    </TooltipView>);

    //To create compareView in case user is read only, as oTimelineData.isContentDisabled will be true it will not get created above.
    if(oVersion.versionNumber !== sCurrentVersionId &&
        ViewUtils.getIsCurrentUserReadOnly() && !bArchiveVisibleStatus) {
      oCompareView = this.getCompareView(oVersion);
    }

    let oVersionLastModified = ViewUtils.getDateAttributeInDateTimeFormat(oVersion.lastModified);
    let oAuthorDetailsTooltipDom = (<div className="maxWidthTooltip">{oVersion.message}</div>);

    let sMainInfoVersionLabel = "";
    if(sVersionNumber == -1) {
      sMainInfoVersionLabel = getTranslation().LIVE_COPY;
    } else {
      sMainInfoVersionLabel = getTranslation().VERSION + " " + sVersionNumber;
    }

    return (<div className={sContainerClassName} key={sVersionNumber}>
      <div className="horiLine"></div>
      <div className="otherInfoContainer">
        <div className="otherInfoDate">
          {oVersionLastModified.date}
          <span className="otherInfoTime">{oVersionLastModified.time}</span>
        </div>
      </div>
      <div className="mainInfoContainer">
        <div className="mainInfoTile">
          <div className="mainInfoBody">
            <div className="mainInfoVersion">
              <div className="mainInfoVersionLabel">{sMainInfoVersionLabel}</div>
              <div className="mainInfoUpperToolbar">
                {oDeleteButtonView}
                {oRestoreButtonView}
                {oSelectView}
                {oRollbackButtonView}
                {oCompareView}
              </div>
              <div className="versionBadgeGroupContainer">
                {aBadgeViews}
              </div>
            </div>
            <div className="mainInfoChanges">
                {aAddedRemovedLanguagesView}
                {aLanguageIndependentChangesView}
                {aLanguageDependentChangesView}
            </div>
          </div>

          <div className="authorDetailsContainer">
            <div className="authorDetailsImage">{oImageDom}</div>
            <div className="authorDetailsText">
              <div className="authorDetailsName">{ViewUtils.getUserDisplayName(oUser)}</div>
              <TooltipView label={oAuthorDetailsTooltipDom}>
                <div className="authorDetailsMessage">{oVersion.message}</div>
              </TooltipView>
            </div>
          </div>
        </div>
      </div>
    </div>);

  };

  getMajorVersionsView = () => {

    var _this = this;
    var aMajorVersions = this.props.timelineData.versions; //MockForTimeline.MajorVersions;
    var aMajorVersionView = [];

    CS.forEach(aMajorVersions, function (oMajorVersion, iIndex) {
      var oVersionView = _this.getVersionView(oMajorVersion, iIndex%2);

      aMajorVersionView.push(oVersionView);
    });

    return <div className="majorVersionContainer">{aMajorVersionView}</div> ;
  };

  getCreatedOnViewOrLoadMore = () => {
    var oTimelineData = this.props.timelineData;
    var bShowCreatedOn = oTimelineData.isCreatedOnVisible; //MockForTimeline.MajorVersions;
    var oView =  null;
    if(bShowCreatedOn) {
      try {
        var oShortDate = ViewUtils.getDateAttributeInDateTimeFormat(oTimelineData.createdOn);
        oView = (
            <div className="createdOnStart">
              {getTranslation().CREATED_ON}
              <div className="createdOnDate">{oShortDate.date}</div>
              <div className="createdOnTime">{oShortDate.time}</div>
            </div>
        );
      }
      catch (oException) {
        oView = null;
        ExceptionLogger.error(oException);
      }
    }
    else {
      oView = (
          <div className="loadMore" onClick={this.handleLoadMoreButtonClicked}>
            <div className="loadMoreLabel">{getTranslation().LOAD_MORE}</div>
          </div>
      )
    }

    return oView;
  };

  getTimeLineToolbarView = () => {
    let oTimeLineData = this.props.timelineData;
    let oItemList = oTimeLineData.timeLineToolbarData.timelineTabToolbarData;
    return (<ToolbarView  toolbarData={oItemList}/>);
  };

  getView = () => {
    var oTimelineData = this.props.timelineData;
    var oMajorVersionsView = this.getMajorVersionsView();
    var oCenterLineView = (
        <div className="centerLine">
          <div className="centerLineHelper"></div>
        </div>
    );
    var oCreatedOnOrLoadMoreView = this.getCreatedOnViewOrLoadMore();
    var sBodyClassName = "timelineViewBody zoom" + oTimelineData.zoomLevel; //Props.getTimelineZoomLevel();
    var aToolbarItems = this.getTimeLineToolbarView();
    var bArchiveVisibleStatus = oTimelineData.isArchiveVisible;

    var sArchiveClassName = "timelineShowArchive";
    var sArchiveText = getTranslation().SHOW_ARCHIVED;
    var bArchiveFlag = true;
    if(bArchiveVisibleStatus) {
      sArchiveClassName = "timelineHideArchive";
      sArchiveText = getTranslation().HIDE_ARCHIVED;
      bArchiveFlag =false;
    }
    var oShowArchiveClick = this.handleShowArchiveClicked.bind(this,bArchiveFlag);

    return (<div className="timelineViewContainer">
      <div className="timelineViewHeader"></div>
      {oCenterLineView}
      <div className= "timeLineHeaderView">
        <div className={sArchiveClassName}>
        <CustomMaterialButtonView
            label={sArchiveText}
            isRaisedButton={true}
            isDisabled={false}
            onButtonClick={oShowArchiveClick}/>
      </div>
      <div className="timelineToolbar">
        {aToolbarItems}
      </div>
      </div>
      <div className="timeLineWrapper">
      <div className={sBodyClassName} >
        {oMajorVersionsView}
        {oCreatedOnOrLoadMoreView}
      </div>
      </div>
    </div>);
  };

  render() {
    var oView = this.getView();

    return (
        <div className="contentTimelineViewContainer">
          {oView}
        </div>
    );
  }
}

export const view = ContentTimelineView;
export const events = oEvents;
