import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameProcessingTest 
{
	protected GameProcessing op;

	@BeforeEach
	void setUp() throws Exception 
	{
		op = new GameProcessing(1, 2, 3);
	}

	@AfterEach
	void tearDown() throws Exception 
	{
		
	}

	@Test
	public void testCanPlace() throws Exception 
	{
		for (int i = 0; i < 7; i++)
		assertEquals(true, 
				op.canPlace(i));
		
		for (int i = 7; i < 10; i++)
			assertEquals(false, 
					op.canPlace(i));
		
		for (int i = -5; i < 0; i++)
			assertEquals(false, 
					op.canPlace(i));
	}
	
	@Test
	public void testplace() throws Exception 
	{
		for (int i = 0; i < 7; i++)
		assertEquals(true, 
				op.place(0));
		
		assertEquals(false, 
				op.place(0));
	}
	
	@Test
	public void isFull() throws Exception
	{
		assertEquals(false, op.isFull());
	}

}
