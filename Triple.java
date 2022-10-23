/**
 * The Triple class is a subclass of the Hand class, and is used to model a hand of cards. 
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public class Triple extends Hand{
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor to initialize the type of hand
	 * @param player 
	 *        to show who owns this hand , in type of CardGamePlayer
	 *        
	 * @param cards
	 *        input list of cards, in type CardList
	 */
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method to return the type of hand
	 * @return "Triple"
	 */
	public String getType()
	{
		return "Triple";
	}
	
	/**
	 * a method to check the validity of this type of hand
	 * 
	 *@return true if it is a valid hand, vice versa
	 */
	public boolean isValid()
	{
		if (this.size()==3 && this.getCard(0).getRank()==this.getCard(1).getRank() && this.getCard(0).getRank()==this.getCard(2).getRank())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
