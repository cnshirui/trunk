function relative_time(time_value) {
	var time_correct = time_value.split('+0000');
	var parsed_date = Date.parse(time_correct[0]+'GMT+0000'+time_correct[1]);
	var relative_to = new Date();
	var delta = parseInt((relative_to.getTime() - parsed_date) / 1000);
	if(delta < 60) return 'less than a minute ago';
	else if(delta < 120) return '大约 1 分钟';
	else if(delta < (45*60)) return '约 '+(parseInt(delta / 60)).toString() + ' 分钟';
	else if(delta < (90*60)) return '大约 1 小时';
	else if(delta < (24*60*60)) return '约 ' + (parseInt(delta / 3600)).toString() + ' 小时';
	else if(delta < (48*60*60)) return '大约 1 天';
	else return (parseInt(delta / 86400)).toString() + ' 天前';
}

