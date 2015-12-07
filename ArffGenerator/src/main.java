import java.io.DataInputStream;
import java.io.IOException;

public class main {

	public static void main(String[] args) {
		/* Reader - Reads a character from the console. Makes sure the user read the information that was printed*/
		DataInputStream in=new DataInputStream(System.in);


		/* The attribute generator class generates a map of strings and integers. The string corresponds to a word, and the integer corresponds
		 * to the number of times that the word appears in all the files. It also creates the listOfWords.txt file. This file needs to
		 * be manually filtered before generating the arff file.
		 */
		System.out.println("The list of attributes is going to be generated.");
		AttributeGenerator att = new AttributeGenerator();
		System.out.println("The list of attributes was generated.\nPlease, filter the list manually in order to continue with the creation of the arff file.\nThen, press any key to continue.");


		/* Reader - Reads a character from the console. Makes sure the user read the information that was printed*/
		try {
			byte b = in.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		/*The arff generator class receives the map that was created on the step above. It is currently not using the map, but it could use
		 * the map in the future to perfect the machine learning process. The arff generator class creates the arff file based on the
		 * list of words/attributes that was created on the step above.
		 */
		System.out.println("Starting to build the .arff file.");
		ArffGenerator arff = new ArffGenerator(att.getWordMap());
		System.out.println("Finished.");
		
		
		/* Now that the arff file was generated, you can test it with Weka*/
	}

}
