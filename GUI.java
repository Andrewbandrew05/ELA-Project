import java.awt.*;

import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.util.List;
public class GUI extends JComponent implements ComponentListener
{
	//create component listener to see when jframe size is chnaged then redraw other components
	private JFrame frame = new JFrame();
    private Data.map_Panel map_Panel=new Data.map_Panel();
	private Data.console_Panel console_Panel = new Data.console_Panel();
	
	public GUI() 
	{
		add(console_Panel, BorderLayout.PAGE_END);
		add(map_Panel, BorderLayout.PAGE_START);
	}
	public void set_Player(Player Player) 
	{
		map_Panel.set_Player(Player);
	}
	public void set_Objects_In_Sight(List<Data.object_In_Sight> objects_In_Sight) 
	{
		map_Panel.set_Objects_In_Sight(objects_In_Sight);
	}
	public void create_GUI() 
	{
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        //frame.setBounds(0,0,(int)screenSize.getWidth(),(int)screenSize.getHeight());
        frame.setMaximumSize(new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight()));
        frame.setMinimumSize(new Dimension((int)(screenSize.getWidth()/2), (int)(screenSize.getHeight()/2)));
        frame.setTitle("GUI");  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content_Pane= frame.getContentPane();
      	content_Pane.setLayout(new BoxLayout(content_Pane, BoxLayout.PAGE_AXIS));
        content_Pane.add(map_Panel);
        content_Pane.add(console_Panel);
        frame.addComponentListener(this);
        frame.setVisible(true);
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		if(e.getComponent()==frame) 
		{
			map_Panel.set_Dimension(frame.getSize());
			console_Panel.set_Dimension(frame.getSize());
			create_GUI();
			/*Rectangle frame_Size=frame.getBounds();
			System.out.println(frame_Size.height);
			System.out.println(frame_Size.width);*/
		}
	}
	public String print(String text, Player Player, Boolean in_Combat, Data.monster Monster) throws InterruptedException 
	{
		return console_Panel.print(text, Player, in_Combat, Monster);
	}
	//not sure what to do with these I think they just need to be here. I barely understand how to sue this library, most of code is just 
	//adapted from stuff I found online
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
