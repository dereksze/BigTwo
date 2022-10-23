/**
 * The Quad class is a subclass of the Hand class, and is used to model a hand of cards. 
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public class Quad extends Hand{
	private static final long serialVersionUID = -3711761437629470849L;
	
	/**
	 * a constructor to initialize the type of hand
	 * @param player 
	 *        to show who owns this hand , in type of CardGamePlayer
	 *        
	 * @param cards
	 *        input list of cards, in type CardList
	 */
	public Quad(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method to return the type of hand
	 * @return "Quad"
	 */
	public String getType()
	{
		return "Quad";
	}
	
	/**
	 * a method to check the validity of this type of hand
	 * 
	 *@return true if it is a valid hand, vice versa
	 */
	public boolean isValid()
	{
		if (this.size()==5)
		{
			this.sort();
			int ca=1,cb=1, a=this.getCard(0).getRank(), b=this.getCard(4).getRank();
			for (int i=1; i<4; i++)
			{
				if (this.getCard(i).getRank()==a)
				{
					ca++;
				}
				else if (this.getCard(i).getRank()==b)
				{
					cb++;
				}
				else
				{
					return false;
				}
			}
			if ((ca==4 && cb==1) || (ca==1 && cb==4))
			{
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}

	}
}
