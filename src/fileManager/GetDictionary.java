package fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.TreeSet;

public class GetDictionary {
	public static TreeSet<String> read(String fileName) {
		String line;
		TreeSet<String> ret = new TreeSet<String>();
		try {
			File file = new File(System.getProperty("user.dir") + fileName);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, Charset.defaultCharset());
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				ret.add(line);
			}
			br.close(); 
		} catch (IOException e) {
			System.out.println("Error: File not found!");
			e.printStackTrace();
			System.exit(0);
		}
		return ret;
	}
}
