
// TODO: What to do @akash-cs
// Object.defineProperty(exports, "__esModule", { value: true });
function getRootElement(doc) {
  return (doc.documentElement || doc.body.parentNode || doc.body);
}
function getScrollTop(win) {
  if (win === void 0) { win = window; }
  return (win.pageYOffset !== undefined) ? win.pageYOffset : getRootElement(win.document).scrollTop;
}
function hasScrollBar(el) {
  return el.clientHeight !== el.scrollHeight && el.tagName !== 'BODY' && el.tagName !== 'HTML';
}
function findAncestors(startEl) {
  var result = [];
  var currentEl = startEl.parentElement;
  while (currentEl) {
    result.push(currentEl);
    currentEl = currentEl.parentElement;
  }
  return result;
}
function findScrollableAncestors(startEl) {
  return findAncestors(startEl).filter(hasScrollBar);
}
function scrollIntoView(targetEl, windowTopOffset, localTopOffset) {
  if (windowTopOffset === void 0) { windowTopOffset = 0; }
  if (localTopOffset === void 0) { localTopOffset = 0; }
  if (!windowTopOffset) {
    targetEl.scrollIntoViewIfNeeded();
    return;
  }
  var pos = targetEl.getBoundingClientRect();
  var scrollableAncestors = findScrollableAncestors(targetEl);
  if (scrollableAncestors.length <= 2) {
    scrollableAncestors.forEach(function (scrollableOuterContainer) {
      var containerPos = scrollableOuterContainer.getBoundingClientRect();
      if (pos.top < containerPos.top + localTopOffset || pos.bottom > containerPos.bottom) {
        scrollableOuterContainer.scrollTop = pos.top - containerPos.top - localTopOffset;
      }
    });
  }
  var scrollTop = getScrollTop();
  if (pos.top < windowTopOffset || pos.bottom > window.innerHeight) {
    window.scrollTo(0, scrollTop + pos.top - windowTopOffset);
  }
}
export default scrollIntoView;
//# sourceMappingURL=scrolling.js.map