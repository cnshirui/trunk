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
	import com.adobe.flex.extras.controls.springgraph.Item;
	
	import flash.external.ExternalInterface;
	
	import mx.core.Application;
	import mx.managers.CursorManager;
	import mx.managers.CursorManagerPriority;

	/**
	 * Represents a single Person item. When created, it uses the person web service
	 * to find out its title, icon, and similar products.
	 * 
	 * @author Mark Shepherd
	 */
	public class PersonItem extends Item
	{
		[Bindable]
		public var name: String;
		
		[Bindable]
		public var imageUrl: String;
				
		private static var app:CampusRoamer = CampusRoamer(Application.application);
		private static var selectedID:String;
		
		public function PersonItem(itemId: String, name: String, image:String) {
			super(itemId);
			this.name = name;
			this.imageUrl = image;
			
			if(selectedID == null) {
				selectedID = itemId;
			}
		}
		
 		public function getFriends():void {
			if(selectedID == this.id) {
				return;
			}
			
			selectedID = this.id;
			ExternalInterface.call("loadFriends", this.id);
			
			CursorManager.setCursor(app.inProgressCursor, CursorManagerPriority.HIGH, -8, -8);
			CursorManager.setBusyCursor();
	    }	 	
	}
}