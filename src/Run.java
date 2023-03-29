import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Classe dédiée au lancement du jeu.
 * @author patrice
 *
 */
public class Run 
{
	// S'il s'agit de un objet dédidé au traitement du jeu.
	static GameProcessing game;

	// Cette variable va intégrer le temps d'une partie.
	static double duration;

	// Celle-ci prend compte l'état du jeu précisé ci-après.
	static int gameState;

	// État du jeu : le joueur 1 gagne.
	static final int PLAYER_1_WON = 1;

	// État du jeu : le  joueur 2 gagne.
	static final int PLAYER_2_WON = 2;

	// État du jeu : égalité entre les joueurs.
	static final int TIE = 3;

	// console est une variable de type Scanner permettant d'analyser le flux System.in dans le cas de ce programme.
	static Scanner console;

	// Cet attribut correspond à la colonne choisie par un joueur ou par l'IA.
	static int moveColumn;

	// Temps maximum accordé à l'IA pour déterminer son meilleur coup.
	static long GIVEN_TIME;

	// Cet attribut servira à instancer un objet de type MCTS_AI. 
	static MCTS_AI ai;

	// Cet attribut réfère au joueur 1 dans le mode 1 vs 1.
	static Player player_1;

	// Cet atttribut correspond au joueur 2 dans le mode 1 vs 1.
	static Player player_2;

	// Cet attribut est dédié au joueur dans le mode joueur vs IA.
	static Player player;

	// Un booléen précisant que le joeur décide de joueur plus d'une partie.
	static boolean replay = false;

	// Un entier précisant le mode de jeu : 1 pour 1vs1, 2 pour 1vsIA.
	static int gameMode;

	// Un entier précisant le mode de jeu : 1 pour 1vs1, 2 pour 1vsIA dans le cas où le ou les joueurs souhaitent continuer la partie.
	static int gameModeReplay;

	// Un objet de type DataBase, le but de cette base de données est d'enregistrer les meilleurs temps de jeu de joueurs victorieux.
	static Database bestTime;



	/**
	 * Il s'agit d'une fonction de saisie proposée à un joueur pour déterminer le mode de jeu :
	 * joueur contre joueur ou joueur contre IA.
	 * 
	 * @return gameMode, un entier de valeur 1 ou 2 suivant le mode de jeu choisi par un utilisateur.
	 * @throws MessageException gère les différents cas d'exceptions lors de la saisie de l'utilisateur.
	 */
	static int input_Game_Mode() throws MessageException 
	{
		console = new Scanner(System.in);

		System.out.println("Puissance 4\n\n"
				+ "Saisir le mode de jeu désiré :\n"
				+ "1 : Joueur contre Joueur;\n"
				+ "2 : Joueur contre IA.");


		String str = console.nextLine();

		char input = str.charAt(0);

		int mode = Character.getNumericValue(input);

		if (mode != 1 
				&& mode != 2
				|| str.length() > 1)
		{
			throw new MessageException("La saisie est incorrecte!!\n\n"
					+ "Saisir soit 1 ou 2.\n");
		}
		return mode;
	}

	/**
	 * Cette fontion de saisie demande à l'utilisateur de renseigner un pseudo.
	 * 
	 * @param player, un objet de type player, relatif au joueur concerné.
	 * @return username, une chaîne de caractères.
	 */
	static String input_Username(Player player)
	{
		console = new Scanner(System.in);

		String username = null;

		System.out.printf("Joueur %s- Saisir votre pseudo :\n", 
				player.getPlayerNumber() == 0 && gameMode == 2 || gameModeReplay == 2? 
						"": player.getPlayerNumber() == 0 && gameMode == 1 || gameModeReplay == 1? 
								1 + " " : 2 + " ");

		username = console.nextLine();

		return username;
	}

	/**
	 * Cette fonction de saisie utilisateur gère le choix de la colonne par un utilisateur tout à long du jeu.
	 * 
	 * @param game, un objet de type GameProcessing.
	 * @throws MessageException gère les différents cas d'exceptions lors de la saisie de l'utilisateur.
	 */
	static void input_Player(GameProcessing game) throws MessageException
	{
		console = new Scanner(System.in);

		System.out.printf("\n%s - Choisir une colonne (entre 0 et 6) : ", 
				game.getNextTurn() == game.isPLAYER_1_TURN() ? 
						gameMode == 1 || gameModeReplay == 1 ? player_1.getUsername() : player.getUsername() 
								: player_2.getUsername());


		String str = console.nextLine();

		char input = str.charAt(0);

		moveColumn = Character.getNumericValue(input);


		if (moveColumn < 0 
				|| moveColumn > 6 
				|| str.length() > 1)
		{
			throw new MessageException("La saisie est incorrecte!!\n\n"
					+ "Saisir un chiffre entre 0 et 6.\n");
		}
	}

	/**
	 * Cette fonction de saisie gère la saisie utilisateur et le choix de l'IA.
	 * 
	 * @param game,  un objet de type GameProcessing.
	 * @param ai, un objet de type MCTS_AI.
	 */
	static void input_Player_VS_AI (GameProcessing game, MCTS_AI ai)
	{
		console = new Scanner(System.in);
		do {
			if (game.getNextTurn() == game.isPLAYER_1_TURN()) 
			{
				boolean input = false;
				while (!input)
				{
					try
					{
						input_Player(game);
						input = true;
					}
					catch (MessageException exception)
					{
						System.err.println("\n" + exception.getMessage() + "\n");
					}
				}	
			}
			else {
				System.out.print("L'IA a déterminée son meilleur coup : ");
				moveColumn = ai.getOptimalMove();
				System.out.println(moveColumn);
			}
		} while (!game.canPlace(moveColumn));
	}

	/**
	 * Proposer au joueur à la fin de la partie, si il souhaite rejouer ou quitter le jeu.
	 * 
	 * @param args, une chaîne de caractères.
	 * @throws MessageException gère les différents cas d'exceptions lors de la saisie de l'utilisateur.
	 */
	public static boolean replay_Or_Quit(String[] args) throws MessageException
	{
		console = new Scanner(System.in);
		char car = 0;
		System.out.println("\nVoulez-vous rejouer ? (O/N) :");
		String input = console.nextLine();
		car = input.charAt(0);

		if (car != 'N' 
				&& car != 'O' 
				&& car != 'o' 
				&& car != 'n'
				|| input.length() > 1)
			throw new MessageException("La saisie est incorrecte!!\n\n"
					+ "Saisir O ou o pour Oui\nN ou n pour Non.\n");

		if (car == 'O' || car == 'o')
		{
			return true;
		}
		else
			return false;
	}


	/**
	 * Cette fonction sert à connaître le mode de jeu choisi par le ou les joueurs.
	 */
	public static void game_Mode_Choice()
	{
		boolean input = false;

		while (!input)
		{
			try
			{
				if (!replay) 
				{
					gameMode = input_Game_Mode();
					input = true;
				}
				else 
				{
					gameModeReplay = input_Game_Mode();
					input = true;
				}

			}
			catch (MessageException exception)
			{
				System.err.println("\n" + exception.getMessage() + "\n");
			}
			catch (InputMismatchException e) 
			{
				System.err.print("La saisie est incorrecte!!\n\n"
						+ "Saisir un chiffre correspondant à une des options du menu.\n\n");
			}
		}
	}



	/**
	 * Cetre fonction instancie les joueurs suivant le mode de jeu choisi initialement.
	 * 
	 * @param args, un tableau de type chaîne de caractères.
	 */
	public static void players_Instanciation(String[] args)
	{
		game = new GameProcessing(PLAYER_1_WON, PLAYER_2_WON, TIE);

		switch (replay == false ? gameMode : gameModeReplay)
		{
		case 1 : // Joueur 1 vs  joueur 2
			if (!replay)
				// Instanciation de deux objets joueur.
			{
				player_1 = new Player();
				// Enregistrer le pseudo de player_1.
				player_1.setUsername(input_Username(player_1));

				player_2 = new Player();
				//Enregistrer le pseudo de player_2.
				player_2.setUsername(input_Username(player_2));
			}
			if (replay && gameMode == 2)  // le jeu continue, le mode de jeu était préalablement en joueur vs AI 
				// et le mode de jeu passe en 1 vs 1.
			{
				player_1 = new Player();
				// Enregistrer le pseudo de player_1.
				player_1.setUsername(input_Username(player_1));

				player_2 = new Player();
				//Enregistrer le pseudo de player_2.s
				player_2.setUsername(input_Username(player_2));
				// gameMode prend la valeur 1 pour éviter d'instancier chaque fois que les joueurs ont envie de rejouer.
				gameMode = gameModeReplay;
			}

			break;

		case 2 : // Joueur vs IA
			if (!replay)
				//Instanciation d'un objet joueur et d'une IA.
			{
				player = new Player();
				player.setUsername(input_Username(player));

				GIVEN_TIME = TimeUnit.SECONDS.toNanos(args.length > 0 ? Integer.parseInt(args[0]) : 2);
				ai = new MCTS_AI(game, GIVEN_TIME);
			}
			// replay = true, je jeu continue, le mode de jeu était préalablement en 1 vs 1 
			// et le mode de jeu passe en joueur vs AI.


			if (replay && gameMode == 2) 
			{
				// Le joueur décide de continuer au moins plus d'une partie, 
				// entrainant une nouvelle instanciation de game et une remise à zéro de l'arbre de décision.
				game = new GameProcessing(PLAYER_1_WON, PLAYER_2_WON, TIE);

				MCTS_Node root = new MCTS_Node(null, game.copy());

				ai.setRoot(root);
			}
			if (replay &&  gameMode == 1)
			{
				player = new Player();
				player.setUsername(input_Username(player));

				GIVEN_TIME = TimeUnit.SECONDS.toNanos(args.length > 0 ? Integer.parseInt(args[0]) : 2);
				ai = new MCTS_AI(game, GIVEN_TIME);
				// gameMode prend la valeur 2 pour éviter d'instancier chaque fois que les joueurs ont envie de rejouer.
				gameMode =  gameModeReplay;
			}

			break;

		default : 
			break;	
		}
	}

	/**
	 * Cette fonction intègre la boucle de traitement du jeu.
	 */
	public static void game_Processing()
	{
		long begin = System.nanoTime();
		while (game.currentGameState() == game.getONGOING()) 
		{

			System.out.println("\n\n" + game);

			switch (replay == false ? gameMode : gameModeReplay)
			{
			case 1 : // Joueur contre Joueur.
				boolean input2 = false;

				while (!input2)
				{
					try
					{
						input_Player(game);
						input2 = true;
					}
					catch (MessageException exception)
					{
						System.err.println("\n" + exception.getMessage() + "\n");
					}
				}
				break;

			case 2 : // Joueur contre IA.
				input_Player_VS_AI (game, ai);
				break;

			default:
				break;
			}

			game.place(moveColumn);

			if (gameMode == 2)
				ai.update(moveColumn);
		}
		long end = System.nanoTime();

		duration = (end - begin ) / 1e9;

		gameState = game.currentGameState();

		System.out.println("\n\n\n\n\n");

		System.out.println(game);

	}



	/**
	 * Cette fonction gère les différents cas de victoire selon si un joueur est victorieux et suivant le mode de jeu choisi.
	 */
	public static void victory_Case_Management()
	{
		bestTime = new Database("test.db");

		bestTime.createNewTable("test.db");



		switch (gameState)
		{
		case PLAYER_1_WON : // Victoire du joueur 1 en 1 vs 1 ou Joueur en 1 vs IA.
			System.out.printf("\nVictoire de %s en  %.1f secondes.\n", 
					gameMode == 1 ? player_1.getUsername() : player.getUsername() , duration);

			if (gameMode == 1) 
			{
				player_1.setNumberOfVictory(1);
				bestTime.insert("test.db", player_1.getUsername(), duration);
			}
			else
			{
				player.setNumberOfVictory(1);
				bestTime.insert("test.db", player.getUsername(), duration);
			}
			break;

		case 2 : // Victoire du joueur 2 en 1 vs 1 ou de l'IA en 1 vs IA.
			System.out.printf("\nVictoire de %s en %.1f secondes.\n", 
					gameMode == 1 ? player_2.getUsername() : "IA" , duration);

			if (gameMode == 1)
			{
				player_2.setNumberOfVictory(1);
				bestTime.insert("test.db", player_2.getUsername(), duration);
			}
			else
			{
				ai.setNumberOfVictory(1);
				bestTime.insert("test.db", "AI", duration);
			}
			break;
		case 3 : // Égalité.
			System.out.printf("\nÉgalité au bout de %.1f secondes.\n", duration);

			if (gameMode == 1)
			{
				player_1.setNumberOfVictory(1);
				bestTime.insert("test.db", player_1.getUsername(), duration);
				player_2.setNumberOfVictory(1);
				bestTime.insert("test.db", player_2.getUsername(), duration);

			}
			else
			{
				player.setNumberOfVictory(1);
				bestTime.insert("test.db", player.getUsername(), duration);
				ai.setNumberOfVictory(1);
				bestTime.insert("test.db", "AI", duration);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Cette fonction est dédiée à l'affichage du score.
	 */
	public static void score_display() 
	{
		System.out.println("\nNombres de victoires :\n");
		switch (gameMode)
		{
		case 1:
			System.out.println(player_1 + " | " + player_2);
			break;
		case 2:
			System.out.println(player + " | IA" + ai);
			break;
		default:
			break;
		}
		System.out.println("\nMeilleurs temps de jeu :\n");
		bestTime.selectFirstThree("test.db");
	}


	/**
	 * Cette fonction lance le jeu.
	 * 
	 * @param args une chaîne de caractère.
	 */
	public static void main(String[] args) 
	{
		game_Mode_Choice();

		players_Instanciation(args);

		game_Processing();

		victory_Case_Management();

		score_display();


		boolean input = false;

		while (!input)
		{
			try
			{
				replay = replay_Or_Quit(args);

				input = true;

				while (replay)
				{
					game_Mode_Choice();

					players_Instanciation(args);

					game_Processing();

					victory_Case_Management();

					score_display();

					input = false;

					while (!input)
					{
						try
						{
							replay = replay_Or_Quit(args);

							input = true;

						}
						catch (MessageException exception)
						{
							System.err.println("\n" + exception.getMessage() + "\n");
						}
						catch (InputMismatchException e) 
						{
							System.err.print("La saisie est incorrecte!!\n\n"
									+ "Saisir un chiffre correspondant à une des options du menu.\n\n");
						}
					}
				}
			}
			catch (MessageException exception)
			{
				System.err.println("\n" + exception.getMessage() + "\n");
			}
			catch (InputMismatchException e) 
			{
				System.err.print("La saisie est incorrecte!!\n\n"
						+ "Saisir un chiffre correspondant à une des options du menu.\n\n");
			}
		}
	}
}
