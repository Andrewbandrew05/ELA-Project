import java.util.*;
import java.awt.Dimension;
import java.io.Serializable;
import java.awt.Toolkit;
public class Player implements Serializable{
	private String name;
	private Boolean uses_own_name;
	private String location;
	private int health;
	private int money;
	private ArrayList<Data.item>items= new ArrayList<Data.item>();
	private int XP;
	private int level;
	private int dexterity;
	private int strength;
	private int speed;
	private int armor_Class;
	private int x_Pos;
	private int y_Pos;
	private int height;
	private int current_Stage;
	Player(String name, Boolean uses_own_name)
	{
		this.name=name;
		this.uses_own_name=uses_own_name;
		this.health=100;
		this.location="path_to_gefrume";
		//ugly but I think necessary
		this.items=new ArrayList<Data.item>(Arrays.asList(Data.item_data_base.get("hatchet"),Data.item_data_base.get("bow"),
				Data.item_data_base.get("bandage"),Data.item_data_base.get("bandage"),Data.item_data_base.get("bandage"),
				Data.item_data_base.get("bandage"),Data.item_data_base.get("bandage")));
		this.money=4;
		this.level=1;
		this.XP=0;
		this.dexterity=10;
		this.strength=10;
		this.speed=10;
		this.armor_Class=5;
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		this.height=6;
		this.x_Pos=0;
		this.y_Pos=0;
		this.current_Stage=1;
	}
	void move(int feet, String direction) 
	{
		if(direction=="right") 
		{
			change_X_Pos(feet);
		}
		else if(direction=="left") 
		{
			change_X_Pos(-feet);
		}
		else if(direction=="forwards") 
		{
			change_Y_Pos(-feet);
		}
		else if(direction=="backwards") 
		{
			change_Y_Pos(feet);
		}
		else 
		{
			System.out.println("player movement failed");
		}
	}
	void set_Current_Stage(int stage) 
	{
		current_Stage=stage;
	}
	void change_X_Pos(int x_Pos_Modifier) 
	{
		this.x_Pos=this.x_Pos+x_Pos_Modifier;
	}
	void set_X_Pos(int x_Pos) 
	{
		this.x_Pos=x_Pos;
	}
	void change_Y_Pos(int y_Pos_Modifier) 
	{
		this.y_Pos=this.y_Pos+y_Pos_Modifier;
	}
	void set_Y_Pos(int y_Pos) 
	{
		this.y_Pos=y_Pos;
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
		return height;
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
	String get_Name() 
	{
		if(uses_own_name==true) 
		{
			return name;
		}
		else 
		{
			return "Gungry";
		}
	}
	int get_Health() 
	{
		return health;
	}
	String get_Location() 
	{
		return location;
	}
	int get_Money() 
	{
		return money;
	}
	int get_Current_Stage() 
	{
		return current_Stage;
	}
	ArrayList<Data.item> get_Items()
	{
		return items;
	}
	Boolean check_If_Item_In_Inventory(String item_name) 
	{
		Boolean item_in_bag=false;
		for (Data.item item: items) 
		{
			if(item.get_Name()==item_name) 
			{
				item_in_bag=true;
			}
		}
		return item_in_bag;
	}
	void change_Health(int health) 
	{
		this.health=this.health+health;
		if(health>100) 
		{
			health=100;
		}
	}
	void add_Money(int money) 
	{
		this.money=this.money+money;
	}
	void add_Or_Remove_Item(Boolean remove, Data.item item) 
	{
		if(remove==false) 
		{
			items.add(item);
		}
		else 
		{
			int index_num=0;
			for(Data.item item2: items) 
			{
				if(item2.get_Name()==item.get_Name()) 
				{
					break;
				}
				index_num+=1;
			}
			items.remove(index_num);
		}
	}
	int get_Level() 
	{
		return level;
	}
	void add_XP(int XP) 
	{
		this.XP=this.XP+XP;
	}
	//FIND A WAY TO FIX THIS IT IS NOT OK TO HAVE TO PASS PLAYER TO A METHOD INSIDE ITSELF (Maybe i dont know if it isnt)
	void level_Up(Player Player) throws InterruptedException 
	{
		if(XP==(20^level)) 
		{
			level+=1;
			while(true) {
				String answer=Main.gui.print("You have leveled up! Because of this, you have gained 1 upgrade point! Would you like to upgrade your strength, "
						+ "dexterity, or speed?~|~", Player, false, null);
				if(answer.toLowerCase().contains("speed")) 
				{
					change_Speed(1);
				}
				else if(answer.toLowerCase().contains("dexterity")) 
				{
					change_Dexterity(1);
				}
				else if(answer.toLowerCase().contains("strength")) 
				{
					change_Strength(1);
				}
				else
				{
					Main.gui.print("Sorry, but I couldn't understand that. Please try again.~", Player, false, null);
				}
			}
		}
	}
	void change_Name(String name) 
	{
		this.name=name;
	}
	void change_Uses_Own_Name(Boolean uses_own_name) 
	{
		this.uses_own_name=uses_own_name;
	}
}
