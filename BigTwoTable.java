/**
 *  It is used to build a GUI for the Big Two card game and handle all user actions
 * @author szehoyin
 * @version 1.0
 *
 */
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;
import java.util.StringTokenizer;
public class BigTwoTable implements CardGameTable{
	private BigTwoClient game;
	private boolean[] selected=new boolean[13];
	private int activePlayer;
	private int localPlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private JLabel labelp1;
	private JLabel labelp2;
	private JLabel labelp3;
	private JLabel labelp4;
	private JLayeredPane cards_1;
	private JLayeredPane cards_2;
	private JLayeredPane cards_3;
	private JLayeredPane cards_4;
	private JLayeredPane cards_table;
	private boolean ENABLE=true;
	private JPanel table;
	private boolean buttonpressed=false;
	private JLabel tabel_label;
	private JTextArea dmArea;
	private JTextField inputArea;
	private Image [] avatar_image;
	/**
	 * a constructor for creating a BigTwoTable
	 * 
	 * @param CardGame game
	 * 		a reference to a card game associates with this table.
	 * 
	 */
	public BigTwoTable(BigTwoClient game)
	{
		String name=JOptionPane.showInputDialog(null,"Input your name:");
		this.game=game;
		this.game.setPlayerName(name);
		frame=new JFrame();
	    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Game");
		frame.setLayout(new GridLayout(1,2));
	    bigTwoPanel = new BigTwoPanel();
	    bigTwoPanel.setLayout(null);
	    bigTwoPanel.setBackground(new Color(0,158,0));
	    labelp1=new JLabel();
	    labelp2=new JLabel();
	    labelp3=new JLabel();
	    labelp4=new JLabel();
	    //menubar
	    JMenuBar menuBar=new JMenuBar();
	    JMenu menu=new JMenu("Game");
	    menuBar.add(menu);
	    
	    JMenuItem menuItem1=new JMenuItem("Connect");
	    JMenuItem menuItem2=new JMenuItem("Quit");
	    menuItem1.addActionListener(new ConnectMenuItemListener());
	    menuItem2.addActionListener(new QuitMenuItemListener());

	    menu.add(menuItem1);
	    menu.add(menuItem2);
	    
	    frame.setJMenuBar(menuBar);
	    
	    //Image of avatars 
	    avatar_image=new Image[4];
	    
	    Image a=new ImageIcon("src/images/captain_america-512.jpg").getImage();
	    Image b=new ImageIcon("src/images/captain_marvel-512.jpg").getImage();
	    Image c=new ImageIcon("src/images/hulk-avangers-marvel-avatars-gartoon-marvel_avatars-hero-512.jpg").getImage();
	    Image d=new ImageIcon("src/images/v-08-512.jpg").getImage();
	    
	    avatar_image[0]=a;
	    avatar_image[1]=b;
	    avatar_image[2]=c;
	    avatar_image[3]=d;


	    //Image of cards
	    cardImages=new Image[13][4];
	    for (int i=1; i<=13; i++)
	    {
	    	for (int j=0; j<4; j++)
	    	{
	    		String rank_of_card=String.valueOf(i);
	    		String suit_of_card="a";
	    		String card=new String();
	    		switch(j)
	    		{
	    		case 0:
	    			suit_of_card="d";
	    			break;
	    		case 1:
	    			suit_of_card="c";
	    			break;
	    		case 2:
	    			suit_of_card="h";
	    			break;
	    		case 3:
	    			suit_of_card="s";
	    			break;
	    		}
	    		card="src/images/"+rank_of_card+suit_of_card+".gif";
	    		cardImages[i-1][j]=new ImageIcon(card).getImage();
	    		cardImages[i-1][j]=cardImages[i-1][j].getScaledInstance(56,72, Image.SCALE_SMOOTH);

	    	}
	    }
	    
	    //Image back of cards
	    cardBackImage=new ImageIcon("src/images/b.gif").getImage();
	    cardBackImage=cardBackImage.getScaledInstance(56,72, Image.SCALE_SMOOTH);

	    //set cardPanel
	    cards_1=new JLayeredPane();
	    cards_2=new JLayeredPane();
	    cards_3=new JLayeredPane();
	    cards_4=new JLayeredPane();
	   
	    //TablePanel
	    table=new JPanel();
	    table.setLayout(null);
	    table.setBounds(0,470,960,120);
	    table.setBackground(new Color(100,70,36));
	    
	    String text;
	    if (game.getHandsOnTable().isEmpty())
	    {
	    	text="Table: ";
	    }
	    else
	    {
	    	text="Table: \n Played by: "+ game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName();
	    }
	    tabel_label=new JLabel(text);
	    tabel_label.setForeground(Color.white);
	    tabel_label.setBounds(10,10,500,10);
	    table.add(tabel_label);
	    
	    cards_table=new JLayeredPane();
    	cards_table.setBounds(0,0,10000,10000);

    	table.add(cards_table);
	    bigTwoPanel.add(table);	    
	    
	    //ButtonsPanel
	    JPanel ButtonPanel=new JPanel();
	    ButtonPanel.setBounds(0,590,980,40);
	    ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    bigTwoPanel.add(ButtonPanel);
	    
	    //PlayButton
	    playButton=new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		ButtonPanel.add(playButton);
		
		//PassButton
		passButton=new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		ButtonPanel.add(passButton);
		
		//msgArea
		JPanel textArea=new JPanel();
		textArea.setLayout(null);
		
		msgArea=new JTextArea();
		JScrollPane sp=new JScrollPane(msgArea);
		sp.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setBounds(0,0,640,295);
		
		//dmArea
		dmArea=new JTextArea();
		JScrollPane sp2=new JScrollPane(dmArea);
		sp2.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));
		sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp2.setBounds(0,295,640,295);
		
		//text input area
		inputArea=new JTextField();
		inputArea.setBounds(100,600,500,20);
		inputArea.addActionListener(new ChatItemListener());
		ButtonPanel.add(inputArea);
		JLabel textAreaInput=new JLabel("Message");
		textAreaInput.setBounds(30,590,100,40);
		//add them to the panel
		textArea.add(sp);
		textArea.add(sp2);
		textArea.add(inputArea);
		textArea.add(textAreaInput);
		
		
	    frame.add(bigTwoPanel);
		frame.add(textArea);
	    frame.setSize(600, 600);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	    this.disable();
	}
	
	/**
	 * a method to update the local player shown on gui
	 * @param int localPlayer
	 * 		the number to be set to be the idx of local player
	 */
	public void setLocalPlayer(int localPlayer)
	{
		this.localPlayer=localPlayer;
	}
	
	/**
	 * a method to update the player list shown on gui
	 */
	public void addPlayerInfo(boolean quit,int quit_playerID)
	{
		if (quit)
	    {
			printMsg(this.game.getPlayerList().get(quit_playerID).getName()+" quited the server\n");
			this.game.getPlayerList().get(quit_playerID).setName("");
	    	switch (quit_playerID)
	    	{
	    	case 0:
	    		labelp1.setIcon(null);
	   			labelp1.setText(null);
	   			break;
	   		case 1:
	   			labelp2.setIcon(null);
	    		labelp2.setText(null);
	    		break;
	   		case 2:
	   			labelp3.setIcon(null);
	   			labelp3.setText(null);
	   			break;
    		case 3:
    			labelp4.setIcon(null);
	    		labelp4.setText(null);
	    		break;
	    	default:
	    		break;
	   		}
	    }
		else
		{
			//setup Players image with JLabel 
			for (int i=0; i<4; i++)
			{
				switch (i)
				{
				case 0:
					if (this.localPlayer==i)
					{
						labelp1.setText("You");
		    		    //set label text color
		    		    labelp1.setForeground(Color.blue);
					}
					else
					{
						labelp1.setText(game.getPlayerList().get(0).getName());
		    		    //set label text color
		    		    labelp1.setForeground(Color.black);
						
					}
					break;
				case 1:
					if (this.localPlayer==i)
					{
						labelp2.setText("You");
		    		    //set label text color
		    		    labelp2.setForeground(Color.blue);
					}
					else
					{
						labelp2.setText(game.getPlayerList().get(1).getName());
		    		    //set label text color
		    		    labelp2.setForeground(Color.black);
					}
					break;
				case 2:
					if (this.localPlayer==i)
					{
						labelp3.setText("You");
		    		    //set label text color
		    		    labelp3.setForeground(Color.blue);

					}
					else
					{
						labelp3.setText(game.getPlayerList().get(2).getName());
		    		    //set label text color
		    		    labelp3.setForeground(Color.black);

					}
					break;
				case 3:
					if (this.localPlayer==i)
					{
						labelp4.setText("You");
		    		    //set label text color
		    		    labelp4.setForeground(Color.blue);
					}
					else
					{
						labelp4.setText(game.getPlayerList().get(3).getName());
		    		    //set label text color
		    		    labelp4.setForeground(Color.black);

					}
					break;
				}
			}
			    
			  
		    
		    //add player info to panel
		    for (int i=0; i<4; i++)
		    {
		    	if (this.game.getPlayerList().get(i).getName()!="")
		    	{
		    		switch(i)
		    		{
		    		case 0:
		    			Image img_a_tuned=avatar_image[0].getScaledInstance(70,70, Image.SCALE_SMOOTH);
		    		    ImageIcon p1=new ImageIcon(img_a_tuned);
		    		    labelp1.setIcon(p1);
		    		    labelp1.setHorizontalTextPosition(JLabel.CENTER);
		    		    labelp1.setVerticalTextPosition(JLabel.TOP);
		    		    
		    		    labelp1.setBounds(10,10,100,100);
		    		    break;
		    		case 1:
		    			Image img_b_tuned=avatar_image[1].getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		    		    ImageIcon p2=new ImageIcon(img_b_tuned);
		    		    labelp2.setIcon(p2);
		    		    labelp2.setHorizontalTextPosition(JLabel.CENTER);
		    		    labelp2.setVerticalTextPosition(JLabel.TOP);
		    		    
		    		    labelp2.setBounds(10,130,100,100);

		    			break;
		    		case 2:
		    			Image img_c_tuned=avatar_image[2].getScaledInstance(70,70, Image.SCALE_SMOOTH);
		    		    ImageIcon p3=new ImageIcon(img_c_tuned);
		    		    labelp3.setIcon(p3);
		    		    labelp3.setHorizontalTextPosition(JLabel.CENTER);
		    		    labelp3.setVerticalTextPosition(JLabel.TOP);
		    		    
		    		    labelp3.setBounds(10,240, 100,100);

		    			break;
		    		case 3:
		    			Image img_d_tuned=avatar_image[3].getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		    			ImageIcon p4=new ImageIcon(img_d_tuned);
		    		    labelp4.setIcon(p4);
		    		    labelp4.setHorizontalTextPosition(JLabel.CENTER);
		    		    labelp4.setVerticalTextPosition(JLabel.TOP);

		    		    labelp4.setBounds(10,350,100,100);

		    			break;
		    		}
		    		
		    	}
		    	
		    	
		    	bigTwoPanel.add(labelp1);
	 		    bigTwoPanel.add(labelp2);
	 		    bigTwoPanel.add(labelp3);
	 		    bigTwoPanel.add(labelp4);
		    }
		}
	    
	    
	    
	    frame.repaint();
	}
	
	/**
	 * a method for setting the index of the active player
	 * @param int activePlayer
	 * 		 to represent the current player
	 */
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer=activePlayer;
	}
	
	/**
	 * a method to show the hands hold by the active player
	 */
	public void setShowHandGUI()
	{
		//set cards back image
		for (int j=0; j<4; j++)
		{
			CardList current_cardList=this.game.getPlayerList().get(j).getCardsInHand();
			switch (j)
			{
			case 0:
				this.cards_1.removeAll();

				if (j==this.localPlayer)
				{
					for (int i=0; i<current_cardList.size(); i++)
					{
						JLabel new_card=new JLabel();
						ImageIcon new_card_toadd= new ImageIcon(cardImages[current_cardList.getCard(i).getRank()][current_cardList.getCard(i).getSuit()]);
						new_card.setIcon(new_card_toadd);
						new_card.setBounds(200+i*20,38,56,72);
						new_card.addMouseListener(new BigTwoPanel());
						cards_1.add(new_card,Integer.valueOf(i));
					}
				}
				else
				{
					labelp1.setForeground(Color.black);
					for (int i=0; i<current_cardList.size(); i++)
				    {
				    	JLabel new_card=new JLabel();
				    	ImageIcon cardBackImage_icon=new ImageIcon(cardBackImage);
				    	new_card.setIcon(cardBackImage_icon);
				    	new_card.setBounds(200+i*20,38,56,72);
					    cards_1.add(new_card,Integer.valueOf(i));
				    }
				}
			    cards_1.setBounds(0,0,10000,10000);
				bigTwoPanel.add(cards_1);
				break;
				
			case 1:
				this.cards_2.removeAll();

				if (j==this.localPlayer)
				{
					for (int i=0; i<current_cardList.size(); i++)
					{
						JLabel new_card=new JLabel();
						ImageIcon new_card_toadd= new ImageIcon(cardImages[current_cardList.getCard(i).getRank()][current_cardList.getCard(i).getSuit()]);
						new_card.setIcon(new_card_toadd);
				    	new_card.setBounds(200+i*20,158,56,72);
						new_card.addMouseListener(new BigTwoPanel());
						cards_2.add(new_card,Integer.valueOf(i));
					}
				}
				else
				{
					labelp2.setForeground(Color.black);
					for (int i=0; i<current_cardList.size(); i++)
				    {
				    	JLabel new_card=new JLabel();
				    	ImageIcon cardBackImage_icon=new ImageIcon(cardBackImage);
				    	new_card.setIcon(cardBackImage_icon);
				    	new_card.setBounds(200+i*20,158,56,72);
					    cards_2.add(new_card,Integer.valueOf(i));
				    }
				}

			    cards_2.setBounds(0,0,10000,10000);
				bigTwoPanel.add(cards_2);
				break;
			case 2:
				this.cards_3.removeAll();

				if (j==this.localPlayer)
				{
					for (int i=0; i<current_cardList.size(); i++)
					{
						JLabel new_card=new JLabel();
						ImageIcon new_card_toadd= new ImageIcon(cardImages[current_cardList.getCard(i).getRank()][current_cardList.getCard(i).getSuit()]);
						new_card.setIcon(new_card_toadd);
				    	new_card.setBounds(200+i*20,268,56,72);
						new_card.addMouseListener(new BigTwoPanel());
						cards_3.add(new_card,Integer.valueOf(i));
					}
				}
				else
				{
					labelp3.setForeground(Color.black);
					for (int i=0; i<current_cardList.size(); i++)
				    {
				    	JLabel new_card=new JLabel();
				    	ImageIcon cardBackImage_icon=new ImageIcon(cardBackImage);
				    	new_card.setIcon(cardBackImage_icon);
				    	new_card.setBounds(200+i*20,268,56,72);
					    cards_3.add(new_card,Integer.valueOf(i));
				    }
				}
				
			    cards_3.setBounds(0,0,10000,10000);
				bigTwoPanel.add(cards_3);
				break;
			case 3:
				this.cards_4.removeAll();

				if (j==this.localPlayer)
				{
					for (int i=0; i<current_cardList.size(); i++)
					{
						JLabel new_card=new JLabel();
						ImageIcon new_card_toadd= new ImageIcon(cardImages[current_cardList.getCard(i).getRank()][current_cardList.getCard(i).getSuit()]);
						new_card.setIcon(new_card_toadd);
				    	new_card.setBounds(200+i*20,378,56,72);
						new_card.addMouseListener(new BigTwoPanel());
						cards_4.add(new_card,Integer.valueOf(i));
					}
				}
				else
				{
					labelp4.setForeground(Color.black);
					for (int i=0; i<current_cardList.size(); i++)
				    {
				    	JLabel new_card=new JLabel();
				    	ImageIcon cardBackImage_icon=new ImageIcon(cardBackImage);
				    	new_card.setIcon(cardBackImage_icon);
				    	new_card.setBounds(200+i*20,378,56,72);
					    cards_4.add(new_card,Integer.valueOf(i));
				    }
				}
				
			    cards_4.setBounds(0,0,10000,10000);
				bigTwoPanel.add(cards_4);
				break;			
			}
		
		//set player label
		for (int i=0; i<4; i++)
		{
			switch(i)
			{
			case 0:
				if (i==this.activePlayer)
				{
					labelp1.setForeground(Color.YELLOW);
				}
				else
				{
					if (i==this.localPlayer)
					{
						labelp1.setForeground(Color.blue);
					}
					else
					{
						labelp1.setForeground(Color.black);
					}
				}
				break;
			case 1:
				if (i==this.activePlayer)
				{
					labelp2.setForeground(Color.YELLOW);
				}
				else
				{
					if (i==this.localPlayer)
					{
						labelp2.setForeground(Color.blue);
					}
					else
					{
						labelp2.setForeground(Color.black);
					}
				}
				break;
			case 2:
				if (i==this.activePlayer)
				{
					labelp3.setForeground(Color.YELLOW);
				}
				else
				{
					if (i==this.localPlayer)
					{
						labelp3.setForeground(Color.blue);
					}
					else
					{
						labelp3.setForeground(Color.black);
					}
				}
				break;
			case 3:
				if (i==this.activePlayer)
				{
					labelp4.setForeground(Color.YELLOW);
				}
				else
				{
					if (i==this.localPlayer)
					{
						labelp4.setForeground(Color.blue);
					}
					else
					{
						labelp4.setForeground(Color.black);
					}
				}	
				break;
			}
		}
		

		
		if (!game.getHandsOnTable().isEmpty())
		{
			Hand last_hand=game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
			cards_table.removeAll();
			for (int i=0; i<last_hand.size(); i++)
			{
				JLabel new_card5=new JLabel();
				ImageIcon new_card_toadd= new ImageIcon(cardImages[last_hand.getCard(i).getRank()][last_hand.getCard(i).getSuit()]);
		    	new_card5.setIcon(new_card_toadd);
				new_card5.setBounds(200+i*20,38,56,72);
				cards_table.add(new_card5,Integer.valueOf(i));
			}
			String text="Table : (Played by: "+last_hand.getPlayer().getName()+")";
			tabel_label.setText(text);
			table.add(cards_table);			
			table.setBackground(Color.black);
		}
		else
		{
			cards_table.removeAll();
			table.setBackground(new Color(100,70,36));
			tabel_label.setText("Table");
		}
		frame.repaint();
	}
}
	/**
	 * a method to get the number of localPlayer idx
	 * @return int localPlayer
	 * 		index of the localPlayer
	 */
	public int getLocalPlayer()
	{
		return this.localPlayer;
	}
	/**
	 * 
	 *  a method for getting an array of indices of the cards selected
	 *  
	 * @return int[]
	 * 		the array of indices of cards selected
	 * 		
	 */
	public int[] getSelected()
	{
		CardGamePlayer player = this.game.getPlayerList().get(activePlayer);
		this.printMsg("\n"+player.getName() + "'s turn: ");

		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}
		int [] cardIdx=null;
		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
		
	}
	
	/**
	 * a method for resetting the list of selected cards. 
	 * 
	 */
	public void resetSelected()
	{
		if (this.selected!=null)
		{
			for (int i=0; i<selected.length; i++)
			{
				this.selected[i]=false;
			}
		}		
	}
	
	/**
	 * a method for repainting the GUI	
	 */
	public void repaint()
	{
		this.frame.repaint();
	}
	
	/**
	 * a method for printing the specified string to the dm area of the GUI. 
	 * @param String msg
	 * 		string to be printed in the message area
	 */
	public void print_dmMsg(String msg)
	{
		dmArea.append(msg);
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI. 
	 * @param String msg
	 * 		string to be printed in the message area
	 */
	public void printMsg(String msg)
	{
		msgArea.append(msg);
	}
	/**
	 *  a method for clearing the message area of the GUI. 
	 *  
	 */
	public void clearMsgArea()
	{
		msgArea.setText(null);
	}
	
	/**
	 * a method for enabling user interactions with the GUI.
	 */
	public void enable ()
	{
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		ENABLE=true;
	}
	
	/**
	 * a method for disabling user interactions with the GUI.
	 */
	public void disable()
	{
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		ENABLE=false;
	}
	/**
	 * a method for resetting the GUI. 
	 * 
	 */
	public void reset()
	{
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
		
	}
	/**
	 * a method to return if the button pressed 
	 * @return boolean
	 * 		buttonpressed
	 */
	public boolean getButtonPressed()
	{
		return buttonpressed;
	}
	
	/**
	 * a method to set the button u pressed 
	 * 
	 */
	public void unpress_ButtonPressed()
	{
		buttonpressed=false;
	}
	/**
	 *  an inner class that extends the JPanel class and implements the MouseListener interface
	 * @author szehoyin
	 */
	class BigTwoPanel extends JPanel implements MouseListener
	{
		/**
		 * a method from MouseListener interface to handle mouse click events
		 */
		private boolean chosen=false;
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel l=(JLabel) e.getSource();
			if (ENABLE)
			{
				if (!chosen)
				{
					l.setBounds(l.getX(),l.getY()-10, l.getWidth(), l.getHeight());
					chosen=true;
					BigTwoTable.this.selected[(l.getX()-200)/20]=true;
				}				
			}	
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// null
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// null			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			//null
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// null
			
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. 
	 * @author szehoyin
	 *
	 */
	class PlayButtonListener implements ActionListener
	{

		/**
		 * a method to handle button-click events for the "Play" button
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			game.makeMove(activePlayer,BigTwoTable.this.getSelected());
			buttonpressed=true;
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface
	 * @author szehoyin
	 *
	 */
	class PassButtonListener implements ActionListener
	{

		@Override
		/**
		 * a method to handle button-click events for the ¡§Pass¡¨ button
		 * @param ActionEvent a
		 */
		public void actionPerformed(ActionEvent a) {
			// TODO Auto-generated method stub
			int []pass={-1};
			game.makeMove(activePlayer,pass);
			buttonpressed=true;
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface
	 * @author szehoyin
	 *
	 */
	class ConnectMenuItemListener implements ActionListener
	{

		@Override
		/**
		 * a method to handle menu-item-click events for the ¡§Connect¡¨ menu item
		 * @param ActionEvent a
		 */
		public void actionPerformed(ActionEvent a) {
			BigTwoTable.this.game.makeConnection();
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface
	 * @author szehoyin
	 *
	 */
	class QuitMenuItemListener implements ActionListener
	{

		@Override
		/**
		 * method from the ActionListener interface to handle menu-item-click events for the ¡§Quit¡¨ menu item
		 *
		 * @param ActionEvent a
		 */
		public void actionPerformed(ActionEvent a) {
			// TODO Auto-generated method stub
			try{
				BigTwoTable.this.game.getOos().close();
			}
			catch(Exception ex)
			{
				printMsg("Fail to close the oos");
			}
			System.exit(0);
		}
	}
	/**
	 * an inner class that implements the ActionListener
	 * @author szehoyin
	 */
	class ChatItemListener implements ActionListener
	{

		@Override
		/**
		 * a method from ActionListener Interface to handle the event when the JTextArea being inputted 
		 */
		public void actionPerformed(ActionEvent arg0) {
			CardGameMessage message=new CardGameMessage(CardGameMessage.MSG,BigTwoTable.this.activePlayer,inputArea.getText());
			BigTwoTable.this.game.sendMessage(message);
			BigTwoTable.this.inputArea.setText("");
		}
		
	}
}
