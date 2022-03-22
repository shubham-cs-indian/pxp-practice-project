import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';
import { view as CustomPopoverView } from '../customPopoverView/custom-popover-view';
import { getTranslations as oTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import { view as ImageFitToContainerView } from './../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';

const oPropTypes = {
  selectedOptions: ReactPropTypes.array,
  store: ReactPropTypes.object,
  showMoreLabel: ReactPropTypes.bool,
  referencedData: ReactPropTypes.object
};

/**
 * @class GroupMssChipsView
 * @memberOf Views
 * @property {object} [selectedOptions] - Options of groups which is already selected.
 * @property {object} [store] - Group Json data to show on context menu.
  */

// @CS.SafeComponent
class GroupMssChipsView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      moreChips: undefined,
      showMoreChips: false
    }
    this.groupChipsCount = React.createRef();
    this.triggerElement = React.createRef();
  }

  setRef = (sRef, element) => {
    this[sRef] = element;
  };

  getIconView = (oData) => {
    let oIconDom = null;

    if (CS.isNotEmpty(oData.iconKey)) {
      let sSrcURL = ViewUtils.getIconUrl(oData.iconKey);
      oIconDom = (
          <div className="customIcon">
            <ImageFitToContainerView imageSrc={sSrcURL}/>
          </div>
      );
    }
    else if (oData.customIconClassName) {
      oIconDom = <div className={"customIcon " + oData.customIconClassName} key="classIcon"></div>;
    }

    return oIconDom;
  };

  /**
   * Function to create dom of chips for already selected options
   */
  getActiveChips = () => {
    let aActiveOptionsDom = [];
    CS.forEach(this.props.selectedOptions, opt => {
      let oOptionData = (this.props.store && this.props.store.getGroupsDataMap(opt.id)) || (this.props.referencedData && this.props.referencedData[opt.id]);
      if (oOptionData) {
        let sClassName = 'groupChips';
        let sLabelOrCode = CS.getLabelOrCode(oOptionData);
        let dom =
        (<div className={sClassName} key={oOptionData.id} ref={this.setRef.bind(this, oOptionData.id)}>
          {this.getIconView(oOptionData)}
          <TooltipView placement={"bottom"} label={oOptionData.toolTip || sLabelOrCode}>
            <div className="selectedItemLabel">{sLabelOrCode}</div>
          </TooltipView>
            {this.props.disabled ? null : <div className="crossIcon" onClick={() => this.props.handleRemove(oOptionData)}></div>}
          </div>);
        aActiveOptionsDom.push(dom);
      }
    })
    let oChipsCountDom = (
            <div className="groupChips hideMe groupChipsCount" onClick={this.showMoreChipsPopup} ref={this.setRef.bind(this, "groupChipsCount")}>
              <div className="groupChipsCountLabel" ref={this.setRef.bind(this, "groupChipsCountLabel")}></div>
              {this.props.showMoreLabel ? <div className="moreFiltersExpand"></div> : null}
            </div>
        );

    aActiveOptionsDom.push(oChipsCountDom);
    return aActiveOptionsDom;
  }

  handleRemove = (chip) => {
    let aMoreChips = this.state.moreChips;
    CS.remove(aMoreChips, (option) => {
      return chip.id == option.id
    })
    let oStateObj = {
      moreChips: aMoreChips
    }
    if (aMoreChips.length == 0) oStateObj.showMoreChips = false;
    this.setState(oStateObj)
    this.props.handleRemove(chip)
  }

  getMoreChipsView = () => {
    let aChips = this.state.moreChips;
    let aMoreChipsDom = [];
    CS.each(aChips, chip=> {
      let sLabelOrCode = CS.getLabelOrCode(chip);
      aMoreChipsDom.push(
          <div className="moreChipsWrapper">
            <TooltipView placement={"bottom"} label={chip.toolTip || sLabelOrCode}>
            <div key={chip.id} className="moreChips">{sLabelOrCode} </div>
            </TooltipView>
            <div className="crossIcon" onClick={() => this.handleRemove(chip)}></div>
          </div>)
    })
    return (
    <CustomPopoverView 
      className="groupMoreChips popover-root"
      open={this.state.showMoreChips}
      anchorEl={this.state.anchorEl}
      anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      transformOrigin={{ horizontal: 'right', vertical: 'top' }}
      onClose={this.hideMoreChipsPopup}
      >
      {aMoreChipsDom}
    </CustomPopoverView>)
  }

  /**
   * Function to update the chips wdth based on parent width
   */
  updateDomVisibility = () => {
    let aSelectedOption = this.props.selectedOptions;
    if (aSelectedOption && this.groupActiveChips) {
      CS.forEach(aSelectedOption, (oObj) => {
        let oOption = this[oObj.id];
        if (oOption) {
          oOption.classList.remove('hideMe');
        }
      });
      let iWrapperWidth = this.groupActiveChips.offsetWidth;
      this.groupChipsCount.classList.remove("hideMe");
      this.groupChipsCountLabel.innerHTML = `+ ${aSelectedOption.length}`;
      let iChipsStyle = getComputedStyle(this.groupChipsCount);
      let iChipsMargin = parseInt(iChipsStyle.marginLeft) + parseInt(iChipsStyle.marginRight);
      let iChipsCount = this.groupChipsCount.offsetWidth + iChipsMargin;
      let iTotalWidth = iWrapperWidth - iChipsCount;
      let iCounter = 0;
      CS.forEach(aSelectedOption, (oObj) => {
        let oOption = this[oObj.id];
        if (oOption) {
          oOption.classList.remove('hideMe');
          let iOptionStyle = getComputedStyle(oOption);
          let iOptionMargin = parseInt(iOptionStyle.marginLeft) + parseInt(iOptionStyle.marginRight);
          let iOptionWidth = oOption.offsetWidth + iOptionMargin;
          iTotalWidth = iTotalWidth - iOptionWidth;
          if (iTotalWidth > 0) {
            iCounter = iCounter + 1;
          } else {
            oOption.classList.add('hideMe');
          }
        }
      });
      if (iCounter < aSelectedOption.length) {
        this.hiddenChips = CS.clone(aSelectedOption).slice(iCounter);
        let sMoreLabel = this.props.showMoreLabel ? oTranslations().MORE : "";
        this.groupChipsCountLabel.innerHTML = `+ ${aSelectedOption.length - iCounter} ` + sMoreLabel;
      } else {
        this.groupChipsCount.classList.add("hideMe");
      }
    }
  }

  componentDidUpdate = () => {
    setTimeout(() => {
      if (this.props.hideChips !== true) this.updateDomVisibility();
    })
  }

  componentDidMount = () => {
    setTimeout(() => {
      if (this.props.hideChips !== true) this.updateDomVisibility();
    })
  }

  showMoreChipsPopup = (oEvent) => {
    let aHiddenChips = [];
    CS.each(this.hiddenChips, chip => {
      let oOptionData = (this.props.store && this.props.store.getGroupsDataMap(chip.id))
      || this.props.referencedData[chip.id];
      if (oOptionData) aHiddenChips.push(oOptionData);
    })
    let oDOM = this.triggerElement.current;
    this.setState({
      showMoreChips: true,
      anchorEl: oDOM,
      moreChips: aHiddenChips
    })
  }

  hideMoreChipsPopup = () => {
    this.setState({
      showMoreChips: false
    });
  }

  render() {
    let oActiveChips = null
    if (this.props.hideChips !== true) oActiveChips = this.getActiveChips();
    return (
      <div className="groupActiveChips" ref={this.setRef.bind(this, 'groupActiveChips')}>
        {oActiveChips}
        <div className="triggerElement" ref={this.triggerElement} >
        </div>
        {this.getMoreChipsView()}
      </div>
    )
  }
}

GroupMssChipsView.propTypes = oPropTypes;

export const view = GroupMssChipsView;
export const propTypes = oPropTypes;
