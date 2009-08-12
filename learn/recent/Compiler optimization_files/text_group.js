var AdBrite_Title_Color_Default = '0000ff';
var AdBrite_Text_Color_Default = '000000';
var AdBrite_Background_Color_Default = 'FFFFFF';
var AdBrite_Border_Color_Default = 'FFFFFF';
var AdBrite_URL_Color_Default = '0000ff';
var AdBrite_Title_Color,AdBrite_Text_Color,AdBrite_Background_Color,AdBrite_Border_Color,AdBrite_URL_Color;
AdBrite_Title_Color = AdBrite_IAB_Zone_Test_Color(AdBrite_Title_Color);
AdBrite_Text_Color = AdBrite_IAB_Zone_Test_Color(AdBrite_Text_Color);
AdBrite_Background_Color = AdBrite_IAB_Zone_Test_Color(AdBrite_Background_Color);
AdBrite_Border_Color = AdBrite_IAB_Zone_Test_Color(AdBrite_Border_Color);
AdBrite_URL_Color = AdBrite_IAB_Zone_Test_Color(AdBrite_URL_Color);
var AdBrite_Title_Color_Processed = (AdBrite_Title_Color) ? AdBrite_Title_Color : AdBrite_Title_Color_Default;
var AdBrite_Text_Color_Processed = (AdBrite_Text_Color) ? AdBrite_Text_Color : AdBrite_Text_Color_Default;
var AdBrite_Background_Color_Processed = (AdBrite_Background_Color) ? AdBrite_Background_Color : AdBrite_Background_Color_Default;
var AdBrite_Border_Color_Processed = (AdBrite_Border_Color) ? AdBrite_Border_Color : AdBrite_Border_Color_Default;
var AdBrite_URL_Color_Processed = (AdBrite_URL_Color) ? AdBrite_URL_Color : AdBrite_URL_Color_Default;
function AdBrite_IAB_Zone_Test_Color(color) {
if (typeof(color) != 'string') return false;
if (!color.match(/^[0-9A-Fa-f]{6}$/) && !color.match(/^[0-9A-Fa-f]{3}$/)) return false;
return color;
}
AdBrite_Title_Color = '';
AdBrite_Text_Color = '';
AdBrite_Background_Color = '';
AdBrite_Border_Color = '';
AdBrite_URL_Color = '';

var ADBRITE_setIFrameContent;

if (!ADBRITE_setIFrameContent) {
	ADBRITE_setIFrameContent = [];
}

function AdBriteRender_3698f1e2_96eb_47b4_a3ef_23c986dd09d2() {
	var frame = frames.AdBriteFrame_3698f1e2_96eb_47b4_a3ef_23c986dd09d2;
	if (frame && frame.document) {
		frame.document.open();
		frame.document.writeln("<html><head><style type='text/css'>");
		frame.document.writeln("body {background-color:#" + AdBrite_Background_Color_Processed + ";font-family:Arial,Helvetica,sans-serif;margin:0;}");
		frame.document.writeln("table {border:1px solid #" + AdBrite_Border_Color_Processed + ";width:468px;height:60px;padding:0;margin:0;overflow:hidden;display:block;}");
		frame.document.writeln("td {vertical-align:middle;text-align:center;font-size:14px;padding:2px 6px;height:54px;width:466px;}");
		frame.document.writeln("td a {text-decoration:none;}");
		frame.document.writeln("td span {display:block;overflow:hidden;}");
		frame.document.write("<\/style><\/head><body><table cellpadding='0' cellspacing='0'><tr><td><span style=''><a href='http://click.adbrite.com/mb/click.php?sid=1008261&banner_id=12691208&variation_id=&uts=1250004763&keyword_id=&ab=168296534&sscup=8ab0f5226632ec215ed88d04edf3d75b&sscra=db743d663956890313f3d0042602a81d&ub=1961038247&guid=6c677656-fa3a-4005-8500-50085fb7fe28&odc=svx&rs=&r=' style='color:#" + AdBrite_Title_Color_Processed + ";font-weight:bold;text-decoration:underline;' target='_top'>Advertise on this site<\/a><\/span><span style=''><a href='http://click.adbrite.com/mb/click.php?sid=1008261&banner_id=12691208&variation_id=&uts=1250004763&keyword_id=&ab=168296534&sscup=8ab0f5226632ec215ed88d04edf3d75b&sscra=db743d663956890313f3d0042602a81d&ub=1961038247&guid=6c677656-fa3a-4005-8500-50085fb7fe28&odc=svx&rs=&r=' style='color:#" + AdBrite_Text_Color_Processed + ";' target='_top'>Powered By AdBrite<\/a><\/span><\/td><\/tr><\/table><a href='http://www.adbrite.com/mb/?s_cid=aba&sid=1008261' style='font-size:11px;text-decoration:none;color:#;position:absolute;bottom:1px;right:2px;z-index:1;' target='_top'>Ads by <b>AdBrite<\/b><\/a><\/body><\/html>");
		frame.document.close();
	}
}

ADBRITE_setIFrameContent.push(AdBriteRender_3698f1e2_96eb_47b4_a3ef_23c986dd09d2);

if (!document.body) document.write('<body>');
document.write("<iframe name='AdBriteFrame_3698f1e2_96eb_47b4_a3ef_23c986dd09d2' width='468' height='60' frameborder='0' scrolling='no'>");
document.write('</iframe>');
document.writeln('<script type="text/javascript"><!--');
document.writeln('AdBriteRender_3698f1e2_96eb_47b4_a3ef_23c986dd09d2();')
document.write('//--></script>');
