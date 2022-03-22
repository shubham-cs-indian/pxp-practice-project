import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from './../tooltipview/tooltip-view';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {};

const oPropTypes = {
  header: ReactPropTypes.string,
  isDefaultExpanded: ReactPropTypes.bool,
  headerData: ReactPropTypes.object
};
/**
 * @class ExpandableView
 * @memberOf Views
 * @property {string} [header] - Header of view.
 * @property {bool} [isDefaultExpanded] - By default the view has to be  expanded or not.
 */

// @CS.SafeComponent
class ExpandableView extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      isExpanded : this.props.isDefaultExpanded || false
    }
  }

  handleExpandCollapsedClicked = () => {
    let bIsExpanded = this.state.isExpanded;
    this.setState({
      isExpanded: !bIsExpanded
    })
  };

  getIconDOM = (sKey) => {
   let sIconKey =  ViewUtils.getIconUrl(sKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sIconKey}/>
        </div>
    );
  };

  getHeaderView = () => {
    let bIsExpanded = this.state.isExpanded;
    let sClassName = bIsExpanded ? "expandCollapsedIcon expanded" : "expandCollapsedIcon collapsed";
    let sTooltipLabel = bIsExpanded ? getTranslation().COLLAPSE : getTranslation().EXPAND;
    let oHeaderData = this.props.headerData;
    let oIconDOM = oHeaderData.showHeaderIcon && this.getIconDOM(oHeaderData.iconKey);

    return (
        <div className="expandableViewHeader">
          <TooltipView placement="bottom" label={sTooltipLabel}>
            <div className={sClassName} onClick={this.handleExpandCollapsedClicked}></div>
          </TooltipView>
          {oIconDOM}
          <div className="expandableViewHeaderLabel">{oHeaderData.header}</div>
        </div>
    )
  };

  getBodyView = () => {
    let oBodyView = this.props.children;

    return this.state.isExpanded ? oBodyView : null;
  };

  render () {
    let sViewUUID = UniqueIdentifierGenerator.generateUUID();

    return(
        <div className="expandableView" key={"expandableView" + sViewUUID}>
          {this.getHeaderView()}
          {this.getBodyView()}
        </div>
    );
  }
}

ExpandableView.propTypes = oPropTypes;

export const view = ExpandableView;
export const events = oEvents;
