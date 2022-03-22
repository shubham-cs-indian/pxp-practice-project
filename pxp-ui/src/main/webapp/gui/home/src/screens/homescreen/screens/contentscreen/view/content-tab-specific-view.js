import CS from '../../../../../libraries/cs';
import ReactPropTypes from 'prop-types';
import React, { Suspense } from 'react';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import ContentScreenConstants from '../store/model/content-screen-constants'
import { view as ContentTimelineView } from './content-timeline-view';
import { view as ContentTimelineComparisonView } from './content-timeline-comparison-view';
import { view as ContentScreenDialogView } from './content-screen-dialog-view';

const getTranslation = ScreenModeUtils.getTranslationDictionary;
const TaskController = React.lazy(()=>import('../../../../../smartviewlibraries/taskview/controller/task-controller'));

const oEvents = {
  CONTENT_TAB_SPECIFIC_COMPARISION_BUTTON_CLICKED: "content_tab_specific_comparision_button_clicked"
};

const oPropTypes = {
  entityViewData: ReactPropTypes.object
};

const ContentTabSpecificView = (oProps) => {

  const handleComparisionDialogButtonClick = (sButtonId) => {
    EventBus.dispatch(oEvents.CONTENT_TAB_SPECIFIC_COMPARISION_BUTTON_CLICKED, sButtonId);
  };

  const getTasksView = () => {
    var oEntityViewData = oProps.entityViewData;
    var oTaskData = oEntityViewData.tabSpecificData;
    oTaskData.usersList = oEntityViewData.userList;
    oTaskData.isDialogOpen = false;

    return <Suspense fallback={<div></div>}>
          <TaskController
              data={oTaskData}
              context={"contentTask"}
              isForceUpdate={oEntityViewData.isTaskForceUpdate}
          />
        </Suspense>
  };

  const getTimeLineView = () => {
    let oEntityViewData = oProps.entityViewData;
    let oTimelineData = oEntityViewData.tabSpecificData;
    let oTimeLineDOM = null;
    let aButtonData = [];
    let bShowSaveDiscardButtons = false;
    if (oTimelineData.isComparisonMode) {
      if (oTimelineData.isActiveEntityDirty) {
        bShowSaveDiscardButtons = true;
      }

      aButtonData = [{
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];

      let fButtonHandler = handleComparisionDialogButtonClick;
      let oTimeLineComparisionDOM = (
          <ContentScreenDialogView buttonData={aButtonData} title={oTimelineData.headerLabel}
                                   onRequestCloseHandler={fButtonHandler.bind(this, aButtonData[0].id)}
                                   onButtonClickHandler={fButtonHandler}>
            <ContentTimelineComparisonView timelineData={oTimelineData}
                                           showSaveDiscardButtons={bShowSaveDiscardButtons}/>
          </ContentScreenDialogView>
      );

      oTimeLineDOM = (
          <div className="contentTimelineComparisionWrapper">
            <ContentTimelineView
                timelineData={oTimelineData}
            />
            {oTimeLineComparisionDOM}
          </div>
      );
    }
    else {
      oTimeLineDOM = (
          <ContentTimelineView
              timelineData={oTimelineData}
          />
      );
    }

    return (<div className={"contentEditTimelineViewContainer"}>{oTimeLineDOM}</div> )
  };

  const getDuplicateAssetsView = () => {
    let oTabSpecificData = oProps.entityViewData.tabSpecificData;
    let oView = oTabSpecificData.customViewForTab || null;
    return (<div className={"duplicateAssetsTabViewWrapper"}>{oView}</div>);
  };

  const getTabSpecificView = () => {
    let oEntityViewData = oProps.entityViewData;
    let oEntityTabData = oEntityViewData.entityTabData;
    let oTabItemConstants = ContentScreenConstants.tabItems;

    switch (oEntityTabData.activeEntitySelectedTabId) {
      case oTabItemConstants.TAB_TIMELINE:
        return getTimeLineView();

      case oTabItemConstants.TAB_TASKS:
        return getTasksView();

      case oTabItemConstants.TAB_DUPLICATE_ASSETS:
        return getDuplicateAssetsView();

      default:
        return null;
    }
  };

  return getTabSpecificView();
};

ContentTabSpecificView.propsTypes = oPropTypes;

export default CS.SafeComponent(ContentTabSpecificView);
export const events = oEvents;
