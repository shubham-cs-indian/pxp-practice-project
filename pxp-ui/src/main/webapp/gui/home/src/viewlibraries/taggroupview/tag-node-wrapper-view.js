import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import { view as TagNodeView } from './tag-node-view.js';
import TagGroupModel from './model/tag-group-model';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import { getTranslations as oTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import Arrow , { oCustomEvents } from '../../commonmodule/HOC/keyboard-navigation-for-lists';

const oPropTypes = {
  tagGroupModel: ReactPropTypes.instanceOf(TagGroupModel).isRequired,
  tagRanges: ReactPropTypes.object,
  tagValues: ReactPropTypes.array,
  extraData: ReactPropTypes.object,
  childView: ReactPropTypes.object,
  searchHandler: ReactPropTypes.func,
  loadMoreHandler: ReactPropTypes.func,
  onApply: ReactPropTypes.func,
  disabled: ReactPropTypes.bool,
  tagValuesList: ReactPropTypes.array,
  masterTag: ReactPropTypes.object,

  onKeyPressHandler: ReactPropTypes.func,
  registerCustomEvent: ReactPropTypes.func,
  itemInFocus: ReactPropTypes.number,
  setIndexMap: ReactPropTypes.func
};
/**
 * @class TagNodeWrapperView
 * @memberOf Views
 * @property {custom} tagGroupModel
 * @property {object} tagRanges
 * @property {array} [tagValues]
 * @property {object} [extraData]
 * @property {object} [childView]
 * @property {func} [searchHandler]
 * @property {func} [loadMoreHandler]
 * @property {func} [onApply]
 * @property {bool} [disabled]
 * @property {array} [tagValuesList]
 */

// @Arrow
// @CS.SafeComponent
class TagNodeWrapperView extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      showPopover: false,
      searchText: ""
    };

    this.oWrapperDivStyle = {
      overflow: "hidden",
      outline: "1px solid #DCDCDC",
      maxHeight: "380px"
    };

    this.triggerElement = React.createRef();
    this.tagValueHeaderContainer = React.createRef();
    this.tagsGroupContainer = React.createRef();
    this.updatePosition = CS.noop;

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  componentDidMount =()=> {
    this.calculateWidth();
    this.props.registerCustomEvent(oCustomEvents.END_OF_LIST, this.handleLoadMore);
  }

  componentDidUpdate =()=> {
    let calculatedWidth = this.calculateWidth.bind(this);
    setTimeout(calculatedWidth);
    this.state.showPopover && this.updatePosition();
  }

  calculateWidth =()=> {
    var oTagSectionHeaderView = this.tagsGroupContainer.current;
    if (oTagSectionHeaderView) {
      var oTagValueHeaderNode = this.tagValueHeaderContainer.current;
      if (oTagValueHeaderNode) {
        if (oTagSectionHeaderView.offsetWidth < 275) {
          oTagValueHeaderNode.classList.add('fullLengthTagValueHeader');
        } else {
          oTagValueHeaderNode.classList.remove('fullLengthTagValueHeader');
        }
      }
    }
  }

  closePopover =()=> {
    window.dontRaise = true; //Do not remove

    this.setState({
      searchText: "",
      showPopover: false
    });
  }

  openPopover =(oEvent)=> {
    if (!oEvent.nativeEvent.dontRaise && !this.props.disabled) {
      let oView = this.triggerElement.current;
      this.anchorElement = oView;
      if (oView) {
        this.oWrapperDivStyle.width = oView.clientWidth + "px";
        this.oWrapperDivStyle.minHeight = oView.clientHeight + "px";
      }
      this.setState({
        showPopover: true
      });
    }
  }

  handleChildContainerClicked =(iItemIndex, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    this.props.setItemInFocus(iItemIndex)
  }

  getTagValuesHeader =()=> {
    let __props = this.props;
    let oTagGroupModel = this.props.tagGroupModel;
    let sTagViewType = oTagGroupModel.properties['tagViewType'];
    let oView = null;

    if (!(this.state.collapsed || __props.disabled)) {
      if (sTagViewType === "radioGroup") {
        let aViewTagValueHeaders = CS.map(__props.tagValues, function (oDataValue) {
          return <div className="tagValueHeader" key={oDataValue.id}>{CS.getLabelOrCode(oDataValue)}</div>
        });
        oView = (<div className="tagValueHeaderContainer" ref={this.tagValueHeaderContainer}>{aViewTagValueHeaders}</div>);
      }
    }

    return oView;
  }

  handleSearchBoxClicked = () => {
    this.props.setItemInFocus(-1)
  };

  getTagValuesView =()=> {
    let _this = this;
    let __props = this.props;
    let oTagNodeModel = __props.tagGroupModel;
    let rPattern = this.getSearchTextRegExPattern();
    let aTagValuesList = __props.tagValuesList || [];

    let aTagNodeViews = [];

    let oEntityTag = oTagNodeModel.entityTag;
    let sTagViewType = oTagNodeModel.properties['tagViewType'];
    let oMasterTag = this.props.masterTag;

    let oChildTagIdVsRelevanceMap = {};

    CS.forEach(oEntityTag.tagValues, function (oTagValue) {
      let iRelevance = (sTagViewType != "doubleSlider") ? oTagValue.relevance : ViewUtils.getTagRelevanceForDoubleSlider(oTagValue);
      oChildTagIdVsRelevanceMap[oTagValue.tagId] = iRelevance;
    });

    this.props.setIndexMap({});
    let iItemIndex = 0;
    let oIndexMap = [];
    if (aTagValuesList && CS.size(aTagValuesList) > 0) {
      CS.forEach(aTagValuesList, function (oTagValue, iTagCount) {

        if (!CS.isFunction(__props.searchHandler) && rPattern && !rPattern.test(CS.getLabelOrCode(oTagValue))) {
          return;
        }

        let oTagGroupModelProperties = {
          context: oTagNodeModel.properties['context'],
          tagGroupId: oTagNodeModel.properties['tagGroupId'],
          entityId: oTagNodeModel.properties['entityId'],
          tagViewType: sTagViewType,
          color: oTagValue.color,
          icon: oTagValue.icon,
          iconKey: oTagValue.iconKey,
        };

        let iRelevance = oChildTagIdVsRelevanceMap[oTagValue.id] || 0;
        iRelevance = ViewUtils.getRoundedTagRelevanceValueByTagType(iRelevance, oMasterTag.tagType);
        let oChildModel = new TagGroupModel(oTagValue.id, oTagValue.id, CS.getLabelOrCode(oTagValue), {}, 1, iRelevance, oTagGroupModelProperties);

        /*
        if (!CS.find(aTagValuesList, {"id": oChildModel.id})) {
          return;
        }

        let bShowOrDiv = oChildModel.properties["showOrDiv");
        let oOrDivDOM = null;
        oChildModel.properties[] = 'extraData', oExtraData);
        if (bShowOrDiv) {
          let sOr = iTagCount > 0 ? oTranslations.OR : "";
          oOrDivDOM = <div className="tagConditionOrBlock">{sOr}</div>;
        }
        */

        let sRef = "contextMenuItem" +iTagCount;
        let sClassName = _this.props.itemInFocus == iTagCount ? 'tagChildNodeContainer inFocus' : 'tagChildNodeContainer';
        oIndexMap[iItemIndex] = iTagCount;
        iItemIndex++;
        aTagNodeViews.push(
            <div className={sClassName} onClick={_this.handleChildContainerClicked.bind(this, iTagCount)} ref={_this.setRef.bind(_this, sRef)}>

              <TagNodeView
                  childModel={oChildModel}
                  key={oTagValue.label + "_" + iTagCount}
                  tagRanges={__props.tagRanges}
                  tagValues={__props.tagValues}
                  onApply={__props.onApply}
                  showDefaultIcon={__props.showDefaultIcon}
              />
            </div>
        );
      });

      this.props.setIndexMap(oIndexMap);

      if (CS.isFunction(__props.loadMoreHandler)) {
        aTagNodeViews.push(
            <div onClick={this.handleLoadMore} className="menuItemLoadMore">{oTranslations().LOAD_MORE}</div>
        );
      }
    }

    let oTagValuesHeader = null;
    if (!CS.isEmpty(aTagNodeViews)) {
      oTagValuesHeader = _this.getTagValuesHeader()
    }
    else {
      aTagNodeViews.push(
          <div className="nothingFoundMessage">{oTranslations().NOTHING_FOUND}</div>
      );
    }

    return (
        <div className="tagsGroupContainer" ref={this.tagsGroupContainer}>
          {oTagValuesHeader}
          <div className="tagsSectionBody">
            {aTagNodeViews}
          </div>
        </div>
    );
  }

  handleLoadMore =()=> {
    if (CS.isFunction(this.props.loadMoreHandler)) {
      this.props.loadMoreHandler();
    }
  }

  getSearchTextRegExPattern =()=>{
    var sSearchText = this.state.searchText;
    sSearchText = (this.props.searchHandler) ? "" : ViewUtils.escapeRegexCharacters(sSearchText);
    return sSearchText ? new RegExp(sSearchText, "i") : null;
  }

  handleSearchTextChanged =(oEvent)=> {
    let sSearchedText = oEvent.target.value;
    this.setState({
      searchText: sSearchedText
    });
  }

  searchTextOnKeyDown =(oEvent)=> {
    if ((oEvent.keyCode === 13) && (CS.isFunction(this.props.searchHandler))) { //13 -> Enter key
      this.props.searchHandler(oEvent.target.value);
    }
  }

  handleSearchClearClicked =()=> {
    this.setState({
      searchText: ""
    });
    if (CS.isFunction(this.props.searchHandler)) {
      this.props.searchHandler("");
    }
  }

  handleTagNodeOnClick = () => {
    window.dontRaise = true; //Do not remove
  }

  getSearchBarView =()=> {
    let sSearchedText = this.state.searchText;
    let sContextMenuSearchClearClassName = "contextMenuSearchClear ";
    sContextMenuSearchClearClassName += sSearchedText ? "" : "noShow ";

    return (
        <div className="contextMenuSearchBarContainer">
          <div className="contextMenuSearch">
            <div className="contextMenuSearchIcon"/>
            <input className="contextMenuSearchInput"
                   value={this.state.searchText}
                   onKeyDown={this.searchTextOnKeyDown}
                   onChange={this.handleSearchTextChanged}
                   placeholder={oTranslations().SEARCH}
                   ref={this.setRef.bind(this, 'searchBox')}
                   onClick={this.handleSearchBoxClicked}
                   autoFocus={true}/>
            <div className={sContextMenuSearchClearClassName} onClick={this.handleSearchClearClicked}/>
          </div>
        </div>
    );
  };

  updatePopoverPosition = (fFunc) => {
    CS.isFunction(fFunc) && (this.updatePosition = fFunc);
  };

  render() {

    let oPopoverStyle = {
      width: "auto",
      height: "auto",
      overflow: "auto",
      maxWidth: "2300px"
    };

    let oWrapperDivStyleClone = CS.cloneDeep(this.oWrapperDivStyle);

    return (
        <div className="tagNodeWrapper">
          <CustomPopoverView className="popover-root tagNodePopover"
                   open={this.state.showPopover}
                   style={ oPopoverStyle}
                   animated={false}
                   anchorEl={this.anchorElement}
                   anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                   transformOrigin={{horizontal: 'left', vertical: 'top'}}
                   updatePosition={this.updatePopoverPosition}
                   onClose={this.closePopover}>
            <div style={oWrapperDivStyleClone} onClick={this.handleTagNodeOnClick} onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>
              {this.getSearchBarView()}
              <div className="tagValuesViewContainer" ref={this.setRef.bind(this, 'scrollbar')}>
                {this.getTagValuesView()}
              </div>
            </div>
          </CustomPopoverView>
          <div className="triggerElement" ref={this.triggerElement} onClick={this.openPopover}>
            {this.props.childView}
          </div>
        </div>
    );

  }

}

TagNodeWrapperView.propTypes = oPropTypes;

export const view = Arrow(TagNodeWrapperView);
