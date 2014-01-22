import java.awt.*; // import statements
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;

/**
 * This class takes care of all operations involving the GUI of the application
 * @author Jason Fang
 * @since November 30, 2011
 */
public class GameGUI extends JFrame implements ActionListener
{
	// declare and initialize global variables
	private JFrame picFrame; // frame of the pop-up that lets the user choose a picture
	
	private JButton[] squareButton = new JButton[100];  // makes up the playing grid
	private JButton scrambleButton; 					// scramble the pieces
	private JButton resetButton; 						// reset the position of all the grid buttons
	private JButton optionsButton; 						// switches to the options
	private JButton backButton;							// switches back to the playing screen
	private JButton applyButton; 						// applies changes in the 
	private JButton uploadButton; 						// lets the user upload his own images
	private JButton picButton;							// lets users the preset image of choice
	private JButton chooseButton;						// chooses the preset image of choice
	private JButton instructionButton;					// shows instructions
	
	private JLabel moveLabel;							// shows amount of moves
	private JLabel urlLabel;							// shows the picture name
	private JLabel pictureLabel;						// holds the current image
	public static JLabel timeLabel;						// shows the current time
	
	private JTextField scrambleField;					// user input number of scrambles
	
	private JPanel gamePanel;							// playing game
	private JPanel gridPanel; 							// holds the game buttons
	private JPanel optionPanel;							// option screen
	
	private JCheckBox chooseInteger, choosePics;		// checks with game style the user wants to play with
	private JComboBox chooseRows, chooseColumns;		// choose the amount of rows or columns
	private JComboBox presetChooser;

	private int numMoves;								// holds number of moves
	private int numScrambles = INIT_SCRAMBLES;			// holds default amount of scrambles
	private int rows = 4, columns = 4; 					// default size
	private int numPlayable = 0;						// holds amount of buttons in use
	
	private String url = "World.jpg";					// stores name of picture file
	private String[] presetChoices = {"Choose a picture", "Fish", "World", "Paris", "Winter", "Badminton", "Turtle", "Fiddlesticks"};
	
	private boolean usePics = false;					// stores whether the user is using pictures or not
	
	private Timer timer;								// timer
	private TimeClass timeClass;						// timer class object
	
	private ImageIcon masterImage;						// image used
	private ImageIcon whiteSquare;						// image used as a white square
	private ImageIcon[] buttonIcons = new ImageIcon[100];// images for each button
	
	// constants for movement of the pieces
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int MOVE_LEFT = 3;
	public static final int MOVE_RIGHT = 4;
	
	// constant for the default amount of scrambles
	public static final int INIT_SCRAMBLES = 250;
	
	// constants for differentiating between different types of pop-ups used
	public static final int CHOOSE_PRESET = 0;
	public static final int CREATE_ORIGINAL = 1;
	
	/**
	 * This constructor declares and formats all the objects on my GUI
	 */
	public GameGUI()
	{
		// sets the title of the JFrame
		super("Jason's Slider Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// set the size of JFrame as well as background color
		setSize(600, 700);
		setLayout(null);
		setResizable(false);
		getContentPane().setBackground(Color.BLACK);
		
		// Panel 2
		optionPanel = new JPanel();
		optionPanel.setVisible(false);
		optionPanel.setBounds(this.getBounds());
		optionPanel.setLayout(null);
		optionPanel.setBackground(Color.BLACK);
		add(optionPanel);
		
		// Panel 1
		gamePanel = new JPanel();
		gamePanel.setBounds(this.getBounds());
		gamePanel.setLayout(null);
		gamePanel.setBackground(Color.BLACK);
		add(gamePanel);
		
		// Panel for grid
		gridPanel = new JPanel();
		gridPanel.setBackground(Color.BLACK);
		gridPanel.setLayout(null);
		gridPanel.setBounds(50, 50, 500, 500);
		gamePanel.add(gridPanel);
		
		// Panel 1 Objects
		scrambleButton = new JButton("Scramble!");
		scrambleButton.setBounds(50, 550, 100, 50);
		scrambleButton.addActionListener(this);
		gamePanel.add(scrambleButton);
		
		resetButton = new JButton("Reset!");
		resetButton.setEnabled(false);
		resetButton.setBounds(250, 550, 100, 50);
		resetButton.addActionListener(this);
		gamePanel.add(resetButton);
		
		optionsButton = new JButton("Options");
		optionsButton.setBounds(450, 550, 100, 50);
		optionsButton.addActionListener(this);
		gamePanel.add(optionsButton);
		
		JLabel titleLabel = new JLabel("Slider Game!");
		titleLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 30));
		titleLabel.setBounds(200, 0, 200, 50);
		titleLabel.setForeground(Color.WHITE);
		gamePanel.add(titleLabel);
		
		moveLabel = new JLabel("Number of moves: " + numMoves);
		moveLabel.setVisible(false);
		moveLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
		moveLabel.setBounds(50, 600, 500, 30);
		moveLabel.setForeground(Color.WHITE);
		gamePanel.add(moveLabel);
		
		timeLabel = new JLabel("Time Elapsed: 0s");
		timeLabel.setVisible(false);
		timeLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
		timeLabel.setBounds(50, 630, 500, 30);
		timeLabel.setForeground(Color.WHITE);
		gamePanel.add(timeLabel);
		
		timeClass = new TimeClass(0); // timer objects
		timer = new Timer(10, timeClass);
		
		// Panel 2 Objects
		JLabel optionLabel = new JLabel("Options");
		optionLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 30));
		optionLabel.setBounds(150, 25, 200, 50);
		optionLabel.setForeground(Color.WHITE);
		optionPanel.add(optionLabel);
		
		JLabel rowLabel = new JLabel("Number of rows:");
		rowLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 25));
		rowLabel.setBounds(50, 100, 300, 50);
		rowLabel.setForeground(Color.WHITE);
		optionPanel.add(rowLabel);
		
		JLabel columnLabel = new JLabel("Number of columns:");
		columnLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 25));
		columnLabel.setBounds(50, 150, 300, 50);
		columnLabel.setForeground(Color.WHITE);
		optionPanel.add(columnLabel);
		
		JLabel scrambleLabel = new JLabel("Number of Scrambles");
		scrambleLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 25));
		scrambleLabel.setBounds(50, 200, 300, 50);
		scrambleLabel.setForeground(Color.WHITE);
		optionPanel.add(scrambleLabel);
		
		JLabel gameLabel = new JLabel("Preference:");
		gameLabel.setForeground(Color.WHITE);
		gameLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 25));
		gameLabel.setBounds(50, 250, 200, 50);
		optionPanel.add(gameLabel);
		
		JLabel uploadLabel = new JLabel("Want to use your own picture?");
		uploadLabel.setForeground(Color.WHITE);
		uploadLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 25));
		uploadLabel.setBounds(50, 300, 450, 50);
		optionPanel.add(uploadLabel);
		
		urlLabel = new JLabel("Current Pic: " + url);
		urlLabel.setFont(new Font("Comic Sans Ms", Font.BOLD, 30));
		urlLabel.setForeground(Color.WHITE);
		urlLabel.setBounds(50, 400, 550, 50);
		optionPanel.add(urlLabel);
	
		String[] numChoice = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
		chooseRows = new JComboBox(numChoice);
		chooseRows.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
		chooseRows.setBounds(300, 115, 50, 25);
		chooseRows.setSelectedIndex(2);
		optionPanel.add(chooseRows);
		
		chooseColumns = new JComboBox(numChoice);
		chooseColumns.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
		chooseColumns.setBounds(300, 165, 50, 25);
		chooseColumns.setSelectedIndex(2);
		optionPanel.add(chooseColumns);
		
		scrambleField = new JTextField(INIT_SCRAMBLES + "");
		scrambleField.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
		scrambleField.setBounds(350, 200, 100, 40);
		optionPanel.add(scrambleField);
		
		instructionButton = new JButton("Instructions");
		instructionButton.setBounds(300, 50, 125, 25);
		instructionButton.addActionListener(this);
		optionPanel.add(instructionButton);
		
		backButton = new JButton("Back");
		backButton.setBounds(50, 525, 100, 50);
		backButton.addActionListener(this);
		optionPanel.add(backButton);
		
		applyButton = new JButton("Apply");
		applyButton.setBounds(200, 525, 100, 50);
		applyButton.addActionListener(this);
		optionPanel.add(applyButton);
		
		uploadButton = new JButton("Upload Picture");
		uploadButton.setFont(new Font("Comic Sans Ms", Font.BOLD, 30));
		uploadButton.setBounds(50, 350, 300, 50);
		uploadButton.addActionListener(this);
		optionPanel.add(uploadButton);
		
		picButton = new JButton("Choose From Preset Images!");
		picButton.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
		picButton.setBounds(50, 450, 400, 50);
		picButton.addActionListener(this);
		optionPanel.add(picButton);
		
		chooseInteger = new JCheckBox("Numbers", true);
		chooseInteger.setOpaque(false);
		chooseInteger.setForeground(Color.WHITE);
		chooseInteger.setFont(new Font("Comic Sans Ms", Font.BOLD, 15));
		chooseInteger.setBounds(200, 250, 100, 50);
		optionPanel.add(chooseInteger);
		
		choosePics = new JCheckBox("Pictures", false);
		choosePics.setOpaque(false);
		choosePics.setForeground(Color.WHITE);
		choosePics.setFont(new Font("Comic Sans Ms", Font.BOLD, 15));
		choosePics.setBounds(300, 250, 100, 50);
		optionPanel.add(choosePics);
		
		ButtonGroup chooseGameStyle = new ButtonGroup(); // groups the checkboxes together
		chooseGameStyle.add(chooseInteger);
		chooseGameStyle.add(choosePics);
		
		// Images needed: Main Image + space image
		masterImage = new ImageIcon("World.jpg");
		whiteSquare = new ImageIcon("whitesquare.jpg");
		
		// create grid buttons
		for(int i = 0; i < squareButton.length; i++)
		{
			squareButton[i] = new JButton();
			squareButton[i].setBackground(Color.GREEN);
			squareButton[i].setActionCommand(i + "");
			squareButton[i].setForeground(Color.BLUE);
			squareButton[i].addActionListener(this);
			gridPanel.add(squareButton[i]);
		}
		
		// formats the grid 
		constructGrid(rows, columns);
		
		setVisible(true);
		repaint();
	} // end constructor method
	
	/**
	 * This method constructs the grid of buttons on the GUI. 
	 * The method will either draw buttons with integers or 
	 * buttons with images depending on the user's input.
	 * 
	 * NOTE: The number of rows or columns cannot exceed 10, 
	 * as there are only 100 JButtons created
	 * 
	 * @param newRows: amount of rows inputed
	 * @param newColumns: amount of columns inputed
	 */
	private void constructGrid(int newRows, int newColumns)
	{
		int xPos = 0;
		int yPos = 0;
		rows = newRows; 
		columns = newColumns;
		numPlayable = newColumns * newRows; // number of buttons in use
		
		Image tempImage; // temporary image on the button
		
		// makes sure that all the buttons are wiped clean
		for(int j = 0; j < numPlayable; j++)
		{
			squareButton[j].setText("");
			squareButton[j].setIcon(null);
		}
		
		// formats all the buttons
		for(int i = 0; i < numPlayable; i++)
		{
			squareButton[i].setEnabled(false);
			squareButton[i].setFont(new Font("Comic Sans Ms", Font.BOLD, ((10-newColumns)*5)+10));
			
			if(i % newColumns == 0 && i != 0) // moving to a new row
			{
				xPos = 0;
				yPos += 500/newRows;
			}
			else{} // not moving to a new row yet
			
			squareButton[i].setBounds(xPos, yPos, 500/newColumns, 500/newRows);
			
			if(i != (newRows*newColumns)-1) // formatting all the buttons 
											// except the last
			{
				if(usePics)
				{
					// cropping out only a portion of the full image 
					// to put on the button
					tempImage = masterImage.getImage();
					tempImage = 
						createImage(new FilteredImageSource(tempImage.getSource(),
					    new CropImageFilter(xPos, yPos, (500/newColumns), (500/newRows)+1)));
					buttonIcons[i] = new ImageIcon(tempImage);
					squareButton[i].setIcon(buttonIcons[i]);
				}
				else // the user chooses to use integers instead
					squareButton[i].setText(i + 1 + "");
				squareButton[i].setBackground(Color.GREEN);
			}
			else // formatting the last button
			{
				squareButton[i].setText("");
				if(usePics)
				{
					// creates a white square image
					tempImage = whiteSquare.getImage(); 
					tempImage = createImage
							(new FilteredImageSource(tempImage.getSource(),
					        new CropImageFilter(0, 0, 500/newColumns, 500/newRows)));
					buttonIcons[i] = new ImageIcon(tempImage);
					squareButton[i].setIcon(buttonIcons[i]);
				}
				else // the user chooses to play with numbers
				{
					squareButton[i].setBackground(Color.WHITE);
				}
			}	
			xPos += 500/newColumns; // moves over 1 column
		} // end for loop
	} // end constructGrid method
	
	/**
	 * This method is called whenever an action is performed in the GUI.
	 * For example, a button may be pressed, etc.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == scrambleButton)
		{
			scramble(); // scrambles the grid
			timeClass.setCounter(0);
			timer.start();
			scrambleButton.setEnabled(false);
			optionsButton.setEnabled(false);
			resetButton.setEnabled(true);
			moveLabel.setVisible(true);
			timeLabel.setVisible(true);
		}
		else if(e.getSource() == resetButton)
		{
			// reset the grid back to normal
			constructGrid(rows, columns);
			scrambleButton.setEnabled(true);
			optionsButton.setEnabled(true);
			resetButton.setEnabled(false);
			moveLabel.setVisible(false);
			numMoves = 0;
			timer.stop();
			timeLabel.setVisible(false);
			timeClass.setCounter(0);
			moveLabel.setText("Number of moves: " + numMoves);
		}
		else if(e.getSource() == optionsButton)
		{
			// switches to the options Panel (2)
			setSize(500, 625);
			optionPanel.setVisible(true);
			gamePanel.setVisible(false);
		}
		else if(e.getSource() == backButton)
		{
			// switches to the game Panel (1)
			setSize(600, 700);
			optionPanel.setVisible(false);
			gamePanel.setVisible(true);
			
			if(usePics)
			{
				createPopUp(CREATE_ORIGINAL);
			}
		}
		else if(e.getSource() == applyButton)
		{
			try
			{
				// applies the changes made to the objects in the options panel
				numScrambles = Integer.parseInt(scrambleField.getText());
				if(numScrambles > 0 && numScrambles < 500)
				{
					if(choosePics.isSelected())
						usePics = true;
					else if(chooseInteger.isSelected())
						usePics = false;
					else // none of the checkboxes are checked
					{
						System.out.println("ERROR: Unhandled Case");
					}
					constructGrid(chooseRows.getSelectedIndex()+2, chooseColumns.getSelectedIndex()+2);
					JOptionPane.showMessageDialog(this, "Changes Successfully Applied!");
					urlLabel.setText("Current Pic: " + url);
					scrambleButton.setEnabled(true);
					resetButton.setEnabled(false);
				}
				else // numScrables is negative or is bigger than 500
					JOptionPane.showMessageDialog(this, "ERROR: Incorrect Input");
			}
			catch(NumberFormatException er) // the user does not input 
											// an integer in the text field
			{
				JOptionPane.showMessageDialog(this, "ERROR: Incorrect Input");
			}
		}
		else if(e.getSource() == uploadButton)
		{
			// lets the user choose his/her own image 
			// to use as the game background
			// converts from file to image to imageicon
			File tempFile = chooseImage();
			if(tempFile != null)
			{
				url = tempFile.getPath();
				url = url.substring(url.lastIndexOf("\\")+1, url.length()); // gets the name of the file out of the path
				Image tempImage = Toolkit.getDefaultToolkit().getImage(tempFile.getPath()); // converts the file to an image
				Image newImage = tempImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH); // scales the image
				masterImage = new ImageIcon(newImage);
			}
			else // the image has a "null" value
				System.out.println("ERROR: The image is NULL!");
		}
		else if(e.getSource() == picButton)
		{
			createPopUp(CHOOSE_PRESET); // create a popup for choosing a preset image
		}
		else if(e.getSource() == chooseButton)
		{
			int choice = presetChooser.getSelectedIndex(); // gets the currently selected image
			if(choice != 0) // the choice is not the default choice
			{
				url = presetChoices[choice]; // sets the file name and image
				masterImage = new ImageIcon(Toolkit.getDefaultToolkit().getImage(url + ".jpg"));
				picFrame.setVisible(false); // closes the pop up
			}
			else // the choice is the default choice
				System.out.println("ERROR: Invalid Choice");
			
		}
		else if(e.getSource() == presetChooser)
		{
			int choice = presetChooser.getSelectedIndex(); // shows a preview image
			pictureLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(presetChoices[choice] + ".jpg")));
		}
		else if(e.getSource() == instructionButton)
		{
			JOptionPane.showMessageDialog(this, "Rules of the Slider Game:" +
												"\n1) Players slide scrambled blocks around to either organize the blocks" +
												"\nfrom smallest to biggest or to create a full picture." +
												"\n2) Players can only slide blocks adjacent to the empty block" +
												"\n3) Try to complete the puzzle as efficiently and as fast as" +
												"\npossible!" +
												"\n" +
												"\nThis slider games supports 2 to 10 columns/rows of play." +
												"\n" +
												"\nNOTE: Remember to ALWAYS click apply changes whenever you change" +
												"\nsomeonething in the options screen. Otherwise, the change will not" +
												"\nregister." +
												"\nNOTE: The maximum amount of scrambles allowed is 500, any more scrambles" +
												"\nwill be redundant." +
												"\n" +
												"\nHave Fun!");
		}
		else // a piece on the game board has been pressed
		{
			movePiece(squareButton[Integer.parseInt(e.getActionCommand())]); // moves the piece selected
			if(squareButton[numPlayable-1].getText().equals(""))
			{
				if(gameDone())
				{
					 // resets the board and gives a congratulations message
					timer.stop(); // stops the timer
					JOptionPane.showMessageDialog(this, "You've completed the puzzle in " + timeClass.getCounter() + "s");
					resetButton.setEnabled(false); // resets all the objects
					scrambleButton.setEnabled(true);
					optionsButton.setEnabled(true);
					for(int i = 0; i < numPlayable; i++)
						squareButton[i].setEnabled(false);
					moveLabel.setVisible(false);
					timeLabel.setVisible(false);
					timeClass.setCounter(0);
					numMoves = 0;
					moveLabel.setText("Number of moves: " + numMoves);
				}
				else{} // the game is not done yet
			}
			else{} // the empty space is not in the last spot on the board
		}
		repaint(); // repaints the GUI
	} // end actionPerformed method
	
	/**
	 * This method creates a pop-up frame that assists with the gameplay.
	 * @param popUpNum: The kind of pop-up being produced
	 */
	private void createPopUp(int popUpNum)
	{
		if(popUpNum == CHOOSE_PRESET) // or '0'
		{
			// creates the objects
			picFrame = new JFrame("Preset Images!");
			picFrame.setSize(500, 600);
			picFrame.setResizable(false);
			picFrame.setLayout(null);
			picFrame.setVisible(true);
			
			JPanel containerPanel = new JPanel();
			containerPanel.setBounds(picFrame.getBounds());
			containerPanel.setBackground(Color.BLACK);
			containerPanel.setLayout(null);
			picFrame.add(containerPanel);
			
			pictureLabel = new JLabel();
			pictureLabel.setBounds(0, 100, 500, 500);
			containerPanel.add(pictureLabel);
			
			chooseButton = new JButton("Choose Picture");
			chooseButton.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
			chooseButton.setBounds(275, 25, 200, 30);
			chooseButton.addActionListener(this);
			containerPanel.add(chooseButton);
			
			presetChooser = new JComboBox(presetChoices);
			presetChooser.setFont(new Font("Comic Sans Ms", Font.BOLD, 20));
			presetChooser.setBounds(50, 25, 200, 30);
			presetChooser.addActionListener(this);
			presetChooser.setSelectedIndex(0);
			containerPanel.add(presetChooser);
		}
		else if(popUpNum == CREATE_ORIGINAL) // or '1'
		{
			// create the objects
			JFrame originalFrame = new JFrame("Original Picture");
			originalFrame.setResizable(false);
			originalFrame.setSize(500, 500);
			originalFrame.setLayout(null);
			originalFrame.setVisible(true);
			
			JLabel orgPictureLabel = new JLabel();
			orgPictureLabel.setBounds(originalFrame.getBounds());
			orgPictureLabel.setIcon(masterImage);
			originalFrame.add(orgPictureLabel);
		}
		else // an invalid pop-up kind was encountered
			System.out.println("ERROR: Invalid Popup!");
	} // end createPopUp method
	
	/**
	 * This method lets the user choose an image that he/she wants to use
	 * as the background for the game 
	 * @return File imageFile: The file that the user has chosen
	 */
	private File chooseImage()
	{
		File imageFile = null;
		JFileChooser chooseImage = new JFileChooser();
		
		// filters the choice to only image file types
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Picture Files", "gif", "png", "jpg", "jpeg");
		chooseImage.setFileFilter(filter);
		
		int returnVal = chooseImage.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			imageFile = chooseImage.getSelectedFile();
		}
		else if(returnVal == JFileChooser.CANCEL_OPTION)
		{
			imageFile = null;
		}
		else // the user did not choose either open or cancel
			System.out.println("ERROR: Option not accounted for");
		return imageFile;
	} // end chooseImage method
	
	/**
	 * This method moves the piece chosen with the empty piece on the board
	 * @param pieceChosen: the piece that the user has chosen to switch
	 */
	private void movePiece(JButton pieceChosen)
	{
		// gets the user's current button position
		int buttonNum = Integer.parseInt(pieceChosen.getActionCommand());
		System.out.println("Button Element: " + buttonNum);
		
		// gets the element of the empty piece
		int emptyNum = getEmptyButton(squareButton);
		System.out.println("Empty Element: " + emptyNum);
		
		// makes sure they are on top of each other
		// makes sure they are not not beside each other
		// on opposite sides
		// makes sure the buttons are beside each other
		if(Math.abs(buttonNum - emptyNum) == columns		
				|| (Math.max(buttonNum, emptyNum) % columns !=0 
				&& Math.abs(buttonNum - emptyNum) == 1))	
		{
			switchPiece(pieceChosen, squareButton[emptyNum]);
			numMoves++;
			moveLabel.setText("Number of moves: " + numMoves);
		}
		else{} // the piece chosen is not beside the empty piece (Invalid Move)
			   // nothing will happen
	} // end movePiece method
	
	/**
	 * This method gets the element of the empty pieceon the game board
	 * @param button: the array of buttons given
	 * @return the element of the empty piece in that array
	 */
	private int getEmptyButton(JButton[] button)
	{
		int num = 0;
		for(int i = 0; i < numPlayable; i++) // tests all the buttons
		{
			if(usePics)
			{
				if(button[i].getIcon().toString().equals(buttonIcons[numPlayable-1].toString()))
					num = i;
				else{} // the button icon is not a white space
			}
			else // the user chose a board of integers
			{
				if(button[i].getText().equals(""))
					num = i;
				else{} // the button is not blank
			}
		}
		return num;
	} // end getEmptyButton method
	
	/**
	 * This method switches the properties of the two buttons: color, text, and icon
	 * @param button1: the 1st button to be switched
	 * @param button2: the 2nd button to be switched
	 */
	private void switchPiece(JButton button1, JButton button2)
	{
		// switch color
		Color orgColor = button1.getBackground();
		button1.setBackground(button2.getBackground());
		button2.setBackground(orgColor);
		
		// switch text
		String orgText = button1.getText();
		button1.setText(button2.getText());
		button2.setText(orgText);
		
		// switch icon
		Icon orgIcon = button1.getIcon();
		button1.setIcon(button2.getIcon());
		button2.setIcon(orgIcon);
		
		repaint();
	} // end switchPiece method
	
	/**
	 * This method scrambles the pieces by making the empty space make random
	 * legal moves around the game board
	 */
	private void scramble()
	{
		for(int i = 0; i < numScrambles; i++) // scrambles a set amount of times
		{
			// gets a random int for a move
			int moveChoice = (int)((Math.random()*4)+1); 
			// gets the element of the empty Button
			int emptyNum = getEmptyButton(squareButton); 
			if(moveChoice == MOVE_UP) // the choice was 1
			{
				System.out.println("Move Up");
				if(emptyNum - columns >= 0)
					switchPiece(squareButton[emptyNum], squareButton[emptyNum - columns]);
				else // the move went above the board bounds
					i--; // decreases that specific time
			}
			else if(moveChoice == MOVE_DOWN) // the choice was 2
			{
				System.out.println("Move Down");
				if(emptyNum + columns <= numPlayable-1)
					switchPiece(squareButton[emptyNum], squareButton[emptyNum + columns]);
				else // the move went below the board bounds
					i--; // decreases that specific time
			}
			else if(moveChoice == MOVE_LEFT) // the choice was 3
			{
				System.out.println("Move Left");
				if(emptyNum - 1 >= 0 && emptyNum % columns !=0)
					switchPiece(squareButton[emptyNum], squareButton[emptyNum - 1]);
				else // the move went too far left out of the board bounds
					i--; // decreases that specific time
			}
			else if(moveChoice == MOVE_RIGHT) // the choice was 4
			{
				System.out.println("Move Right");
				if(emptyNum + 1 <= numPlayable && (emptyNum + 1) % columns != 0)
					switchPiece(squareButton[emptyNum], squareButton[emptyNum + 1]);
				else // the move went too far right out of the board bounds
					i--; // decreases that specific time
			}
			else // move choice was not valid
				System.out.println("ERROR: Invalid move choice encountered");
		}
		
		if(gameDone()) // the scrambled board is not scrambled
			scramble();
		else // the scrambled board is scrambled
			System.out.println("Board is READY to PLAY!");
		for(int i = 0; i < numPlayable; i++) // lets the user play after scrambling
			squareButton[i].setEnabled(true);
	} // end scramble method
	
	/**
	 * This method checks if the puzzle is solved or not by checking through
	 * every single button and comparing it to the finished result
	 * @return whether the game is done or not
	 */
	private boolean gameDone()
	{
		// checks through all the buttons except the last
		// since if all the buttons are right except the last,
		// the last one must be right too.
		for(int i = 0; i < numPlayable-1; i++) 
		{
			if(usePics)
			{
				if(!squareButton[i].getIcon().toString().equals(buttonIcons[i].toString()))
					return false;
				else // the button is in the right spot based on it's icon
					System.out.println("Element " + i + " is in the RIGHT spot");
			}
			else // the user chose to play with numbers
			{
				if(!squareButton[i].getText().equalsIgnoreCase(i + 1 + ""))
					return false;
				else// the button is in the right spot based on it's text
					System.out.println("Element " + i + " is in the RIGHT spot");
			}
		}
		return true; // if the for loop was run successfully without finding one 
					 // button that was out of place
	} // end gameDone method
} // end GameGUI class
