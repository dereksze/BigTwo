/**
 * The StraightFlush class is a subclass of the Hand class, and is used to model a hand of cards. 
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public class StraightFlush extends Hand{
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor to initialize the type of hand
	 * @param player 
	 *        to show who owns this hand , in type of CardGamePlayer
	 *        
	 * @param cards
	 *        input list of cards, in type CardList
	 */
	public StraightFlush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method to return the type of hand
	 * @return "StraightFlush"
	 */
	public String getType()
	{
		return "StraightFlush";
	}
	
	/**
	 * a method to check the validity of this type of hand
	 * 
	 * @return true if it is a valid hand, vice versa
	 */
	public boolean isValid()
	{
		this.sort();
		boolean first=true;
		if (this.size()==5)
		{
			for (int i=0; i<4; i++)
			{
				if (this.getCard(i).getSuit()!=this.getCard(i+1).getSuit())
				{
					first=false;
				}
			}
			if (first)
			{
				if ((this.getCard(0).getRank()==10 && this.getCard(1).getRank()==11 && this.getCard(2).getRank()==12 && this.getCard(3).getRank()==0 && this.getCard(4).getRank()==1)
						|| (this.getCard(0).getRank()==9 && this.getCard(1).getRank()==10 && this.getCard(2).getRank()==11 && this.getCard(3).getRank()==12 && this.getCard(4).getRank()==0))
					{
						return true;
					}
					else
					{
						
						for (int i=0; i<4; i++)
						{
							if (this.getCard(i).getRank()+1!=this.getCard(i+1).getRank())
							{
								return false;
							}
						}
						return true;
					}
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
