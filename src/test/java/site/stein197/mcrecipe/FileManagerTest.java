package site.stein197.mcrecipe;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;

import org.junit.Test;

public class FileManagerTest {

	@Test(expected = IllegalArgumentException.class)
	public void passNull_toConstructor_throwsException() throws URISyntaxException {
		new FileManager(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void passEmptyString_toConstructor_throwsException() throws URISyntaxException {
		new FileManager("");
	}

	@Test
	public void loadUnexistingResource_returnsNull() {
		assertNull(FileManager.getResource("/unexisting"));
	}

	@Test
	public void loadExistingResource_returnsObject() {
		assertNotNull(FileManager.getResource("/images/grass_side.png"));
	}
}
// TODO loadFile() test may be
