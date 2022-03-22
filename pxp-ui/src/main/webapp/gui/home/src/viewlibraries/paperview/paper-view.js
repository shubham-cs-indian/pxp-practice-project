import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager';
import TooltipView from './../tooltipview/tooltip-view';
import PaperViewModel from './model/paper-model';

const oEvents = {
  PAPER_VIEW_HEADER_CLICKED: 'paper_view_header_clicked',
  PAPER_VIEW_REMOVE_ICON_CLICKED: "paper_view_remove_icon_clicked",
  PAPER_VIEW_SKIP_ICON_CLICKED: "paper_view_skip_icon_clicked"
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(PaperViewModel).isRequired,
  isExpanded: ReactPropTypes.bool
};
/**
 * @class PaperView - Use to display tab view like context tab view, variant dialog view etc.
 * @memberOf Views
 * @property {custom} model - pass model contain parameters like height, icon, id, label, maxHeight, minHeight, propeties.
 * @property {bool} [isExpanded] -  boolean value for isExpandedthe accordian or not.
 */

// @CS.SafeComponent
class PaperView extends React.Component {

  constructor(props) {
    super(props);
  }

  getViewStyles =()=> {

    var __props = this.props;

    return {
      "height": __props.model.height || 'auto',
      "minHeight": __props.model.minHeight || 0,
      "maxHeight": __props.model.maxHeight || 'none'
    };
  }

  handleOnHeaderClicked =(event)=>{
    var oPaperViewModel = this.props.model;
    EventBus.dispatch(oEvents.PAPER_VIEW_HEADER_CLICKED, this, oPaperViewModel);
  }

  handlePaperSectionRemoveClicked =(oEvent)=> {
    var oPaperViewModel = this.props.model;
    EventBus.dispatch(oEvents.PAPER_VIEW_REMOVE_ICON_CLICKED, oPaperViewModel);
  }

  handlePaperSectionSkipToggled =(oEvent)=> {
    var oPaperViewModel = this.props.model;
    EventBus.dispatch(oEvents.PAPER_VIEW_SKIP_ICON_CLICKED, oPaperViewModel);
  }

  //TODO: Refactor for section icon [Tauseef]
  getSectionIcon =(sPaperIconContainerClass, sPaperIcon)=> {
    return (<div className={sPaperIconContainerClass}>
      <img className="paperIcon" src={sPaperIcon} />
    </div>);
  }

  getEntityCountDom =()=> {
    var oModel = this.props.model;
    var iCount = oModel.properties["innerEntityCount"];
    if (iCount >= 0) {
      var sCountLabel = getTranslation().COUNT;
      return (
          <div className="paperEntityCountContainer">
            <TooltipView label={sCountLabel} placement="bottom">
              <div className="paperCountNumber">{iCount}</div>
            </TooltipView>
          </div>
      );
    } else {
      return null;
    }
  }

  getPaperIcons =()=> {
    var oModel = this.props.model;
    var bIsSectionRemovable = oModel.properties["enableRemoveSection"];
    var aIcons = [];

    if (bIsSectionRemovable) {
      aIcons.push (
          <TooltipView label={getTranslation().REMOVE}>
            <div className="paperRemoveIcon" onClick={this.handlePaperSectionRemoveClicked}></div>
          </TooltipView>)
    }

    var bEnableSkipSection = oModel.properties["enableSkipSection"];
    var bIsSkipped = oModel.properties["isSkipped"];

    if (bEnableSkipSection) {
      aIcons.push (
          <TooltipView label={getTranslation().SKIP}>
            <div className="paperSkipOption" onClick={this.handlePaperSectionSkipToggled}>
              <span className="paperSkipLabel">{getTranslation().SKIP}</span>
              <input type="checkbox" className="paperSkipCheck" checked={bIsSkipped} />
            </div>
          </TooltipView>)
    }

    return aIcons;
  }

  render() {

    var oModel = this.props.model;
    var oStyles = this.getViewStyles();
    var sPaperHeaderName = CS.getLabelOrCode(oModel) || '';
    var sPaperIcon = oModel.icon || '';
    var sContext = oModel.properties["context"];
    var bIsSelected = oModel.properties['isSelected'];
    var sPaperIconContainerClass = "paperIconContainer ";
    var sPaperClass = "paperViewContainer ";
    var sExpandedClassName = "arrow arrow-down";
    var sExpandTitle = getTranslation().COLLAPSE;
    var aImageView = [];
    var sCustomClassName = this.props.className || "";
    sPaperClass += (sCustomClassName + " ");

    if(sPaperIcon) {
      aImageView.push(this.getSectionIcon(sPaperIconContainerClass, sPaperIcon));
    } else {
      if (oModel.id === 'structure') {
        sPaperIconContainerClass += "structureSectionIcon";
        aImageView.push(this.getSectionIcon(sPaperIconContainerClass, sPaperIcon));
      }
      else {
        if(sContext != "taskSection") {
          sPaperIconContainerClass += "defaultSectionIcon";
          aImageView.push(this.getSectionIcon(sPaperIconContainerClass, sPaperIcon));
        }
      }
    }

    var oEntityCountDom = null;
    if(this.props.isExpanded != undefined) {
      if(!this.props.isExpanded) {
        sExpandedClassName = "arrow arrow-right";
        sExpandTitle = getTranslation().EXPAND;
        sPaperClass += "collapsed";
        oEntityCountDom = this.getEntityCountDom();
      }
    }

    if(bIsSelected == true) {
      sPaperClass += " selected";
    }

    if(sPaperClass.indexOf("articleRelationshipPaper showAvailableEntity") != -1){
      sExpandedClassName = "arrow noImage";

      return(
          <div className={sPaperClass} style={oStyles}>
              {this.props.children}
          </div>
      );
    }

    var fExpandCollapseHeaderClicked = !this.props.isExpanded ? this.handleOnHeaderClicked : null;
    var fExpandCollapsedButtonClicked = this.props.isExpanded ? this.handleOnHeaderClicked : null;

    var oPaperIcons = this.getPaperIcons();
    return (
        <div className={sPaperClass} style={oStyles}>
            <div className="paperViewHeader" onClick={fExpandCollapseHeaderClicked}>
              <TooltipView placement="bottom" label={sExpandTitle}>
                <div className={sExpandedClassName} onClick={fExpandCollapsedButtonClicked}></div>
              </TooltipView>
              {/*aImageView*/}
              <div className="paperHeaderLabel">
                {sPaperHeaderName}
              </div>
              {oEntityCountDom}
              {oPaperIcons}
            </div>
            <div className="paperViewBody">
              {this.props.children}
            </div>
        </div>
    );
  }
}

PaperView.propTypes = oPropTypes;

export const view = PaperView;
export const events = oEvents;
