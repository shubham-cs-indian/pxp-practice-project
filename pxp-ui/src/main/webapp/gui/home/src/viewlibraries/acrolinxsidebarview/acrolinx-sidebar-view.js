import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {AbstractRichtextEditorAdapter, InputAdapter, AcrolinxPlugin, AutoBindAdapter} from '@acrolinx/sidebar-sdk';
import match_1 from '@acrolinx/sidebar-sdk/dist/utils/match';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

/** ------------- SCROLLING - we have duplicated this util from acrolinx, in order to change the code. ---------------*/
import scrolling_1 from '../../libraries/acrolinx/scrolling';

/** ---------------------------------- Overriding Acrolinx functions ------------------------------------------------ */

AbstractRichtextEditorAdapter.prototype.scrollIntoView  = function (sel) {
  try {
    var range = sel.getRangeAt(0);
    var tmp = range.cloneRange();
    tmp.collapse(false);
    var text = document.createElement('span');
    tmp.insertNode(text);
    text.scrollIntoViewIfNeeded();
    text.click();
    text.remove();
  }
  catch (e) {
    console.error(e);
  }

};

InputAdapter.prototype.scrollAndSelect = function (matches) {
  try {
    var newBegin = matches[0].range[0];
    var matchLength = match_1.getCompleteFlagLength(matches);
    var el = this.element;
    if (el.clientHeight < el.scrollHeight) {
      // This funny trick causes scrolling inside of the textarea.
      var text = this.element.value;
      el.value = text.slice(0, newBegin);
      el.focus();
      el.scrollTop = 1e9; // Scroll to the end of the textarea.
      var cursorScrollTop = el.scrollTop;
      el.value = text;
      if (cursorScrollTop > 0) {
        el.scrollTop = cursorScrollTop + el.clientHeight / 2;
      }
    }
    el.setSelectionRange(newBegin, newBegin + matchLength);
    el.focus();
    scrolling_1.scrollIntoView(el, this.config.scrollOffsetY);
  }
  catch (e) {
    console.error(e);
  }
};

/** ----------------------------------------------------------------------------------------------------------------- */


const oEvents = {};

const oPropTypes = {
  isVisible: ReactPropTypes.bool,
  onCheckHandler: ReactPropTypes.func
};

// @CS.SafeComponent
class AcrolinxSidebarView extends React.Component {

  constructor (props) {
    super(props);

    this.acrolinxPlugin = null;
    this.onCheckHandler = this.onCheckHandler.bind(this);
  }

  componentDidMount () {
    this.initiateAcrolinx();
  }

  componentDidUpdate () {
    this.initiateAcrolinx();
  }

  initiateAcrolinx () {
    try {
      if (this.props.isVisible) {
        let basicConf = {
          sidebarContainerId: 'sidebarContainer',
          serverAddress: 'https://test-ssl.acrolinx.com/',
          clientSignature: 'SW50ZWdyYXRpb25EZXZlbG9wbWVudERlbW9Pbmx5',
          getDocumentReference: function () {
            return window.location.href;
          },
        };

        this.acrolinxPlugin = new AcrolinxPlugin(basicConf);
        this.acrolinxPlugin.configure({readOnlySuggestions: true});
        var autoBindAdapter = new AutoBindAdapter({});
        autoBindAdapter.registerCheckResult = this.onCheckHandler;
        this.acrolinxPlugin.registerAdapter(autoBindAdapter);
        this.acrolinxPlugin.init();

      } else {
        if (this.acrolinxPlugin) {
          this.acrolinxPlugin.dispose(() => {
            this.acrolinxPlugin = null;
          });
        }
      }
    }
    catch (oException) {
      ExceptionLogger.error("Error :" + oException);
    }
  }

  onCheckHandler (oCheckResult) {
    if (CS.isFunction(this.props.onCheckHandler)) {
      let aCheckInformation = oCheckResult.embedCheckInformation;
      let oScore = CS.find(aCheckInformation, {key: "score"});
      let sScoreValue = oScore ? oScore.value : "";
      let oScoreUrl = CS.find(aCheckInformation, {key: "scorecardUrl"});
      let sScoreCardUrl = oScoreUrl ? oScoreUrl.value : "";
      this.props.onCheckHandler(sScoreValue, sScoreCardUrl);
    }
  }

  render () {

    let sAcrolinxContainerClass = "acrolinxContainer ";
    if (this.props.isVisible) {
      sAcrolinxContainerClass += "isVisible";
    }

    return (
        <div className={sAcrolinxContainerClass} id="sidebarContainer"/>
    );
  }

}

AcrolinxSidebarView.propTypes = oPropTypes;

export const view = AcrolinxSidebarView;
export const events = oEvents;
export const propTypes = AcrolinxSidebarView.propTypes;
