import ZoomScroll from 'diagram-js/lib/navigation/zoomscroll/ZoomScroll';

function CustomZoomScroll (config, eventBus, canvas) {
  ZoomScroll.call(this, config, eventBus, canvas)
}

CustomZoomScroll.prototype = ZoomScroll.prototype;
CustomZoomScroll.prototype.scroll = function scroll (delta) {
};
let _handleWheel = ZoomScroll.prototype._handleWheel;
CustomZoomScroll.prototype._handleWheel = function (event) {
  if (event.ctrlKey) {
    _handleWheel.call(this, event);
  }
};


CustomZoomScroll.$inject = [
  'config.zoomScroll',
  'eventBus',
  'canvas'
];

export const __init__ = ['zoomScroll'];
export default ['type', CustomZoomScroll];