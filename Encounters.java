import java.util.*;
public class Encounters {
	public static void boss_Fight (Player Player, String boss_Name) throws InterruptedException 
	{
		Data.monster boss = null;
		if(boss_Name=="random") 
		{
			boss=Data.boss_data_base.get(Methods.get_Ran_Object_From_Dict(Data.boss_data_base));
		}
		else
		{
			for(String key : Data.boss_data_base.keySet()) 
			{
				if(key==boss_Name) 
				{
					boss=Data.boss_data_base.get(key);
				}
			}
		}
		Methods.fight(Player, boss);
	}
	public static void random_Encounter(Player Player) throws InterruptedException
	{
		Random rand = new Random();
		int which_encounter=rand.nextInt(1);
		if(which_encounter==0) 
		{
			monster_Encounter(Player);
		}
		else if (which_encounter==1) 
		{
			item_Stash_Encounter(Player);
		}
		else 
		{
			System.out.println("reeeeeee");
		}
	}
	public static void monster_Encounter(Player Player) throws InterruptedException 
	{
		
		Data.monster Monster=Data.monster_data_base.get(Methods.get_Ran_Object_From_Dict(Data.monster_data_base));
		Main.gui.print("You encounter a "+Monster.get_Name()+"!~", Player, true, Monster);
		Methods.fight(Player, Monster);
	}
	public static void item_Stash_Encounter(Player Player) throws InterruptedException 
	{
		Main.gui.print("You have found an item stash!~", Player, false, null);
		Random rand=new Random();
		int object_rarity_level= Math.round((float)((rand.nextInt(9)+1)*0.4));
		String object_rarity="";
		if(object_rarity_level==1) 
		{
			object_rarity="common";
		}
		else if(object_rarity_level==2) 
		{
			object_rarity="uncommon";
		}
		else if(object_rarity_level==3) 
		{
			object_rarity="rare";
		}
		else 
		{
			object_rarity="legendary";
		}
		int num_possible_objects=0;
		for(Data.item item : Data.item_data_base.values()) 
		{
			if(item.get_Rarity_Level()==object_rarity) 
			{
				num_possible_objects+=1;
			}
		}
		int which_object_to_give=rand.nextInt(num_possible_objects);
		int times_looped=0;
		for(Data.item item : Data.item_data_base.values()) 
		{
			if(item.get_Rarity_Level()==object_rarity) 
			{
				if(times_looped==which_object_to_give) 
				{
					Player.add_Or_Remove_Item(false, item);
					times_looped+=1;
					Main.gui.print("You have found a " +item.get_Name()+"!~", Player, false, null);
				}
			}
		}

	}
}
