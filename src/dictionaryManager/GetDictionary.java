package dictionaryManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.TreeSet;

public class GetDictionary {
	public static TreeSet<String> getDictFromFile(String fileName) {
		String line;
		TreeSet<String> ret = new TreeSet<>();
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

	public static TreeSet<Words> getCommonDictFromFile(String fileName) {
        String line;
        TreeSet<Words> ret = new TreeSet<>(new CompareWords());
        try {
            File file = new File(System.getProperty("user.dir") + fileName);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, Charset.defaultCharset());
            BufferedReader br = new BufferedReader(isr);
            int rank = 0;
            while ((line = br.readLine()) != null) {
                ret.add(new Words(line, rank));
                rank++;
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
