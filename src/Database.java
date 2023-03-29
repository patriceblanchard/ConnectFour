import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Source : https://www.sqlitetutorial.net/sqlite-java/

/**
 * Cette classe développe une base de données SQL.
 * @author patrice
 */
public class Database 
{
	// Cet attribut est en fait une chaîne de caractères correspondant au nom du fichier de la base de données.
	private String fileName;

	// Il s'agit d'une chaîne de caractères dédiée à l'emplacement de la base de données.
	private String url;

	/**
	 * Constructeur de la classe Database assignant les attributs filename et url.
	 * 
	 * @param fileName, le nom du fichier correspondant à la base de données.
	 */
	public Database(String fileName)
	{
		this.fileName = fileName;

		this.url = "jdbc:sqlite:" + fileName;
	}

	/**
	 * Cette méthode sert à créer une table de données.
	 * 
	 * @param fileName, une chaîne de caractères correspondant au nom du fichier de la base de données.
	 */
	public void createNewTable(String fileName)
	{
		// Instruction SQL pour la création d'une nouvelle table.
		String sql = "CREATE TABLE IF NOT EXISTS bestTime (\n"
				+ "	id integer PRIMARY KEY,\n"
				+ "	username text NOT NULL,\n"
				+ "	duration real\n"
				+ ");";

		// En se connectant à la base de données SQLite, 
		// celle-ci est crée automatiquement si la base est inexistante.
		try (Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement()) 
		{
			// Création d'une nouvelle table.
			stmt.execute(sql);
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Cette méthode insère des données username et duration à partir d'une requête 
	 * INSERT INTO dans une table fileName proposée en argument 
	 * 
	 * @param fileName, une chaîne de caractères correspondant au nom du fichier de la base de données.
	 * @param username, une chaîne de caractères dédiée au nom du fichier de la base de données.
	 * @param duration, un réel de type double ou sera assigné un temps de jeu.
	 */
	public void insert(String fileName, String username, double duration) 
	{
		// Préparation de la requête INSERT.
		String sql = "INSERT INTO bestTime(username,duration) VALUES(?,?)";

		// Connection à la base de donnée
		try (Connection conn = DriverManager.getConnection(url);
				// Créer une instance PreparedStatement à partir de la connection.
				PreparedStatement pstmt = conn.prepareStatement(sql)) 
		{
			// Définir les valeurs pour chaque espace réservé ? de la requête 
			// à l'aide des méthodes setString et setDouble de preparedStatement.
			pstmt.setString(1, username);
			pstmt.setDouble(2, duration);
			// Exécution de l'instruction.
			pstmt.executeUpdate();
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Cette méthode permet d'interroger une table dont le nom du fichier fileName est précisé en paramètre.
	 * Elle affichera les trois premières lignes de cette table ordonnée de manière croissante suivant 
	 * la valeur de la variable duration. 
	 * 
	 * @param fileName, une chaîne de caractère correspondant au nom du fichier de la base de données.
	 */
	public void selectFirstThree(String fileName)
	{	
		// La requête sous la forme d'une chaîne de caractère.
		String sql = "SELECT username, duration FROM bestTime ORDER BY duration LIMIT 3";

		// Connection à la base de données.
		try (Connection conn = DriverManager.getConnection(url);
				// Création d'une instance de type Statement à partir de la connection.
				Statement stmt = conn.createStatement();
				// Création d'un instance ResultSet en appelant la méthode executeQuery à partir d'une requête.
				ResultSet rs = stmt.executeQuery(sql))
		{

			// Une boucle à travers le jeu de résultats.
			while (rs.next()) 
			{
				System.out.println(rs.getString("username") + " : " +
						rs.getDouble("duration"));
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}
}

