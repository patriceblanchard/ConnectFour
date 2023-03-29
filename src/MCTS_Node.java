
/**
 * Classe implémentant un noeud de l'arbre de recherche Monté-Carlo
 * @author patrice
 */
class MCTS_Node 
{
	// Le noeud parent d'une branche.
	private MCTS_Node parent;

	// Les enfants nécessaires lors de l'expansion de l'arbre.
	private MCTS_Node[] children;

	// Le nombre de visite d'un noeud autrement dit de simulation.
	private int visit;

	// Le nombre de victoires.
	private double victory;

	// Le traitement du jeu intégrant le plateau de jeu ainsi que 
	// les différentes fonctions de vérification des états du jeu.
	private final GameProcessing board;


	/**
	 * Constructeur de la classe MCTS_Node assignant 
	 * les attributs parent, board, visit, victory, children.
	 * 
	 * @param parent, un attribut de type MCTS_Node.
	 * @param board, le plateau de jeu.
	 */
	public MCTS_Node(MCTS_Node parent, GameProcessing board) 
	{
		this.parent = parent;

		this.board = board;

		this.visit = 0;

		this.victory = 0;

		this.children = new MCTS_Node[board.getWidth()];
	}

	/**
	 * Incrémenter l'attribut visit.
	 * 
	 * @return un entier, l'attribut visit une fois incrémenté.
	 */
	public int incrementVisit() 
	{
		return ++visit;
	}

	/**
	 * Incrémenter l'attribut victory de la valeur de result.
	 * 
	 * @param result, un entier de type double qui prendre la valeur 0 
	 * si la simulation aboutie une défaite et la valeur 1 
	 * si la simulation se termine par une victoire.
	 * 
	 * @return un entier de type double.
	 */
	public double incrementVictory(double result) 
	{
		victory += result;
		return victory;
	}

	/**
	 * Retourner la valeur de l'attribut parent.
	 * 
	 * @return parent, un attribut du type MCTS_Node.
	 */
	public MCTS_Node getParent() 
	{
		return parent;
	}

	/**
	 * Retourner la valeur de l'attribut visit.
	 * 
	 * @return visit, un entier de type int.
	 */
	public int getVisits() 
	{
		return visit;
	}

	/**
	 * Retourner le plateau de jeu dénommé board présent 
	 * dans la classe GameProcessing.
	 * 
	 * @return board, un tableau de type int[][].
	 */
	public GameProcessing getBoard() 
	{
		return board;
	}

	/**
	 * Retourner l'attribut children correspondant à un ou des enfants de l'arbre de décision.
	 * 
	 * @return children, un tableau de type MCTS_Node.
	 */
	public MCTS_Node[] getChildren() 
	{
		return children;
	}

	/**
	 * Retourner la valeur de l'attribut victory.
	 * 
	 * @return victory, un entier de type double.
	 */
	public double getVictory() 
	{
		return victory;
	}
}
