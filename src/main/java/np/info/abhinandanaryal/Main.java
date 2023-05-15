package np.info.abhinandanaryal;

import np.info.abhinandanaryal.datamodel.Image;
import np.info.abhinandanaryal.datamodel.ImageData;
import np.info.abhinandanaryal.datamodel.ImageDistribution;
import np.info.abhinandanaryal.datamodel.ImagePrediction;
import np.info.abhinandanaryal.services.CSVReaderService;
import np.info.abhinandanaryal.services.CentroidClassifier;
import np.info.abhinandanaryal.services.ImageCsvDAO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Main {

    public static void main(String[] args) throws IOException {
        // This function covers up all the functions that I made for testing the assignments.
        // ImageData, ImageDistribution classes were used for testing purposes.
        // Similarly, the class "CSVReaderService" incorporates the testing functions only.

        // testingPurpose();

        // ACTUAL IMPLEMENTATION
        String currentDir = System.getProperty("user.dir");

        File csvFileTest = new File(currentDir + "/src/dataset/mnist_test.csv");
        File csvFileTrain = new File(currentDir + "/src/dataset/mnist_train.csv");

        //Getting all test Images into a list
        ImageCsvDAO imageCsvDAOTest = new ImageCsvDAO(csvFileTest);
        List<Image> testImages = imageCsvDAOTest.getAllImages();

        //Getting all train Images into a list
        ImageCsvDAO imageCsvDAOTrain = new ImageCsvDAO(csvFileTrain);
        List<Image> trainImages = imageCsvDAOTrain.getAllImages();

        //Creating an instance of Centroid Classifier
        CentroidClassifier centroidClassifier = new CentroidClassifier();

        //Getting list of Images along with the centroid data
        List<Image> centroidData = centroidClassifier.trainCentroids(trainImages);

        //Getting List of Images along with their predictions
        List<ImagePrediction> imagePredictions = centroidClassifier.getImagePredictionList(testImages, centroidData);

        //Getting Confusion matrix in 2D Array Form
        Long[][] confusionMatrix = centroidClassifier.getConfusionMatrix(imagePredictions);

        //Printing Confusion Matrix
        centroidClassifier.showConfusionMatrix(imagePredictions);

        //Evaluating the model
        centroidClassifier.evaluateModel(confusionMatrix);
    }

    /**
     * This is the function that is used to call the test methods from the Test CSVReaderService.
     */
    private static void testingPurpose() throws IOException{
        String currentDir = System.getProperty("user.dir");
        File csvFileTest = new File(currentDir + "/src/dataset/mnist_test.csv");

        CSVReaderService csvReaderService = new CSVReaderService(csvFileTest);

        // For testing purpose, I am using the TEST DATASET

        //Reading two lines from test data
        List<String> stringList = csvReaderService.readFileData(2);

        // Printing two lines of data
        stringList.forEach(item -> {
            System.out.println(item);
        });

        //Reading all lines from data
        List<String> stringListAll = csvReaderService.readFileData(0);

        //Removing the first header line from the test data
        stringListAll.remove(0);
        List<ImageData> testImageData = csvReaderService.changeTheSructure(stringListAll);

        //Reading the 23 item from the testImageData and printing it in console.
        csvReaderService.showMatrix(testImageData, 23);

        //Calculating the distribution of labels in the data
        List<ImageDistribution> imageDistributionList = csvReaderService.calculateDistribution(testImageData);

        //Printing the distribution
        System.out.println("\nDistribution of DATA");
        imageDistributionList.forEach(item -> {
            System.out.println(item.getDigit() + " -> " + item.getDistribution());
        });

    }
}