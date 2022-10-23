/**
 * The Flush class is a subclass of the Hand class, and is used to model a hand of cards. 
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public class Flush extends Hand{
	private static final long serialVersionUID = -3711761437629470849L;
	/**
	 * a constructor to initialize the type of hand
	 * @param player 
	 *        to show who owns this hand , in type of CardGamePlayer
	 *        
	 * @param cards
	 *        input list of cards, in type CardList
	 */
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method to return the type of hand
	 * @return "Flush"
	 */
	public String getType()
	{
		return "Flush";
	}
	
	/**
	 * a method to check the validity of this type of hand
	 * 
	 *@return true if it is a valid hand, vice versa
	 */
	public boolean isValid()
	{
		boolean first=true;
		if (this.size()==5)
		{
			int suit=this.getCard(0).getSuit();
			for (int i=1; i<5; i++)
			{
				if (this.getCard(i).getSuit()!=suit)
				{
					first=false;
				}
			}
			if (first)
			{
				for (int i=0; i<3; i++)
				{
					if (this.getCard(i).getRank()+1!=this.getCard(i+1).getRank())
					{
						return true;
					}
				}
				return false;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
