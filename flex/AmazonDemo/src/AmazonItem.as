////////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2006 Adobe Macromedia Software LLC and its licensors.
//  All Rights Reserved. The following is Source Code and is subject to all
//  restrictions on such code as contained in the End User License Agreement
//  accompanying this product.
//
////////////////////////////////////////////////////////////////////////////////

package
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.core.Application;
	import com.adobe.flex.extras.controls.springgraph.Item;

	/**
	 * Represents a single Amazon item. When created, it uses the amazon web service
	 * to find out its title, icon, and similar products.
	 * 
	 * @author Mark Shepherd
	 */
	public class AmazonItem extends Item
	{
		[Bindable]
		public var name: String;
		
		[Bindable]
		public var imageUrl: String;
				
		private var similarProducts: XMLList;
		private var createSimilarsASAP: Boolean = false;
		
		public function AmazonItem(itemId: String, name: String) {
			super(itemId);
			this.name = name;
			AmazonService.getItemInfo(itemId, this);
		}
		
		public function getItemInfoResult(event:ResultEvent):void {
			var info: XML = XML(event.result);
			var ns:Namespace = info.namespace("");
			this.name = info.ns::Items.ns::Item.ns::ItemAttributes.ns::Title;
			this.imageUrl = info.ns::Items.ns::Item.ns::SmallImage.ns::URL;
			similarProducts = info.ns::Items.ns::Item.ns::SimilarProducts.ns::SimilarProduct;
			if(createSimilarsASAP)
				createSimilars();
		}

		public function getItemInfoFault(event:FaultEvent):void {
			Alert.show("getItemInfoFault " + event.toString());
		}
		
		public function createSimilars(): void {
			if(similarProducts == null) {
				createSimilarsASAP = true;
				return;
			}
			var app: AmazonDemo = AmazonDemo(Application.application);
			app.createItems(similarProducts, this);
			similarProducts = null;
		}
	}
}