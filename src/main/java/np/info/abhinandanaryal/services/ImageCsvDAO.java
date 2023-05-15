package np.info.abhinandanaryal.services;
import np.info.abhinandanaryal.datamodel.Image;
import np.info.abhinandanaryal.datamodel.ImageData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageCsvDAO {
    File csvFile;

    public  ImageCsvDAO(File inputCsvFile){
        csvFile = inputCsvFile;
    }

    /**
     * Returns the list of the Image.
     * @return  List of all the lines transformed as Image
     */
    public List<Image> getAllImages() throws IOException {

        //reading all the lines from the file
        List<String> allLines = Files.readAllLines(csvFile.toPath());

        List<Image> listOfImageData = new ArrayList<>();

        //Removing the first line from the string list as it contains the header
        allLines.remove(0);
        allLines.forEach(line -> {

            //splitting the string to string array
            String[] _tempArray = line.split(",");

            //converting each string to a double and creating the array of double
            double[] _doubleArray = Arrays.stream(_tempArray)
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            Image _image = new Image();

            //setting the label of the Image as the first index of _doubleArray contains the label.
            _image.setLabel(_doubleArray[0]);

            //copying data to another array from Index Position 1
            double[] _anotherArray = new double[28*28];
            for(int i = 1; i < _doubleArray.length; i++){
                _anotherArray[i-1] = _doubleArray[i];
            }

            //converting the 1D double array to a 2D array
            double[][] _tempMatrix = new double[28][28];
            for(int row = 0; row < 28; row++){
                for(int col = 0; col < 28; col++){
                    _tempMatrix[row][col] = _anotherArray[row * 28 + col];
                }
            }

            //setting the data matrix of the Image class with the newly constructed 2D array
            _image.setDataMatrix(_tempMatrix);

            //adding the image instance to the list
            listOfImageData.add(_image);
        });

        return listOfImageData;
    }
}
