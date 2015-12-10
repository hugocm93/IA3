import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SVMTester {

	//ler arquivo arff

	//criar modelo SVM

	//para cada review

	//passar parametros para o svm

	//comparar valor obtido com o esperado

	//incrementar a variavel de acertos ou a variavel de erros


	//imprimir a quantidade de erros e acertos


	////////////////////Início

	public SVMTester() {
		/*Paths and names of files*/
		String userDir = System.getProperty("user.dir");
		String arff = new String("/MovieDataSet_REDUCED.arff");

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

				System.out.println(aux);
				attributes[i++] = aux;
			}
			
			/*Reads garbage*/
			br.readLine();
			
			
			/*Variable to iterate over the reviews*/
			int revs = 0;
			
			/*for each review*/
			while(revs<50000){
				
				/*Reads the line*/
				String aux2 = br.readLine();
				
				/*Split the numbers by , or space*/
				String[] reviews = aux2.split(",| ");

				/*Prints all the numbers of a review in a line.*/
				for(int j=0;j<51;j++){
					System.out.print(reviews[j] + " ");
					if(j==50){
						System.out.println();
					}
				}
				
				
				//Here you can pass some of the information to the SVM algorith for testing purposes. Let's say 66%
				//SVM_function(String[] reviews);
				
				/*Go to next review*/
				revs++;
			}


		}
		catch(IOException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
	}
}
