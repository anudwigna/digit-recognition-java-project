package np.info.abhinandanaryal.services;

import np.info.abhinandanaryal.datamodel.Image;
import np.info.abhinandanaryal.datamodel.ImagePrediction;
import np.info.abhinandanaryal.datamodel.ModelEvaluate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CentroidClassifier {

    /**
     * The function used to train the images
     * @param listOfImages -> List<Image> List of Images to train
     * @return  List of Labels with a centroid matrix
     */
    public List<Image> trainCentroids(List<Image> listOfImages){
        List<Image> listOfDigitWithCentroid = new ArrayList<>();
        Map<Double, List<Image>> imagesWithArray = listOfImages.stream()
                .collect(Collectors.groupingBy(Image::getLabel));

        imagesWithArray.forEach((digit, imageList) -> {
            double[][] averageArray = new double[28][28];
            imageList.forEach(item -> {
                for(int row =0; row < 28; row++){
                    for(int col = 0; col < 28; col++){
                        averageArray[row][col] += item.getDataMatrix()[row][col];
                    }
                }
            });

            for(int row =0; row < 28; row++){
                for(int col = 0; col < 28; col++){
                    averageArray[row][col] = averageArray[row][col] / imageList.size();
                }
            }

            Image _tempImageData = new Image();
            _tempImageData.setLabel(digit);
            _tempImageData.setDataMatrix(averageArray);

            listOfDigitWithCentroid.add(_tempImageData);
        });

        return listOfDigitWithCentroid;
    }

    /**
     * The function used to calculate the distance bettween the matrices.
     * @param inputA first matrix
     * @param inputB second matrix
     * @return Distance between the matrices in Double
     */
    private double calculateDistanceBetweenTwoMatrices(double[][] inputA, double[][] inputB){
        double sum = 0.0;
        for(int row =0; row < 28; row++) {
            for (int col = 0; col < 28; col++) {
                double diff = inputA[row][col] - inputB[row][col];
                double diffSquared = diff * diff;
                sum += diffSquared;
            }
        }
        return Math.sqrt(sum);
    }

    /**
     * The function used to predict the label of the input matrix
     * @param inputImage An image object to predict its label
     * @param centroidData A list of all the centroids along with the labels
     * @return Predicted Label value in Double
     */
    private double predict(Image inputImage, List<Image> centroidData){
        Map<Double, Double> result = new HashMap<>();
        centroidData.forEach(item -> {
            double _dist = calculateDistanceBetweenTwoMatrices(item.getDataMatrix(), inputImage.getDataMatrix());
            result.put(item.getLabel(), _dist);
        });

        Comparator<Map.Entry<Double, Double>> comp =
                Comparator.comparing((Map.Entry<Double, Double> entry)->entry.getValue());

        Map.Entry<Double, Double> mini = Collections.min(result.entrySet(),comp);

        return mini.getKey();
    }

    /**
     * The function used to return the number of occurrence (count) of a label in the given list
     * @param number A label(digit) to count in the list
     * @param inputList A list to count the occurrence of input number
     * @return returns the count of the label in Long
     */
    private long returnCount(Double number, List<ImagePrediction> inputList){
        return inputList.stream().filter(x -> x.getPredictedLabel() == number).count();
    }

    /**
     * The function used to get the list of images along with their prediction status
     * @param inputData A label(digit) to count in the list
     * @param centroidData The list of Image along with centroid Data
     */
    public List<ImagePrediction> getImagePredictionList(List<Image> inputData, List<Image> centroidData){
        List<ImagePrediction> predictionList = new ArrayList<>();
        inputData.forEach(item -> {
            ImagePrediction _tempImagePrediction = new ImagePrediction();
            double _tempPredicted = predict(item, centroidData);
            _tempImagePrediction.setLabel(item.getLabel());
            _tempImagePrediction.setPredictedLabel(_tempPredicted);
            predictionList.add(_tempImagePrediction);
        });
        predictionList.sort(Comparator.comparingDouble(ImagePrediction::getLabel));
        return predictionList;
    }

    /**
     * The function used to get the confusion matrix in 2D Array
     * @param predictionList A list of Images along with their predictions label
     */
    public Long[][] getConfusionMatrix(List<ImagePrediction> predictionList){
        Integer row = 10;
        Integer col = 10;
        Long[][] _tempConfusionMatrix = new Long[row][col];

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                Double _tempRow = (double) i;
                Double _tempCol = (double) j;
                List<ImagePrediction> _tempInput = predictionList.stream().filter(x -> x.getLabel() == _tempRow).collect(Collectors.toList());
                _tempConfusionMatrix[i][j] = returnCount(_tempCol, _tempInput);
            }
        }
        return _tempConfusionMatrix;
    }

    /**
     * The function used to show the confusion matrix
     * @param predictionList A list of Images along with their predictions label
     */
    public void showConfusionMatrix(List<ImagePrediction> predictionList){
        Map<Double, List<ImagePrediction>> groupByLabel = predictionList
                .stream()
                .collect(Collectors.groupingBy(ImagePrediction::getLabel, LinkedHashMap::new, Collectors.toList()));

        String leftAlignFormat = "%-8s %-8d %-8d %-8d %-8d %-8d %-8d %-8d %-8d %-8d %-8d %n";
        System.out.println("CONFUSION MATRIX");
        System.out.format(leftAlignFormat, "A/P", 0, 1,2,3,4,5,6,7,8,9);
        groupByLabel.forEach((key, items) -> {
            System.out.format(leftAlignFormat
                    ,String.format("%.0f", key)
                    ,returnCount(0.0, items)
                    ,returnCount(1.0, items)
                    ,returnCount(2.0, items)
                    ,returnCount(3.0, items)
                    ,returnCount(4.0, items)
                    ,returnCount(5.0, items)
                    ,returnCount(6.0, items)
                    ,returnCount(7.0, items)
                    ,returnCount(8.0, items)
                    ,returnCount(9.0, items)
            );
        });
    }

    /**
     * The function used to evaluate the model. Calculate different metrics of evaluation of a ML model
     * @param inputArray a multiclass confusion matrix
     */
    public void evaluateModel(Long[][] inputArray){
        Integer row = inputArray.length;
        Integer col = inputArray[0].length;

        List<ModelEvaluate> evaluationList = new ArrayList<>();
        Long SumAll = 0L;
        for(int i = 0; i < row; i++){
            ModelEvaluate _modelEvaluate = new ModelEvaluate();
            _modelEvaluate.setLabel(i);

            Long TPCount = 0L;
            Long FPCount = 0L;
            Long FNCount = 0L;
            for(int j = 0; j < col; j++){
                SumAll += inputArray[i][j];
                if(i == j){
                    TPCount = inputArray[i][j];
                }
                if(i != j){
                    FPCount += inputArray[j][i];
                    FNCount += inputArray[i][j];
                }
            }
            _modelEvaluate.setTPCount(TPCount);
            _modelEvaluate.setFPCount(FPCount);
            _modelEvaluate.setFNCount(FNCount);
            evaluationList.add(_modelEvaluate);
        }

        Long finalSumAll = SumAll;
        evaluationList.forEach(item -> {
            Long _otherValues = item.getTPCount() +item.getFPCount() + item.getFNCount();
            item.setTNCount(finalSumAll - _otherValues);

            Double _sensitivity = (item.getTPCount() / (double) (item.getTPCount() + item.getFNCount()));
            Double _specificity = (item.getTNCount() / (double) (item.getTNCount() + item.getFPCount()));
            Double _accuracy = (item.getTNCount() + item.getTPCount()) /
                    (double) (item.getTNCount() + item.getTPCount() + item.getFNCount() + item.getFPCount());

            Double _precision = (item.getTPCount() / (double) (item.getTPCount() + item.getFPCount()));

            Double f1Score = 2 * (item.getTPCount()) / (double) (2 * item.getTPCount() + item.getFPCount() + item.getFNCount());

            item.setSensitivity(_sensitivity);
            item.setSpecificity(_specificity);
            item.setAccuracy(_accuracy);
            item.setPrecision(_precision);
            item.setF1Score(f1Score);
        });



        String leftAlignFormat = "| %-6s | %-6d | %-6d | %-6d | %-6d | %,.9f | %,.9f | %,.7f | %,.7f |%n";

        System.out.print("\n\n");
        System.out.println("MODEL EVALUATION ON THE BASIS OF EACH LABEL");

        System.out.format("+--------+--------+--------+--------+--------+-------------+-------------+-----------+-----------+%n");
        System.out.format("| Label  | TP     | TN     | FP     | FN     | Sensitivity | Specificity | Accuracy  | F1-Score  |%n");
        System.out.format("+--------+--------+--------+--------+--------+-------------+-------------+-----------+-----------+%n");

        evaluationList.forEach(item -> {
            System.out.format(
                    leftAlignFormat,
                    item.getLabel(),
                    item.getTPCount(),
                    item.getTNCount(),
                    item.getFPCount(),
                    item.getFNCount(),
                    item.getSensitivity(),
                    item.getSpecificity(),
                    item.getAccuracy(),
                    item.getF1Score()
            );
        });
        System.out.format("+--------+--------+--------+--------+--------+-------------+-------------+-----------+-----------+%n");

    }
}
