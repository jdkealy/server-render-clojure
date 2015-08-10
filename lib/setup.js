var global = global || this;
var self = self || this;
var window = window || this;
var document = global.document || {};
window.XMLHttpRequest = function () {}
var console = global.console || {};
['error', 'log', 'info', 'warn'].forEach(function (fn) {
    if (!(fn in console)) {
          console[fn] = function () {};
            }
});
