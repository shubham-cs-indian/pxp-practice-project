import CS from '../../libraries/cs';
import React from 'react';
import ReactDom from 'react-dom';
import CircularProgress from '@material-ui/core/CircularProgress';
import ReactPropTypes from 'prop-types';
import RequestMapping from '../../libraries/requestmappingparser/request-mapping-parser.js';
import {HASH_SYMBOL_CODE} from '../tack/view-library-constants';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
const POLLING_COUNT = 5;

const oEvents = {};

const oPropTypes = {
  imageSrc: ReactPropTypes.string,
  classLabel: ReactPropTypes.string,
  typeClassLabel: ReactPropTypes.string,
  onLoad: ReactPropTypes.func,
  onError: ReactPropTypes.func,
  alt: ReactPropTypes.string,
  srcSet: ReactPropTypes.string,
  sizes: ReactPropTypes.string,
  shouldPoll: ReactPropTypes.bool,
  previewImageSrc: ReactPropTypes.string,
  imgKey: ReactPropTypes.string
};
/**
 * @class ImageSimpleView - use for display image in PIM, DAM profile edit.
 * @memberOf Views
 * @property {string} [imageSrc]- path of the image.
 * @property {string} [classLabel] - classname of image.
 * @property {string} [typeClassLabel] - type of thumbnail preview.
 * @property {func} [onLoad] -  function which is used on load event fire.
 * @property {func} [onError] -  function which is used on error event fire.
 * @property {string} [alt] - lternate name for image.
 * @property {string} [srcSet] - srcSet of image.
 * @property {string} [sizes] -  string of size of image.
 * @property {bool} [shouldPoll] -  boolean value of shoulPoll or not.
 */

// @CS.SafeComponent
class ImageSimpleView extends React.Component {

  constructor(props) {
    super(props);

    this.count = 0;

    this.state = {
      originalSrc: this.props.imageSrc,
      imageSrc: this.props.imageSrc,
      renderImage: false,
      loaded: false,
      imgKey: this.props.imgKey || UniqueIdentifierGenerator.generateUUID()
    }
  }

  shouldComponentUpdate =(oNextProps, oNextState)=> {

    //todo: onError function was not equal to the old onError function even when function was same. Probably because of changes in the bound 'this' object. Hence comparing the non-function properties separately instead of using CS.isEqual.

    var bArePropsDifferent = true;
    if ((oNextProps.alt === oNextProps.alt) &&
        (oNextProps.classLabel === oNextProps.classLabel) &&
        (oNextProps.imageSrc === oNextProps.imageSrc) &&
        (oNextProps.sizes === oNextProps.sizes) &&
        (oNextProps.srcSet === oNextProps.srcSet) &&
        (oNextProps.typeClassLabel === oNextProps.typeClassLabel)) {
      bArePropsDifferent = false;
    }

    if(!CS.isEqual(this.props.imgKey, oNextProps.imgKey)){
      bArePropsDifferent = true;
    }

    return bArePropsDifferent || !CS.isEqual(oNextState, this.state);

    /*
     var oPreviousProp = this.props;
     if (oPreviousProp.imageSrc != oNextProps.imageSrc ||/!* this.reRender*!/) {
      // this.reRender = false;
      return true;
    }
    return false;*/
  }

  /*componentWillReceiveProps =(oNextProps)=> {
    if(this.state.originalSrc != oNextProps.imageSrc) {
      this.resetState({
          originalSrc: oNextProps.imageSrc,
          imageSrc: oNextProps.imageSrc,
          renderImage: false
      });
    }
  }*/

  static getDerivedStateFromProps(oNextProps, oState){
    if(oState.originalSrc !== oNextProps.imageSrc) {
      return {
        originalSrc: oNextProps.imageSrc,
        imageSrc: oNextProps.imageSrc,
        renderImage: false
      };
    }

    return null;
  }

  resetState =(oObj)=> {
    // this.reRender = true;
    this.setState(oObj);
  }

  getImageDom =()=> {
    return ReactDom.findDOMNode(this);
  }

  handleImageOnLoad =(oEvent)=> {
    if(this.props.onLoad instanceof Function){
      this.props.onLoad.call(this, oEvent)
    }
    var oDOM = this.getImageDom();
    oDOM.classList.remove('hiddenImage');

    this.setState({
      loaded: true,
      renderImage: true,
    });
  }

  handleImageOnError = (oEvent) => {
    if (!CS.isEmpty(this.props.imageSrc) && this.props.onError instanceof Function) {
      this.props.onError.call(this, oEvent)
    }

    if (this.props.shouldPoll && this.count <= POLLING_COUNT) {
      this.count++;
      this.setState({
        // imageSrc: "",
        renderImage: true,
        imgKey: UniqueIdentifierGenerator.generateUUID(),
      });
    }
    else {
      this.setState({
        imageSrc: this.props.previewImageSrc ? this.props.previewImageSrc : "",
        renderImage: true
      });
    }
  };

  isAssetNotFoundException =(oResponse)=> {
    var oFailure = oResponse.failure;
    if (oFailure) {
      var oExceptionDetails = oFailure.exceptionDetails;
      return (oExceptionDetails && oExceptionDetails[0].key === "AssetObjectNotFoundException");
    }
    return false;
  }

  getLoadingStatus =(sType, sId, iCount, oResponse)=> {
    var _this = this;
    sId = sId.replace(HASH_SYMBOL_CODE, '');
    var sImageSrc = _this.props.imageSrc;
    if((!CS.isEmpty(oResponse) && !oResponse.failure) || iCount == 2/*oResponse.status == 100*/) {
      var sNewImageSrc = sImageSrc.replace(HASH_SYMBOL_CODE, "");
      _this.resetState({
        imageSrc: sNewImageSrc,
        renderImage: true
      });
    } else if (_this.isAssetNotFoundException(oResponse)) {
      _this.resetState({
        imageSrc: "",
        renderImage: true
      });
    } else if(CS.isEmpty(oResponse) || oResponse.failure){
      iCount++;
      setTimeout(function () {
        return CS.customGetRequest((RequestMapping.getRequestUrl(sImageSrc, {type: sType, id: sId}) + "/"), null,
            _this.getLoadingStatus.bind(_this, sType, sId, iCount), _this.getLoadingStatus.bind(_this, sType, sId, iCount), null,{}, true);
      }, 5000);
    }
    return true;
  }

/*
  getImageAjaxCall =(sType, sId)=> {
    var _this = this;
    sId = sId.replace(HASH_SYMBOL_CODE, '');
    var sImageSrc = _this.props.imageSrc;
    var iCount = 0;
    return CS.customGetRequest((RequestMapping.getRequestUrl(sImageSrc, {type: sType, id: sId}) + "/"), null,
        _this.getLoadingStatus.bind(_this, sType, sId, iCount), _this.getLoadingStatus.bind(_this, sType, sId, iCount), null,{}, true);

  }
*/

  getView = (sImageSrc) => {
    let sPreviewImageSrc = this.props.previewImageSrc ? this.props.previewImageSrc : "";
    if(CS.isEmpty(sImageSrc)){
      sImageSrc = CS.isNotEmpty(sPreviewImageSrc) ?  sPreviewImageSrc  : "";
    }
    var aSplittedSrcArray = sImageSrc.split('/');
    var sId = !CS.isEmpty(aSplittedSrcArray) && aSplittedSrcArray[aSplittedSrcArray.length - 1];
    if (sId == "") {
      sId = !CS.isEmpty(aSplittedSrcArray) && aSplittedSrcArray[aSplittedSrcArray.length - 2];
    }

    var sClassName = this.props.classLabel + " imageSimpleViewImage ";
    var sAlt = this.props.alt || "";
    var sSrcSet = this.props.srcSet || "";
    var sSizes = this.props.sizes || "";
    var oCircularLoadingDOM = null;
    let sKey = this.props.imgKey || this.state.imgKey;
    let sSrc = CS.isNotEmpty(this.state.imageSrc) ? this.state.imageSrc : "";

    let oImageDom = (<img key={sKey}
                          className={sClassName}
                          onError={this.handleImageOnError}
                          onLoad={this.handleImageOnLoad}
                          src={sSrc}
                          alt={sAlt}
                          srcSet={sSrcSet}
                          sizes={sSizes}
    />);
    if (!this.state.loaded && !CS.isEmpty(sId)) {
      //this.getImageAjaxCall(aSplittedSrcArray[1], sId);
      // this.getLoadingStatus(aSplittedSrcArray[1], sId, {});
      oCircularLoadingDOM = (<div className="loadingWrapper"><CircularProgress/></div>);
    } else if (CS.isEmpty(sId) && this.state.imageSrc === "") {
      sClassName = " imageSimpleViewNoImage ";
      if (this.props.typeClassLabel) {
        sClassName += "imageWrapper " + this.props.typeClassLabel;
      }
      return (
          <div className={sClassName}></div>
      );
    }

    return (
        <React.Fragment>
          {oCircularLoadingDOM}
          {oImageDom}
        </React.Fragment>
    )
  }

  render() {
    return this.getView(this.state.imageSrc);
  }
}


ImageSimpleView.propTypes = oPropTypes;

export const view = ImageSimpleView;
export const events = oEvents;
