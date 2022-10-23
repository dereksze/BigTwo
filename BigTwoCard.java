/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game.
 * 
 * @author SzeHoYin
 * @version 1.0
 */
public class BigTwoCard extends Card{
	private static final long serialVersionUID = -713898713776577970L;
	
	/**
	 * Creates and returns an instance of the Card class.
	 * 
	 * @param suit
	 *            an int value between 0 and 3 representing the suit of a card:
	 *            <p>
	 *            0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank
	 *            an int value between 0 and 12 representing the rank of a card:
	 *            <p>
	 *            0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11
	 *            = 'Q', 12 = 'K'
	 */
	public BigTwoCard(int suit, int rank)
	{
		super(suit,rank);
	}
	
	/**
	 * a method for comparing the order of this card with the specified card.
	 * 
	 * @param card
	 * 			the card to be compared
	 * @return  a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card)
	{
		int modifiedthis=this.rank-2,modifiedcard=card.rank-2;
		if (modifiedthis==-1)//2
		{
			modifiedthis=14;
		}
		if (modifiedthis==-2)//A
		{
			modifiedthis=13;
		}
		if (modifiedcard==-1)//2
		{
			modifiedcard=14;
		}
		if (modifiedcard==-2)//A
		{
			modifiedcard=13;
		}
		if (modifiedthis>modifiedcard)
		{
			return 1;
		}
		else if (modifiedthis < modifiedcard)
		{
			return -1;
		}
		else if (this.suit>card.suit)
		{
			return 1;
		}
		else if (this.suit<card.suit)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
}
