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
	import mx.rpc.http.HTTPService;
	import mx.managers.CursorManager;
	
	/**
	 * Encapsulation of the Amazon Web Service. Contains static functions for performing specific calls on the service.
	 * 
	 * @author Mark Shepherd
	 */
	public class AmazonService
	{
		private static var urlBase: String = "http://webservices.amazon.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=00WQKXFRETRXJMXW8682&";
		
		public static function getItemInfo(id: String, client: Object): void {
			var service: HTTPService = new HTTPService();
			service.resultFormat="e4x";
			service.url = urlBase + "Operation=ItemLookup&ResponseGroup=Reviews,Images,Large&ItemId=" + id;
			service.addEventListener("result", client.getItemInfoResult);
			service.addEventListener("fault", client.getItemInfoFault);
			service.send();
		}
		
		public static function getDetailPageUrl(id: String): String {
			return "http://www.amazon.com/gp/pdp/profile/" + id;
		}
	}
}