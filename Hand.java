/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. 
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public abstract class Hand extends CardList{
	private static final long serialVersionUID = -3711761437629470849L;
	private CardGamePlayer player;
	
	/**
	 * a constructor to initialize the type of hand
	 * 
	 * @param player
	 *        in type of CardGamePlayer to store the information which player own this hand
	 * @param cards
	 *        in type of CardList to store the 
	 */
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player=player;
		for (int i=0; i<cards.size(); i++)
		{
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * a method for retrieving the player of this hand
	 * 
	 * @return player
	 */
	public CardGamePlayer getPlayer()
	{
		return player;
	}
	
	/**
	 *  a method for retrieving the top card of this hand.
	 *  
	 *  @return Card;
	 */
	public Card getTopCard()
	{
		Card ans=this.getCard(0);
		for (int i=0; i<this.size(); i++)
		{
			if (this.getCard(i).compareTo(ans)==1)
			{
				ans=this.getCard(i);
			}
		}
		return ans;
	}
	
	/**
	 *  a method for checking if this hand beats a specified hand.
	 *  
	 * @param hand
	 * 		  a specific hand to be compared
	 * 
	 * @return true if this hand beats the specific hand, vice versa
	 */
	public boolean beats(Hand hand)
	{
		if (hand.size()!=this.size())
		{
			return false;
		}
		else if (hand.getType()==this.getType())
		{
			if (hand.getTopCard().compareTo(this.getTopCard())==1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			switch(this.getType()){
			case "Straight":
				return false;
			case "Flush":
				if (hand.getType()!="Straight")
				{
					return false;
				}
				else
				{
					return true;
				}
			case "FullHouse":
				if (hand.getType()!="Straight" && this.getType()!="Flush")
				{
					return false;
				}
				else
				{
					return true;
				}
			case "Quad":
				if (hand.getType()=="StraightFlush")
				{
					return false;
				}
				else
				{
					return true;
				}
			case "StraightFlush":
				return true;
			default:
				return true;
			}
		}
	}
	
	/**
	 * the abstract method for the sub-class of hand to check the validity of the hand
	 * @return the the validity of the hand
	 */
	public abstract boolean isValid();
	
	/**
	 * the abstract method for the sub-class of hand to show the type of the hand
	 * @return String 
	 * 			the type of the hand
	 */
	public abstract String getType();
	
}
