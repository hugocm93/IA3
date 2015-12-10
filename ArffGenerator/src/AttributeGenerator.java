import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AttributeGenerator {


	/*Paths to files*/
	String userDir = System.getProperty("user.dir");
	String[] paths = new String[] { "/part1/neg/", "/part1/pos/",
			"/part2/neg/", "/part2/pos/" };

	/*HashMap that holds the words and the number of times that they appear*/
	HashMap<String, Integer> wordMap = new HashMap<String, Integer>();


	 /*Number of words that are going to appear on the list of attributes. You can change this value. 
	 * Make sure you also change this value on the ArffGenerator class
	 */
	int numberOfWords = 600;

	public AttributeGenerator() {
		super();

		/*Counts the number of reviews read*/
		int count = 0;
		
		/*Variable to iterate over the four different paths*/
		for (int u = 0; u < 4; u++) {
			
			/*Variable to iterate over all the files in a folder. Each folder has 12500 files*/
			for (int y = 0; y < 12500; y++) {
				
				/*Iterates over the names of the review files. */
				for (int j = 1; j <= 10; j++) {
					
					/*Reads a file*/
					try (BufferedReader br = new BufferedReader(new FileReader(
							userDir + paths[u] + y + '_' + j + ".txt"))) {

						/*Increase counter, and print the percentage on the screen*/
						count++;
						if (count % 400 == 0) {
							System.out.println("Processing: "
									+ (int) ((count / 50000.) * 100) + '%');
						}

						
						/*Read line per line*/
						String sCurrentLine;
						while ((sCurrentLine = br.readLine()) != null) {
							
							/*Split line into various words*/
							String[] words = sCurrentLine
									.split(" |<br />|/|-|_");

							
							/*For each word*/
							for (int i = 0; i < words.length; i++) {

								
								/*Format it*/
								words[i] = words[i].replaceAll("[^A-Za-z0-9]","");
								words[i] = words[i].toLowerCase();

								/*Check if it was mapped or not. If yes, its frequency is increased*/
								Integer n = wordMap.get(words[i]);
								if (n != null) {
									wordMap.put(words[i], n.intValue() + 1);
								} else {
									wordMap.put(words[i], 1);
								}

							}
						}

					} catch (IOException e) {
						/*File does not exist in the folder. Skip it, and try the next file*/
					}
				}
			}
		}

		
		/*Sorts the map. Increasing order*/
		Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		};
		HashMap<String, Integer> sortedMap = wordMap
				.entrySet()
				.stream()
				.sorted(valueComparator)
				.collect(
						Collectors.toMap(Entry::getKey, Entry::getValue, (e1,
								e2) -> e1, LinkedHashMap::new));

		
		/*Variable used to get the last X  attributes, where X equals numberOfWords*/
		int tam = sortedMap.size() - numberOfWords;
		
		
		/*Create the file ListOfWords.txt*/
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("ListOfWords.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		
		/*Writes the attributes to the ListOfWords.txt file. Those are the most frequent words*/
		int counter = 0;
		for (Entry<String, Integer> entry : sortedMap.entrySet()) {
			String key = entry.getKey();
			
			if(counter>=tam){
				writer.println(key);
			}
			
			counter++;
		}

		/*Finishes writing arff file*/
		writer.close();
	}

	public HashMap<String, Integer> getWordMap() {
		return wordMap;
	}

}
