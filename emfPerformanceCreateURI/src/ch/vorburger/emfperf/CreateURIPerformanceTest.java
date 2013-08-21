package ch.vorburger.emfperf;

import java.net.URL;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class CreateURIPerformanceTest {

	@Test
	public void testCreateURIPerformance() throws Exception {
		URL url = Resources.getResource(this.getClass(), "mdfWithManyStrings.txt");
		//URL url = Resources.getResource(this.getClass(), "mdfWithoutStrings.txt");
		List<String> mdfNames = Resources.readLines(url, Charsets.UTF_8);
		long startTime = System.currentTimeMillis();
		for (String fqMdfName : mdfNames) {
			String[] splitMdfName = fqMdfName.split(":");
			String domainName = splitMdfName[0];
			String localName = splitMdfName[1];
			
			// Performance VERY BAD
			URI.createGenericURI("mdfname", fqMdfName, null);
			
			// Good Performance
			//String[] segments = new String[] { domainName, localName };
			//URI.createHierarchicalURI("mdfname", null, null, segments , null, null);
		}
		long duration = System.currentTimeMillis() - startTime;
		System.out.println(duration);
	}

}
