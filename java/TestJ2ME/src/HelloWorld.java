import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class HelloWorld extends MIDlet
{
	private Display display;
	private Form form;

	public HelloWorld()
	{
		super();
		// TODO 自动生成构造函数存根
		display = Display.getDisplay(this);		
		form = new Form("Example Form");
	}

	protected void startApp() throws MIDletStateChangeException
	{
		// TODO 自动生成方法存根
		form.append("shirui");
		display.setCurrent(form);
	}

	protected void pauseApp()
	{
		// TODO 自动生成方法存根

	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		// TODO 自动生成方法存根

	}

}
