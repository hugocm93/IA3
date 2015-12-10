import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVMTester {
	final double TRAINING_PERCENTAGE = 0.6;
	final int NUM_REVIEWS = 50000;
	final int NUM_ATTRIBUTES = 50;

	/* Paths and names of files */
	String userDir = System.getProperty("user.dir");
	String arff = new String("/MovieDataSet_REDUCED.arff");

	/* Saves all the reviews. Each review is a line. Each line has 51 words */

	/* Counts the right and wrong answers */
	int rightAnswer = 0;
	int wrongAnswer = 0;

	int allReviews[][] = new int[NUM_REVIEWS][];

	public SVMTester() {

		/* Reads the arff file */
		try (BufferedReader br = new BufferedReader(new FileReader(userDir + arff))) {

			/* Counts each attribute of the file */
			int i = 0;

			/* There are 51 attributes, but let's add some extra space */
			String[] attributes = new String[60];

			/* Reads garbage */
			br.readLine();
			br.readLine();

			/*
			 * While to capture each word and save it to a string. I am not sure
			 * if this is going to be needed, but anyway...
			 */
			while (i < 51) {

				/* Last line has a different pattern */
				if (i == 50) {
					String aux = br.readLine();
					aux = aux.replaceAll("@attribute ", "");
					aux = aux.replaceAll(" {0,1}", "");

					System.out.println(aux);
					attributes[i++] = aux;
				}

				/* Read each line and extract the word */
				String aux = br.readLine();
				aux = aux.replaceAll("@attribute ", "");
				aux = aux.replaceAll(" numeric", "");

				// System.out.println(aux);
				attributes[i++] = aux;
			}

			/* Reads garbage */
			br.readLine();

			/* Variable to iterate over the reviews */
			int revs = 0;

			System.out.println("Reading...");
			/* for each review */
			while (revs < NUM_REVIEWS) {

				/* Reads the line */
				String aux2 = br.readLine();

				/* Split the numbers by , or space */
				String[] reviews = aux2.split(",| ");

				int temp[] = new int[reviews.length];
				for (int q = 0; q < reviews.length; q++) {
					temp[q] = Integer.parseInt(reviews[q]);
				}
				allReviews[revs] = temp;

				/* Go to next review */
				revs++;
			}

		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}

		/*
		 * Here you can pass some of the information to the SVM algorith for
		 * testing purposes. Let's say 66%. This method generates the model. It
		 * makes the computer learn
		 */
		int trainingReviewCount = (int) Math.floor(allReviews.length * this.TRAINING_PERCENTAGE);
		int testingReviewCount = this.NUM_REVIEWS - trainingReviewCount;
		
		long start = System.currentTimeMillis();
		System.out.println("Training...");
		
		svm_model model = SVM_generateModel(allReviews, trainingReviewCount);

		long end = System.currentTimeMillis();
		System.out.println("Training took " + (end - start) + "ms");
		
		System.out.println("Testing...");
		start = System.currentTimeMillis();
		
		/* Now that the model was built, I can test it with all instances */

		/*
		 * For each review, compare the expected value with the received value
		 * and compute the percentages
		 */
		for (int i = trainingReviewCount; i < NUM_REVIEWS; i++) {

			svm_node[] x = new svm_node[NUM_ATTRIBUTES];
			
			for (int j = 0; j < NUM_ATTRIBUTES; j++) {
				x[j] = new svm_node();
				x[j].index = j;
				x[j].value = allReviews[i][j];
			}
			
			double received = svm.svm_predict(model, x);
			if (Double.compare(received, allReviews[i][50]) == 0) {
				rightAnswer++;
			} else {
				wrongAnswer++;
			}
		}
		System.out.println("Testing took " + (end - start) + "ms");

		// printInformation(allReviews);

		/* Prints the percentages */
		System.out.println("Right answers: " + (rightAnswer / (double) testingReviewCount) * 100 + "%");
		System.out.println("Wrong answers: " + (wrongAnswer / (double) testingReviewCount) * 100 + "%");
	}

	/* Method to print Everything */
	void printInformation(int allreviews[][]) {
		for (int i = 0; i < 50000; i++) {
			for (int k = 0; k < 51; k++) {
				System.out.print(allreviews[i][k] + " ");
				if (k == 50) {
					System.out.println();
				}
			}
		}
	}

	svm_model SVM_generateModel(int allreviews[][], int limit) {

		svm_parameter p = new svm_parameter();
		p.svm_type = svm_parameter.C_SVC;
		p.kernel_type = svm_parameter.LINEAR;
		p.gamma = 0.5;
		p.nu = 0.5;
		p.cache_size = 1100;
		p.C = 1;
		p.eps = 0.001;
		p.p = 0.1;
		
		svm_problem problem = new svm_problem();
		problem.l = limit;
		problem.y = new double[limit];
		problem.x = new svm_node[limit][];
		
		for (int i = 0; i < limit; i++) {
			problem.x[i] = new svm_node[NUM_ATTRIBUTES];
			
			// for each attribute
			for (int j = 0; j < NUM_ATTRIBUTES; j++) {
				svm_node node = new svm_node();
				node.index = j;
				node.value = allreviews[i][j];
				problem.x[i][j] = node;
			}

			problem.y[i] = allreviews[i][50]; //class
		}

		return svm.svm_train(problem, p);
	}

}
