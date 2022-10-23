import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 * The BigTwoClient class is used to model a Big Two card game
 * 
 * @author SzeHoYin
 * @version 2.0
 */
public class BigTwoClient implements CardGame,NetworkGame {
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos ;
	private int currentIdx;
	private BigTwoTable table;
	private boolean stop_game=false;
	private boolean no_action=true;
	
	/**
	 * a constructor for creating a Big Two client.
	 */
	public BigTwoClient()
	{
		// create 4 players and add them to the list of players
		playerList=new ArrayList<CardGamePlayer> (4);
		for (int i=0; i<4; i++)
		{
			CardGamePlayer n=new CardGamePlayer();
			n.setName("");
			playerList.add(n);
		}
		handsOnTable=new ArrayList<Hand>();
		
		//create a Big Two table which builds the GUI for the game and handles user actions;
		table=new BigTwoTable(this);
		

		// make a connection to the game server by calling the makeConnection() method from the NetworkGame interface
		this.makeConnection();
	
	}

	@Override
	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 * @return int playerID
	 * 		the playerID of local player
	 */
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	/**
	 * ¡V a method for setting the playerID (i.e., index) of the local player.
	 * @param int playerID
	 * 		the number to represent the specific player
	 */
	public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID=playerID;
	}

	@Override
	/**
	 * a method for setting the playerID (i.e., index) of the local player.
	 *
	 *@return String playerName
	 *		name of the local player
	 */
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return playerName;
	}

	@Override
	/**
	 * a method for setting the name of the local player
	 * @param String playerName
	 * 		the name of the local player
	 */
	public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName=playerName;
	}

	@Override
	/**
	 * a method for getting the IP address of the game server.
	 * 
	 * @return String serverIP
	 * 		the IP address of the game server
	 */
	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP;
	}

	@Override
	/**
	 * ¡V a method for setting the IP address of the game server
	 * @param String serverIP
	 * 		the number of IP address of local server
	 */
	public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP=serverIP;
	}

	@Override
	/**
	 * a method for getting the TCP port of the game server.
	 * 
	 * @return int severPort
	 * 		the number of TCP port of the game server
	 */
	public int getServerPort() {
		// TODO Auto-generated method stub
		return serverPort;
	}

	@Override
	/**
	 * a method for setting the TCP port of the game server
	 * @param int serverPort
	 * 		number of the TCP Port
	 */
	public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort=serverPort;
	}

	@Override
	/**
	 * a method for making a socket connection with the game server
	 */
	public void makeConnection(){
		try
		{
			serverIP="127.0.0.1";
			serverPort=2396;
			sock=new Socket(serverIP,serverPort);
			
			//create an ObjectOutputStream for sending messages to the game server
			oos=new ObjectOutputStream(sock.getOutputStream());
			
			//create thread for receiving message from game server
			Thread t=new Thread(new ServerHandler());
			t.start();
			
			

			this.table.printMsg("Connected to the server at "+this.serverIP+":2396 "+"\n");
		}
		catch(Exception ex)
		{
			this.table.printMsg("Failed to connect to the server\n");
		}
		
	}

	@Override
	/**
	 * a method for parsing the messages received from the game server
	 * @param GameMessage message
	 * 		the message inputed 
	 */
	public synchronized void parseMessage(GameMessage message) {
		switch (message.getType()){
			case CardGameMessage.PLAYER_LIST:
				//set name of the player
				boolean once=true;
				for(int i=0; i<4; i++)
				{
					String[] ans=(String []) message.getData();
					if (ans[i]!=null)
					{
						this.playerList.get(i).setName(ans[i]);
					}
					else if (once)
					{
						playerList.get(i).setName(playerName);
						this.table.setLocalPlayer(i);
						once=false;
					}
				}
				//snd msg of type JOIN to game server, data: player ID=-1, String playerName
				CardGameMessage join_message=new CardGameMessage(CardGameMessage.JOIN,-1,playerName);
				this.sendMessage(join_message);
				
				break;
			case CardGameMessage.FULL:
				table.printMsg("The server is full, you fail to join the game.\n");
				break;
			case CardGameMessage.JOIN:
				table.printMsg((String)message.getData()+" joined the game\n");
				this.playerList.get(message.getPlayerID()).setName((String)message.getData());
				this.table.addPlayerInfo(false,-1);
				
				if (message.getPlayerID()==table.getLocalPlayer())
				{
					//snd msg of type READY to game server, data: playerID=-1, data=null
					CardGameMessage readyMessage=new CardGameMessage(CardGameMessage.READY,-1,null);
					this.sendMessage(readyMessage);
				}
				
				break;
			case CardGameMessage.QUIT:
				this.table.addPlayerInfo(true,message.getPlayerID());
				this.table.disable();
	//end of game implementation
				if (!stop_game)
				{
					stop_game=true;
					CardGameMessage ready_msg=new CardGameMessage(CardGameMessage.READY,-1,null);
					this.sendMessage(ready_msg);
				}
				break;
			case CardGameMessage.READY:
				table.printMsg(playerList.get(message.getPlayerID()).getName()+" is ready for the game.\n");
				break;
			case CardGameMessage.START:
				start((Deck)message.getData());
				stop_game=false;
				break;
			case CardGameMessage.MOVE:
				this.checkMove(message.getPlayerID(), (int[])message.getData());
				break;
			case CardGameMessage.MSG:
				table.print_dmMsg((String)message.getData()+"\n");
				break;
			default:
				// invalid message
				break;
		}
	}

	/**
	 * a method to get the oos
	 * @return ObjectOutputStream oos
	 * 
	 */
	public ObjectOutputStream getOos()
	{
		return oos;
	}
	
	/**
	 * a method for sending the specified message to the game server
	 * @param GameMessage message
	 * 		the message inputed
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		try
		{
			oos.writeObject(message);	

		}
		catch(Exception ex)
		{
			table.printMsg("Failed to send a message");
		}
	}

//CardGame Interface
	
	@Override
	/**
	 * a method for getting the number of players.
	 * @return int numOfPlayers
	 * 		the number of players 
	 */
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return numOfPlayers;
	}

	@Override
	/**
	 * a method for getting the deck of cards being used
	 * @return Deck deck
	 * 		the deck of cards being used
	 */
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return deck;
	}

	@Override
	/**
	 * a method for getting the list of players
	 * @return ArrayList<CardGaemPlayer> playerList
	 * 		the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return playerList;
	}

	@Override
	/**
	 * a method for getting the list of hands played on the table
	 * @return ArrayList<Hand> handsOnTable
	 * 		the list of hands played
	 */
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return handsOnTable;
	}

	@Override
	/**
	 * a method for getting the index of the player for the current turn
	 * @return int currentIdx
	 * 		the current player index
	 */
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return currentIdx;
	}

	@Override
	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards.
	 * @param Deck deck
	 * 		the shuffled deck to be used
	 */
	public void start(Deck deck) {
		//(1) remove all the cards from the player as well as from the table
		this.table.reset();
		if (handsOnTable.size()!=0)
		{
			while (handsOnTable.size()!=0)
			{
				handsOnTable.remove(0);
			}
		}
		this.table.printMsg("New Game Started !!!!!!!!\n" );
		for (int i=0; i<4; i++)
		{
			this.getPlayerList().get(i).removeAllCards();
		}
		//(2) distribute cards to players
		//(3) identify the player who holds the 3 of Diamonds
		//(4) set both the currentIdx of the BigTwo instance and the activePlayer of the BigTwoTable instance to the index of the player who holds the 3 of Diamonds.

		int kickstart=-1,num=0;
		CardGamePlayer player = playerList.get(num);
		this.deck=deck;
		for (int i=0; i<deck.size(); i++)// assign deck of cards to players and find the one who with the diamond 3 to start the game
		{
			player.addCard(deck.getCard(i));
			if (deck.getCard(i).getRank()==2 && deck.getCard(i).getSuit()==0)
			{
				kickstart=num;
			}
			if ((i+1)%13==0 && num<3)
			{
				num++;
				player=this.playerList.get(num);
			}

		}
		this.currentIdx=kickstart;
		this.table.setActivePlayer(kickstart);
		
		//(5) logic
		
		//game loop
		Thread game=new Thread(new GameHandler());
		game.start();
		
		

		
	}

	@Override
	/**
	 * a method for making a move by a player with the specified playerID using the cards specified by the list of indices.
	 * 
	 * @param int playerID
	 * 		specified which player plays the hand
	 * @param int[] cardIdx
	 * 		specified which cards are chosen
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage message=new CardGameMessage(CardGameMessage.MOVE,playerID,cardIdx);
		this.sendMessage(message);
	}

	@Override
	/**
	 * a method for checking a move made by a player
	 * 
	 * @param int playerID
	 * 		specified which player plays the hand
	 * @param int[] cardIdx
	 * 		specified which cards are chosen
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		if (cardIdx!=null && cardIdx[0]==-1)//pass
		{
			this.table.printMsg("{pass}\n");
			if (this.getCurrentIdx()!=3)
			{
				currentIdx++;
			}
			else
			{
				currentIdx=0;
			}
			table.setShowHandGUI();
			no_action=false;
		}
		else // play a hand
		{
			CardGamePlayer player=playerList.get(playerID);
			CardList current=new CardList();
			if (cardIdx!=null)
			{
				for (int i=0; i<cardIdx.length; i++)
				{
					current.addCard(player.getCardsInHand().getCard(cardIdx[i]));//build the cardlist
				}
				
				Hand hand=this.composeHand(player, current);

				if (hand!=null && (this.handsOnTable.isEmpty() || hand.beats(this.handsOnTable.get(this.handsOnTable.size()-1)) || this.handsOnTable.get(this.handsOnTable.size()-1).getPlayer()==this.getPlayerList().get(this.currentIdx)))
				{
					String text="{"+hand.getType()+"}";
					table.printMsg(text);
					
					
					if (current.size() > 0) {
						for (int i = 0; i < current.size(); i++) {
							String string = "";
							if (true) {
								string = string + "[" + current.getCard(i) + "]";
							} else {
								string = string + "[  ]";
							}
							if (i % 13 != 0) {
								string = " " + string;
							}
							table.printMsg(string);
							if (i % 13 == 12 || i == current.size() - 1) {
								table.printMsg("");
							}
						}
						table.printMsg("\n");

					}
					
					
					player.removeCards(current);
					this.handsOnTable.add(hand);
					if (this.getCurrentIdx()!=3)
					{
						currentIdx++;
					}
					else
					{
						currentIdx=0;
					}
					no_action=false;
				}
				else
				{
					table.printMsg("<== Not a legal move!!!\n");
					table.resetSelected();
				}
				table.setShowHandGUI();
			}
			else
			{
				table.printMsg("<== Not a legal move!!!\n");
				table.resetSelected();
				table.setShowHandGUI();
			}
		}
	}

	@Override
	/**
	 * a method for checking if the game ends
	 * 
	 * @return true if the game ends
	 */
	public boolean endOfGame() {
		for (int i=0; i<4; i++)
		{
			if (playerList.get(i).getNumOfCards()==0)
			{
				return true;
			}
		}
		return false;	
	}
	
//public static method
	/**
	 * a method for creating an instance of BigTwoClient
	 * @param String [] args
	 */
	public static void main(String []args)
	{
		BigTwoClient bigTwoClient=new BigTwoClient();
		
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player
	 * 
	 * @param CardGamePlayer player
	 * 			the specific player who hold this hand
	 * @param CardList cards
	 * 			the hand that the player hold
	 * @return the specific type of valid hand, if no valid hand return null
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		Single single=new Single(player,cards);
		Pair p=new Pair(player,cards);
		Triple tri=new Triple(player,cards);
		Straight straight=new Straight(player,cards);
		Flush flush=new Flush(player,cards);
		FullHouse fullhouse=new FullHouse(player,cards);
		Quad quad=new Quad(player,cards);
		StraightFlush straightflush=new StraightFlush(player,cards);
		if (single.isValid())
		{
			return single;
		}
		else if (p.isValid())
		{
			return p;
		}
		else if (tri.isValid())
		{
			return tri;
		}
		else if (straight.isValid())
		{
			return straight;
		}
		else if (flush.isValid())
		{
			return flush;
		}
		else if (fullhouse.isValid())
		{
			return fullhouse;
		}
		else if (quad.isValid())
		{
			return quad;
		}
		else if (straightflush.isValid())
		{
			return straightflush;
		}

		else
			return null;
	}
	//innerclass
		/**
		 * ¡V an inner class that implements the Runnable interface.
		 * 
		 * @author szehoyin
		 *
		 */
		class ServerHandler implements Runnable
		{
			@Override
			/**
			 * a method to declare the job of a thread to be makeConnection()
			 */
			public void run() {
				try
				{
					ObjectInputStream ois=new ObjectInputStream(sock.getInputStream());
					CardGameMessage message;
					while ((message = (CardGameMessage) ois.readObject()) != null)
					{
						BigTwoClient.this.parseMessage(message);
						try{
							Thread.sleep(50);
						}
						catch(Exception ex){}
					}
				}
				catch(Exception ex)
				{
					
				}
			}
		}	
		/**
		 * ¡V an inner class that implements the Runnable interface.
		 * 
		 * @author szehoyin
		 *
		 */
		class GameHandler implements Runnable
		{
			
			/**
			 * a method to declare the job of a thread to be run the game()
			 */
			CardGamePlayer player;
			public void run() 
			{
				while (!BigTwoClient.this.endOfGame() && !stop_game)
				{
					player=BigTwoClient.this.playerList.get(currentIdx);
					String msg;
					if (BigTwoClient.this.table.getLocalPlayer()==BigTwoClient.this.currentIdx)
					{
						msg="Your turn: \n";
					}
					else
					{
						msg=player.getName()+"'s turn: \n";
					}
					table.printMsg(msg);
					
					
					BigTwoClient.this.no_action=true;
					table.setActivePlayer(BigTwoClient.this.getCurrentIdx());

					for (int i=0; i<4; i++)
					{
						BigTwoClient.this.playerList.get(i).sortCardsInHand();
					}
				
					table.setShowHandGUI();
					if (BigTwoClient.this.table.getLocalPlayer()==BigTwoClient.this.currentIdx)
					{
						BigTwoClient.this.table.enable();
						while (!table.getButtonPressed())
						{
							try{
								Thread.sleep(2000);
							}
							catch(Exception ex){}
						}
						table.unpress_ButtonPressed();
						table.resetSelected();
					}
					else
					{
						BigTwoClient.this.table.disable();
						while (no_action && !stop_game)
						{
							try{
								Thread.sleep(2000);
							}
							catch (Exception ex){}
						}
						if (stop_game)
						{
							break;
						}
					}
					
				}
				
				//end of game
				if (BigTwoClient.this.endOfGame())
				{
					JFrame frame=new JFrame();
					String message="";
					String title;
					for (int i=0; i<4; i++)
					{
						if (playerList.get(i).getNumOfCards()==0)
						{
							message=playerList.get(i).getName()+" wins the game.\n";
						}
						else
						{
							message=playerList.get(i).getName()+" has "+playerList.get(i).getNumOfCards()+" cards in hand.";
						}
						String p=(playerList.get(i).getName()+message);
						table.printMsg(p+"\n");
					}
					if (BigTwoClient.this.table.getLocalPlayer()==BigTwoClient.this.currentIdx)
					{
						title="You win!";
					}
					else
					{
						title="You lose!";
					}
					JOptionPane.showMessageDialog(frame, message,title,JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
}

