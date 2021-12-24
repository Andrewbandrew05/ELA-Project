import java.util.*;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
public class Data {
	static class console_Panel extends JPanel implements ActionListener
	{
		private JTextField console = new JTextField();
		private JTextArea console_Output = new JTextArea(5,20);
		private JScrollPane scroll_Pane = new JScrollPane(console_Output);
		private Font f;
		private volatile String last_Text_Inputted=null;
		public console_Panel() 
		{
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			//f = new Font("Font_1", Font.PLAIN, (int)((screenSize.height/120)*1.33333));
			//System.out.println((int)((screenSize.height/30)/1.3333));
			//console_Output.setFont(f);
			setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/10));
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			console.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/60));
			console.addActionListener(this);	
			scroll_Pane.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/12));
			console_Output.setLineWrap(true);
			console_Output.setEditable(false);
			scroll_Pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			add(scroll_Pane);
			add(console);
			setBackground(Color.BLACK);
		}
		public void set_Dimension(Dimension frame_Size) 
		{			
			setPreferredSize(new Dimension(frame_Size.width, frame_Size.height/5));
			console.setPreferredSize(new Dimension(frame_Size.width, frame_Size.height/30));
			scroll_Pane.setPreferredSize(new Dimension(frame_Size.width, frame_Size.height/6));	
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String text = console.getText();
	        console.selectAll();
	        //Make sure the new text is visible, even if there
	        //was a selection in the text area.
	        this.last_Text_Inputted=text;	     
	    }
		//see if you can make the scroll bar scroll to bottom of textarea on update (I think the problem onlky occurs if thred.sleep is less than 1)
		public String print(String text, Player Player, Boolean in_Combat, monster Monster) throws InterruptedException 
		{
			String input="";
			char[] text_char_array=text.toCharArray();
			for(int i =0; i<text_char_array.length; i+=1) 
			{
				Thread.sleep(5);
				if(text_char_array[i]=='~') 
				{
					this.console_Output.append("\n");
					this.console_Output.append("\n");
			        console_Output.setCaretPosition(console_Output.getDocument().getLength());		
				}
				else if(text_char_array[i]=='`') 
				{
					this.console_Output.append("\n");
			        console_Output.setCaretPosition(console_Output.getDocument().getLength());		
				}
				else if(text_char_array[i]=='|') 
				{
					while(true) 
					{
						if(this.last_Text_Inputted!=null) 
						{
							input=last_Text_Inputted;
							this.console_Output.append(last_Text_Inputted);
							this.last_Text_Inputted=null;
							break;
						}
					}
					if(input.toLowerCase().contains("menu") && Player!=null) 
					{
						this.console_Output.append("\n");
						this.console_Output.append("\n");
				        console_Output.setCaretPosition(console_Output.getDocument().getLength());		
						Methods.take_Non_Combat_Actions(Player, in_Combat, Monster);
						i=-1;
						continue;
					}
				}
				else 
				{
					this.console_Output.append(String.valueOf(text_char_array[i]));
			        console_Output.setCaretPosition(console_Output.getDocument().getLength());		
				}
			}
			return input;		
		}
	}
	static class map_Panel extends JPanel 
	{
		private Player Player;
		private List<object_In_Sight> objects_In_Sight;
		Rectangle map_Panel_Size=new Rectangle();
		public map_Panel() 
		{
			setBackground(new Color(107, 106, 60));
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			map_Panel_Size.setBounds(0, 0, screenSize.width/2, (screenSize.height*4)/10);
			setPreferredSize(new Dimension(screenSize.width/2, (screenSize.height*4)/10));
			setLayout(new BorderLayout());
		}
		public void set_Objects_In_Sight(List<object_In_Sight> objects_In_Sight) 
		{
			this.objects_In_Sight=objects_In_Sight;
			repaint();
		}
		public void set_Player(Player Player) 
		{
			this.Player=Player;
			repaint();
		}
		public void set_Dimension(Dimension frame_Size) 
		{
			setPreferredSize(new Dimension(frame_Size.width, (frame_Size.height*4)/5));
			map_Panel_Size=getBounds();

		}
		//make it that the stuff is positioned based on size of frame (like x and y)
		//do this by taking in positioning based on x to the right y above etc etc and then mathingb them into visual representation
		//maybe looking at prob in wrong way talk to nico hank mr travis and yeah
		public void paintComponent(Graphics g) 
		{
			//modify ratio to be about twice as big
			super.paintComponent(g);
			if(Player !=null && objects_In_Sight!=null) {
				//the circles are drawn as if they are a partially filled rectangle and are positioned based on top left corner coordinates of said rectangle in order to center the object one must subtract half the width from x and half the height from y
				int least_X=999999;
				int least_Y=9999999;
				int greatest_X=-99999999;
				int greatest_Y=-999999;

				//code to standardize size of items and make full use of viewport
				for (Data.object_In_Sight object_In_Sight:objects_In_Sight) 
				{
					//dont use if else because in first runthrough it will prob be both which may be fine but well yeah that can be
					//decided later
					if(object_In_Sight.get_X_Pos()<least_X) 
					{
						least_X=object_In_Sight.get_X_Pos();
					}
					if(object_In_Sight.get_X_Pos()>greatest_X) 
					{
						greatest_X=object_In_Sight.get_X_Pos();
					}
					if(object_In_Sight.get_Y_Pos()<least_Y) 
					{
						least_Y=object_In_Sight.get_Y_Pos();
					}
					if(object_In_Sight.get_Y_Pos()>greatest_Y) 
					{
						greatest_Y=object_In_Sight.get_Y_Pos();
					}
				}
				int ratio_To_Multiply_Size_By;
				if(greatest_X-least_X==0 || greatest_Y-least_Y==0) 
				{
					if(map_Panel_Size.width<map_Panel_Size.height) 
					{
						ratio_To_Multiply_Size_By=map_Panel_Size.width/20;
					}
					else 
					{
						ratio_To_Multiply_Size_By=map_Panel_Size.height/20;
					}
				}
				else {
					//getting better still cant see trees tho
					if(map_Panel_Size.width<map_Panel_Size.height)
					{
						double answer_Possibility_1=(-greatest_X-Math.sqrt(Math.pow(greatest_X,2)-2*(-map_Panel_Size.width-least_X)));
						double answer_Possibility_2=(-greatest_X+Math.sqrt(Math.pow(greatest_X,2)-2*(-map_Panel_Size.width-least_X)));
						if(answer_Possibility_1>0) 
						{
							ratio_To_Multiply_Size_By=(int)answer_Possibility_1;
						}
						else 
						{
							ratio_To_Multiply_Size_By=(int)answer_Possibility_2;

						}
					}
					else 
					{
						double answer_Possibility_1=(-greatest_Y-Math.sqrt(Math.pow(greatest_Y,2)-2*(-map_Panel_Size.height-least_Y)));
						double answer_Possibility_2=(-greatest_Y+Math.sqrt(Math.pow(greatest_Y,2)-2*(-map_Panel_Size.height-least_Y)));
						if(answer_Possibility_1>0) 
						{

							ratio_To_Multiply_Size_By=(int)answer_Possibility_1;
						}
						else 
						{
							ratio_To_Multiply_Size_By=(int)answer_Possibility_2;

						}
					}
				}
				Graphics2D g2= (Graphics2D) g;
		        g2.setColor(Color.blue);
		        g2.setStroke(new BasicStroke(map_Panel_Size.width/350));
		        g2.fillOval(map_Panel_Size.width/2-ratio_To_Multiply_Size_By/2, map_Panel_Size.height/2-ratio_To_Multiply_Size_By/2, ratio_To_Multiply_Size_By, ratio_To_Multiply_Size_By);
				//g2.setColor(Color.black);
		        //g2.drawOval(map_Panel_Size.width/2-ratio_To_Multiply_Size_By/2, map_Panel_Size.height/2-ratio_To_Multiply_Size_By/2, ratio_To_Multiply_Size_By, ratio_To_Multiply_Size_By);
				for(Data.object_In_Sight object_In_Sight : objects_In_Sight) 
				{
					boolean circle=false;
					if(object_In_Sight.get_Name()=="vase" || object_In_Sight.get_Name()=="globe") 
					{
						circle=true;
					}
					g2.setStroke(new BasicStroke(0));
					g2.setColor(object_In_Sight.get_Color());
					int object_X_For_Display=((map_Panel_Size.width/2)-((Player.get_X_Pos()-object_In_Sight.get_X_Pos())*ratio_To_Multiply_Size_By));
					int object_Y_For_Display=((map_Panel_Size.height/2)-((Player.get_Y_Pos()-object_In_Sight.get_Y_Pos())*ratio_To_Multiply_Size_By));
					if (circle==true) 
					{
						g2.fillOval(object_X_For_Display-(ratio_To_Multiply_Size_By*object_In_Sight.get_Width())/2, object_Y_For_Display-(ratio_To_Multiply_Size_By*object_In_Sight.get_Width())/2, ratio_To_Multiply_Size_By*object_In_Sight.get_Width(), ratio_To_Multiply_Size_By*object_In_Sight.get_Width());
					}
					else 
					{	
						g2.fillRect(object_X_For_Display-(object_In_Sight.get_Width()*ratio_To_Multiply_Size_By)/2, object_Y_For_Display-(object_In_Sight.get_Width()*ratio_To_Multiply_Size_By)/2, object_In_Sight.get_Width()*ratio_To_Multiply_Size_By, object_In_Sight.get_Width()*ratio_To_Multiply_Size_By);

					}
				}
			}
		}
		
	}
	static class clue
	{
		private String clue_Text;
		clue(String clue_Text)
		{
			this.clue_Text=clue_Text;
		}
		String get_Clue_Text() 
		{
			return clue_Text;
		}
	}
	static class object
	{
		private String name;
		private int height;
		private int width;
		private monster monster;
		private clue clue;
		private String next_Stage;
		private int min_Stage_To_Show;
		private Color color;
		object(String name, int width, int height)
		{
			this.min_Stage_To_Show=0;
			this.name=name;
			this.height=height;
			this.width=width;
			this.color=Color.black;
		}
		object(String name, int width, int height, String next_Stage, int min_Stage_To_Show, Color color)
		{
			this.name=name;
			this.height=height;
			this.width=width;
			this.next_Stage=next_Stage;
			this.min_Stage_To_Show=min_Stage_To_Show;
			this.color=color;
		}
		object(String name, int width, int height, String next_Stage, int min_Stage_To_Show)
		{
			this.name=name;
			this.height=height;
			this.width=width;
			this.next_Stage=next_Stage;
			this.min_Stage_To_Show=min_Stage_To_Show;
			this.color=Color.black;
		}
		object(String name, int width, int height, clue clue, Color color)
		{
			this.color=color;
			this.min_Stage_To_Show=0;
			this.name=name;
			this.height=height;
			this.width=width;
			this.clue=clue;
		}
		object(String name, int width, int height, clue clue)
		{
			this.color=color.black;
			this.min_Stage_To_Show=0;
			this.name=name;
			this.height=height;
			this.width=width;
			this.clue=clue;
		}
		object (monster monster)
		{
			this.color=color.red;
			this.min_Stage_To_Show=0;
			this.monster=monster;
			this.name=monster.name;
			this.height=monster.height;
			this.width=monster.width;
		}
		String get_Next_Stage() 
		{
			return next_Stage;
		}
		String get_Name() 
		{
			return name;
		}
		int get_Height() 
		{
			return height;
		}
		int get_Width() 
		{
			return width;
		}
		monster get_Monster() 
		{
			return monster;
		}
		clue get_Clue() 
		{
			return clue;
		}
		String get_Clue_Text() 
		{
			return clue.get_Clue_Text();
		}
		int get_Min_Stage_To_Show() 
		{
			return min_Stage_To_Show;
		}
		Color get_Color() 
		{
			return color;
		}
	}
	static class object_In_Sight
	{
		private int x_Pos;
		private int y_Pos;
		private object object;
		object_In_Sight(String name, int x_Pos, int y_Pos, int width, int height)
		{
			this.object=new object(name, width, height);
			this.x_Pos=x_Pos;
			this.y_Pos=y_Pos;
		}
		object_In_Sight(String name, int x_Pos, int y_Pos, int width, int height, clue clue)
		{
			this.object=new object(name, width, height, clue);
			this.x_Pos=x_Pos;
			this.y_Pos=y_Pos;
		}
		object_In_Sight(String name, int x_Pos, int y_Pos, int width, int height, String next_Stage, int min_Stage_To_Show)
		{
			this.object=new object(name, width, height, next_Stage, min_Stage_To_Show);
			this.x_Pos=x_Pos;
			this.y_Pos=y_Pos;
		}
		object_In_Sight(monster monster, int x_Pos, int y_Pos)
		{
			this.object=new object(monster);
			this.x_Pos=x_Pos;
			this.y_Pos=y_Pos;
		}
		object_In_Sight(object object, int x_Pos, int y_Pos)
		{
			this.object=object;
			this.x_Pos=x_Pos;
			this.y_Pos=y_Pos;
		}
    	String get_Name() 
		{
			return object.name;
		}
		int get_X_Pos() 
		{
			return x_Pos;
		}
		int get_Y_Pos() 
		{
			return y_Pos;
		}
		int get_Height() 
		{
			return object.height;
		}
		int get_Width() 
		{
			return object.width;
		}
		monster get_Monster() 
		{
			return object.monster;
		}
		object get_Object()
		{
			return object;
		}
		clue get_Clue() 
		{
			return object.get_Clue();
		}
		String get_Clue_Text() 
		{
			return object.get_Clue_Text();
		}
		String get_Next_Stage() 
		{
			return object.get_Next_Stage();
		}
		int get_Min_Stage_To_Show() 
		{
			return object.get_Min_Stage_To_Show();
		}
		Color get_Color() 
		{
			return object.get_Color();
		}
	}
	static class monster
	{
		private int health;
		private int damage;
		private String name;
		private int level;
		private int XP_gained_on_death;
		private int dexterity;
		private int strength;
		private int speed;
		private int armor_Class;
		private int maximum_Dexterity_Buff;
		private int height;
		private int width;
		monster(int health, int damage, String name, int level, int XP_gained_on_death, int dexterity, int strength, int speed, int armor_Class, int maximum_Dexterity_Buff, int width, int height)
		{
			this.health=health;
			this.damage=damage;
			this.name=name;
			this.level=level;
			this.XP_gained_on_death=XP_gained_on_death;
			this.dexterity=dexterity;
			this.strength=strength;
			this.speed=speed;
			this.armor_Class=armor_Class;
			this.maximum_Dexterity_Buff=maximum_Dexterity_Buff;
			this.height=height;
			this.width=width;
		}
		int get_Maximum_Dexterity_Buff() 
		{
			return maximum_Dexterity_Buff;
		}
		String get_Name() 
		{
			return name;
		}
		int get_Health() 
		{
			return health;
		}
		int get_Damage() 
		{
			return damage;
		}
		int get_Width() 
		{
			return width;
		}
		int get_Height() 
		{
			return height;
		}
		void change_Health(int change_in_durabilty) 
		{
			health=health+change_in_durabilty;
		}
		int get_Level() 
		{
			return level;
		}
		int get_XP_Gained_On_Death() 
		{
			return XP_gained_on_death;
		}
		void change_Armor_Class(int armor_Class_Modifier) 
		{
			armor_Class=armor_Class+armor_Class_Modifier;
		}
		void change_Strength(int strength_Modifier) 
		{
			strength=strength+strength_Modifier;
		}
		void change_Dexterity(int dexterity_Modifier) 
		{
			dexterity=dexterity+dexterity_Modifier;
		}
		void change_Speed(int speed_Modifier) 
		{
			speed=speed+speed_Modifier;
		}
		int get_Dexterity() 
		{
			return dexterity;
		}
		int get_Strength() 
		{
			return strength;
		}
		int get_Speed() 
		{
			return speed;
		}
		int armor_Class() 
		{
			return armor_Class;
		}
	}
	static class input_Choice
	{
		private String parent_word;
		private String[] children_words;
		input_Choice(String parent_word, String[] children_words)
		{
			this.parent_word=parent_word;
			this.children_words=children_words;
		}
		input_Choice(String parent_word)
		{
			this.parent_word=parent_word;
		}
		String get_Parent_Word() 
		{
			return parent_word;
		}
		String[] get_Children_Words() 
		{
			return children_words;
		}
	}
	static class item implements Serializable
	{
		private int durability;
		private int damage;
		private String name;
		private String rarity_level;
		private int maximum_Dexterity_Buff;
		item(int durability, int damage, String name, String rarity_level, int maximum_Dexterity_Buff)
		{
			this.durability=durability;
			this.damage=damage;
			this.name=name;
			this.rarity_level=rarity_level;
			this.maximum_Dexterity_Buff=maximum_Dexterity_Buff;
		}
		int get_Maximum_Dexterity_Buff() 
		{
			return maximum_Dexterity_Buff;
		}
		String get_Name() 
		{
			return name;
		}
		int get_Durability() 
		{
			return durability;
		}
		int get_Damage() 
		{
			return damage;
		}
		void change_Durability(int change_in_durabilty) 
		{
			durability=durability+change_in_durabilty;
		}
		String get_Rarity_Level() 
		{
			return rarity_level;
		}
	}
	static class movement_Command
	{
		public int feet_To_Move;
		public String direction_To_Move;
		movement_Command(int feet_To_Move, String direction_To_Move)
		{
			this.feet_To_Move=feet_To_Move;
			this.direction_To_Move=direction_To_Move;
		}
		movement_Command(){ 
		}
	}
	public static String[] introductory_Text_to_Randomly_Generated_Stages= new String[] {};
	public static String[] mountain_Stage_Filler_Text= new String[] {}; 
	public static String[] forest_Stage_Filler_Text= new String[] {}; 
	public static String[] plains_Stage_Filler_Text= new String[] {}; 
	public static String[] flood_Plains_Stage_Filler_Text= new String[] {}; 
	public static String[] village_Stage_Filler_Text= new String[] {};
	public static String museum_Stage_1_Answer="family";
	public static String museum_Stage_2_Answer="dystopian";
	public static String museum_Stage_3_Answer="contemplative";

	public static object[][] museum_Starter_Room=
		{
			//top row then the stuff going across is columns and the stuff in the arrays represent z
			//x is up down y left right (maybe should invert so less confusing I dunno tho)
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("door", 1, 200, "Pet Sematary", 2, new Color(153, 118, 66)), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null,null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null,null , null, null, null, null, null, new object("door", 1, 200, "Tender is The Flesh", 3, new Color(153, 118, 66))},
			{new object("door", 1, 200, "Turn of the Screw", 0, new Color(153, 118, 66)), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("door", 1, 200, "end", 4, new Color(153, 118, 66)), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)}
			//second row
		};
	public static object[][] museum_Stage_1=
		{
			//top row then the stuff going across is columns and the stuff in the arrays represent z
			//x is up down y left right (maybe should invert so less confusing I dunno tho)
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("table", 1, 1, new clue("The ghosts have scrambled up these words, unscrambled them and put them in order to solve this clue.`rorrHo lOd isrBtih"), new Color(133, 114, 80)), null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, new object("coffin",1,1, new clue("When the governess walks into the children's lives, she is replacing the previous governess who is mysteriously dead, so she walks into a ghost story. But she also walks into the middle of a _____ drama involving the relationship between an orphaned sister, brother, and their uncle."), new Color(64, 53, 36)), new object("coffin",1,1, new clue("When the governess walks into the children's lives, she is replacing the previous governess who is mysteriously dead, so she walks into a ghost story. But she also walks into the middle of a _____ drama involving the relationship between an orphaned sister, brother, and their uncle."), new Color(64, 53, 36)), null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("door", 1, 200,"starter", 0, new Color(153, 118, 66))},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, new object("vase", 1, 1, new clue("By the comforts of a blazing fireplace on a cold Christmas Eve night, guests at a holiday party share stories of phantoms and ghosts of Christmases past. Yet one guest delivers a tale of sheer fright for which no one listening was prepared. As the story goes, after losing both parents, a young boy and girl move into a large wooded estate to be held under the care of their uncle. Wanting nothing to do with raising the children, the uncle hires a young governess to attend to their care. Yet the governess never could have anticipated the horrors that await her discovery. When it becomes evident that the children have some supernatural connection with a deceased former governess and her lover, the young governess finds herself scrambling to regain control of two children slipping away from her grasp."), new Color(179, 174, 166)), null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)}
			//second row
		};
	public static object[][] museum_Stage_2=
		{
			//top row then the stuff going across is columns and the stuff in the arrays represent z
			//x is up down y left right (maybe should invert so less confusing I dunno tho)
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("table", 1, 1, new clue("A perfect yet not so perfect world."), new Color(133, 114, 80)), null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, new object("coffin",1,1, new clue("Welcome to Derry, Maine. It's a small city, a place as hauntingly familiar as your own hometown. Only in Derry the haunting is real. (King)"), new Color(64, 53, 36)), new object("coffin",1,1, new clue("Welcome to Derry, Maine. It's a small city, a place as hauntingly familiar as your own hometown. Only in Derry the haunting is real."), new Color(64, 53, 36)), null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, new object("book", 1, 1, new clue("The ghosts have scrambled up these words, unscrambled them and put them in order to solve this clue.`dsypotani"), new Color(152, 60, 189)), null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("door", 1, 200,"starter", 0, new Color(153, 118, 66))},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, new object("vase", 1, 1, new clue("Did you know?`Stephen King stayed in a house that was on a road commonly used by semi trucks, he watched a cat be hit by one of the passing semis. He took this situation and added his own ideas to it to come up with theme of this book."), new Color(179, 174, 166)), null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, new object("trash can", 1, 3, new clue("Did you know?`It's (the book) inspiration came from The Three Billy Gotas Gruff, a Norwegian Fairy tale about three goats who outsmart a bridge troll. He took the main outline of the story and turned it into a city with sewers and tunnels underneath. The city representing the bridge and the tunnels and sewers underneath represent the area where the troll is. In the story, instead of three billy goats, there are children who are meant to defeat the 'troll' underneath the city.")), null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)}
			//second row
		};
	public static object[][] museum_Stage_3=
		{
			//top row then the stuff going across is columns and the stuff in the arrays represent z
			//x is up down y left right (maybe should invert so less confusing I dunno tho)
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("table", 1, 1, new clue("Although my book contains clear criticism of the meat industry, I also wrote the novel because I have always believed that in our capitalist, consumerist society, we devour each other. We phagocyte each other in many ways and in varying degrees: human trafficking, war, precarious work, modern slavery, poverty, gender violence are just a few examples of extreme violence. (Bazterrica, Irish Times)"), new Color(133, 114, 80)), null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, new object("coffin",1,1, new clue("Many people have normalized what the media insist on calling the \"Transition.\" But he hasn't because he knows that transition is a word that doesn't convey how quick and ruthless the process was. One word to sum up and classify the unfathomable. An empty word. Change, transformation, shift: synonyms that appear to mean the same thing, though the choice of one over the other speaks to a distinct view of the world. They've all normalized cannibalism, he thinks. Cannibalism, another word that could cause him major problems. (Bazterrica, 4)"), new Color(64, 53, 36)), new object("coffin",1,1, new clue("Many people have normalized what the media insist on calling the \"Transition.\" But he hasn't because he knows that transition is a word that doesn't convey how quick and ruthless the process was. One word to sum up and classify the unfathomable. An empty word. Change, transformation, shift: synonyms that appear to mean the same thing, though the choice of one over the other speaks to a distinct view of the world. They've all normalized cannibalism, he thinks. Cannibalism, another word that could cause him major problems. (Bazterrica, 4)"), new Color(64, 53, 36)), null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, new object("globe", 1, 1, new clue("The ghosts have scrambled up these words, unscrambled them and put them in order to solve this clue.`cenlepmtativo"), new Color(50, 168, 82)), null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("door", 1, 200,"starter", 0, new Color(153, 118, 66))},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, new object("vase", 1, 1, new clue("His wife has left him, his father is sinking into dementia, and Marcos tries not to think too hard about how he makes a living. After all, it happened so quickly. First, it was reported that an infectious virus has made all animal meat poisonous to humans. Then governments initiated the â€œTransition.â€� Now, eating human meatâ€”â€œspecial meatâ€�â€”is legal. Marcos tries to stick to numbers, consignments, processing. Then one day heâ€™s given a gift: a live specimen of the finest quality. Though heâ€™s aware that any form of personal contact is forbidden on pain of death, little by little he starts to treat her like a human being. And soon, he becomes tortured by what has been lostâ€”and what might still be saved. (Bazterrica)"), new Color(179, 174, 166)), null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200)},
			{new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200)}
			//second row
		};
	public static object[][] outside=
		{
			//top row then the stuff going across is columns and the stuff in the arrays represent z
			//x is up down y left right (maybe should invert so less confusing I dunno tho)
			{new object("wall", 1, 200), null, null, null, new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null},
			{null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null},
			{null, null, new object("wall", 1, 200), null, null, null, null, null, null, null, null, null, null, null, null},
			{null, null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), new object("wall", 1, 200), null, null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), null, null},
			{null, null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), null, null},
			{null, null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), null, null},
			{null, null, new object("wall", 1, 200), null, null, new object("wall", 1, 200), new object("wall", 1, 200), null, null, new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), new object("wall", 1, 200), null},
			{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null, null, null, new object("wall", 1, 200), null, null, null, null},
			{new object("wall", 1, 200), null, null, null, new object("wall", 1, 200), null, null, null, new object("wall", 1, 200), null, null, null, new object("wall", 1, 200),new object("wall", 1, 200), new object("wall", 1, 200)},
			{null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, new object("wall", 1, 200), null,null, new object("wall", 1, 200), null, new object("wall", 1, 200), null, new object("wall", 1, 200)},
			{null, null, new object("wall", 1, 200), null, null, null, new object("wall", 1, 200), null, null, null, new object("wall", 1, 200), null , new object("wall", 1, 200), null, new object("wall", 1, 200)},
			//second row
		};
	public static Hashtable<String, monster> monster_data_base=new Hashtable<String, monster>()
	{{
		//Once you know how add items here
		put("goblin",new monster(30,10,"goblin",1,5,3,2,10,4,5,1,3));
	}};
	public static Hashtable<String, item> item_data_base=new Hashtable<String, item>()
	{{
		//Once you know how add items here
		put("hatchet",new item(100,10,"hatchet","common", 5));
		put("bow",new item(50,15,"bow","common", 2));
		put("bandage", new item(1,-15,"bandage","common", 20));
	}};
	public static Hashtable<String, monster> boss_data_base=new Hashtable<String, monster>()
	{{
		//Once you know how add items here
		put("Minotaur",new monster(200, 30, "Minotaur", 5, 50,5,20,10,20,3,2,8));
	}};
}

