package
{
	import com.gerantech.extensions.gplus.GPlusExtension;
	import com.gerantech.extensions.gplus.events.GPlusEvent;
	import com.gerantech.extensions.gplus.models.Result;
	
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	
	public class gplus_extension_test extends Sprite
	{
		private var extension:GPlusExtension;
		private var log:TextField;
		
		public function gplus_extension_test()
		{
			super();
			
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			
			var initBtn:CustomButton = new CustomButton("init");
			initBtn.y = 0;
			initBtn.addEventListener(MouseEvent.CLICK, initBtn_clickHandler);
			addChild(initBtn);
			
			var imeiBtn:CustomButton = new CustomButton("login");
			imeiBtn.y = 200;
			imeiBtn.addEventListener(MouseEvent.CLICK, loginBtn_clickHandler);
			addChild(imeiBtn);
			
			var shareBtn:CustomButton = new CustomButton("logout");
			shareBtn.y = 400;
			shareBtn.addEventListener(MouseEvent.CLICK, logoutBtn_clickHandler);
			addChild(shareBtn);
			
			var launchBtn:CustomButton = new CustomButton("revokeAccess");
			launchBtn.y = 600;
			launchBtn.addEventListener(MouseEvent.CLICK, revokeAccessBtn_clickHandler);
			addChild(launchBtn);
			
			extension = GPlusExtension.instance;
			extension.addEventListener(GPlusEvent.GPLUS_RESULT , extension_eventsHandler);
		}
		

		protected function revokeAccessBtn_clickHandler(event:MouseEvent):void
		{
			extension.revokeAccess();
		}
		
		protected function logoutBtn_clickHandler(event:MouseEvent):void
		{
			extension.logout();
		}
		
		protected function loginBtn_clickHandler(event:MouseEvent):void
		{
			extension.login();
		}
		
		protected function initBtn_clickHandler(event:MouseEvent):void
		{
			extension.init();
		}
		
		
		protected function extension_eventsHandler(event:GPlusEvent):void
		{
			if(log==null)
			{
				log=new TextField();
				log.width = width;
				log.height = height;
				log.setTextFormat(new TextFormat("_sans", 40));
				addChildAt(log, 0);
			}
			
			switch(event.result.response)
			{
				case Result.CONNECTION_FAILED:
				case Result.LOGIN_CANCELED:
				case Result.LOGIN_FAILED:
				case Result.NETWORK_ERROR:
				case Result.PERSON_NULL:
				case Result.REVOKE_ACCESS:
					trace(event.result.message);
					log.appendText(event.result.response + "  " + event.result.success + "  " + event.result.message+ "\n");
					break;
				case Result.PERSON_INFORMATION:
					log.appendText(event.result.person.name + " " + event.result.person.email + " " + event.result.person.friends.length);
					break;
				
			}
			//trace(event.type, event.data);
		}


	}
}