steal.dev={regexps:{colons:/::/,words:/([A-Z]+)([A-Z][a-z])/g,lowerUpper:/([a-z\d])([A-Z])/g,dash:/([a-z\d])([A-Z])/g},underscore:function(b){var a=this.regexps;return b.replace(a.colons,"/").replace(a.words,"$1_$2").replace(a.lowerUpper,"$1_$2").replace(a.dash,"_").toLowerCase()},isHappyName:function(b){for(var a=steal.cur().path.replace(/\.[^$]+$/,"").split("/"),d=b.split("."),c=0;c<d.length&&a.length;c++)a[c]&&d[c].toLowerCase()!=a[c]&&this.underscore(d[c])!=a[c]&&this.underscore(d[c])!=a[c].replace(/_controller/,
"")&&this.warn("Are you sure "+b+" belongs in "+steal.cur().path)},logLevel:0,warn:function(b){2>steal.config().logLevel&&(Array.prototype.unshift.call(arguments,"steal.js WARN:"),window.console&&console.warn?this._logger("warn",Array.prototype.slice.call(arguments)):window.console&&console.log?this._logger("log",Array.prototype.slice.call(arguments)):window.opera&&window.opera.postError&&opera.postError("steal.js WARNING: "+b))},log:function(b){1>steal.config().logLevel&&(window.console&&console.log?
(Array.prototype.unshift.call(arguments,"steal.js INFO:"),this._logger("log",Array.prototype.slice.call(arguments))):window.opera&&window.opera.postError&&opera.postError("steal.js INFO: "+b))},_logger:function(b,a){if(console.log.apply)console[b].apply(console,a);else console[b](a)}};