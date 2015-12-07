import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class ArffGenerator {

	/*Paths and names of files*/
	String userDir = System.getProperty("user.dir");
	String[] paths = new String[] { "/part1/neg/", "/part1/pos/","/part2/neg/", "/part2/pos/" };
	String listOfWords = "ListOfWords.txt";


	/*Number of words that are going to appear on the list of attributes. You can change this value. 
	 * Make sure you also change this value on the AttributeGenerator class
	 */
	int numberOfWords = 600;

	public ArffGenerator(HashMap<String, Integer> wordMap) {

		/*Tries to open the list of words*/
		try (BufferedReader br = new BufferedReader(new FileReader(userDir + '/' + listOfWords))) {

			/*Creates a String[] and a int[]. They are used as auxiliar variables to write the arff file*/
			String[] attributes = new String[numberOfWords];
			int atValues[] = new int[numberOfWords];
			for(int k=0;k<numberOfWords; k++){
				atValues[k] = 0;
			}

			/*Real size of the attributes array*/
			int atSize = 0;

			/*Reads the list of words, line by line*/
			String sCurrentLine1;
			while ((sCurrentLine1 = br.readLine()) != null) {

				/*Breaks the line into various words*/
				String[] words = sCurrentLine1.split(" |<br />|/|-|_");

				/*For each word, format it and check if that word is one of the attributes that are in the listOfWords file*/
				for (int i = 0; i < words.length; i++) {
					words[i] = words[i].replaceAll("[^A-Za-z0-9]","");
					words[i] = words[i].toLowerCase();

					if(!words[i].equals("")){
						attributes[atSize++] = words[i];
					}

				}
			}


			/*Creates the arff file, and initialize it with headers*/
			PrintWriter writer = new PrintWriter("MovieDataSet.arff", "UTF-8");
			writer.println("@relation MovieDataSet");
			for(int a=0 ; a < atSize; a++){
				writer.println("@attribute " + "'" + attributes[a] + "'" + " real");
			}
			writer.println("@attribute 'class' {0,1}");
			writer.println("@data");


			/*Counts the number of files*/
			int count = 0;

			/*Iterates over the four different paths*/
			for (int u = 0; u < 4; u++) {

				/*Iterates over the 12500 files in each folder*/
				for (int y = 0; y < 12500; y++) {

					/*Iterates over the names of the review files*/
					for (int j = 1; j <= 10; j++) {
						try (BufferedReader br2 = new BufferedReader(new FileReader(userDir + paths[u] + y + '_' + j + ".txt"))) {

							/*Increases the count number, and shows the percentage on the screen*/
							count++;
							if (count % 400 == 0) {
								System.out.println("Creating arff: " + (int) ((count / 50000.) * 100) + '%');
							}

							/*Resets the int[] values to zero*/
							for(int k=0;k<numberOfWords; k++){
								atValues[k] = 0;
							}

							/*Reads line by line*/
							String sCurrentLine;
							while ((sCurrentLine = br2.readLine()) != null) {
								/*Splits a line into many words*/
								String[] words = sCurrentLine.split(" |<br />|/|-|_");

								/*For each word, format it and create the int[]. This array is going to be used to write the arff file*/
								for (int i = 0; i < words.length; i++) {
									words[i] = words[i].replaceAll("[^A-Za-z0-9]","");
									words[i] = words[i].toLowerCase();

									for(int a=0 ; a < atSize; a++){
										if(attributes[a].equals(words[i])){
											
											/*Determines if the attribute is binary or not*/
											//atValues[a] = 1;
											atValues[a] = atValues[a]+1;
										}
									}
								}
							}

							/*Writes the int[] into the arff file*/
							for(int r=0; r<atSize;r++){
								writer.print(atValues[r] + ",");
							}


							 /*Defines the class of each file. They can either be positive(1), or negative(0) reviews.
							 * This is decided by which folder they are into
							 */
							if(u%2==0){
								writer.println(0);
							}
							else{
								writer.println(1);
							}

						} catch (IOException e) {
							/*File does not exist in the folder. Skip it.*/
						}
					}
				}
			}

			writer.close();

		} catch (IOException e) {
			System.out.println("The file ListOfWords.txt does not exist");
			return ;
		}
	}

}
