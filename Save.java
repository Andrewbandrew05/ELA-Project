import java.io.Serializable;
public class Save implements Serializable{
	private Player Player;
	private String level_name;
	private int level_stage;
	Save (Player Player, String level_name, int level_stage) 
	{
		this.Player=Player;
		this.level_name=level_name;
		this.level_stage=level_stage;
	}
	public Player get_Player() 
	{
		return Player;
	}
	public String get_level_name() 
	{
		return level_name;
	}
	public int get_level_stage() 
	{
		return level_stage;
	}
}
