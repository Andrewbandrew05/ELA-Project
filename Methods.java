import java.util.*;
import java.io.*;
public class Methods {
	static Scanner scanner =new Scanner(System.in);
	//Method to make the player move forwards
	public static Data.movement_Command process_Commands(String command, Player Player) throws InterruptedException 
	{
		if(command.toLowerCase().contains("step") || command.toLowerCase().contains("walk") || command.toLowerCase().contains("move"))
		{
			int feet_To_Move=0;
			for(String potential_Num : command.split(" ")) 
			{
				try 
				{
					feet_To_Move=Integer.parseInt(potential_Num);
				}
				catch (Exception e)
				{
					//Do nothing
				}
			}
			if(command.toLowerCase().contains("forwards") || command.toLowerCase().contains("front") || command.toLowerCase().contains("up")) 
			{
				return	new Data.movement_Command(feet_To_Move, "forwards");		
			}
			else if(command.toLowerCase().contains("backwards") || command.toLowerCase().contains("back") || command.toLowerCase().contains("down")) 
			{
				return	new Data.movement_Command(feet_To_Move, "backwards");		
			}
			else if(command.toLowerCase().contains("left")) 
			{
				return	new Data.movement_Command(feet_To_Move, "left");		
			}
			else if(command.toLowerCase().contains("right")) 
			{
				return	new Data.movement_Command(feet_To_Move, "right");		
			}
			else 
			{
				Main.gui.print("Sorry but I could not understand that please try again.~", Player, false, null);
			}
		}
		else if (command.toLowerCase().contains("guess")) 
		{
			return new Data.movement_Command(0, "guess~"+command.toLowerCase().replace("guess", ""));		
		}
		else if (command.toLowerCase().contains("inspect") || command.toLowerCase().contains("search") || command.toLowerCase().contains("interact") || command.toLowerCase().contains("open")) 
		{
			return new Data.movement_Command(0, "inspect"); 
		}
		else 
		{
			Main.gui.print("Sorry but I could not understand that please try again.~", Player, false, null);
		}
		return	new Data.movement_Command();		
	}
	//make a class 'gui' that has functions like draw console draw surroundings etc, and particular variables. Also create a keyboard listener thing in their for input which will be returned when that function is called
	public static void draw_Surroundings(List<Data.object_In_Sight> objects_In_Sight, Player Player) 
	{
        Main.gui.set_Objects_In_Sight(objects_In_Sight);
        Main.gui.set_Player(Player);
	}
	//Prototype method for generating description fo where a player is based on a three demensional array
	/*public static Data.object_In_Sight make_Object_In_Sight(String z_Value, int x_Pos, int y_Pos) 
	{
		
		if(z_Value=="random_Monster") 
		{
			String monster_Name=get_Ran_Object_From_Dict(Data.monster_data_base);
			return new Data.object_In_Sight(Data.monster_data_base.get(monster_Name),x_Pos, y_Pos);
		}
		for(String monster_Name : Data.monster_data_base.keySet()) 
		{
			//if theres a problem with this code try making this .contains
			if(monster_Name==(z_Value)) 
			{
				return new Data.object_In_Sight(Data.monster_data_base.get(monster_Name),x_Pos, y_Pos);
			}
		}
		return new Data.object_In_Sight(z_Value,x_Pos, y_Pos, height);

	}
	*/
	public static Data.object_In_Sight[] calculate_Line_of_Sight(Data.object_In_Sight current_Object, Data.object_In_Sight potential_Object, Player Player) 
	{
		//calculates distance between current object x and player x
		if (current_Object.get_Monster()==null && potential_Object.get_Monster()==null) 
		{
	    	return new Data.object_In_Sight[]{current_Object, potential_Object};
		}
		double current_Dif_X_Pos=(current_Object.get_X_Pos()-Player.get_X_Pos());
		double current_Dif_Y_Pos=(current_Object.get_Y_Pos()-Player.get_Y_Pos());
		double potential_Dif_X_Pos=(potential_Object.get_X_Pos()-Player.get_X_Pos());
		double potential_Dif_Y_Pos=(potential_Object.get_Y_Pos()-Player.get_Y_Pos());
		try {
			if (Math.atan(current_Dif_Y_Pos/current_Dif_X_Pos)==Math.atan(potential_Dif_Y_Pos/potential_Dif_X_Pos))
			{
			    double current_Total_Diff_2D=Math.pow((Math.pow(current_Dif_X_Pos, 2)+Math.pow(current_Dif_Y_Pos,2)),(1/2));
			    double potential_Total_Diff_2D=Math.pow((Math.pow(potential_Dif_X_Pos, 2)+Math.pow(potential_Dif_Y_Pos,2)),(1/2));
			    double current_Dif_Height=(current_Object.get_Height()-Player.get_Height());
				double potential_Dif_Height=(potential_Object.get_Height()-Player.get_Height());
			    if (Math.atan(current_Dif_Height/current_Total_Diff_2D)==Math.atan(potential_Dif_Height/potential_Total_Diff_2D)) 
			    {
				    if (current_Total_Diff_2D>potential_Total_Diff_2D) 
				    {
				    	if(current_Object.get_Monster()==null) 
				    	{
				    		return new Data.object_In_Sight[]{potential_Object, current_Object};
				    	}
				    	return new Data.object_In_Sight[]{potential_Object};
	
				    }
				    else 
				    {
				    	if(potential_Object.get_Monster()==null) 
				    	{
					    	return new Data.object_In_Sight[]{potential_Object, current_Object};
					    }
				    	return new Data.object_In_Sight[]{current_Object};
				    }
			    }
			}
		}
		catch (Exception e)
		{
			if(potential_Object.get_Y_Pos()>current_Object.get_Y_Pos()) 
			{
				if(potential_Object.get_Monster()==null) 
				{
			    	return new Data.object_In_Sight[]{potential_Object, current_Object};
			    }
		    	return new Data.object_In_Sight[]{current_Object};
			}
			else 
			{
				if(current_Object.get_Monster()==null) 
				{
			    	return new Data.object_In_Sight[]{potential_Object, current_Object};
			    }
			    return new Data.object_In_Sight[]{potential_Object};
			}
		}
    	return new Data.object_In_Sight[]{current_Object, potential_Object};
	}
	public static Data.object_In_Sight is_Touching(Data.object_In_Sight object_In_Sight, Player Player) 
	{
		if (Math.abs((Player.get_X_Pos())-(object_In_Sight.get_X_Pos()))<(object_In_Sight.get_Width()/2)+0.5 && Math.abs((Player.get_Y_Pos())-(object_In_Sight.get_Y_Pos()))<(object_In_Sight.get_Width()/2)+0.5) 
		{
			return object_In_Sight;					
		}
		return null;
	}
	public static List<Data.object_In_Sight> get_Objects_In_Sight(Data.object [][] level_Stage, Player Player) 
	{
		List<Data.object_In_Sight> objects_In_Sight=new ArrayList<Data.object_In_Sight>();
		//havent added coordinates to Player yet will prob just make them get functions and do away with these variables in final edition or whategv its called
		int x_Pos=0;
		int y_Pos=0;
		for(Data.object [] y_Value : level_Stage) 
		{
			x_Pos=0;
			for(Data.object  object : y_Value) 
			{
				if (object==null) 
				{
					x_Pos+=1;
					continue;
				}
				if(object.get_Min_Stage_To_Show()>Player.get_Current_Stage()) 
				{
					if(x_Pos==0 || y_Pos==0 || x_Pos==y_Value.length-1 || y_Pos==level_Stage.length-1) 
					{
						object=new Data.object("wall", 1, 200);
					}
					else 
					{
						continue;
					}
				}
				if(objects_In_Sight.size()>0) 
				{
					boolean add_Object=true;
					List<Data.object_In_Sight> objects_To_Remove=new ArrayList<Data.object_In_Sight> ();
					for(Data.object_In_Sight object_In_Sight : objects_In_Sight) 
					{
						//minus one because player has width of one
						Data.object_In_Sight touched_Object=is_Touching(object_In_Sight, Player);
						if (touched_Object!=null) 
						{
							return new ArrayList<Data.object_In_Sight>(){{add(touched_Object);}};					
						}
						Data.object_In_Sight[] returned_Objects=calculate_Line_of_Sight(object_In_Sight, new Data.object_In_Sight(object, x_Pos, y_Pos), Player);
						if (returned_Objects.length==2) 
						{
							continue;
						}
						else if(returned_Objects[0].get_Object()==object) 
						{
							objects_To_Remove.add(object_In_Sight);
						}
						else 
						{
							add_Object=false;
							//objects_To_Remove.add(new Data.object_In_Sight(object, x_Pos, y_Pos));
							break;
						}
						//broken		
					}
					if (add_Object==true) 
					{
						for (Data.object_In_Sight object_To_Remove : objects_To_Remove) 
						{
							if (objects_In_Sight.contains(object_To_Remove)) 
							{
								objects_In_Sight.remove(object_To_Remove);
							}
						}
						objects_In_Sight.add(new Data.object_In_Sight(object, x_Pos, y_Pos));
					}
						//if divide by zero occurs theres only one possibility in which the object would be in line of sight which is that player and the two objects all have same x
				}	
				else
				{
					objects_In_Sight.add(new Data.object_In_Sight(object, x_Pos, y_Pos));
				}									
				x_Pos+=1;
			}
			y_Pos+=1;
		}
		return objects_In_Sight;
	}
	public static void give_Description_of_Surroundings(List<Data.object_In_Sight> objects_In_Sight, Player Player) throws InterruptedException 
	{
		for(Data.object_In_Sight object_In_Sight : objects_In_Sight) 
		{
			if (object_In_Sight.get_Name()=="wall") 
			{
				continue;
			}
			if(object_In_Sight.get_Name()!=null) 
			{
				Main.gui.print("There is a "+object_In_Sight.get_Name()+" ",null,false,null);
			}
			else 
			{
				Main.gui.print("There is a "+object_In_Sight.get_Monster().get_Name()+" ",null,false,null);
	
			}
			if(Player.get_X_Pos()-object_In_Sight.get_X_Pos()>0) 
			{
				Main.gui.print(Player.get_X_Pos()-object_In_Sight.get_X_Pos()+" feet to your left and ",null, false, null);
			}
			else 
			{
				Main.gui.print(object_In_Sight.get_X_Pos()-Player.get_X_Pos()+" feet to your right and ",null, false, null);
			}
			if(Player.get_Y_Pos()-object_In_Sight.get_Y_Pos()>0) 
			{
				Main.gui.print(Player.get_Y_Pos()-object_In_Sight.get_Y_Pos()+" feet in front of you.`",null, false, null);
			}
			else 
			{
				Main.gui.print(object_In_Sight.get_Y_Pos()-Player.get_Y_Pos()+" feet behind you.`",null, false, null);
			}
		}
	}
	public static void generate_Random_Level_Stage(Player Player) throws NumberFormatException, InterruptedException 
	{
		Random rand=new Random();
		List<String> already_used_Filler_text= new ArrayList<String>();
		int choice_num=take_Input(new Data.input_Choice[][] {new Data.input_Choice[] {new Data.input_Choice("both")}, new Data.input_Choice[] {new Data.input_Choice("only", new String[] 
				{"monsters"}), new Data.input_Choice("no", new String[]{"item", "encounters"})}},"Would you like to have both monster and item encounters in your level or just monster "
						+ "encounters?", Player, false, null);
		Boolean item_and_monster_encounters=false;
		if(choice_num==1) 
		{
			item_and_monster_encounters=true;
		}
		int num_of_encounters=Integer.parseInt(Main.gui.print("How many encounters do you want to have in your level? ~|~", Player, false, null));
		String introductory_Phrase_to_Stage="";
		int which_Introductory_Phrase_to_Use=rand.nextInt(Data.introductory_Text_to_Randomly_Generated_Stages.length)+1;
		int what_Level_Area=rand.nextInt(5)+1;
		//upper bound=num levels(will be five at game completion)
		int times_looped=0;
		for (String introductory_Phrase : Data.introductory_Text_to_Randomly_Generated_Stages) 
		{
			if(times_looped+1==which_Introductory_Phrase_to_Use) 
			{
				introductory_Phrase_to_Stage=introductory_Phrase;
			}
		}
		//changes a set of chars to the level name allowing for universal introductory phrases (90 signifies lowercase 09 uppercase)
		if(what_Level_Area==1) 
		{
			Main.gui.print(introductory_Phrase_to_Stage.replace("~``~&*90%^%@", "museum").replace("~``~&*09%^%@", "Museum"), Player, false, null);
		}
		else if(what_Level_Area==2) 
		{
			Main.gui.print(introductory_Phrase_to_Stage.replace("~``~&*90%^%@", "mountain").replace("~``~&*09%^%@", "Mountain"), Player, false, null);
		}
		else if(what_Level_Area==3) 
		{
			Main.gui.print(introductory_Phrase_to_Stage.replace("~``~&*90%^%@", "plains").replace("~``~&*09%^%@", "Plains"), Player, false, null);
		}
		else if(what_Level_Area==4) 
		{
			Main.gui.print(introductory_Phrase_to_Stage.replace("~``~&*90%^%@", "flood plains").replace("~``~&*09%^%@", "Flood plains"), Player, false, null);
		}
		else if(what_Level_Area==5) 
		{
			Main.gui.print(introductory_Phrase_to_Stage.replace("~``~&*90%^%@", "village").replace("~``~&*09%^%@", "Village"), Player, false, null);
		}
		//We could just make it that it goes phrase encounter phrase encounter and so on but I have another idea that I will detail when we call
	}
	public static Boolean does_Attack_Hit(int attacker_Speed, int defender_Speed, int maximum_Dexterity_Buff, int attacker_Dexterity, int defender_Dexterity) 
	{
		Random rand=new Random();
		if(attacker_Speed+rand.nextInt(5)+1>defender_Speed+rand.nextInt(5)+1 && attacker_Dexterity+rand.nextInt(maximum_Dexterity_Buff)+1>defender_Dexterity+rand.nextInt(5)+1) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public static void do_Damage(Player Player, Data.monster Monster, Data.item weapon , Boolean player_Attacking) throws InterruptedException 
	{
		Random rand = new Random();
		if(player_Attacking==true) 
		{
			if(does_Attack_Hit(Player.get_Speed(),Monster.get_Speed(), weapon.get_Maximum_Dexterity_Buff(), Player.get_Dexterity(), Monster.get_Dexterity())) 
			{
				Monster.change_Health(Monster.armor_Class()-(weapon.get_Damage()+(weapon.get_Damage()*((Player.get_Strength()+rand.nextInt(5)+1)/20))));
				weapon.change_Durability(-1);
				Main.gui.print("You did "+weapon.get_Damage()+" damage to the "+Monster.get_Name()+"! It has "+Monster.get_Health()
				+" health left!~", Player, true, Monster);
			}
			else 
			{
				Main.gui.print("Your attack missed the "+Monster.get_Name()+"! It has "+Monster.get_Health()
				+" health left!~", Player, true, Monster);
			}
		}
		else 
		{
			if(does_Attack_Hit(Monster.get_Speed(),Player.get_Speed(), Monster.get_Maximum_Dexterity_Buff(), Monster.get_Dexterity(), Player.get_Dexterity())) 
			{
				Player.change_Health(Player.armor_Class()-(Monster.get_Damage()+(Monster.get_Damage()*((Monster.get_Strength()+rand.nextInt(5)+1)/20))));
				Main.gui.print("The "+Monster.get_Name()+" did "+Monster.get_Damage()+" damage to you! You have "+Player.get_Health()
				+" health left!~", Player, true, Monster);
			}
			else 
			{
				Main.gui.print("The "+Monster.get_Name()+"'s attack missed! You have "+Player.get_Health()
				+" health left!~", Player, true, Monster);
			}
		}
	}
	public static void start_Program() throws InterruptedException 
	{
		Main.gui.create_GUI();
		File new_directory=new File("game_saves");
		Boolean new_directory_already_exists=new_directory.exists();
		if(new_directory_already_exists==false) 
		{
			new_directory.mkdir();
		}
		if(new_directory.list().length>0) 
		{
			Main.gui.print("Here are the save files you can load.~", null, false, null);
			for(File file: new_directory.listFiles()) 
			{
				Main.gui.print(file.getName()+"`", null, false, null);
			}
			Main.gui.print("`", null, false, null);
			while(true) {
				String answer=Main.gui.print("Would you like to load one of these saves, create a new one, or delete one?~|~", null, false, null);
				if(answer.toLowerCase().contains("create") || answer.toLowerCase().contains("new")) 
				{
					create_New_Save(new_directory);
					break;
				}
				else if(answer.toLowerCase().contains("load")) 
				{
					load_Save_File(new_directory);
				}
				else if(answer.toLowerCase().contains("delete")) 
				{
					delete_save(new_directory);
				}
				else 
				{
					Main.gui.print("Sorry, but I couldn't understand that. Please try again.~", null, false, null);
				}
			}
		}
		else 
		{
			Main.gui.print("You do not have any save files. Please create a new one.~", null, false, null);
			create_New_Save(new_directory);
		}
	}
	public static void fight(Player Player, Data.monster Monster) throws InterruptedException 
	{
		Random rand= new Random();
		while (true) 
		{
			if(Player.get_Health()<=0) 
			{
				Main.gui.print("You have died.", null, false, null);
				Main.player_is_alive=false;
				break;
			}
			if(Monster.get_Health()<=0) 
			{
				Main.gui.print("You have killed the "+Monster.get_Name()+"! You have gained "+Monster.get_XP_Gained_On_Death()+"XP!~", Player, false, null);
				Player.add_XP(Monster.get_XP_Gained_On_Death());
				Player.level_Up(Player);
				break;
			}
			int option_choice_num=take_Input(new Data.input_Choice[][] {new Data.input_Choice[] 
					{new Data.input_Choice("attack")}, new Data.input_Choice[] {new Data.input_Choice("run")}},"Would you like to attack the "+ Monster.get_Name() +" "
							+ "or attempt to run away?",Player, true, Monster);
			if(option_choice_num==1) 
			{
				ArrayList<Data.item>items_to_use_in_fight= new ArrayList<Data.item>();
				String names_of_items_to_use_in_fight="";
				for(Data.item item : Player.get_Items()) 
				{
					if(item.get_Damage()>0) 
					{
						items_to_use_in_fight.add(item);
						names_of_items_to_use_in_fight=names_of_items_to_use_in_fight+"`"+item.get_Name();
					}
				}
				while(true) {
					Boolean did_item_get_used=false;
					String item_to_use=Main.gui.print("You may use the following items: "+names_of_items_to_use_in_fight+"~Which one would you "
							+ "like to use? ~|~", Player, true, Monster);
					for(Data.item item :items_to_use_in_fight) 
					{
						if(item_to_use.toLowerCase().contains(item.get_Name())) 
						{
							do_Damage(Player, Monster, item, true);
							did_item_get_used=true;
						}
					}
					if(did_item_get_used==true) 
					{
						break;
					}
					else 
					{
						Main.gui.print("Sorry, but we couldn't understand your answer. Please try again.~", Player, true, Monster);
					}
				}
				do_Damage(Player, Monster, null, false);
			}
			else if(option_choice_num==2) 
			{
				int did_you_escape=rand.nextInt(3);
				if(did_you_escape==2) 
				{
					Main.gui.print("You escaped!~", Player, true, Monster);
					break;
				}
				else 
				{
					Main.gui.print("You have failed to escape.~", Player, true, Monster);
					do_Damage(Player, Monster, null, false);
				}
			}
		}
	}
	public static void delete_save(File directory) throws InterruptedException 
	{
		while (true) {
			String answer=Main.gui.print("Which file would you like to delete? Or if you would like to see your files again type list files. "
					+ "Finally if you decide you don't want to delete a file just type back.~|~", null, false, null);
			if(answer.toLowerCase().contains("back")) 
			{
				start_Program();
			}
			else if(answer.toLowerCase().contains("list") && answer.toLowerCase().contains("files")) 
			{
				for(File file: directory.listFiles())
				{
					Main.gui.print(file.getName()+"`", null, false, null);
				}
			}
			Boolean success=false;
			for(File file : directory.listFiles()) 
			{
				if(answer.contains(file.getName())) 
				{
					success=true;
					answer=Main.gui.print("Are you sure you want to delete "+file.getName()+"?~|~", null, false, null);
					if(answer.toLowerCase().contains("yes"))
					{
						file.delete();
						start_Program();
					}
					else 
					{
						break;
					}
				}
			}
			if(success==false) 
			{
				Main.gui.print("Sorry, but I could not understand that.~", null, false, null);
			}
		}
	}
	public static void load_Save_File(File directory) throws InterruptedException 
	{
		while(true)
		{
			Boolean success=false;
			String answer=Main.gui.print("What file would you like to load? Please type the name exactly as it appears on your screen. "
					+ "Or if you would like to see the list of files again type list files, and if you would like to return to the menu"
					+ "you can type back.~|~", null, false, null);
			if(answer.toLowerCase().contains("back")) 
			{
				start_Program();
			}
			else if(answer.toLowerCase().contains("list") && answer.toLowerCase().contains("files")) 
			{
				for(File file: directory.listFiles()) 
				{
					Main.gui.print(file.getName()+"`", null, false, null);
				}
				Main.gui.print("`", null, false, null);
				continue;
			}
			for(File file : directory.listFiles()) 
			{
				if(answer.contains(file.getName())) 
				{
					Main.current_save_file_path=directory+answer;
					success=true;
				}
			}
			if(success==true) 
			{
				break;
			}
			else 
			{
				Main.gui.print("Sorry, but I couldn't understand that, please try again.~", null, false, null);
			}
		}
		try {
			FileInputStream save_file=new FileInputStream(Main.current_save_file_path);
			ObjectInputStream in = new ObjectInputStream(save_file);
			Save save=null;
			save=(Save)in.readObject();
			in.close();
			save_file.close();
			//Seems incredibly inefficient see if there's a better way to do this
			if(save.get_level_name().contains("Museum")) 
			{
				Museum.starting_Room(save.get_Player());
			}
			else if (save.get_level_name()=="Mountain") 
			{
				//Write code to go to mountain sections once those are coded
			}
			else if(save.get_level_name()=="Plains") 
			{
				//Write code to go to plains sections once those are coded
			}
			else if(save.get_level_name()=="Lakes") 
			{
				//Write code to go to lakes sections once those are coded
			}
			else if (save.get_level_name()=="Village") 
			{
				//Write code to go to villages sections once those are coded
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void create_New_Save(File directory) throws InterruptedException 
	{
		while (true) 
		{
			String answer=Main.gui.print("What would you like this save to be called? Or if you would like to return to the menu just type"
					+ "back.~|~", null, false, null);
			if(answer.toLowerCase().contains("back"))
			{
				start_Program();
			}
			else {
				if(answer.toLowerCase().contains("delete")) 
				{
					Main.gui.print("Sorry but you cannot name the file that. Please try again.", null, false, null);
					continue;
				}
				try {
					File file=new File(directory+"/"+answer);
					Boolean success=file.createNewFile();
					
					if(success==true) 
					{
						Main.gui.print("Successfully created new save file named "+answer+"~", null, false, null);
						Main.current_save_file_path=directory+answer;
						break;
					}
					else 
					{
						Main.gui.print("File creation failed. Please try another name.~", null, false, null);
					}
				}
				catch(Exception e) 
				{
					Main.gui.print("Sorry, but file creation for that name has failed. This error may be caused by including characters such"
							+ "as slashes in your file name. Please remove such charaacters and try again~", null, false, null);
				}
			}
		}
		Player Player;
		String name=Main.gui.print("Welcome to this random game. What is your name?~|~", null, false, null);
		//test for new method of taking input
		int option_num_chosen=take_Input(new Data.input_Choice[][] {new Data.input_Choice[] {new Data.input_Choice("default"), new Data.input_Choice("regular"), new Data.input_Choice("Gungry")}, 
			new Data.input_Choice[] {new Data.input_Choice("my"), new Data.input_Choice("mine"), new Data.input_Choice("name", new String[] {"this"})}}, 
				"Nice to meet you "+name+". Would you like to be called by this name during gameplay or use the default name of Gungry? (Doesn't matter you'll never actually be spoken to.", null, false, null);
		if(option_num_chosen==1) 
		{
			Player = new Player(name, false);
		}
		else if(option_num_chosen==2) 
		{
			Player = new Player(name, true);
		}
		//only here to keep lines 324 and 325 from dying, would suggest looking into how to stop them from complaining
		else 
		{
			Player =new Player(name, false);
		}
		//outline of quest
		Main.gui.print("Que storyline... ok never mind we're out of time. :) Basically go to rooms, look for clues, figure out what main theme/genre"
				+ "they're pointing to and guess by typing 'guess' followed by a space and your guess in the text box.~", Player, false, null);
		save_Game(Player,"Museum",0);
		Museum.starting_Room(Player);
	}
	public static void save_Game(Player Player, String level_name, int level_stage) 
	{
		try {
			Save save =new Save(Player, level_name, level_stage);
			FileOutputStream save_file = new FileOutputStream(Main.current_save_file_path); 
	        ObjectOutputStream out = new ObjectOutputStream(save_file); 
	        out.writeObject(save);
	        out.close();
	        save_file.close();
		}
		catch(IOException ex) 
        { 
            System.out.println(ex); 
        } 
	}
	public static String get_Ran_Object_From_Dict(Hashtable table) 
	{
		//By creating dictionary with defined parameters I can then set it to table and it wont explode.
		Random rand=new Random();
		int object_num_to_retrieve=rand.nextInt(table.size());
		Hashtable<String, String> hashtable=new Hashtable<String, String>();
		hashtable=table;
		int num_looped=0;
		for(String key :hashtable.keySet()) 
		{
			if(num_looped==object_num_to_retrieve) 
			{
				return key;
			}
			num_looped+=1;
		}
		return "System_fail";
	}
	//Prototype code for input taker that should make it easier to have wider parameters need to make it that it can return a specific word as well
	public static int take_Input(Data.input_Choice[][] option_Array, String question, Player Player, Boolean in_Combat, Data.monster Monster) throws InterruptedException 
	{
		while(true) {
			String input=Main.gui.print(question+"~|~", Player, in_Combat, Monster);
			int times_looped=0;
			for (Data.input_Choice[] array_of_input_choices_for_option : option_Array) 
			{
				for(Data.input_Choice input_choice: array_of_input_choices_for_option) 
				{
					Boolean input_choice_match=false;
					if(input.toLowerCase().contains(input_choice.get_Parent_Word()))
					{
						try {
							for(String word : input_choice.get_Children_Words()) 
							{
								if(input.toLowerCase().contains(word)) 
								{
									input_choice_match=true;
								}
							}
						}
						catch(Exception e)
						{
							return times_looped+1;
						}
					}
					if(input_choice_match==true) 
					{
						return times_looped+1;
					}
				}
				times_looped+=1;
			}
			Main.gui.print("Sorry, but I couldn't understand that. Please try again.~", Player, in_Combat, Monster);
		}
	}
	
	public static void take_Non_Combat_Actions(Player Player, Boolean in_Combat, Data.monster Monster) throws InterruptedException
	{
		while (true) {
			String answer=Main.gui.print("Would you like to view your items, use an item, or change your name? "
					+ "Or if you would like to return to the game just type done.~|~", Player, in_Combat, Monster);
			if(answer.toLowerCase().contains("view") && answer.toLowerCase().contains("items")) 
			{
				Main.gui.print("You have the following items.`", Player, in_Combat, Monster);
				for(Data.item item :Player.get_Items()) 
				{
					Main.gui.print(item.get_Name()+"`", Player, in_Combat, Monster);
				}
				answer=Main.gui.print("Would you like to see a particular items stats? If so please type the name of the item. "
						+ "When you are done viewing your items please type done.~|~", Player, in_Combat, Monster);
				while(true) {
					
					if(answer.toLowerCase().contains("done")) 
					{
						break;
					}
					else 
					{
						Boolean success=false;
						for(Data.item item:Player.get_Items()) 
						{
							if(answer.toLowerCase().contains(item.get_Name())) 
							{
								if(item.get_Damage()>0) 
								{
									Main.gui.print("name: "+item.get_Name()+"`damage: "+item.get_Damage()+"`durability: "
								+item.get_Durability()+"` rarity level: "+item.get_Rarity_Level(), Player, in_Combat, Monster);
								}
								else 
								{
									Main.gui.print("name: "+item.get_Name()+"`heals: "+-item.get_Damage()+"`durability: "
											+item.get_Durability()+"`rarity level: "+item.get_Rarity_Level(), Player, in_Combat, Monster);
								}
								success=true;
							}
						}
						if(success==false) 
						{
							answer=Main.gui.print("Sorry, but I couldn't understand that please try again.~|~", Player, in_Combat, Monster);
						}
					}
				}
			}
			else if(answer.toLowerCase().contains("use") && answer.toLowerCase().contains("items")) 
			{
				//modify/rewrite if more complicated non-combat items are added
				Main.gui.print("You can use the following items to heal yourself. The number next to the items name is how much health it "
			    		+ "will heal. You have "+Player.get_Health()+" health right now.`", Player, in_Combat, Monster);
				for(Data.item item : Player.get_Items()) 
				{
					if(item.get_Damage()<0) 
					{
						Main.gui.print(item.get_Name()+" "+-item.get_Damage()+"`", Player, in_Combat, Monster);
					}
				}
				String item_to_use=Main.gui.print("Which of the items would you like to use? If you decide to not use any just type none.~|~", Player, in_Combat, Monster);
			    Boolean success=false;
				while (true) 
			    {
			    	for(Data.item item: Player.get_Items()) 
			    	{
			    		if(item_to_use.toLowerCase().contains(item.get_Name())) 
			    		{
			    			if(in_Combat==true) 
			    			{
			    				int option_choice=take_Input(new Data.input_Choice[][] {new Data.input_Choice[] {new Data.input_Choice("yes")}},"You are in combat. "
			    						+ "You may still heal yourself in combat but you should know that you will be attacked by the opposing monster after using it. Do you "
			    						+ "still wish to proceed?~|~", null, in_Combat, Monster);
			    				if(option_choice!=1) 
			    				{
			    					success=true;
			    					break;
			    				}
			    			}
			    			Player.change_Health(item.get_Damage());
			    			Player.add_Or_Remove_Item(true, item);
			    			Main.gui.print("You used one "+item.get_Name()+". You now have "+Player.get_Health()+" health.~", Player, in_Combat, Monster);
			    			if(in_Combat==true) 
			    			{
			    				do_Damage(Player, Monster, null, false);
			    			}
			    			success=true;
			    			break;
			    		}
			    	}
			    	if(success==true) 
			    	{
			    		break;
			    	}
			    	else 
			    	{
			    		item_to_use=Main.gui.print("Sorry, but I could not understand that. Please try again.~|~", Player, in_Combat, Monster);
			    	}
			    }
			}
			else if(answer.toLowerCase().contains("name")) 
			{
				String name=Main.gui.print("What would you like to change your name to?~|~", Player, in_Combat, Monster);
				Player.change_Name(name);
				answer=Main.gui.print("Your character's name is now " + name+". Would you like to use this name during gameplay?~|~", Player, in_Combat, Monster);
				while(true) 
				{
					if(answer.toLowerCase().contains("yes")) 
					{
						Player.change_Uses_Own_Name(true);
						break;
					}
					else if(answer.toLowerCase().contains("no")) 
					{
						Player.change_Uses_Own_Name(false);
						break;
					}
					else 
					{
						answer=Main.gui.print("Sorry but I couldn't understand that. Please try again.~|~", Player, in_Combat, Monster);
					}
				}
			}
			else if(answer.toLowerCase().contains("done")) 
			{
				break;
			}
			else 
			{
				Main.gui.print("Sorry, but I didn't understand that, please try again.~", Player, in_Combat, Monster);
			}
		}
	}
}
