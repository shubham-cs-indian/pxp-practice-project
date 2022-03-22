import React, {Component} from "react";

import PropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import ContentComparisonView from './content-comparison-view';
import FullScreenViewObj from '../../viewlibraries/fullscreenview/fullscreen-view';

const FullScreenView = FullScreenViewObj.view;

const oEvents = {
  CONTENT_COMPARISON_FULL_SCREEN_BUTTON_CLICKED: "content_comparison_full_screen_button_clicked"
};

class ContentComparisonDialogView extends Component {

  handleContentComparisonFullScreenButtonClicked = () => {
    EventBus.dispatch(oEvents.CONTENT_COMPARISON_FULL_SCREEN_BUTTON_CLICKED)
  };

  getContentComparisonView () {
    let oComparisonViewData = this.props.comparisonViewData;
    let sHeaderLabel = oComparisonViewData.headerLabel;
    let oView = <ContentComparisonView {...oComparisonViewData}/>;

    return (
        <FullScreenView
            header={sHeaderLabel}
            showHeader={true}
            bodyView={oView}
            isDefaultFullScreen={true}
            fullScreenHandler={this.handleContentComparisonFullScreenButtonClicked}
        />);
  };

  render () {
    return (
        <div className={"contentComparisonFullScreenViewWrapper"}>
          {this.getContentComparisonView()}
        </div>)
  }
}

ContentComparisonDialogView.protoTypes = {
  comparisonViewData: PropTypes.object
};

export default ContentComparisonDialogView;
export var events = oEvents;
