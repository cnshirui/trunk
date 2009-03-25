package {
	import flash.display.Sprite;
	import flash.net.URLRequest;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;
	import flash.net.URLRequestMethod;
	
//	import mx.uti

	public class TestRequest extends Sprite
	{
		public function TestRequest()
		{
			trace("start...");
//			var request:URLRequest = new URLRequest("http://www.google.com");
//			var queryStr:URLVariables = new URLVariables();
//			queryStr.random = Math.random();
//			request.data = queryStr;				
//			trace(request.url);

            var url:String = "http://shg-d-11-rshi:8080/designer_server/XcelsiusDoc";
            var request:URLRequest = new URLRequest(url);
            var variables:URLVariables = new URLVariables();
            variables.action = "setDocument";
            variables.document = "hello";
            request.data = variables;
            request.method = URLRequestMethod.POST;            
            navigateToURL(request);
//            navigateToURL("");


			trace("over...");
		}
	
	/*
		public function getXML:XML() {
			var xml:XML = <?xml version="1.0" encoding="utf-8"?>
<CXCanvas guid="2202450108-26755-4573-184-180-23921125186053" width="800" height="600"><ExportSettings useCurrent="-1" path=""/><font fontName="Verdana"><embed>0</embed></font><colorTheme>Nova</colorTheme><Notes><![CDATA[]]></Notes><colorScheme></colorScheme><component id="C4D44E46-1BAD-2601-724D-B78E3E8DB107" className="xcelsius.charts.PieChart" styleName="" displayName="Pie Chart 1"><colorScheme></colorScheme><properties>
			<property>
				<name>
					<string>title</string>
				</name>
				<value>
					<string>Pie Chart</string>

				</value>
			</property>
			<property>
				<name>
					<string>percentLabel</string>
				</name>
				<value>
					<string>Percent:</string>

				</value>
			</property>
			<property>
				<name>
					<string>subtitle</string>
				</name>
				<value>
					<string>Sample Sub Title</string>

				</value>
			</property>
			<property>
				<name>
					<string>width</string>
				</name>
				<value>
					<number>269</number>

				</value>
			</property>
			<property>
				<name>
					<string>height</string>
				</name>
				<value>
					<number>235</number>

				</value>
			</property>
			<property>
				<name>
					<string>minWidth</string>
				</name>
				<value>
					<number>25</number>

				</value>
			</property>
			<property>
				<name>
					<string>minHeight</string>
				</name>
				<value>
					<number>25</number>

				</value>
			</property>
			<property><name><string>x</string></name><value><number>78</number></value></property><property><name><string>y</string></name><value><number>72</number></value></property><property><name><string>dataNumberFormats</string></name><value><array><property id="0"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property></array></value></property><property><name><string>categoryLabelsNumberFormats</string></name><value><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></value></property><property><name><string>showLegend</string></name><value><false/></value></property></properties>
		<Persist><array><property id="0"><object><property id="name"><string>_isDataInRows</string></property><property id="value"><number>2.000000</number></property></object></property><property id="1"><object><property id="name"><string>_isDataPointExisted</string></property><property id="value"><true/></property></object></property><property id="2"><object><property id="name"><string>_mainTab</string></property><property id="value"><number>0.000000</number></property></object></property><property id="3"><object><property id="name"><string>_behaviorSubTab</string></property><property id="value"><number>1.000000</number></property></object></property><property id="4"><object><property id="name"><string>_dataRangeBinding</string></property><property id="value"><string>BOF44C4887B8A44F7DB93FA9037D3D9E54</string></property></object></property></array></Persist><bindings><property><name><string>categoryLabels</string></name><value><binding><endpoint type="excelbinding" id="BO5298444A42794DF58A781F803999E295" displayname="Sheet1!$B$2:$B$6"><workbook>[Object]</workbook><inputtype><string></string></inputtype><outputtype><string>xcelsius.binding.tableMaps.output.TableToArray</string></outputtype><range>Sheet1!$B$2:$B$6</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R2C2</string></property></array></property><property id="1"><array><property id="0"><string>Sheet1!R3C2</string></property></array></property><property id="2"><array><property id="0"><string>Sheet1!R4C2</string></property></array></property><property id="3"><array><property id="0"><string>Sheet1!R5C2</string></property></array></property><property id="4"><array><property id="0"><string>Sheet1!R6C2</string></property></array></property></array></property></array></cells><chain><array></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>output</string></direction></endpoint></binding>

				</value></property><property><name><string>data</string></name><value><binding><endpoint type="excelbinding" id="BO8717EE9BFF134E68BD1469902F3547C6" displayname="Sheet1!$C$2:$C$6"><workbook>[Object]</workbook><inputtype><string></string></inputtype><outputtype><string>xcelsius.binding.tableMaps.output.TableToArray</string></outputtype><range>Sheet1!$C$2:$C$6</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R2C3</string></property></array></property><property id="1"><array><property id="0"><string>Sheet1!R3C3</string></property></array></property><property id="2"><array><property id="0"><string>Sheet1!R4C3</string></property></array></property><property id="3"><array><property id="0"><string>Sheet1!R5C3</string></property></array></property><property id="4"><array><property id="0"><string>Sheet1!R6C3</string></property></array></property></array></property></array></cells><chain><array><property id="0"><object><property id="className"><string>xcelsius.binding.chain::ArrayChainAccessor</string></property><property id="properties"><array><property id="0"><object><property id="name"><string>index</string></property><property id="value"><number>0.000000</number></property></object></property></array></property></object></property></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>output</string></direction></endpoint></binding>
				</value></property></bindings></component><component id="10C3DB64-9258-E9E9-3D98-B78E5D609852" className="xcelsius.charts.cartesian.ColumnChart" styleName="" displayName="Column Chart 1"><colorScheme></colorScheme><properties>

			<property>
				<name>
					<string>title</string>
				</name>
				<value>
					<string>Column Chart</string>
				</value>
			</property>

			<property>
				<name>
					<string>subtitle</string>
				</name>
				<value>
					<string>Sample Sub Title</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextOn</string>
				</name>
				<value>
					<string>Grow</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextOnTip</string>
				</name>
				<value>
					<string>Enable Scale Growth</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextOff</string>
				</name>
				<value>
					<string>Off</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextOffTip</string>
				</name>
				<value>
					<string>Disable Scaling</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextAuto</string>
				</name>
				<value>
					<string>Auto</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextAutoTip</string>
				</name>
				<value>
					<string>Enable Full Automatic Scaling</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextFocusTip</string>
				</name>
				<value>
					<string>Focus Chart Data</string>
				</value>
			</property>

			<property>
				<name>
					<string>scaleTextResetTip</string>
				</name>
				<value>
					<string>Reset Chart Scale</string>
				</value>
			</property>

			<property>
				<name>
					<string>width</string>
				</name>
				<value>
					<number>273</number>
				</value>
			</property>

			<property>
				<name>
					<string>height</string>
				</name>
				<value>
					<number>235</number>
				</value>
			</property>

			<property>
				<name>
					<string>minWidth</string>
				</name>
				<value>
					<number>25</number>
				</value>
			</property>

			<property>
				<name>
					<string>minHeight</string>
				</name>
				<value>
					<number>25</number>
				</value>
			</property>

			<property>
				<name>
					<string>defaultSeriesName</string>
				</name>
				<value>
					<string>Series {0}</string>
				</value>
			</property>

			<property><name><string>x</string></name><value><number>362</number></value></property><property><name><string>y</string></name><value><number>72</number></value></property><property><name><string>seriesNames</string></name><value><array><property id="0"><string>Series1</string></property></array></value></property><property><name><string>axisBySeries</string></name><value><array><property id="0"><number>0.000000</number></property></array></value></property><property><name><string>selectedMarksBySeries</string></name><value><array><property id="0"><number>0.000000</number></property></array></value></property><property><name><string>data</string></name><value><array><property id="0"><array></array></property></array></value></property><property><name><string>dataNumberFormats</string></name><value><array><property id="0"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property></array></value></property><property><name><string>yLabelNumberFormat</string></name><value><string></string></value></property><property><name><string>yLabelNumberFormat2</string></name><value><string></string></value></property><property><name><string>seriesNamesNumberFormats</string></name><value><array><property id="0"><string></string></property></array></value></property><property><name><string>alertTarget</string></name><value><array><property id="0"><number>100.000000</number></property><property id="1"><number>100.000000</number></property><property id="2"><number>100.000000</number></property><property id="3"><number>100.000000</number></property><property id="4"><number>100.000000</number></property></array></value></property><property><name><string>categoryLabelsNumberFormats</string></name><value><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></value></property><property><name><string>categoryLabels</string></name><value><array></array></value></property></properties>

		<Persist><array><property id="0"><object><property id="name"><string>_isDataInRows</string></property><property id="value"><null/></property></object></property><property id="1"><object><property id="name"><string>_alertTargetDP</string></property><property id="value"><array><property id="0"><number>100.000000</number></property><property id="1"><number>100.000000</number></property><property id="2"><number>100.000000</number></property><property id="3"><number>100.000000</number></property><property id="4"><number>100.000000</number></property></array></property></object></property><property id="2"><object><property id="name"><string>_isSeriesExisted</string></property><property id="value"><true/></property></object></property><property id="3"><object><property id="name"><string>_isByRange</string></property><property id="value"><false/></property></object></property><property id="4"><object><property id="name"><string>_currentSeriesMaxNum</string></property><property id="value"><number>2.000000</number></property></object></property></array></Persist><bindings><property><name><string>data</string></name><value><binding><endpoint type="excelbinding" id="BOD1EAE86D55F64D4D893BF787A44FA827" displayname="Sheet1!$C$2:$C$6"><workbook>[Object]</workbook><inputtype><string></string></inputtype><outputtype><string>xcelsius.binding.tableMaps.output.TableToArray</string></outputtype><range>Sheet1!$C$2:$C$6</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R2C3</string></property></array></property><property id="1"><array><property id="0"><string>Sheet1!R3C3</string></property></array></property><property id="2"><array><property id="0"><string>Sheet1!R4C3</string></property></array></property><property id="3"><array><property id="0"><string>Sheet1!R5C3</string></property></array></property><property id="4"><array><property id="0"><string>Sheet1!R6C3</string></property></array></property></array></property></array></cells><chain><array><property id="0"><object><property id="className"><string>xcelsius.binding.chain::ArrayChainAccessor</string></property><property id="properties"><array><property id="0"><object><property id="name"><string>index</string></property><property id="value"><number>0.000000</number></property></object></property></array></property></object></property></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>output</string></direction></endpoint></binding>

				</value></property><property><name><string>categoryLabels</string></name><value><binding><endpoint type="excelbinding" id="BOEB045A5BA3974510BB9B7400AB5A5E22" displayname="Sheet1!$B$2:$B$6"><workbook>[Object]</workbook><inputtype><string></string></inputtype><outputtype><string>xcelsius.binding.tableMaps.output.TableToArray</string></outputtype><range>Sheet1!$B$2:$B$6</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R2C2</string></property></array></property><property id="1"><array><property id="0"><string>Sheet1!R3C2</string></property></array></property><property id="2"><array><property id="0"><string>Sheet1!R4C2</string></property></array></property><property id="3"><array><property id="0"><string>Sheet1!R5C2</string></property></array></property><property id="4"><array><property id="0"><string>Sheet1!R6C2</string></property></array></property></array></property></array></cells><chain><array></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>output</string></direction></endpoint></binding>
				</value></property></bindings></component><component id="D93B111F-CCD6-0CCB-6EB4-B78EF77DB526" className="xcelsius.controls.Label" styleName="" displayName="Label 1"><colorScheme></colorScheme><properties>
			<property>
				<name>

					<string>width</string>
				</name>
				<value>
					<number>345</number>
				</value>
			</property>
			<property>
				<name>

					<string>height</string>
				</name>
				<value>
					<number>34</number>
				</value>
			</property>
			<property>
				<name>

					<string>defaultText</string>
				</name>
				<value>
					<string>Beijing 2008</string></value>
			</property>
			<property><name><string>x</string></name><value><number>78</number></value></property><property><name><string>y</string></name><value><number>42</number></value></property></properties>

		<Persist><array><property id="0"><object><property id="name"><string>_mainTab</string></property><property id="value"><number>0.000000</number></property></object></property><property id="1"><object><property id="name"><string>_appearanceSubTab</string></property><property id="value"><number>1.000000</number></property></object></property></array></Persist><styles><property><name><string>fontFamily</string></name><value><string>Arial</string></value></property><property><name><string>fontSize</string></name><value><number>18.000000</number></value></property><property><name><string>fontWeight</string></name><value><string>bold</string></value></property><property><name><string>color</string></name><value><number>28864.000000</number></value></property></styles></component><component id="A2E8D41E-16DA-DF97-6625-B78F0B249B52" className="xcelsius.controls.Checkbox" styleName="" displayName="Check Box 1"><colorScheme></colorScheme><properties>
			<property>
				<name>

					<string>width</string>
				</name>
				<value>
					<number>20</number>
				</value>
			</property>
			<property>
				<name>

					<string>height</string>
				</name>
				<value>
					<number>20</number>
				</value>
			</property>
			<property>
				<name>

					<string>minWidth</string>
				</name>
				<value>
					<number>5</number>
				</value>
			</property>
			<property>
				<name>

					<string>minHeight</string>
				</name>
				<value>
					<number>5</number>
				</value>
			</property>
			<property><name><string>x</string></name><value><number>79</number></value></property><property><name><string>y</string></name><value><number>315</number></value></property><property><name><string>insertIn</string></name><value><array><property id="0"><number>0.000000</number></property></array></value></property><property><name><string>label</string></name><value><string>show table</string></value></property></properties>

		<Persist><array></array></Persist><bindings><property><name><string>insertIn</string></name><value><binding><endpoint type="excelbinding" id="BO03A27F2568A14B80BE0803EE86B1461D" displayname="Sheet1!$B$9"><workbook>[Object]</workbook><inputtype><string>xcelsius.binding.tableMaps.input.ArrayToTable</string></inputtype><outputtype><string></string></outputtype><range>Sheet1!$B$9</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R9C2</string></property></array></property></array></property></array></cells><chain><array></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>input</string></direction></endpoint></binding>
				</value></property></bindings></component><component id="2E88086A-D3E5-7838-9351-B78F34E40436" className="xcelsius.spreadsheet.SpreadsheetTable" styleName="" displayName="Spreadsheet Table 1"><colorScheme></colorScheme><properties>
			<property>
				<name>
					<string>width</string>

				</name>
				<value>
					<number>552</number>
				</value>
			</property>
			<property>
				<name>
					<string>height</string>

				</name>
				<value>
					<number>115</number>
				</value>
			</property>
			<property>
				<name>
					<string>minWidth</string>

				</name>
				<value>
					<number>20</number>
				</value>
			</property>
			<property>
				<name>
					<string>minHeight</string>

				</name>
				<value>
					<number>20</number>
				</value>
			</property>
			<property><name><string>x</string></name><value><number>80</number></value></property><property><name><string>y</string></name><value><number>341</number></value></property><property><name><string>dataCellFormat</string></name><value><array><property id="0"><array><property id="0"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="1"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="2"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="3"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="4"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>72.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property></array></property><property id="1"><array><property id="0"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="1"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="2"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="3"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="4"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>72.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property></array></property><property id="2"><array><property id="0"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="1"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="2"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="3"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="4"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>72.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property></array></property><property id="3"><array><property id="0"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="1"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="2"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="3"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="4"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>72.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property></array></property><property id="4"><array><property id="0"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="1"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="2"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="3"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="4"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>72.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property></array></property><property id="5"><array><property id="0"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="1"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="2"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="3"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>111.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property><property id="4"><object><property id="AWrapText"><false/></property><property id="BEdgeBotColor"><number>0.000000</number></property><property id="BEdgeBotLineStyle"><string>None</string></property><property id="BEdgeBotWeight"><string>Thin</string></property><property id="BEdgeLtColor"><number>0.000000</number></property><property id="BEdgeLtLineStyle"><string>None</string></property><property id="BEdgeLtWeight"><string>Thin</string></property><property id="BEdgeRtColor"><number>0.000000</number></property><property id="BEdgeRtLineStyle"><string>None</string></property><property id="BEdgeRtWeight"><string>Thin</string></property><property id="BEdgeTopColor"><number>0.000000</number></property><property id="BEdgeTopLineStyle"><string>None</string></property><property id="BEdgeTopWeight"><string>Thin</string></property><property id="ColumnWidth"><number>72.000000</number></property><property id="FUnderline"><string>None</string></property><property id="FontColor"><number>0.000000</number></property><property id="FontName"><string>�?�?</string></property><property id="FontSize"><number>11.000000</number></property><property id="FontStyle"><string>Regular</string></property><property id="HAlign"><string>General</string></property><property id="InteriorColor"><number>16777215.000000</number></property><property id="Pattern"><string>None</string></property><property id="RowHeight"><number>18.000000</number></property><property id="numFormat"><string></string></property></object></property></array></property></array></value></property><property><name><string>dataNumberFormat</string></name><value><array><property id="0"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property><property id="1"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property><property id="2"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property><property id="3"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property><property id="4"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property><property id="5"><array><property id="0"><string></string></property><property id="1"><string></string></property><property id="2"><string></string></property><property id="3"><string></string></property><property id="4"><string></string></property></array></property></array></value></property><property><name><string>tableScale</string></name><value><number>1.069767</number></value></property><property><name><string>displayKey</string></name><value><string>1</string></value></property></properties>

		<Persist><array><property id="0"><object><property id="name"><string>_mainTab</string></property><property id="value"><number>1.000000</number></property></object></property></array></Persist><bindings><property><name><string>data</string></name><value><binding><endpoint type="excelbinding" id="BO2C13C14100C54F66AC64E6A1273EDD76" displayname="Sheet1!$B$1:$F$6"><workbook>[Object]</workbook><inputtype><string></string></inputtype><outputtype><string>xcelsius.binding.tableMaps.output.TableToArray2D</string></outputtype><range>Sheet1!$B$1:$F$6</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R1C2</string></property><property id="1"><string>Sheet1!R1C3</string></property><property id="2"><string>Sheet1!R1C4</string></property><property id="3"><string>Sheet1!R1C5</string></property><property id="4"><string>Sheet1!R1C6</string></property></array></property><property id="1"><array><property id="0"><string>Sheet1!R2C2</string></property><property id="1"><string>Sheet1!R2C3</string></property><property id="2"><string>Sheet1!R2C4</string></property><property id="3"><string>Sheet1!R2C5</string></property><property id="4"><string>Sheet1!R2C6</string></property></array></property><property id="2"><array><property id="0"><string>Sheet1!R3C2</string></property><property id="1"><string>Sheet1!R3C3</string></property><property id="2"><string>Sheet1!R3C4</string></property><property id="3"><string>Sheet1!R3C5</string></property><property id="4"><string>Sheet1!R3C6</string></property></array></property><property id="3"><array><property id="0"><string>Sheet1!R4C2</string></property><property id="1"><string>Sheet1!R4C3</string></property><property id="2"><string>Sheet1!R4C4</string></property><property id="3"><string>Sheet1!R4C5</string></property><property id="4"><string>Sheet1!R4C6</string></property></array></property><property id="4"><array><property id="0"><string>Sheet1!R5C2</string></property><property id="1"><string>Sheet1!R5C3</string></property><property id="2"><string>Sheet1!R5C4</string></property><property id="3"><string>Sheet1!R5C5</string></property><property id="4"><string>Sheet1!R5C6</string></property></array></property><property id="5"><array><property id="0"><string>Sheet1!R6C2</string></property><property id="1"><string>Sheet1!R6C3</string></property><property id="2"><string>Sheet1!R6C4</string></property><property id="3"><string>Sheet1!R6C5</string></property><property id="4"><string>Sheet1!R6C6</string></property></array></property></array></property></array></cells><chain><array></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>output</string></direction></endpoint></binding>

				</value></property><property><name><string>displayStatus</string></name><value><binding><endpoint type="excelbinding" id="BOC4A1D53A2E184196AEA1E3485C942792" displayname="Sheet1!$B$9"><workbook>[Object]</workbook><inputtype><string></string></inputtype><outputtype><string>xcelsius.binding.tableMaps.output.TableToSingleton</string></outputtype><range>Sheet1!$B$9</range><cells><array><property id="0"><array><property id="0"><array><property id="0"><string>Sheet1!R9C2</string></property></array></property></array></property></array></cells><chain><array></array></chain><inputmapproperties><array></array></inputmapproperties><outputmapproperties><array></array></outputmapproperties><direction><string>output</string></direction></endpoint></binding>
				</value></property></bindings></component><ColorSchemes/></CXCanvas>
;

		}
	*/
	}
}
