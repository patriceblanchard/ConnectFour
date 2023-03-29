import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MCTS_NodeTest 
{
	protected GameProcessing game = new GameProcessing(1,2,3);
	protected MCTS_Node op;
	protected MCTS_Node parent;

	
	@BeforeEach
	public void setUp() 
	{
		op = new MCTS_Node(parent, game);
	}

	@AfterEach
	public void tearDown() 
	{

	}

	@Test
	public void testIncrementVisit() throws Exception 
	{
		assertEquals(1, 
				op.incrementVisit());
	}
	
	@Test
	public void testGetVisits() throws Exception 
	{
		assertEquals(0, op.getVisits());
	}

	@Test
	public void testGetVictory() throws Exception 
	{
		assertEquals(0.0, op.getVictory());
	}
}
