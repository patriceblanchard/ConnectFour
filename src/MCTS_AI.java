// Monte Carlo Tree Search AI for Connect 4

import java.util.ArrayList;

/**
 * 
 * @author patrice
 * 
 * La recherche arborescente Monté-Carlo ou Monte Carlo tree search (MCTS) est un algorithme 
 * de recherche heuristique utilisé dans le cadre de la prise de décision.
 * 
 * Cet algorithme explore un arbre de possibilités suivant les quatre phases suivantes :
 * 
 * - la sélection;
 * - l'expansion;
 * - la simulation; 
 * - La rétropropagation (BackPropagation).
 * 
 * Ainsi MCTS conserve en mémoire un arbre qui correspond aux noeuds déjà explorés de l'arbre des possibles.
 * Une feuille de cet arbre est soit une configuration finale (gagné ou match nul), soit un noeud dont tous les enfants n'ont pas été explorés.
 * Dans chaque noeud le nombre de simulations gagnantes et le nombre total de simulations est stockés.
 */
public class MCTS_AI extends Player
{
	// root est un attribut de type MCTS_Node assimilable à l'état initiale du jeu autrement dit à la racine de l'arbre de décision.
	private MCTS_Node root; 

	// width est la large du plateau de jeu.
	private final int width;

	// EXPLORATION_PARAMETER est un paramètre d'exploration nécessaire dans le cadre de la sélection.
	private static final double EXPLORATION_PARAMETER = Math.sqrt(2);

	// Temps de traitement maximal de l'algorithme MCTS.
	private final long givenTime;

	/**
	 * Constructeur de la classe MCTS_AI assignant les attributs width, givenTime, root.
	 * 
	 * @param board, un tableau de type int[][] correspondant au plateau de jeu;
	 * @param givenTime temps de recherche maximum d'une solution.
	 */
	public MCTS_AI(GameProcessing board, long givenTime) 
	{

		this.width = board.getWidth();

		this.givenTime = givenTime;

		root = new MCTS_Node(null, board.copy());
	}

	/**
	 * Cette méthode met à jour l'arbre de décision suivant la variable move correspondant 
	 * au meilleur coup trouvé par l'IA ou la colonne choisie par le joueur.
	 * 
	 * @param move, un entier.
	 */
	public void update(int move) 
	{
		root = root.getChildren()[move] != null ? 
				root.getChildren()[move] : 
					new MCTS_Node(null, root.getBoard().getNextState(move));
	}

	/**
	 * Cette méthode recherche le meilleur coup dans l'arbre de recherche Monté-Carlo.
	 * 
	 * @return maxIndex, un entier correspondant à la colonne choisie par l'IA.
	 */
	public int getOptimalMove() 
	{
		for (long stop = System.nanoTime()+givenTime; stop > System.nanoTime();) 
		{
			MCTS_Node selectedNode = select();

			if (selectedNode == null)
				continue;

			MCTS_Node expandedNode = expand(selectedNode);
			double result = simulate(expandedNode);
			backpropagate(expandedNode, result);
		}

		int maxIndex = -1;

		for (int i = 0; i < width; i++) 
		{
			if (root.getChildren()[i] != null) {
				if (maxIndex == -1 || root.getChildren()[i].getVisits() > root.getChildren()[maxIndex].getVisits())
					maxIndex = i;
				// System.out.printf("\nlocation%d: p1wins: %f/%d = %f", i, root.children[i].Win, root.children[i].visits, root.children[i].Win/root.children[i].visits);
			}
		}
		// System.out.println();
		return maxIndex;
	}

	/**
	 * Appel de la fonction select().
	 * 
	 * @return select(root), la méthode select avec comme paramètre l'attribut root de type MCTS_Node.
	 */

	private MCTS_Node select() 
	{
		return select(root);
	}

	/**
	 * À  partir de la racine, une sélection des enfants est opérée jusqu'à atteindre une feuille
	 * où un compromis est établi entre exploitation et exploration. L'exploitation consiste à 
	 * trouver un enfant prometteur tandis que l'exploration correspond au fait de visiter 
	 * d'autres enfants moins prometteur mais avec un potentiel.
	 * 
	 * @param parent , une variable de type MCTS_Node.
	 * @return une variable parent de type MCS_Node.
	 */
	private MCTS_Node select(MCTS_Node parent) 
	{
		// Si le parent a au moins un enfant sans statistiques, sélectionner le parent.

		for (int i = 0; i < width; i++) 
		{
			if (parent.getChildren()[i] == null && parent.getBoard().canPlace(i)) 
			{
				return parent;
			}
		}

		//Si tous les enfants ont des statistiques, utilisez la formule "Upper Confidence Bound 1 applied to Trees" 
		//pour sélectionner le prochain nœud à visiter.

		double maxSelectionVal = -1;
		int maxIndex = -1;

		for (int i = 0; i < width; i++) 
		{
			if (!parent.getBoard().canPlace(i))
				continue;

			MCTS_Node currentChild = parent.getChildren()[i];

			double wins = parent.getBoard().getNextTurn() == true 
					? currentChild.getVictory() 
							: (currentChild.getVisits()-currentChild.getVictory());

			double selectionVal = wins/currentChild.getVisits() 
					+ EXPLORATION_PARAMETER*Math.sqrt(Math.log(parent.getVisits())/currentChild.getVisits());// (UCT sera expliqué dans un rapport dédié à ce jeu)
			if (selectionVal > maxSelectionVal) 
			{
				maxSelectionVal = selectionVal;
				maxIndex = i;
			}
		}

		if (maxIndex == -1)
			return null;

		return select(parent.getChildren()[maxIndex]);
	}

	/**
	 * L'expansion est la phase d'extension d'une branche si la feuille n'est pas finale. 
	 * Un ou plusieurs enfants sont crées pour au final en choisir qu'un.
	 * 
	 * @param selectedNode, une variable selectedNode correspondant à un noeux choisi pour l'expansion.
	 * @return une variable de type MCTS_Node
	 */
	private MCTS_Node expand(MCTS_Node selectedNode) 
	{
		// Obtenir les noeuds enfants non visités.
		ArrayList<Integer> unvisitedChildrenIndices = new ArrayList<Integer>(width);

		for (int i = 0; i < width; i++) 
		{
			if (selectedNode.getChildren()[i] == null && selectedNode.getBoard().canPlace(i)) 
			{
				unvisitedChildrenIndices.add(i);
			}
		}

		// Sélectionner aléatoirement un enfant non visité et créer un nœud pour lui.
		int selectedIndex = unvisitedChildrenIndices.get((int)(Math.random()*unvisitedChildrenIndices.size()));

		selectedNode.getChildren()[selectedIndex] = new MCTS_Node(selectedNode, selectedNode.getBoard().getNextState(selectedIndex));
		return selectedNode.getChildren()[selectedIndex];
	} 

	/**
	 * La simulation est effectuée au hasard sur l'enfant choisi après l'expansion, jusqu'à atteindre un résultat final.
	 * 
	 * @param expandedNode, un noeud ayant reçu un traitement d'expansion.
	 * @return un entier de type double.
	 */
	private double simulate(MCTS_Node expandedNode) 
	{
		GameProcessing simulationBoard = expandedNode.getBoard().copy();
		while (simulationBoard.currentGameState() == 0) 
		{
			simulationBoard.place((int)(Math.random()*width));
		}
		// System.out.println(simulationBoard);

		switch (simulationBoard.currentGameState()) 
		{
		case 1:
			return 1;
		case 2:
			return 0;
		default:
			return 0.5;
		}
	}

	/**
	 * Cette étape consiste à remonter le résultat de la partie de la feuille vers la racine 
	 * et de mettre à jour les informations sur l'ensemble d'une branche. En effet, chaque noeud d'une branche stocke 
	 * le nombre de simulations gagnantes et le nombre total de simulations.
	 * 
	 * @param expandedNode, un noeud ayant reçu un traitement d'expansion.
	 * @param simulationResult, un entier de type double, valeur de retour de la méthode simulate.
	 */
	private void backpropagate(MCTS_Node expandedNode, double simulationResult) 
	{
		MCTS_Node currentNode = expandedNode;

		while (currentNode != null) 
		{
			currentNode.incrementVisit();

			currentNode.incrementVictory(simulationResult);

			currentNode = currentNode.getParent();
		}
	}

	/**
	 * Cette méthode prend une variable de type MCTS_Node en paramètre et l'enregistre dans l'attribut root.
	 * 
	 * @param root, une variable de type MCTS_Node.
	 */
	public void setRoot(MCTS_Node root) 
	{
		this.root = root;
	}
}