import React, { Component } from "react";
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';
import CommonUtils from "../../commonmodule/util/common-utils";

export default class HorizontalSlider extends Component {
    static propTypes = {
        elements: ReactPropTypes.array.isRequired,
        prevButtonStyle: ReactPropTypes.object,
        nextButtonStyle: ReactPropTypes.object,
        listViewStyle: ReactPropTypes.object
    };

    constructor(props) {
        super(props);

        this.iWidthDifference = 0; /** difference in width of slideSection and sliderList */
        this.scrollContainer = React.createRef();
        this.sliderListView = React.createRef();
        this.leftScrollButton = React.createRef();
        this.rightScrollButton = React.createRef();
    }

    componentDidMount = () => {
        !CS.isEmpty(this.props.elements) && this.handleScrollButtonVisibility();
    };

    componentDidUpdate = () => {
        !CS.isEmpty(this.props.elements) && this.handleScrollButtonVisibility();
    };

/*
    getChildrenWidth = oSliderListDOM => {
        let nWidth = 0;
        for (let i = 0; i < oSliderListDOM.children.length; i++) {
            nWidth += oSliderListDOM.children[i].offsetWidth;
        }
        return nWidth;
    }
*/

    handleScrollButtonVisibility = () => {
        try {
            //todo: may have to be called on window resize!
            let oScrollContainerDOM = this.scrollContainer.current;
            /** The Container */
            let iScrollContainerWidth = Math.ceil(oScrollContainerDOM.offsetWidth) || 0;
            let oSliderListDOM = this.sliderListView.current;
            /** The inner section */
            let iSliderListWidth = Math.ceil(oSliderListDOM.offsetWidth) || 0;//Math.ceil(this.getChildrenWidth(oSliderListDOM)) || 0; //Math.ceil(oSliderListDOM.offsetWidth) || 0;

            let oLeftScrollButton = this.leftScrollButton.current;
            let oRightScrollButton = this.rightScrollButton.current;
            oLeftScrollButton.style.visibility = "hidden";
            oRightScrollButton.style.visibility = "hidden";

            if (iSliderListWidth > iScrollContainerWidth) {
                // this.iWidthDifference = iSliderListWidth - iScrollContainerWidth;
                /** all possible 'left' values of the oSliderListDOM will lie between 0px and -(iWidthDifference)px */
                let iSliderListLeft = Math.ceil(oScrollContainerDOM.scrollLeft) || 0;
                if (iSliderListLeft) {
                    //left button visible
                    oLeftScrollButton.style.visibility = "visible";
                }
                if ((iSliderListLeft + iScrollContainerWidth) < iSliderListWidth) {
                    //right button visible
                    oRightScrollButton.style.visibility = "visible";
                }
            }
        } catch (oException) {
            ExceptionLogger.error("Something went wrong in 'handleScrollButtonVisibility' (File" + // eslint-disable-line
              " - horizontal-slider) : ");
            ExceptionLogger.error(oException); // eslint-disable-line
        }
    };

    handleScrollButtonClicked = (bScrollLeft) => {
        let iScrollByWidth = 200; /** HARDCODED ALERT */
        let oScrollContainer = this.scrollContainer.current;
        let oSliderListDOM = this.sliderListView.current;
        let iCurrentLeft = Math.ceil(oScrollContainer.scrollLeft);
        let iNewLeft = 0;

        /**
         * As per scrollBy requirement,
         * 1. for scrolling left, -ve value is expected,
         * 2. for scrolling right, +ve value is expected.
         *
         * if the scrolling width exceeds 200, then set the width as 200, else continue with width.
         * */
        let iSliderListWidth = Math.ceil(oSliderListDOM.offsetWidth) || 0;//Math.ceil(this.getChildrenWidth(oSliderListDOM)) || 0;
        if (bScrollLeft) {
            iNewLeft = (iCurrentLeft + Math.ceil(oScrollContainer.offsetWidth)) - Math.ceil(iSliderListWidth);
            if ((Math.abs(iNewLeft) > iScrollByWidth) || Math.abs(iNewLeft) == 0) {
                iNewLeft = -iScrollByWidth;
            };
        } else {
            iNewLeft = Math.ceil(iSliderListWidth) - (iCurrentLeft + Math.ceil(oScrollContainer.offsetWidth));
            iNewLeft = (iNewLeft > iScrollByWidth) ? iScrollByWidth : iNewLeft;
        }
        CommonUtils.scrollBy(oSliderListDOM.parentElement, { left: iNewLeft }, "", this.handleScrollButtonVisibility);
    };

    render() {
        let { elements, prevButtonStyle, nextButtonStyle, listViewStyle } = this.props;
        return (<div className="horizontalSliderView">
            <div className="horizontalSliderSection">
                <div className="scrollButton left" style={prevButtonStyle || {}} ref={this.leftScrollButton}
                    onClick={this.handleScrollButtonClicked.bind(this, true)}>
                </div>
                <div className="horizontalParentContainer" ref={this.scrollContainer}>
                    <div className="horizontalListView scrollable" style={listViewStyle || {}} ref={this.sliderListView}>
                        {elements}
                    </div>
                </div>
                <div className="scrollButton right" style={nextButtonStyle || {}} ref={this.rightScrollButton}
                    onClick={this.handleScrollButtonClicked.bind(this, false)}></div>
            </div>
        </div>)
    }
}
