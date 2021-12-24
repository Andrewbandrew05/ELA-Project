//Once the main game is done make the game run on a thread and have code that
//says that if player_is_dead==true stop thread.(maybe)

import java.io.*;
public class Main {
	public static Boolean player_is_alive=true;
	public static String current_save_file_path="";
	public static GUI gui=new GUI();
	public static void main(String[] args) throws InterruptedException
	{
		/*Player Player=new Player("bob", true);
		String[][][] level_Stage=new String[500][500][500];
		level_Stage[50][50][1]="tree";
		Methods.give_Description_of_Surroundings(level_Stage, Player);	*/	
		Methods.start_Program();
	}
}
