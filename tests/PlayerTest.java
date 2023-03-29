import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest 
{
protected Player op;
	
	@BeforeEach
	void setUp() throws Exception 
	{
		op = new Player();
	}

	@AfterEach
	void tearDown() throws Exception
	{

	}

	@Test
	public void TestGetNumberOfVictory()
	{
		assertEquals(0, op.getNumberOfVictory());
	}
	
	@Test
	public void TestGetPlayerNumber()
	{
		assertEquals(0, op.getPlayerNumber());
	}
}
