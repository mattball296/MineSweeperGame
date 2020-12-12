import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.BorderFactory.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;
import javax.swing.text.html.StyleSheet;
import javax.swing.Timer;
import java.util.Collections;

/*

	A version of the classic game minesweeper. 
	
	Classes are used to represent menus,  buttons, the board,  the board squares,  options combo boxes and more - mainly to save time setting fonts/texts
	All the Texts needed for buttons and labels are stored in String arrays so button creation is easy by looping over the array once rather than inputting each text when the button is created.
	A MineSweeper is uniquely defined by the number of rows,  columns and mines on the board.
	
	There are 9 Menus:
	 - Main Menu
	 - Game Over 
	 - Game Won
	 - Game Modes 
	       - Create New Gane Mode
		   - Delete Game Mode
	 - Options
	 - Pause 
	 - Stats

*/

public class MineSweeperGame{
	
	public final static int[] hor = {1, 0};
	public final static int[] ver = {0, 1};
	public final static int Flag = 0; // represents the locations of the characters/colours representing flag,  mine,  and number in the arrays
	public final static int Mine = 1;
	public final static int Number = 2;
	
	public final static int MainInt = 0; // an int for each menu involved in the game
	public final static int GameOverInt = 1;
	public final static int GameWonInt = 2;
	public final static int GameModeInt = 3;
	public final static int NewGameModeInt = 4;
	public final static int DeleteGameModeInt = 5;
	public final static int OptionsInt = 6;
	public final static int PauseInt = 7;
	public final static int StatsInt = 8;

	public Color SelectedButtonColor = Color.BLUE; // if a button is selcted,  the text turns blue
	
	public GameMode CurrentGameMode; // the most recently used GameMode
	
	public Menu Background; // the background (an instance of the menu class but not included as a menu)
	public JTextField[][] BackGroundSquares;
	
	public Font NumberFont; // font used for the game
	
	public Font MainMenuFont = new Font("Spaced", Font.BOLD, 20); // the various fonts used across the menus
	public Font GameModeButtonsFont = new Font("Spaced", Font.BOLD, 18);
	public Font SliderLabelFont = new Font("Spaced", Font.PLAIN, 14);
	public Font GameModeLowerFont = new Font("Spaced", Font.BOLD, 20);
	public Font GameModeUpperFont = new Font("Spaced", Font.PLAIN, 16);
	public Font NewGameModeTextFont = new Font("Spaced", Font.PLAIN, 18);
	public Font DeleteGameModeTitleFont = new Font("Spaced", Font.BOLD, 22);
	public Font OptionsComboFont = new Font("Spaced", Font.BOLD, 18);
	public Font GameWonTimesFont = new Font("Spaced", Font.BOLD, 18);
	public Font GameOverTimeFont = new Font("Spaced", Font.BOLD, 20);
	public Font PauseFont = new Font("Spaced", Font.BOLD, 18);
	public Font LocalStatsFont = new Font("Spaced", Font.PLAIN, 18);
	
	public String[] TitlesArray = new String[]{"MENU", "GAME OVER", "YOU WIN!", "", "New Game Mode", "", "", "Game Paused", ""}; // titles for each menu
	public int[] TitleFontSizesArray = new int[]{25, 24, 24, -1, 20, 22, -1, 24, -1}; // font sizes for the titles. -1 indicates no text
	
	public ArrayList<GameMode> GameModesArray = new ArrayList<GameMode>(); // dynamic array containing all game modes
	public ArrayList<String> OptionsArray = new ArrayList<String>(); // contains the user defined options
	public ArrayList<Color> ColorArray = new ArrayList<Color>(); // contains the colours for in game texts
	
	public int LOCATION_X = 50; // location of the game on screen
	public int LOCATION_Y = 50; 
	
	public int MAX_X = 1200; // maximum size of the window containing the game
	public int MAX_Y = 900;
	
	public MenuButton CustomButton; // button to create a custom GameMode
	public ArrayList<MenuButton> GameModeButtons = new ArrayList<MenuButton>(); // buttons for each GameMode
	public boolean SlidersActive = false; // whether sliders can be used
	public GameModeSlider[] GameModeSliders; // sliders to create a new GameMode
	public ArrayList<JLabel> GameModeSliderLabels = new ArrayList<JLabel>(); // labels for the sliders
	public Hashtable<Integer, JLabel> MineSliderLabel; // labels for the slider changing the number of mines
	
	public String[] GameModeUpperButtonTexts = {"New", "Delete"}; // Buttons in the upper part of the GameMode menu
	public String CurrentSelectedGameModeText; // Name of currently selected Game mode
	
	public String[] NewGameModeLabelTexts = {"Name:", "Number of Rows:", "Number of Columns:", "Number of Mines:"}; // Labels for the New Game Mode menu
	public ArrayList<JLabel> NewGameModeLabels = new ArrayList<JLabel>();
	public ArrayList<JTextField> NewGameModeTextFields = new ArrayList<JTextField>();
	
	public String[] NewGameModeBannedWords = {"New", "SAVE", "BACK", "Delete", "START"}; // Words which you cannot call a Game Mode otherwise there are bugs (I found out the hard way)
	
	public String[] FlagOptions = {"?", "O", "!", "F", "#"}; // Potential characters to represent flags. Default is '?'
	public String[] MineOptions = {"X", "!", "M", "O", "[O]"}; // Choices for characters to represent a Mine. Default is 'X' 
	public String[] ColorOptions = {"Red", "Blue", "Black", "Green", "Lime", "Yellow", "Maroon", "Gray", "Purple", "Fuchsia"}; // Potential colour optiond
	public String[] OptionsComboLabels = {"Flag Icon:", "Mine Icon:", "Flag Colour:", "Mine Colour:", "Number Colour:"}; // Label texts for Options Menu
	public String[][] OptionsArrays = {FlagOptions, MineOptions, ColorOptions, ColorOptions, ColorOptions}; // These arrays in a larger array
	public int[] OptionsSizeArray = new int[]{55, 55, 100, 100, 100}; 
	
	public GameBoard CurrentGameBoard; // Game Board currently in use
	public GameBoardSquare[][] GameBoardSquares; // The squares of the current board
	public int GameBoardSquareSize = 55;
	
	public String[] LocalStatsLabelTexts = {"Games played", "Wins", "Losses", "Win %"}; // Label texts for stats menu
	
	public String[] MainMenuButtonTexts = new String[]{"New Game", "Game Mode", "Options", "Statistics", "Exit"}; // labels for buttons on each menu
	public String[] GameWonButtonTexts = {"MENU", "RESTART"};
	public String[] GameOverButtonTexts = {"MENU", "RESTART"};
	public String[] GameModeLowerButtonTexts = {"BACK", "START"};
	public String[] NewGameModeLowerButtonTexts = {"BACK", "SAVE"};
	public String[] DeleteGameModeButtonTexts = {"CANCEL", "YES"};
	public String[] PauseButtonsTexts = {"QUIT", "RESUME"};
	public String[] StatsButtonTexts = {"BACK"};
	public String[] OptionsLowerButtonTexts = {"SAVE"};
	
	public String[][] ButtonStringsArray = new String[][]{MainMenuButtonTexts, GameOverButtonTexts, GameWonButtonTexts, GameModeLowerButtonTexts, NewGameModeLowerButtonTexts, DeleteGameModeButtonTexts, OptionsLowerButtonTexts, PauseButtonsTexts, StatsButtonTexts};
	public int[] ButtonFontSizesArray = new int[]{20, 20, 22, 20, 18, 18, 20, 20, 20};
	public int[][] ButtonOrientationArray = new int[][]{ver, hor, hor, hor, hor, hor, hor, hor, hor}; // whether buttons go horizonally across or vertically up the menu
	public String[] ButtonLocationsArray = new String[]{BorderLayout.CENTER, BorderLayout.PAGE_END, BorderLayout.PAGE_END, // locations of buttons in the BorderLayout of each menu
														BorderLayout.PAGE_END, BorderLayout.PAGE_END, BorderLayout.PAGE_END, 
														BorderLayout.PAGE_END, BorderLayout.PAGE_END, BorderLayout.PAGE_END, 
														BorderLayout.PAGE_END, BorderLayout.PAGE_END, BorderLayout.PAGE_END};

	public Menu MainMenu = new Menu(200, 245, MainInt, 30); // All the Menus are created
	public Menu GameOverMenu = new Menu(300, 150, GameOverInt, 0);
	public Menu GameWonMenu = new Menu(300, 200, GameWonInt, 0);
	public Menu GameModeMenu = new Menu(410, 0, GameModeInt, 45);
	public Menu NewGameModeMenu = new Menu(300, 200, NewGameModeInt, 15);
	public Menu DeleteGameModeMenu = new Menu(250, 100, DeleteGameModeInt, 0);
	public Menu OptionsMenu = new Menu(300, 240, OptionsInt, 20);
	public Menu PauseMenu = new Menu(300, 200, PauseInt, 0);
	public Menu StatsMenu = new Menu(300, 300, StatsInt, 40);
	
	public Menu[] Menus = {MainMenu, GameModeMenu, NewGameModeMenu, DeleteGameModeMenu, OptionsMenu, PauseMenu, StatsMenu};
	public Menu[] InGameMenus = {GameOverMenu, GameWonMenu, PauseMenu};
	
	public int[] WinsLosses = new int[2]; // records wins vs losses for all time stats
	
	public MineSweeperGame(){	
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  // sets look and feel
		} 
		catch (Exception e) { 
			System.err.println(e.getMessage()); 
		}	
		readGameModes(); // reads in stored Game modes to GameMode array 
		readOptions(); // reads in options
		readStats(); // reads in stats
		setUpMenus(); //sets up menus
		setUpBackground(true, true); // sets up the background with text and decorations
		Background.setVisible(true); // background and main menu are initally all that is visible on launch of game.
		Menus[0].setVisible(true);
	}
	
	/*
	
	Function to create the background behind the menus - treated as an instance of Menu which extends JFrame
	The design of the background is a 9x9 game board with a title over the squares and some numbers/flags revealed
	
	@param  : boolean withText - does the background have a title. boolean withDecor - does the background have any decorations (some numbers/flags/mines on lower squares
	@return : none

	*/
	
	public void setUpBackground(boolean withText,  boolean withDecor){
		Background = new Menu("MineSweeper", 500, 500, "Background"); // Menu extends JFrame
		Background.setLocation(LOCATION_X, LOCATION_Y);
		NumberFont = new Font("Spaced", Font.BOLD, 20);
		Background.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel grid = new JPanel(new GridLayout(9, 9, 0, 0)); // sets up the grid for the game board
		BackGroundSquares = new JTextField[9][9];
		for(int R=0; R<9; R++){
			for(int C=0; C<9; C++){	
				JTextField J = new JTextField(3);
				J.setBorder(BorderFactory.createLoweredBevelBorder());
				J.setHorizontalAlignment(SwingConstants.CENTER);
				J.setFont(NumberFont);
				J.setEditable(false);
				BackGroundSquares[R][C] = J;
				grid.add(J);
			}
		}
		if(withDecor){ // adds some decoration
			BackGroundSquares[8][0].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[8][1].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[7][0].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[7][1].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[6][1].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[7][2].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[6][0].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[5][0].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[8][8].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[8][7].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[7][8].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[7][7].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[7][6].setBorder(new EmptyBorder(0, 0, 0, 0));
			BackGroundSquares[6][8].setBorder(new EmptyBorder(0, 0, 0, 0));
			
			BackGroundSquares[8][2].setText(OptionsArray.get(0));
			BackGroundSquares[8][1].setText("1");
			BackGroundSquares[7][0].setText("");
			BackGroundSquares[6][1].setText("1");
			BackGroundSquares[7][2].setText("3");
			BackGroundSquares[6][0].setText("2");
			BackGroundSquares[5][0].setText(OptionsArray.get(0));
			BackGroundSquares[8][7].setText("1");
			BackGroundSquares[6][8].setText("2");
			BackGroundSquares[6][7].setText(OptionsArray.get(0));
			BackGroundSquares[7][6].setText("4");
			BackGroundSquares[5][8].setText(OptionsArray.get(1));
			BackGroundSquares[8][4].setText(OptionsArray.get(1));
			
			BackGroundSquares[8][2].setForeground(ColorArray.get(0));
			BackGroundSquares[5][0].setForeground(ColorArray.get(0));
			BackGroundSquares[8][1].setForeground(ColorArray.get(2));
			BackGroundSquares[7][1].setForeground(ColorArray.get(2));
			BackGroundSquares[6][1].setForeground(ColorArray.get(2));
			BackGroundSquares[7][2].setForeground(ColorArray.get(2));
			BackGroundSquares[6][0].setForeground(ColorArray.get(2));
			BackGroundSquares[8][7].setForeground(ColorArray.get(2));
			BackGroundSquares[7][8].setForeground(ColorArray.get(2));
			BackGroundSquares[6][7].setForeground(ColorArray.get(0));
			BackGroundSquares[7][6].setForeground(ColorArray.get(2));
			BackGroundSquares[5][8].setForeground(ColorArray.get(1));
			BackGroundSquares[8][4].setForeground(ColorArray.get(1));
			BackGroundSquares[6][8].setForeground(ColorArray.get(2));
		}
		if(withText){
			setBackgroundText("mine", "  sweeper");
		}
	
		Background.addComponentListener(new ComponentAdapter(){ // If the background is moved then the locations of the game are updated so menus move with it
			public void componentMoved(ComponentEvent e){
				LOCATION_X = Background.getX();
				LOCATION_Y = Background.getY();
				Background.X = Background.getWidth();
				Background.Y = Background.getHeight();
				keepWithBackGround(); // sets position off all menus to relfect the new background position
			}
		});
		
		Background.addWindowListener(new WindowAdapter(){ // if the game is closed, game modes, stats and options are saved on a local file
			public void windowClosing(WindowEvent e){
				writeGameModes();
				writeOptions();
				writeStats();
			}
		});
		
		Background.MainPanel.add(grid, BorderLayout.CENTER);
		Background.setUpMenu(); // sets up the menu
	}
	
	/*
	
	Function to set the title of the background
	
	@param  : String s to represent the word on top, String t to represent the word underneath 
	@return : none
	
	*/
	
	public void setBackgroundText(String s,  String t){
		if(s.length() > 9 || t.length() > 9){ // words will not fit
			return; 
		}
		for(int i=0; i<s.length(); i++){
			BackGroundSquares[0][i].setForeground(ColorArray.get(2));
			BackGroundSquares[0][i].setText(""+s.charAt(i)); // adds the text to the top row
		}
		for(int i=0; i<t.length(); i++){
			BackGroundSquares[1][i].setForeground(ColorArray.get(1));
			BackGroundSquares[1][i].setText(""+t.charAt(i)); // adds text to second row
		}
	}
	
	/*
	
	Function to set menu positions to the background position
	
	@param  : none
	@return : none
	
	*/	

	public void keepWithBackGround(){
		for(Menu M : Menus){
			try{
				M.setCentralLocation(Background); // sets location to the background
				
			}
			catch(NullPointerException npe){ // menu not set up 
			}
		}
		for(Menu M : InGameMenus){
			try{
				M.setCentralLocation(CurrentGameBoard); // if in game menu, there is no background so the current game board is used
			}
			catch(NullPointerException npe){ // menu not set up 
			}
		}
	}
	
	/*
	
	Function to set up the menus - just calls the create function for every menu 
	
	@param  : none
	@return : none
	
	*/		
	public void setUpMenus(){
		for(Menu M : Menus){
			M.create();
		}
		for(Menu M : InGameMenus){
			M.create();
		}
	}
	
	/*
	
	Function to update the menu arrays after each menu is created/changed
	
	@param  : none
	@return : none
	
	*/		
	public void updateMenusArray(){
		Menus = new Menu[]{MainMenu, GameModeMenu, NewGameModeMenu, DeleteGameModeMenu, OptionsMenu, StatsMenu};
		InGameMenus = new Menu[]{GameOverMenu, GameWonMenu, PauseMenu}; 
	}
	
	/*
	
	Function to start the game - creates a new game board, removes menus and background and then makes this game board visible
	
	@param  : none
	@return : none
	
	*/		
	public void startGame(){
		Background.setVisible(false);
		CurrentGameBoard = new GameBoard(CurrentGameMode);
		keepWithBackGround();
		CurrentGameBoard.setVisible(true);
	}
	
	/*
	
	Function to read in game modes from the local file "GameModes.txt"
	The game modes are stored as a string followed by 8 integers in this order:
		- name
		- rows of the game board
		- columns of the game board
		- mines in the game board
		- the best 3 times in seconds
		- number of wins in all plays
		- number of losses in all plays
	The first game Mode represents the currently selected one, and will appear again in the list
	
	@param  : none
	@return : none
	
	*/		
	public void readGameModes(){
		try{
			Scanner scanner = new Scanner(new BufferedReader(new FileReader("GameModes.txt")));
			boolean isFirst = true;
			GameModesArray.clear();
			while(scanner.hasNext()){
				String name = scanner.next(); // read in all values
				int rows = scanner.nextInt(); 
				int cols = scanner.nextInt();
    			int mines = scanner.nextInt();
    			int time1 = scanner.nextInt();
    			int time2 = scanner.nextInt();
    			int time3 = scanner.nextInt();
				int w = scanner.nextInt();
				int l = scanner.nextInt();
				GameMode G = new GameMode(rows, cols, mines, name); // creates GameMode
				G.BestTimes = new int[]{time1, time2, time3}; // sets best times 
				G.GameModeWinsLosses = new int[]{w, l}; // sets GameMode stats
				if(isFirst){ // if its the first one
					CurrentGameMode = G;
					CurrentSelectedGameModeText = G.Name;
					TitlesArray[DeleteGameModeInt] = "Delete \""+G.Name+"\"?"; // when a GameMode is deleted it is the currently selected one
				}
				if(!isFirst){
					GameModesArray.add(G); // the first one will not be added as it represents the currently selcted one
				}
				isFirst = false; // no other ones are first
			}
		}		
		catch(Exception e){
			System.err.println("An error has occured. See below for details");
			e.printStackTrace();
		}
	}
	
	/*
	
	Function to write the Game Modes array to the local text file
	
	@param  : none
	@return : none
	
	*/		
	public void writeGameModes(){
		try{
			FileWriter fw = new FileWriter("GameModes.txt");
			String s = CurrentGameMode.writeString()+"\n";
			for(GameMode G : GameModesArray){
				s+= G.writeString()+"\n"; // the writeString function outputs the string and integers in the correct order
			}
			fw.write(s);
			fw.close();
		}		
		catch(Exception e){
			System.err.println("An error has occured. See below for details");
			e.printStackTrace();
		}
	}
	
	/*
	
	Function to read in the user set options from the local Options.txt file.
	The options are stored as strings in the order of the OptionsArray attribute.
	@param  : none
	@return : none
	
	*/		
	public void readOptions(){
		try{
			Scanner scanner = new Scanner(new BufferedReader(new FileReader("Options.txt")));
			while(scanner.hasNext()){
				String N = scanner.next();
				OptionsArray.add(N); // writes the strings to options array 
			}
			updateColors(); // sets colours to new user chosen ones
		}		
		catch(Exception e){
			System.err.println("An error has occured. See below for details");
			e.printStackTrace();
		}
	}
	
	/*
	
	Function to set the colours to user chosen ones
	
	@param  : none
	@return : none
	
	*/		
	public void updateColors(){
		ColorArray.clear();
		for(int q=2; q<5; q++){ // the colour strings for numbers, flags and mines are stored in indexes 2,3 and 4 of the options menu
			StyleSheet s = new StyleSheet();
			Color C = s.stringToColor(OptionsArray.get(q)); // changes the strings to colours
			ColorArray.add(C); // the colours array is used when setting up each menu to determine colours
		}
	}
	
	/*
	
	Function to write the user chosen options to a local text file
	
	@param  : none
	@return : none
	
	*/		
	public void writeOptions(){
		try{
			FileWriter fw = new FileWriter("Options.txt");
			String s="";
			for(String S : OptionsArray){
				s+= S+"\n"; // adds each option
			}
			fw.write(s);
			fw.close();
		}		
		catch(Exception e){
			System.err.println("An error has occured. See below for details");
			e.printStackTrace();
		}
	}
	
	/*
	
	Function to read in the users stats
	
	@param  : none
	@return : none
	
	*/		
	public void readStats(){
		try{
			Scanner scanner = new Scanner(new BufferedReader(new FileReader("Stats.txt")));
			int i=0;
			while(scanner.hasNext()){
				WinsLosses[i] = scanner.nextInt();  // inputs the overall wins and losses int the form of an int array [wins, losses]
				i++;
			}
		}		
		catch(Exception e){
			System.err.println("An error has occured. See below for details");
			e.printStackTrace();
		}
	}
	
	/*
	
	Function to write the users wins and losses to store them
	
	@param  : none
	@return : none
	
	*/		
	public void writeStats(){
		try{
			FileWriter fw = new FileWriter("Stats.txt");
			String s="";
			for(int i : WinsLosses){
				s+= i+"\n"; // adds the integers representing wins and losses
			}
			fw.write(s);
			fw.close();
		}		
		catch(Exception e){
			System.err.println("An error has occured. See below for details");
			e.printStackTrace();
		}
	}
	
	/*
	
	Function to find the button which is selected in an array of MenuButtons. A Button is "selected" if it is the last button clicked.
	
	@param  : A MenuButton[] MB_Arr to search through
	@return : The first 'selected' MenuButton or null if none are
	
	*/		
	public MenuButton findSelectedButton(ArrayList<MenuButton> MB_Arr){
		for(MenuButton MB : MB_Arr){
			if(MB.isSelected()){
				return MB;
			}
		}
		return null;
	}
	
	/*
	
	Function to set the current GameMode based on the current slider values. Used to go into custom games without creating a new GameMode.
	
	@param  : none
	@return : none
	
	*/		
	public void setCurrentGameModeFromSliders(){
		CurrentGameMode = new GameMode(GameModeSliders[0].getValue(), GameModeSliders[1].getValue(), GameModeSliders[2].getValue(), CurrentSelectedGameModeText); // reads the slider values and creates a GameMode
		for(GameMode G : GameModesArray){ 
			if(G.Name.equals(CurrentGameMode.Name)){
				CurrentGameMode.BestTimes = G.BestTimes; // if this is an existing GameMode then Best Times are copied
			}
		}
	}
	
	/*
	
	Function to 
	
	@param  : 
	@return : 
	
	*/		
	public MenuButton getSelectedButton(ArrayList<MenuButton> MB_Arr){
		for(MenuButton M : MB_Arr){
			if(M.isSelected()){
				return M;
			}
		}
		return null;
	}
	
	/*
	
	Function to create a String representing time in hours:mins:secs from an integer representing seconds
	
	@param  : int i representing seconds
	@return : String representing the time in a human readable format
	
	*/		
	public String createTimeString(int i){
		if(i==-1){
			return " - ";
		}
		int secs = (int)i%60;
		int mins = (int)Math.floor(i/60)%60;
		int hours = (int)Math.floor(Math.floor(i/60)/60);
		return addZero(hours)+":"+addZero(mins)+":"+addZero(secs);
	}
	
	/*
	
	Function to add zero to the beginning of a single digit integer (solely used in createTimeString(i) above)
	
	@param  : int i to have zero added
	@return : String representation of the int for a time purpose
	
	*/			
	public String addZero(int i){
		if(i<10){
			return "0"+i;
		}
		else{
			return i+"";
		}
	}
	
	/*
	
	Function to create a JLabel with correct alignment and foregound for use in menus
	
	@param  : String for Text, Font for the font, Colour for text colour
	@return : JLabel with correct formatting for menus
	
	*/		
	public JLabel createMenuLabel(String s,  Font F, Color C){
		JLabel J = new JLabel(s);
		J.setFont(F);
		J.setForeground(C);
		J.setHorizontalAlignment(SwingConstants.CENTER);
		return J;
	}
	
	/*
	
	Function to find GameMode in Array by name
	
	@param  : String of desired GameMode name
	@return : GameMode if that name exists in the GameModesArray, null if not
	
	*/		
	public GameMode findGameMode(String S){
		for(GameMode G : GameModesArray){
			if(G.Name.equals(S)){
				return G;
			}
		}
		return null;
	} 
	
	/*
	
	Function to create a GridPanel with correct formatting for a Menu Title. Is also abused for creation of labels and texts
	
	@param  : String for text, Font for the font of the text, Boolean for whether or not the panel has a border, COlour for colour of the etxt
	@return : GridPanel ready for use in Menus. 
	
	*/		
	public GridPanel createTitle(String T,  Font F, boolean hasBorder, Color C){
		JTextField MenuText = new JTextField(T);
		MenuText.setHorizontalAlignment(SwingConstants.CENTER);
		MenuText.setAlignmentX(Component.CENTER_ALIGNMENT);
		MenuText.setEditable(false);
		MenuText.setForeground(C);
		MenuText.setFont(F);
		if(!hasBorder){
			MenuText.setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		GridPanel MenuTextPanel = new GridPanel(ver);
		MenuTextPanel.add(MenuText);
		return MenuTextPanel;
	}
	
	class Menu extends JFrame{
		
		public int X;
		public int Y;
		public int Id;
		public int offY;
		public String name;
		
		public JPanel MainPanel = new JPanel(new BorderLayout(5, 5));
		
		public Menu(int x,  int y, int Id, int offY){
			super();
			this.offY = offY;
			this.X = x;
			this.Y = y;
			this.Id = Id;
			this.setPreferredSize(new Dimension(X, Y));
		}
		
		public Menu(String s, int x, int y, String name){
			super(s);
			this.X = x;
			this.Y = y;
			this.setPreferredSize(new Dimension(X, Y));
			this.name = name;
			this.Id = -1;
		}
		
		public void create(){
			if(Id!=DeleteGameModeInt&&Id!=OptionsInt&&Id!=StatsInt){
				this.MainPanel.add(createTitle(TitlesArray[Id], new Font("", Font.BOLD, TitleFontSizesArray[Id]), true, Color.BLACK), BorderLayout.PAGE_START);
			}
			this.MainPanel.add(createButtonsFromStringArray(ButtonStringsArray[Id], new Font("", Font.BOLD, ButtonFontSizesArray[Id]), Id, ButtonOrientationArray[Id]), ButtonLocationsArray[Id]);
			switch(this.Id){
				case GameOverInt:
					createGameOverMenu();
				break;
				case GameWonInt:
					createGameWonMenu();
				break;
				case GameModeInt:
					createGameModeMenu();
				break;
				case NewGameModeInt:
					createNewGameModeMenu();
				break;
				case DeleteGameModeInt:
					createDeleteGameModeMenu();
				break;
				case OptionsInt:
					createOptionsMenu();
				break;
				case PauseInt:
					createPauseMenu();
				break;
				case StatsInt:
					createStatsMenu();
				break;
			}
			this.setUpMenu();
			keepWithBackGround();
			updateMenusArray();
		}
		
		public void createGameModeMenu(){
			GameModeSliderLabels.clear();
			int N = GameModesArray.size();
			int n = 45;
			while(n*(N)>(150)){
				n--;
			}
			int y = n*(N)+150;
			this.Y = Math.max(300, y);
			this.setPreferredSize(new Dimension(X, Y));
			
			GridPanel GameModeMenuMiddlePanel = new GridPanel(hor);
			
			for(GameMode G : GameModesArray){
				MenuButton M = new MenuButton(G.Name,  GameModeButtonsFont);
				GameModeButtons.add(M);
				M.addGameModeAction();
			}
	
			CustomButton = new MenuButton("Custom", GameModeButtonsFont);
			CustomButton.addGameModeAction();
			GameModeButtons.add(CustomButton);
			
			MenuButtonsPanel GameModeButtonsPanel = new MenuButtonsPanel(ver, GameModeButtons);
			GameModeMenuMiddlePanel.add(GameModeButtonsPanel);
			
			GridPanel GameModeSlidersPanel = new GridPanel(ver);
			GameModeSliders = new GameModeSlider[]{new RCSlider(CurrentGameMode.r, "Rows"),  new RCSlider(CurrentGameMode.c, "Columns"), new MineSlider(CurrentGameMode.m, "Mines")};
			for(GameModeSlider G : GameModeSliders){
				JPanel SliderPanel = new JPanel();
				SliderPanel.setLayout(new BoxLayout(SliderPanel, BoxLayout.PAGE_AXIS));
				JLabel SliderLabel = new JLabel(G.Name+"   ["+G.getValue()+"]");
				SliderLabel.setFont(SliderLabelFont);
				GameModeSliderLabels.add(SliderLabel);
				SliderPanel.add(SliderLabel);
				SliderPanel.add(G);
				GameModeSlidersPanel.add(SliderPanel);
			}
			GameModeMenuMiddlePanel.add(GameModeSlidersPanel);
			MainPanel.add(GameModeMenuMiddlePanel, BorderLayout.CENTER);
			for(MenuButton M : GameModeButtons){
				if(M.getText().equals(CurrentGameMode.Name)){
					M.selectButton(GameModeButtons);
				}
			}
			
			MenuButtonsPanel Upper = createButtonsFromStringArray(GameModeUpperButtonTexts, GameModeUpperFont, GameModeInt, hor);
			this.add(Upper, BorderLayout.PAGE_START);
		}
		
		public void createNewGameModeMenu(){
			JPanel NewGameModeMiddlePanel = new JPanel(new GridBagLayout());
			int y = 0;
			for(String T : NewGameModeLabelTexts){
				GridBagConstraints C = new GridBagConstraints();
				C.ipadx = 5;
				C.gridx = 0;
				C.gridy = y;
				JLabel P = new JLabel(T, SwingConstants.LEFT);
				P.setFont(NewGameModeTextFont);
				NewGameModeLabels.add(P);
				NewGameModeMiddlePanel.add(P, C);
				C.gridx++;
				JTextField J = new JTextField(4);
				J.setHorizontalAlignment(SwingConstants.CENTER);
				J.setFont(NewGameModeTextFont);
				NewGameModeTextFields.add(J);
				NewGameModeMiddlePanel.add(J, C);
				y++;
			}
			MainPanel.add(NewGameModeMiddlePanel);
			MainPanel.add(new JPanel(), BorderLayout.LINE_START);
			MainPanel.add(new JPanel(), BorderLayout.LINE_END);
		}
		
		public void createDeleteGameModeMenu(){
			MainPanel.add(createTitle("Delete \""+CurrentSelectedGameModeText+"\"?", DeleteGameModeTitleFont, true, Color.BLACK), BorderLayout.CENTER);
		}
		
		public void createOptionsMenu(){
			GridPanel OptionsPanel = new GridPanel(ver);
			for(int i=0; i<5; i++){
				OptionsComboPanel O = new OptionsComboPanel(i);
				OptionsPanel.add(O);
			}
			MainPanel.add(OptionsPanel);
		}
		
		public void createGameWonMenu(){
			JPanel Times = new JPanel();
			Times.setLayout(new BoxLayout(Times, BoxLayout.PAGE_AXIS));
			Times.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			try{
				Times.add(new TimesPanel(CurrentGameMode, GameWonTimesFont, CurrentGameMode.getBestTimesIndexOf(CurrentGameBoard.GameTimer.s)));
			}
			catch(NullPointerException e){}
			MainPanel.add(Times);
			
		}
		
		public void createGameOverMenu(){
			try{
				String str = createTimeString(CurrentGameBoard.GameTimer.s);
				GridPanel Times = new GridPanel(ver);
				Times.add(createGameOverTimeText("Time : "+str));
				Times.add(createGameOverTimeText("Best Time : "+createTimeString(CurrentGameMode.BestTimes[0])));
				MainPanel.add(Times);
			}
			catch(NullPointerException npe){
			}
		}
		
		public void createPauseMenu(){
			MainPanel.add(createTitle("NB - Quitting will count as a loss.", PauseFont, false, null));
		}
		
		public void createStatsMenu(){
			GridPanel StatsPanels = new GridPanel(ver);
			JPanel LocalStats = new JPanel();
			TimesPanel Times = new TimesPanel(null, LocalStatsFont, -1);
			StatsPanel LocalStatsPanel = new StatsPanel(WinsLosses, LocalStatsLabelTexts, LocalStatsFont);
			
			ArrayList<String> StatsComboTexts = new ArrayList<String>();
			StatsComboTexts.add("All Game Modes");
			for(GameMode G : GameModesArray){
				StatsComboTexts.add(G.Name);
			}
			
			JComboBox<String> LocalStatsCombo = new JComboBox<String>(StatsComboTexts.toArray(new String[StatsComboTexts.size()])); 
			LocalStatsCombo.setFont(new Font("", Font.BOLD, 18));
			((JLabel)LocalStatsCombo.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
			MainPanel.add(LocalStatsCombo, BorderLayout.PAGE_START);
			LocalStatsCombo.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try{
						GameMode G = findGameMode((String)LocalStatsCombo.getSelectedItem());
						Times.updateLabels(G);
						LocalStatsPanel.updateLabels(G.GameModeWinsLosses);
					}
					catch(NullPointerException npe){
						Times.updateLabels(null);
						LocalStatsPanel.updateLabels(WinsLosses);
					}
				}
			});
			StatsPanels.add(LocalStatsPanel);
			StatsPanels.add(Times);
			
			
			MainPanel.add(StatsPanels);
		}
		
		public JTextField createGameOverTimeText(String str){
			JTextField TimeField = new JTextField(str);
			TimeField.setEditable(false);
			TimeField.setHorizontalAlignment(SwingConstants.CENTER);
			TimeField.setFont(GameOverTimeFont);
			TimeField.setBorder(new EmptyBorder(0, 0, 0, 0));
			return TimeField;
		}
		
		public void addLowerPanel(ArrayList<MenuButton> MB_Arr){
			MenuButtonsPanel LowerPanel = new MenuButtonsPanel(hor, MB_Arr);
			MainPanel.add(LowerPanel, BorderLayout.PAGE_END);
		}
		
		public MenuButtonsPanel createButtonsFromStringArray(String[] S_Arr, Font F, int MenuType, int[] Orientation){
			ArrayList<MenuButton> MB_Arr = new ArrayList<MenuButton>();
			for(String S : S_Arr){
				MenuButton M = new MenuButton(S, F);
				switch(MenuType){
					case MainInt:
						M.addMainMenuAction();
					break;
					case GameOverInt:
						M.addGameOverAction();
					break;
					case GameWonInt:
						M.addGameWonAction();
					break;
					case GameModeInt:
						M.addGameModeAction();
					break;
					case NewGameModeInt:
						M.addNewGameModeAction();
					break;
					case DeleteGameModeInt:
						M.addDeleteGameModeAction();
					break;
					case OptionsInt:
						M.addOptionsAction();
					break;
					case PauseInt:
						M.addPauseAction();
					break;
					case StatsInt:
						M.addStatsAction();
					break;
				}
				MB_Arr.add(M);
			}
			return new MenuButtonsPanel(Orientation, MB_Arr);
		}
		
		public void setUpMenu(){
			this.add(MainPanel);
			this.setResizable(false);
			updateMenusArray();
			keepWithBackGround();
			if(Id!=-1){
				try{
					this.setUndecorated(true);
					this.setAlwaysOnTop(true);
				}
				catch(IllegalComponentStateException e){
				}
			}
			this.pack();
		}	
		
		public void setCentralLocation(Menu M){
			this.setLocation((int)((M.getX()+(M.X)/2)-(X/2)), (int)(M.getY()+((M.Y)/2)-(Y/2)+offY));
		}
		
		public void setCentralLocation(GameBoard G){
			this.setLocation((int)((G.getX()+(G.X)/2)-(X/2)), (int)(G.getY()+((G.Y)/2)-(Y/2)+offY));
		}
	
	}

	class MenuButton extends JButton{
		public Font TextFont;
		
		public MenuButton(String t, Font F){
			super(t); 
			this.TextFont = F;
			this.setFont(this.TextFont);
			this.setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		public void addMainMenuAction(){
			String Text = this.getText();
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(Text.equals("New Game")){
						MainMenu.setVisible(false);
						startGame();
					}
					if(Text.equals("Game Mode")){
						setBackgroundText(" game    ", "    mode ");
						MainMenu.setVisible(false);
						GameModeMenu.setVisible(true);
					}
					if(Text.equals("Options")){
						setBackgroundText("         ", " options ");
						MainMenu.setVisible(false);
						OptionsMenu = new Menu(300, 240, OptionsInt, 20);
						OptionsMenu.create();
						OptionsMenu.setVisible(true);
					}
					if(Text.equals("Statistics")){
						setBackgroundText("      ", "  stats  ");
						StatsMenu.dispose();
						StatsMenu = new Menu(300, 300, StatsInt, 40);
						StatsMenu.create();
						StatsMenu.setVisible(true);
						MainMenu.setVisible(false);
					}
					if(Text.equals("Exit")){
						writeGameModes();
						writeOptions();
						writeStats();
						System.exit(0);
					}
				}
			});
		}
		
		public void addGameModeAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String s = getText();
					if(s.equals("START")){
						setCurrentGameModeFromSliders();
						writeGameModes();
						GameModeMenu.setVisible(false);
						startGame();
						return;
					}
					if(s.equals("BACK")){
						setCurrentGameModeFromSliders();
						writeGameModes();
						GameModeMenu.setVisible(false);
						MainMenu.setVisible(true);
						setBackgroundText("mine   ", "  sweeper");
						return;
					}
					if(s.equals("New")){
						GameModeMenu.setVisible(false);
						GameModeMenu.dispose();
						NewGameModeMenu.setVisible(true);
						return;
					}
					if(s.equals("Delete")){
						if(GameModeButtons.get(GameModeButtons.size()-1).isSelected()){
							return;
						}
						if(CurrentSelectedGameModeText.equals("Custom")){
							return;
						}
						GameModeMenu.setVisible(false);
						DeleteGameModeMenu = new Menu(250, 100, DeleteGameModeInt, 15);
						DeleteGameModeMenu.create();
						DeleteGameModeMenu.setVisible(true);
						return;
					}
					for(GameMode G : GameModesArray){	
						if(G.Name.equals(getText())){
							GameModeSliders[0].setValue(G.r);
							GameModeSliders[1].setValue(G.c);
							GameModeSliders[2].setValue(G.m);
						} 
					}
					String t = s;
					if(s.charAt(0)=='>'){
						t = "";
						for(int i=2; i<s.length(); i++){
							t+=s.charAt(i);
						}
					}
					CurrentSelectedGameModeText = t;
					selectButton(GameModeButtons);
				}
			});
		}
		
		public void addNewGameModeAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String Text = getText();
					if(Text.equals("BACK")){
						NewGameModeMenu.setVisible(false);
						GameModeMenu.setVisible(true);
					}
					if(Text.equals("SAVE")){
						String q = getBannedWord(NewGameModeTextFields.get(0));
						if(!q.equals("")){
							NewGameModeLabels.get(0).setText("Name cannot be "+q);
							NewGameModeLabels.get(0).setForeground(Color.RED);
							return;
						}
						else{
							reset(NewGameModeLabels.get(0));
						}
						for(int i=1; i<4; i++){
							JTextField JTF = NewGameModeTextFields.get(i);
							if(!isNumeric(JTF)){
								NewGameModeLabels.get(i).setText("Must be a number");
								NewGameModeLabels.get(i).setForeground(Color.RED);
								return;
							}
							else{
								reset(NewGameModeLabels.get(i));
							}
						}
						int R = Integer.parseInt(NewGameModeTextFields.get(1).getText());
						int C = Integer.parseInt(NewGameModeTextFields.get(2).getText());
						int M = Integer.parseInt(NewGameModeTextFields.get(3).getText());
						int[]MRC = new int[]{R, C, M};
						for(int j=1; j<4; j++){
							if(MRC[j-1]<=0){
								NewGameModeLabels.get(j).setText("Must be positive");
								NewGameModeLabels.get(j).setForeground(Color.RED);
								return;
							}
							if(j==3){
								if(M>=R*C){
									NewGameModeLabels.get(j).setText("Too many mines!");
									NewGameModeLabels.get(j).setForeground(Color.RED);
									return;
								}
							}
						}
						for(JLabel J : NewGameModeLabels){
							reset(J);
						}
						NewGameModeMenu.setVisible(false);
						GameMode New = new GameMode(R, C, M, NewGameModeTextFields.get(0).getText());
						GameModesArray.add(New);
						for(JTextField J : NewGameModeTextFields){
							J.setText("");
						}
						GameModeButtons.clear();
						GameModeMenu = new Menu(410, 0, GameModeInt, 45);
						GameModeMenu.create();
						GameModeMenu.setVisible(true);
					}
				}
			});
		}
		
		public void addDeleteGameModeAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent E){
					String Text = getText();
					if(Text.equals("CANCEL")){
						DeleteGameModeMenu.dispose();
						GameModeMenu.setVisible(true);
					}
					int index = 0;
					if(Text.equals("YES")){
						for(GameMode G : GameModesArray){
							if(G.Name.equals(CurrentSelectedGameModeText)){
								index = (GameModesArray.indexOf(G));
							}
						}
						DeleteGameModeMenu.dispose();
						GameModesArray.remove(index);
						GameModeButtons.clear();
						GameModeMenu.dispose();
						GameModeMenu = new Menu(410, 0, GameModeInt, 45);
						GameModeMenu.create();
						GameModeMenu.setVisible(true);
					}
				}
			});
		}
		
		public void addOptionsAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					updateColors();
					writeOptions();
					setBackgroundText("mine", "  sweeper");
					OptionsMenu.dispose();
					Background.dispose();
					setUpBackground(true, true);
					Background.setVisible(true);
					MainMenu.setVisible(true);
				}
			});
		}
		
		public void addGameWonAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					CurrentGameBoard.dispose();
					GameWonMenu.dispose();
					if(getText().equals("MENU")){
						Background.setVisible(true);
						setBackgroundText("mine", "  sweeper");
						MainMenu.setVisible(true);
					}
					if(getText().equals("RESTART")){
						startGame();
					}
				}
			});
		}
		
		public void addGameOverAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					CurrentGameBoard.dispose();
					GameOverMenu.dispose();
					if(getText().equals("MENU")){
						Background.setVisible(true);
						setBackgroundText("mine", "  sweeper");
						MainMenu.setVisible(true);
					}
					if(getText().equals("RESTART")){
						startGame();
					}	
				}
			});
		}
		
		public void addPauseAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String Text = getText();
					if(Text.equals("RESUME")){
						if(CurrentGameBoard.isCounting){
							CurrentGameBoard.GameTimer.T.start();
						}
						CurrentGameBoard.isPaused = false;
						CurrentGameBoard.isPlayable = true;
						PauseMenu.setVisible(false);
					}
					if(Text.equals("QUIT")){
						CurrentGameMode.GameModeWinsLosses[1]++;
						findGameMode(CurrentGameMode.Name).GameModeWinsLosses[1]++;
						writeGameModes();
						WinsLosses[1]++;
						writeStats();
						CurrentGameBoard.dispose();
						PauseMenu.setVisible(false);
						MainMenu.setVisible(true);
						Background.setVisible(true);
					}
				}
			});
		}
		
		public void addStatsAction(){
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					StatsMenu.setVisible(false);
					setBackgroundText("mine     ", "  sweeper");
					MainMenu.setVisible(true);
				}
			});
		}
		
		public void reset(JLabel J){
			int idx = NewGameModeLabels.indexOf(J);
			J.setText(NewGameModeLabelTexts[idx]);
			J.setForeground(null);
		}
		
		public boolean isNumeric(JTextField JTF){
			try{
				Integer.parseInt(JTF.getText());
				return true;
			}
			catch(Exception e){
				return false;
			}
		}
		
		public String getBannedWord(JTextField JTF){
			String s = JTF.getText();
			for(GameMode G : GameModesArray){
				if(s.equals(G.Name)){
					return G.Name;
				}
			}
			for(String S : NewGameModeBannedWords){
				if(s.equals(S)){
					return S;
				}
			}
			return "";
		}
	
		public boolean isSelected(){
			return this.getText().contains(">")&&this.getForeground().equals(SelectedButtonColor);
		}
	
		public void toggleSelected(){
			if(this.isSelected()){
				this.setForeground(Color.BLACK);
				String PlainText = "";
				int Length = this.getText().length();
				for(int i = 2; i < Length; i++){
					PlainText += this.getText().charAt(i); 
				}
				this.setText(PlainText);
			}
			else if(!this.isSelected()){
				this.setForeground(SelectedButtonColor);
				String NewText = "> "+this.getText();
				this.setText(NewText);
			}
		}
	
		public void selectButton(ArrayList<MenuButton> MB_Arr){
			for(MenuButton M : MB_Arr){
				if(M.isSelected()){
					M.toggleSelected();
				}
			}
			this.toggleSelected();
		}
	
	}

	class MenuButtonsPanel extends GridPanel{

		public int[] Orientation;
		public ArrayList<MenuButton> Buttons;
		
		public MenuButtonsPanel(int[] Orientation,  ArrayList<MenuButton> Buttons){
			super(Orientation);
			this.Orientation = Orientation;
			this.Buttons = Buttons;
			
			for(MenuButton M : Buttons){
				this.add(M);
			}
		}
		
	}
	
	class GameMode{
		public int r;
		public int c;
		public int m;
		public String Name;
		public int[] BestTimes = new int[3];
		public int[] GameModeWinsLosses;
		
		public GameMode(int r,  int c,  int m,  String name){
			this.r = r;
			this.c = c;
			this.m = m;
			this.Name = name;
			this.BestTimes = new int[]{-1, -1, -1};
			this.GameModeWinsLosses = new int[]{0, 0};
		}
		
		public void placeTime(int s){
			ArrayList<Integer> I_Arr = new ArrayList<Integer>();
			for(int i : BestTimes){
				if(i>=0){
					I_Arr.add(i);
				}
			}
			I_Arr.add(s);
			Collections.sort(I_Arr);
			for(int i=0; i<3; i++){
				try{
					BestTimes[i] = I_Arr.get(i);
				}
				catch(IndexOutOfBoundsException ioobe){
					BestTimes[i] = -1;
				}
			}
		}
		
		public int getBestTimesIndexOf(int i){
			for(int j=0; j<3; j++){
				if(BestTimes[j]==i){
					return j;
				}
			}
			return -1;
		}
		
		public String toString(){
			return this.Name;
		}
		
		public String writeString(){
			return Name+" "+r+" "+c+" "+m+" "+BestTimes[0]+" "+BestTimes[1]+" "+BestTimes[2]+" "+this.GameModeWinsLosses[0]+" "+this.GameModeWinsLosses[1];
		}
		
		public boolean equals(GameMode G){
			return this.Name==G.Name;
		}
	}

	class GridPanel extends JPanel{
		public int Orientation;
		
		public GridPanel(int[] O){
			super(new GridLayout(O[0], O[1]));
		}
		
		public GridPanel(ArrayList<JLabel> J, int[]O){
			super(new GridLayout(O[0], O[1]));
			for(JLabel j : J){
				this.add(j);
			}
		}
	}

	class GameModeSlider extends JSlider{
		public String Name;
		public int BestTime;
		public int BestTime2;
		public int BestTime3;
		
		public GameModeSlider(int v, String t){
			super();
			this.Name = t;
			this.setValue(v);
			this.setFocusable(false);
			this.setPaintTicks(true);
			this.setPaintLabels(true);
			this.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e){
					try{
						for(int i=0; i<3; i++){
							GameModeSliderLabels.get(i).setText(GameModeSliders[i].Name+"  ["+GameModeSliders[i].getValue()+"]");
						}
					}
					catch(IndexOutOfBoundsException E){}
					update();
					CurrentSelectedGameModeText = "Custom";
					CustomButton.selectButton(GameModeButtons);
				}
			}); 
		}
		
		public void update(){
			try{
				int minRC = Math.min(GameModeSliders[0].getValue(), GameModeSliders[1].getValue());
				int MineMin = Math.max(minRC-20, 1);
				GameModeSliders[2].setMinimum(MineMin);
				GameModeSliders[2].setMaximum(GameModeSliders[0].getValue()*GameModeSliders[1].getValue()-1);
				MineSliderLabel = new Hashtable<>();
				MineSliderLabel.put(MineMin, new JLabel(MineMin+""));
				int MineMax = GameModeSliders[0].getValue()*GameModeSliders[1].getValue()-1;
				MineSliderLabel.put(MineMax, new JLabel(MineMax+""));
				GameModeSliders[2].setLabelTable(MineSliderLabel);
				GameModeSliders[2].setMinorTickSpacing((int)(MineMax/15));
				GameModeSliders[2].setMajorTickSpacing(MineMax-2);
			}
			catch(Exception e){}
		}
	}
	
	class RCSlider extends GameModeSlider{
		public RCSlider(int v, String name){
			super(v, name);
			this.setMinimum(9);
			this.setMaximum(45);
			this.setMinorTickSpacing(2);
			this.setMajorTickSpacing(36);
		}
	}
	
	class MineSlider extends GameModeSlider{
		public MineSlider(int v,  String name){
			super(v, name);
			this.setValue(CurrentGameMode.m);
			this.setMinimum(1);
			int MineMax = CurrentGameMode.r*CurrentGameMode.c;
			this.setMaximum(MineMax);
			this.setMinorTickSpacing((int)MineMax/15);
			this.setMajorTickSpacing(MineMax-1);
		}
		
	}
	
	class MineSweeper{
		public int[][] grid;
		public int rows;
		public int columns;
		public int mines;
		
		public MineSweeper(int rows,  int cols,  int mines){
			this.rows = rows;
			this.columns = cols;
			this.mines = mines;
			this.grid = new int[rows][cols];
			int MineNumber = 0;
			while(MineNumber<mines){
				int r = (int)(Math.random()*rows);
				int c = (int)(Math.random()*cols);
				if(grid[r][c]!=-1){
					grid[r][c] = -1;
					MineNumber++;
				}
			}
			for(int x = 0; x < rows; x++){
				for(int y = 0; y < cols; y++){
					grid[x][y] = adjacentMines(x, y);
				}
			}		
		}
		
		public int adjacentMines(int r,  int c){
			if(grid[r][c]==-1){
				return -1;
			}
			else{
				int mineCount = 0;
				for(int x = -1; x < 2; x++){
					for(int y = -1; y < 2; y++){
						try{
							int S = grid[r+x][c+y];
							if(S==-1){
								mineCount+=1;
							}
						}
						catch(Exception e){
							continue;
						}
					}
				}
				return mineCount;
			}
		}
	
	}	

	class OptionsComboPanel extends JPanel{
		public JComboBox<String> ComboBox;
		public JLabel ComboLabel;
		public String SelectedItem;
		public int i;
		
		public OptionsComboPanel(int i){
			super(new BorderLayout(3, 3));
			this.i=i;
			ComboLabel = new JLabel(OptionsComboLabels[i]);
			ComboLabel.setFont(OptionsComboFont);
			this.add(ComboLabel, BorderLayout.CENTER);
			ArrayList<String> S_Arr1 = new ArrayList<String>(); 
			ArrayList<String> S_Arr2 = new ArrayList<String>(); 
			for(String S : OptionsArrays[i]){
				if(S.equals(OptionsArray.get(i))){
					S_Arr2.add(S);
				}
				else{
					S_Arr1.add(S);
				}
			}
			for(String S : S_Arr1){
				S_Arr2.add(S);
			}
			String[] ModifiedOptionsArray = S_Arr2.toArray(new String[0]);
			ComboBox = new JComboBox<String>(ModifiedOptionsArray);
			ComboBox.setPreferredSize(new Dimension(OptionsSizeArray[i], 40));
			ComboBox.setFont(OptionsComboFont);
			ComboBox.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String SelectedItem = (String)ComboBox.getSelectedItem();
					OptionsArray.set(i, SelectedItem);
				}
			});
			this.add(ComboBox, BorderLayout.LINE_END);
			this.add(new JPanel(), BorderLayout.LINE_START);
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
	}

	class GameBoard extends JFrame{
		public GameMode G;
		public JPanel BoardGrid;
		public MineSweeper M;
		public int X;
		public int Y;
		public TimerPanel GameTimer;
		public boolean isPlayable = true;
		public boolean isCounting = false;
		public MenuButton Pause = new MenuButton("Pause", new Font("", Font.BOLD, 18));
		public Timer T;
		public int idx;
		public boolean isFinished;
		public int FlagCounter = 0;
		public JTextField FlagField;
		public boolean isPaused = false;
		
		public GameBoard(GameMode G){
			super("MineSweeper");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.G = G;
			BoardGrid = new JPanel(new GridLayout(G.r, G.c, 0, 0));
			int n = 55;
			while(n*G.r>MAX_X||n*G.c>MAX_Y){
				n--;
			}
			GameBoardSquareSize = n;
			this.X = n*G.c;
			this.Y = n*G.r;
			BoardGrid.setPreferredSize(new Dimension(X, Y));
			this.setSize(new Dimension(X, Y+50));
			int size = (int)Math.max((20*n/55), 13);
			NumberFont = new Font("Spaced", Font.BOLD, size);
			M = new MineSweeper(G.r, G.c, G.m);
			GameBoardSquares = new GameBoardSquare[G.r][G.c];
			for(int R=0; R<G.r; R++){
				for(int C=0; C<G.c; C++){
					GameBoardSquare J = new GameBoardSquare(R, C);
					GameBoardSquares[R][C] = J;
					BoardGrid.add(J);
				}
			}
			this.add(BoardGrid, BorderLayout.CENTER);
			
			Pause.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(!isPaused){
						if(isCounting){
							GameTimer.T.stop();
						}
						isPaused = true;
						isPlayable = false;
						PauseMenu.setVisible(true);
					}
					else{
						if(isCounting){
							GameTimer.T.start();
						}
						isPaused = false;
						isPlayable = true;
						PauseMenu.setVisible(false);
					}
				}
			});
			
			FlagField = new JTextField("Mines Left : "+(CurrentGameMode.m - FlagCounter));
			FlagField.setFont(new Font("", Font.BOLD, 18));
			FlagField.setEditable(false);
			FlagField.setHorizontalAlignment(SwingConstants.CENTER);
			FlagField.setBorder(new EmptyBorder(0, 0, 0, 0));
			
			GridPanel PauseTimeFlag = new GridPanel(hor);
			GameTimer = new TimerPanel();
			PauseTimeFlag.add(Pause);
			PauseTimeFlag.add(GameTimer);
			PauseTimeFlag.add(FlagField);
			this.add(PauseTimeFlag, BorderLayout.PAGE_END);
			this.addComponentListener(new ComponentAdapter(){
				public void componentMoved(ComponentEvent e){
					LOCATION_X = getX();
					LOCATION_Y = getY();
					X = getWidth();
					Y = getHeight();
					keepWithBackGround();
				}
			});
		}
		
		public boolean isWon(){
			for(GameBoardSquare[] G_Arr : GameBoardSquares){
				for(GameBoardSquare G : G_Arr){
					if((!G.isClicked&&CurrentGameBoard.M.grid[G.r][G.c]>=0)){
						return false;
					}
				}
			}
			return true;
		}
		
		public void revealMines(){
			ArrayList<GameBoardSquare> MineList = new ArrayList<GameBoardSquare>();
			for(int L=0; L<Math.max(G.r, G.c);L++){
				for(int R=0; R<=L;R++){
					try{
						if(M.grid[R][L]==-1){
							MineList.add(GameBoardSquares[R][L]);
						}
						if(M.grid[L][R]==-1){
							MineList.add(GameBoardSquares[L][R]);
						}
					}
					catch(IndexOutOfBoundsException i){}
				}
			}
			ActionListener MineDelay = new ActionListener(){
				public int m=GameBoard.this.G.m;
				public void actionPerformed(ActionEvent e){
					try{
						if(m>=0){
							FlagField.setText("Mines Left : "+m);
							MineList.get(idx).set(Mine);
							GameBoard.this.idx++;
							m--;
						}
					}
					catch(IndexOutOfBoundsException ioobe){
						T.stop();
					}
				}
			};
			T = new Timer(10, MineDelay);
			T.start();
		}
	}

	class GameBoardSquare extends JTextField{
		public int r;
		public int c;
		public boolean isClicked = false;
		
		public GameBoardSquare(int r,  int c){
			super(3);
			this.r=r;
			this.c=c;
			this.setBorder(BorderFactory.createLoweredBevelBorder());
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setFont(NumberFont);
			this.setEditable(false);
			this.setSize(new Dimension(GameBoardSquareSize, GameBoardSquareSize));
			this.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(!CurrentGameBoard.isPlayable){
						return;
					}
					if(!CurrentGameBoard.isCounting){
						CurrentGameBoard.GameTimer.T.start();
						CurrentGameBoard.isCounting = true;
					}
					if(SwingUtilities.isLeftMouseButton(e)){
						clickedOn();
					}
					else if(SwingUtilities.isRightMouseButton(e)){
						String s = getText();
						if(s.equals("")){
							set(Flag);
							CurrentGameBoard.FlagCounter++;
							if(CurrentGameBoard.FlagCounter<=CurrentGameMode.m){
								CurrentGameBoard.FlagField.setText("Mines Left : "+(CurrentGameMode.m - CurrentGameBoard.FlagCounter));
							}
						}
						if(s.equals(OptionsArray.get(Flag))){
							setText("");
							CurrentGameBoard.FlagCounter--;
							if(CurrentGameBoard.FlagCounter<=CurrentGameMode.m){
								CurrentGameBoard.FlagField.setText("Mines Left : "+(CurrentGameMode.m - CurrentGameBoard.FlagCounter));
							}
						}
					}
				}
			});
		}
		
		public void clickedOn(){
			if(getText().equals(OptionsArray.get(Flag))){
				return;
			}
			if(!isClicked){
				isClicked = true;
				this.setBorder(new EmptyBorder(0, 0, 0, 0));
				if(CurrentGameBoard.M.grid[r][c]==-1){
					CurrentGameBoard.revealMines();
					CurrentGameBoard.isPlayable = false;
					CurrentGameBoard.GameTimer.T.stop();
					CurrentGameMode.GameModeWinsLosses[1]++;
					findGameMode(CurrentGameMode.Name).GameModeWinsLosses[1]++;
					GameOverMenu = new Menu(300, 150, GameOverInt, 0);
					GameOverMenu.create();
					GameOverMenu.setVisible(true);
					set(Mine);
					writeGameModes();
					WinsLosses[1]++;
					writeStats();
				}
				else if(CurrentGameBoard.M.grid[r][c]==0){
					for(int x=-1; x<2 ; x++){
						for(int y = -1; y<2; y++){
							try{
								if((x!=0||y!=0)&&(!GameBoardSquares[r+x][c+y].isClicked)){
									GameBoardSquares[r+x][c+y].clickedOn();
								}
							}
							catch(ArrayIndexOutOfBoundsException e){
							}
						}
					}
				}
				else{
					set(Number);
				}
			}
			else{
				int flagCount = 0;
				for(int x=-1; x<2 ; x++){
					for(int y = -1; y<2; y++){
						try{
							if(GameBoardSquares[r+x][c+y].getText().equals(OptionsArray.get(0))){
								flagCount++;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){
						}
					}
				}
				if(flagCount==CurrentGameBoard.M.grid[r][c]){
					for(int x=-1; x<2 ; x++){
						for(int y = -1; y<2; y++){
							try{
								if(!GameBoardSquares[r+x][c+y].isClicked&&(x!=0||y!=0)){
									GameBoardSquares[r+x][c+y].clickedOn();
								}
							}
							catch(ArrayIndexOutOfBoundsException e){
							}
						}
					}
				}	
			}
			if(CurrentGameBoard.isWon()&&!CurrentGameBoard.isFinished){
				CurrentGameBoard.isFinished = true;
				CurrentGameBoard.GameTimer.T.stop();
				GameMode G = findGameMode(CurrentGameMode.Name);
				try{
					G.placeTime(CurrentGameBoard.GameTimer.s);
				}
				catch(NullPointerException npe){}
				G.GameModeWinsLosses[0]++;
				CurrentGameMode = G;
				CurrentGameBoard.isPlayable = false;
				CurrentGameBoard.revealMines();
				GameWonMenu.dispose();
				GameWonMenu = new Menu(300, 200, GameWonInt, 0);
				GameWonMenu.create();
				GameWonMenu.setCentralLocation(CurrentGameBoard);
				GameWonMenu.setVisible(true);
				
				writeGameModes();
				WinsLosses[0]++;
				writeStats();
			}
		}	
		
		public void set(int i){
			this.setForeground(ColorArray.get(i));
			if(i<2){
				this.setText(OptionsArray.get(i));
			}
			else{
				this.setText(CurrentGameBoard.M.grid[r][c]+"");
			}
		}
	}
	
	class TimerPanel extends JPanel{
		public JTextField Timer = new JTextField();
		public int s = 0;
		public int m = 0;
		public ActionListener Counter = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				m++;
				int t = s;
				s = (int)(m/100);
				if(s!=t){
					Timer.setText(createTimeString(s));
				}
			}
		};
		public Timer T = new Timer(10, Counter);
		
		public TimerPanel(){
			super();
			Timer.setFont(NumberFont);
			Timer.setBorder(new EmptyBorder(0, 0, 0, 0));
			Timer.setEditable(false);
			Timer.setText(createTimeString(s));
			this.add(Timer);
		}
	}
	
	class StatsPanel extends JPanel{
		public int[] WL;
		public String[] LabelTexts;
		public String[] StatsNumbers;
		public ArrayList<JLabel> NumberLabels = new ArrayList<JLabel>();
		
		public StatsPanel(int[] WL,  String[] LabelTexts, Font F){
			super(new GridLayout(0, 2, 0, 0));
			this.WL = WL;
			this.LabelTexts = LabelTexts;
			if(WL[0]==0){
				this.StatsNumbers = new String[]{WL[0]+WL[1]+"", WL[0]+"", WL[1]+"", (double)0+"%"};
			}
			else{
				this.StatsNumbers = new String[]{WL[0]+WL[1]+"", WL[0]+"", WL[1]+"", String.format("%.2f%s", (double)(100*(double)WL[0]/(WL[0]+WL[1])), "%")};
			}
			for(int i=0; i<4; i++){
				JLabel J = createMenuLabel(LocalStatsLabelTexts[i], F, null);
				J.setHorizontalAlignment(SwingConstants.RIGHT);
				this.add(J);
				JLabel K = createMenuLabel(":   "+StatsNumbers[i], F, null);
				K.setHorizontalAlignment(SwingConstants.LEFT);
				this.add(K);
				NumberLabels.add(K);
			}
		}
		
		public void updateLabels(int[] WL){
			this.WL = WL;
			if(WL[0]==0){
				this.StatsNumbers = new String[]{WL[0]+WL[1]+"", WL[0]+"", WL[1]+"", (double)0+"%"};
			}
			else{
				this.StatsNumbers = new String[]{WL[0]+WL[1]+"", WL[0]+"", WL[1]+"", String.format("%.2f%s", (double)(100*(double)WL[0]/(WL[0]+WL[1])), "%")};
			}
			for(int i=0; i<4; i++){
				NumberLabels.get(i).setText(":   "+StatsNumbers[i]);
			}
		}
		
	}
	
	class TimesPanel extends GridPanel{
		public ArrayList<JLabel> TimesLabels = new ArrayList<JLabel>();
		
		public TimesPanel(GameMode G, Font F, int Location){
			super(ver);
			this.add(createTitle("Best Times : ", new Font("", Font.BOLD, 20), false, null));
			try{
				boolean SelectedOne = false;
				for(int i : G.BestTimes){
					JLabel J;
					if(Location!=-1&&i==G.BestTimes[Location]&&!SelectedOne){
						SelectedOne = true;
						String S = "> ";
						S+=createTimeString(i)+" <";
						J = createMenuLabel(S, F, Color.BLUE);
					}
					else{
						String str = createTimeString(i);
						J = createMenuLabel(str, F, null);
					}
					TimesLabels.add(J);
				} 
			}
			catch(NullPointerException npe){
				for(int m=0; m<3; m++){
					String str = " - ";
					JLabel J = createMenuLabel(str, F, null);
					TimesLabels.add(J);
				}
			}
			for(JLabel J : TimesLabels){
				this.add(J);
			}
		}
		
		public void updateLabels(GameMode G){
			if(G!=null){
				int[] I = G.BestTimes;
				for(int i=0; i<3; i++){
					TimesLabels.get(i).setText(createTimeString(I[i]));
				}
			}
			else{
				for(int i=0; i<3; i++){
					TimesLabels.get(i).setText(" - ");
				}
			}
		}
	}
	
	public static void main(String[] args){
		MineSweeperGame MSG = new MineSweeperGame();
	}
}