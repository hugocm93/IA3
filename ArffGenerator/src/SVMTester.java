import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SVMTester {
	/*Paths and names of files*/
	String userDir = System.getProperty("user.dir");
	String arff = new String("/MovieDataSet_REDUCED.arff");

	/*Saves all the reviews. Each review is a line. Each line has 51 words*/
	int allReviews[][] = new int[50000][];

	/*Counts the right and wrong answers*/
	int rightAnswer = 0;
	int wrongAnswer = 0;

	public SVMTester() {

		/*Reads the arff file*/
		try (BufferedReader br = new BufferedReader(new FileReader(userDir + arff))) {

			/*Counts each attribute of the file*/
			int i=0;

			/*There are 51 attributes, but let's add some extra space*/
			String[] attributes = new String[60];

			/*Reads garbage*/
			br.readLine();
			br.readLine();

			/*While to capture each word and save it to a string. I am not sure if this is going to be needed, but anyway...*/
			while(i<51){

				/*Last line has a different pattern*/
				if(i==50){
					String aux = br.readLine();
					aux = aux.replaceAll("@attribute ", "");
					aux = aux.replaceAll(" {0,1}", "");

					System.out.println(aux);
					attributes[i++] = aux;
				}

				/*Read each line and extract the word*/
				String aux = br.readLine();
				aux = aux.replaceAll("@attribute ", "");
				aux = aux.replaceAll(" numeric", "");

				//System.out.println(aux);
				attributes[i++] = aux;
			}

			/*Reads garbage*/
			br.readLine();


			/*Variable to iterate over the reviews*/
			int revs = 0;

			/*for each review*/
			while(revs<50000){

				System.out.println("Reading...");
				
				/*Reads the line*/
				String aux2 = br.readLine();

				/*Split the numbers by , or space*/
				String[] reviews = aux2.split(",| ");
				
				int temp[] = new int[reviews.length];
				for(int q=0 ; q<reviews.length; q++){
					temp[q] = Integer.parseInt(reviews[q]);
				}
				allReviews[revs] = temp;

				/*Go to next review*/
				revs++;
			}

		}
		catch(IOException e){
			System.out.println("File not found");
			e.printStackTrace();
		}


		/*Here you can pass some of the information to the SVM algorith for testing purposes. Let's say 66%. 
		 * This method generates the model. It makes the computer learn
		 */
		SVM_generateModel(allReviews);

		
		/*Now that the model was built, I can test it with all instances*/


		/*For each review, compare the expected value with the received value and compute the percentages*/
		for(int indexOfReview=0 ; indexOfReview<50000 ; indexOfReview++){
			
			System.out.println("Comparing...");
			
			int received = SVM_applyOnModel(allReviews[indexOfReview]);
			int expected = allReviews[indexOfReview][50];
			if(received==expected){
				rightAnswer++;
			}
			else{
				wrongAnswer++;
			}
		}
		
		//printInformation(allReviews);

		/*Prints the percentages*/
		System.out.println("Right answers: " + (rightAnswer/50000.)*100 + "%");
		System.out.println("Wrong answers: " + (wrongAnswer/50000.)*100 + "%");
	}


	/*Method to print Everything*/
	void printInformation(int allreviews[][]){
		for(int i=0;i<50000;i++){
			for(int k=0;k<51;k++){
				System.out.print(allreviews[i][k] + " ");
				if(k==50){
					System.out.println();
				}
			}
		}
	}
	
	void SVM_generateModel(int allreviews[][]){

	}

	int SVM_applyOnModel(int review[]){

		return 0;
	}

	
}
