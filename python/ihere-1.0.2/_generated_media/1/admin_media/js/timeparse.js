var timeParsePatterns=[{re:/^\d{1,2}$/i,handler:function(a){if(a[0].length==1){return"0"+a[0]+":00"}else{return a[0]+":00"}}},{re:/^\d{2}[:.]\d{2}$/i,handler:function(a){return a[0].replace(".",":")}},{re:/^\d[:.]\d{2}$/i,handler:function(a){return"0"+a[0].replace(".",":")}},{re:/^(\d+)\s*([ap])(?:.?m.?)?$/i,handler:function(b){var a=parseInt(b[1]);if(a==12){a=0}if(b[2].toLowerCase()=="p"){if(a==12){a=0}return(a+12)+":00"}else{if(a<10){return"0"+a+":00"}else{return a+":00"}}}},{re:/^(\d+)[.:](\d{2})\s*([ap]).?m.?$/i,handler:function(c){var a=parseInt(c[1]);var b=parseInt(c[2]);if(b<10){b="0"+b}if(a==12){a=0}if(c[3].toLowerCase()=="p"){if(a==12){a=0}return(a+12)+":"+b}else{if(a<10){return"0"+a+":"+b}else{return a+":"+b}}}},{re:/^no/i,handler:function(a){return"12:00"}},{re:/^mid/i,handler:function(a){return"00:00"}}];function parseTimeString(d){for(var a=0;a<timeParsePatterns.length;a++){var c=timeParsePatterns[a].re;var b=timeParsePatterns[a].handler;var e=c.exec(d);if(e){return b(e)}}return d};