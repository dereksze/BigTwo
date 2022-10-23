/**
 * The Single class is a subclass of the Hand class, and is used to model a hand of cards. 
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public class Single extends Hand{
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor to initialize the type of hand
	 * @param player 
	 *        to show who owns this hand , in type of CardGamePlayer
	 *        
	 * @param cards
	 *        input list of cards, in type CardList
	 */
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method to return the type of hand
	 * @return "Single
	 */
	public String getType()
	{
		return "Single";
	}
	
	/**
	 * a method to check the validity of this type of hand
	 * 
	 *@return true if it is a valid hand, vice versa
	 */
	public boolean isValid()
	{
		if (this.size()==1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
