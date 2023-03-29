
/**
 * Classe dédiée au attributs et comportements d'un joueur.
 * @author patrice
 *
 */
public class Player
{
	// Chaque joueur dispose d'un disque avec un numéro différent pour les distinguer. 
	private final int playerNumber;

	// Cet attribut intègre le pseudo du joueur.
	private String username;

	// Attribut d'incrémentation de la valeur diskNumber au moment de l'instanciation d'un objet Player.
	private static int nextNumber = 0;

	// Cet attribut conserve le nombre de victoire d'un joueur.
	private int numberOfVictory;

	/**
	 * Constructeur de la classe Player assignant les attributs diskNumber et numberOfVictory.
	 */
	public Player()
	{
		this.playerNumber = nextNumber;

		this.nextNumber++;

		this.numberOfVictory = 0;

		this.username = "";
	}

	/**
	 * Cette méthode retourne la valeur de la variable numberOfVictory.
	 * 
	 * @return un entier, la valeur de numberOfVictory.
	 */
	public int getNumberOfVictory() 
	{
		return numberOfVictory;
	}

	/**
	 * Cette méthode assigne une nouvelle valeur à l'attribut numberOfVictory.
	 * 
	 * @param numberOfVictory, un entier correspondant à la valeur d'incrémentation 
	 * à ajouter à la valeur de numberOfVictory.
	 */
	public void setNumberOfVictory(int numberOfVictory) 
	{
		this.numberOfVictory += numberOfVictory;
	}

	/**
	 * Cette classe retourne le nom du joueur.
	 * 
	 * @return une chaîne de caractères.
	 */
	public String getUsername() 
	{
		return username;
	}

	/**
	 * Cette classe assigne une nouvelle valeur à l'attribut username.
	 * 
	 * @param username, une chaîne de caractères.
	 */
	public void setUsername(String username) 
	{
		this.username = username;
	}

	/**
	 * Cette retourne la valeur de l'attribut playerNumber;
	 * 
	 * @return playerNumber, un entier.
	 */
	public int getPlayerNumber() 
	{
		return playerNumber;
	}

	/**
	 * Cette classe retourne une chaîne de caractère affichant les valeurs de l'ensemble des attributs de la classe.
	 */
	@Override
	public String toString() 
	{
		return username + " - " + numberOfVictory;
	}
}
