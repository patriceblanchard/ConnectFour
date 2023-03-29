/**
 * Classe dédiée au traitement du jeu.
 * @author patrice
 */
public class GameProcessing
{
	// Attributs en lien avec le contenu du plateau de jeu :

	// Emplacement vide.
	private final int EMPTY_SLOT = 0;

	// Emplacement pris par le joueur 1.
	private final int PLAYER_1_SLOT = 1;

	// Emplacement pris par le joueur 2.
	private final int PLAYER_2_SLOT = 2;

	// Gestion du tour de jeu.
	private final boolean PLAYER_1_TURN = true;

	// Le prochain tour de jeu.
	private boolean nextTurn;

	// État du jeu : jeu en cours.
	private final int ONGOING = 0;

	// État du jeu : le joueur 1 gagne.
	private final int PLAYER_1_WON;

	// État du jeu : le joueur 2 gagne.
	private final int PLAYER_2_WON;

	// État du jeu : égalité.
	private final int TIE;

	// Le plateau de jeu.
	private int[][] board;

	// La hauteur du plateau de jeu.
	private int boardHeight;

	// La largeur du plateau de jeu.
	private int boardWidth;

	/**
	 * Constructeur de la classe Gameprocessing
	 * assignant les attributs boardHeight, boardWidth, board, nextTurn, PLAYER_1_WON, PLAYER_2_WON, TIE.
	 * 
	 * @param PLAYER_1_WON, un entier précisant que si le joueur 1 est victorieux.
	 * @param PLAYER_2_WON, un entier spécifie la victoire du joueur 2.
	 * @param TIE, une entier indiquant une égalite.
	 */
	public GameProcessing(int PLAYER_1_WON, int PLAYER_2_WON, int TIE) 
	{
		this.boardWidth = 7;

		this.boardHeight = 7;

		this.board = new int[boardHeight][boardWidth];

		this.nextTurn = PLAYER_1_TURN;

		this.PLAYER_1_WON = PLAYER_1_WON; // 1

		this.PLAYER_2_WON = PLAYER_2_WON; // 2

		this.TIE = TIE; // 3
	}

	/**
	 * Constructeur de la classe GameProcessing demandant en paramètres contents et nextTurn
	 * assignant les attributs boardHeight, boardWidth, board, nextTurn, PLAYER_1_WON, PLAYER_2_WON, TIE.
	 * 
	 * @param contents, un tableau de type int[][].
	 * @param nextTurn, un booléen identifiant le tour du jeu.
	 */
	public GameProcessing(int[][] contents, boolean nextTurn) 
	{
		this.boardWidth = contents[0].length;

		this.boardHeight = contents.length;

		this.setBoard(new int[boardHeight][boardWidth]);

		this.setBoard(loadContents(contents));

		this.nextTurn = nextTurn;

		this.PLAYER_1_WON = 1;

		this.PLAYER_2_WON = 2;

		this.TIE = 3;
	}

	/**
	 * Cette classe nécessaire au constructeur du MCTS, elle assigne 
	 * dans le plateau de jeu board les valeurs de contents.
	 * 
	 * @param contents, une variable de type int[][].
	 * @return board, un tableau de type int[][].
	 */
	public int[][] loadContents(int[][] contents) 
	{
		for (int i = 0; i < boardHeight; i++)
			for (int j = 0; j < boardWidth; j++)
				getBoard()[i][j] = contents[i][j];
		
		return getBoard();
	}

	/**
	 * Cette méthode vérifie si la variable column proposée en argument 
	 * n'est pas supérieur à la largeur du plateau de jeu et
	 * que la colonne n'est pas remplie de jeton.
	 * 
	 * @param column, un entier déterminant une colonne du plateau de jeu.
	 * @return true si la colonne est n'est pas remplie sinon false.
	 */
	public boolean canPlace(int column) 
	{
		return column >= 0 && column < boardWidth && getBoard()[0][column] == EMPTY_SLOT;
	}

	/**
	 * Cette méthode place un jeton d'un joueur et gère le changement du tour du jeu.
	 * 
	 * @param column, un entier déterminant une colonne du plateau de jeu.
	 * @return true si un jeton a été placé sinon false.
	 */
	public boolean place(int column) 
	{
		int disk = (nextTurn == PLAYER_1_TURN) ? PLAYER_1_SLOT : PLAYER_2_SLOT;

		if (!canPlace(column))
			return false;

		int diskHeight = boardHeight - 1;

		while (getBoard()[diskHeight][column] != EMPTY_SLOT)
			diskHeight--;

		getBoard()[diskHeight][column] = disk;

		nextTurn = !nextTurn;

		return true;
	}

	/**
	 * Cette classe est nécessaire à la classe GetNextState et au traitement du MCTS, 
	 * elle instancie un object de type GameProcessing à l'aide du constructeur 
	 * GameProcessing(int[][] contents, boolean nextTurn).
	 * 
	 * @return un object de type GameProcessing.
	 */
	public GameProcessing copy() 
	{
		return new GameProcessing(getBoard(), this.nextTurn);
	}

	/**
	 * Cette méthode est nécessaire au MCTS, elle effectue une copie de l'objet 
	 * Gameprocessing puis appelle la méthode place avec l'argument column.
	 * 
	 * @param column, un entier déterminant une colonne du plateau de jeu.
	 * @return un objet de type GameProcessing.
	 */
	public GameProcessing getNextState(int column) 
	{
		GameProcessing next = this.copy();

		next.place(column);

		return next;
	}

	/**
	 * Cette classe spécifie si un joueur a gagné la partie.
	 * 
	 * @param playerSlot, un entier déterminant le joueur 1 ou 2.
	 * @return un booléen true si le joueur (précisé par la valeur 
	 * de playerSlot proposée en argument) a gagné sinon false.
	 */
	public boolean didPlayerWin(int playerSlot) 
	{
		// contrôle horiozontal
		int height = board.length;
		int width = board[0].length;

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width - 3; j++)
				for(int k = j; k < j + 4 && getBoard()[i][k] == playerSlot; k++)
					if(k == j + 3)
						return true;
		
		// Contrôle vertical.
		for(int i = 0; i < height - 3; i++)
			for(int j = 0; j < width; j++)
				for(int k = i; k < i + 4 && getBoard()[k][j] == playerSlot; k++)
					if(k == i + 3)
						return true;
		
		// Contrôle diagonal en bas à droite.
		for(int i = 0; i < height - 3; i++)
			for(int j = 0; j < width - 3; j++)
				for(int k = 0; k < 4 && getBoard()[i+k][j+k] == playerSlot; k++)
					if(k == 3)
						return true;
		
		// Contrôle diagonal vers le bas.
		for(int i = 0; i < height - 3; i++)
			for(int j = 3; j < width; j++)
				for(int k = 0; k < 4 && getBoard()[i+k][j-k] == playerSlot; k++)
					if(k == 3)
						return true;
		
		return false;
	}

	/**
	 * Cette méthode vérifie si le plateau est rempli de jetons ou non.
	 * 
	 * @return un booléen, true si le plateau est rempli sinon false.
	 */
	public boolean isFull() 
	{
		for (int i = 0; i < getBoard().length; i++)
			for (int j = 0; j < getBoard()[i].length; j++)
				if (getBoard()[i][j] == EMPTY_SLOT)
					return false;
		
		return true;
	}

	/**
	 * Cette méthode permet de savoir si un joueur a gagné la partie 
	 * ou si celle-ci s'est terminée par une égalité 
	 * ou si la partie est encore en cours.
	 * 
	 * @return un entier, 0 (partie en cours), 1 (victoire du joueur 1), 2 (victoire du joueur 2), 3 (égalité).
	 */
	public int currentGameState() 
	{
		return this.didPlayerWin(PLAYER_1_SLOT) ? PLAYER_1_WON
				: this.didPlayerWin(PLAYER_2_SLOT) ? PLAYER_2_WON
						: this.isFull() ? TIE
								: ONGOING;
	}

	/**
	 * Cette méthode toString renvoie le tableau de jeu sous forme d'une chaîne de caractères.
	 * 
	 * @return une variable de type String.
	 */
	@Override
	public String toString() 
	{
		String result = "|-";
		for (int j = 0; j < boardWidth; j++) 
		{
			result += "--|-";
		}
		result = result.substring(0, result.length() - 1) + "\n";
		for (int i = 0; i < boardHeight; i++) {
			result += "| ";
			for (int j = 0; j < boardWidth; j++) 
			{
				result += (getBoard()[i][j] == EMPTY_SLOT ? " " : (getBoard()[i][j] == 1 ? "X" : "O"))+" | ";
			}
			result = result.substring(0, result.length() - 1);
			result += "\n|-";
			for (int j = 0; j < boardWidth; j++) 
			{
				result += "--|-";
			}
			result = result.substring(0, result.length() - 1);
			result += "\n";
		}
		result += "  0   1   2   3   4   5   6  ";
		return result.substring(0, result.length() - 1);
	}

	/**
	 * Cette méthode retourne la valeur de l'attribut nextTurn.
	 * 
	 * @return nextTurn, un booléen désignant le tour de jeu d'un joueur.
	 */

	public boolean getNextTurn() 
	{
		return nextTurn;
	}

	/**
	 * Cette méthode retourne la valeur de l'attribut boardWidth.
	 * 
	 * @return boardWidth, un entier correspondant à la largeur du plateau de jeu.
	 */
	public int getWidth() 
	{
		return boardWidth;
	}

	/**
	 * Cette méthode retourne la valeur de PLAYER_1_TURN.
	 * 
	 * @return PLAYER_1_TURN, un booléen de valeur true si c'est le tour du joueur 1.
	 */
	public boolean isPLAYER_1_TURN() 
	{
		return PLAYER_1_TURN;
	}

	/**
	 * Cette méthode retourne la valeur de ONGOING.
	 * 
	 * @return ONGOING, un entier de valeur 0, précisant que le jeu est en cours et non achevé.
	 */
	public int getONGOING() 
	{
		return ONGOING;
	}
	

	/**
	 * Cette méthode retourne la valeur de l'attribut board.
	 * 
	 * @return board, un tableau de type[][] désignant le plateau de jeu.
	 */
	
	public int[][] getBoard() 
	{
		return board;
	}

	/**
	 * Cette méthode assigne une nouvelle valeur à l'attribut board.
	 * 
	 * @param board, un tableau de type int[][].
	 */
	public void setBoard(int[][] board) 
	{
		this.board = board;
	}
}