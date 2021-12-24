
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Museum {

	public static String process_Stage(Data.object[][] level_Stage, Player Player) throws InterruptedException 
	{
		List<Data.object_In_Sight> objects_In_Sight=Methods.get_Objects_In_Sight(level_Stage, Player);
		//Methods.give_Description_of_Surroundings(objects_In_Sight, Player);
		Methods.draw_Surroundings(objects_In_Sight, Player);
		Data.object_In_Sight object_Being_Touched=null;
		while(true) 
		{
			String command=Main.gui.print("What would you like to do?~|~",Player, false, null);
			Data.movement_Command movement_Command=Methods.process_Commands(command, Player);
			if (movement_Command.direction_To_Move!=null) 
			{
				if (movement_Command.direction_To_Move=="inspect") 
				{
					if(object_Being_Touched!=null ) 
					{
						if(object_Being_Touched.get_Clue()!=null) 
						{
							Main.gui.print(object_Being_Touched.get_Clue_Text()+"~", Player, false, null);
							continue;
						}
						else if(object_Being_Touched.get_Next_Stage()!=null) 
						{
							String answer=Main.gui.print("This "+object_Being_Touched.get_Name()+" will take you to the "+object_Being_Touched.get_Next_Stage()+" room. Are you sure you would like to go there?~|~", Player, false, null).toLowerCase();
							if(answer.contains("yes") || answer.contains("yeah")) 
							{
								if (object_Being_Touched.get_Next_Stage()=="starter") 
								{
									starting_Room(Player);
								}
								else if (object_Being_Touched.get_Next_Stage()=="Turn of the Screw") 
								{
									stage_1(Player);
								}
								else if (object_Being_Touched.get_Next_Stage()=="Pet Sematary")
								{
									stage_2(Player);
								}	
								else if(object_Being_Touched.get_Next_Stage()=="end") 
								{
									end(Player);
								}
								else 
								{
									stage_3(Player);
								}
							}
							continue;
						}
					}
					Main.gui.print("Sorry, but you are not currently touching an inspectable object.~", Player, false, null);
				}
				else if (movement_Command.direction_To_Move.contains("guess"))
				{
					return movement_Command.direction_To_Move.split("~")[1];
				}
				else 
				{
					for(int x=0;x<movement_Command.feet_To_Move;x+=1) 
					{
						Player.move(1, movement_Command.direction_To_Move);
						objects_In_Sight=Methods.get_Objects_In_Sight(level_Stage, Player);
						if(objects_In_Sight.size()==1 && Methods.is_Touching(objects_In_Sight.get(0), Player)!=null) 
						{
							Main.gui.print("Sorry, but you may not move there.~", Player, false, null);
							Player.move(-1, movement_Command.direction_To_Move);
							object_Being_Touched=objects_In_Sight.get(0);
							break;
						}
						else 
						{
							Methods.draw_Surroundings(objects_In_Sight, Player);
							object_Being_Touched=null;
						}
						TimeUnit.MILLISECONDS.sleep(90);
					}
				}
			}
			//Methods.give_Description_of_Surroundings(Methods.get_Objects_In_Sight(level_Stage, Player), Player);
		}
	}
	public static void stage_1(Player Player) throws InterruptedException 
	{
		//Player starting positions (set to actual ones once you know what you want it to be
		Player.set_X_Pos(8);
		Player.set_Y_Pos(10);
		Main.gui.print("The first room is dedicated to The Turn of The Screw; a 19th century American horror tale.~", Player, false, null);
		String guess=process_Stage(Data.museum_Stage_1, Player);
		
		if (guess.toLowerCase().contains(Data.museum_Stage_1_Answer)) 
		{
			Main.gui.print("Congragulations! You have completed stage one.~", Player, false, null);
			Player.set_Current_Stage(2);
			Methods.save_Game(Player, "Museum", 2);
			starting_Room(Player);
		}
	}
	public static void stage_2(Player Player) throws InterruptedException 
	{
		Player.set_X_Pos(8);
		Player.set_Y_Pos(10);
		Main.gui.print("The second room is dedicated to Pet Sematary; a 20th century American horror tale.~", Player, false, null);
		String guess=process_Stage(Data.museum_Stage_2, Player);
		if (guess.toLowerCase().contains(Data.museum_Stage_2_Answer)) 
		{
			Main.gui.print("Congragulations! You have completed stage two.~", Player, false, null);
			Player.set_Current_Stage(3);
			Methods.save_Game(Player, "Museum", 3);
			starting_Room(Player);
		}
	}
	public static void stage_3(Player Player) throws InterruptedException 
	{
		Player.set_X_Pos(8);
		Player.set_Y_Pos(10);
		Main.gui.print("The third room is dedicated to Tender is the Flesh; a 21st century horror tale by Agustina Bazzterica.~", Player, false, null);
		String guess=process_Stage(Data.museum_Stage_3, Player);
		if (guess.toLowerCase().contains(Data.museum_Stage_3_Answer)) 
		{
			Main.gui.print("Congragulations! You have completed stage three.~", Player, false, null);
			Player.set_Current_Stage(4);
			Methods.save_Game(Player, "Museum", 4);
			starting_Room(Player);
		}
	}
	public static void starting_Room(Player Player) throws InterruptedException
	{
		Player.set_X_Pos(6);
		Player.set_Y_Pos(10);
		Main.gui.print("This first room is nothing more than an entry hall. Doors surround you..some obvious, some less so. "
				+ "The only thing of great interest is a console in the center of the room.~", Player, false, null);
		process_Stage(Data.museum_Starter_Room, Player);
	}
	public static void end (Player Player) throws InterruptedException 
	{
		Player.set_X_Pos(6);
		Player.set_Y_Pos(4);
		Methods.draw_Surroundings(Methods.get_Objects_In_Sight(Data.outside, Player), Player);
		Main.gui.print("Noble, Barnes. \"The Turn of the Screw: Illustrated Edition(vertical bar)Paperback.\" Barnes & Noble, 12 June 2021, www.barnesandnoble.com/w/the-turn-of-the-screw-henry-james/1139681056.`"
				+ "King, S., & Dobner, T. (2019). It. Sperling & Kupfer.`"
				+ "Bazterrica, A. (2021). Tender is the flesh.`"
				+ "Bazterrica, Agustina. \"â€˜I Have Always Believed That in Our Capitalist, Consumerist Society, We Devour Each Other.â€™\" The Irish Times, The Irish Times, 21 Feb. 2020, www.irishtimes.com/culture/books/i-have-always-believed-that-in-our-capitalist-consumerist-society-we-devour-each-other-1.4179631.`"
				+ "King, S. (2019). Pet sematary. Hodder & Stoughton.`"
				+ "Lindop, C., Re, D. G., & James, H. (2019). The turn of the screw. Oxford University Press.`", Player, false, null);
	}
}
