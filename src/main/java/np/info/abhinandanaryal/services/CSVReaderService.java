package np.info.abhinandanaryal.services;

import np.info.abhinandanaryal.datamodel.ImageData;
import np.info.abhinandanaryal.datamodel.ImageDistribution;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class CSVReaderService {
    File csvFile;

    public CSVReaderService(File inputFile){
        this.csvFile = inputFile;
    }

    public List<String> readFileData(int numberOfLines) throws IOException
    {
        List<String> allLines = Files.readAllLines(csvFile.toPath());
        if(numberOfLines != 0){
            List<String> limitedList = allLines.stream().limit(numberOfLines).collect(Collectors.toList());
            return limitedList;
        } else{
            return allLines;
        }
    }

    public List<ImageData> changeTheSructure(List<String> lines){
        List<ImageData> listOfImageData = new ArrayList<>();
        lines.forEach(line -> {
            String[] _tempArray = line.split(",");
            double[] _doubleArray = Arrays.stream(_tempArray)
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            ImageData _imageData = new ImageData();
            _imageData.setDigit(_doubleArray[0]);

            double[] _anotherArray = new double[28*28];

            for(int i = 1; i < _doubleArray.length; i++){
                _anotherArray[i-1] = _doubleArray[i];
            }

            double[][] _tempMatrix = new double[28][28];

            for(int row = 0; row < 28; row++){
                for(int col = 0; col < 28; col++){
                    _tempMatrix[row][col] = _anotherArray[row * 28 + col];
                }
            }

            _imageData.setMatrix(_tempMatrix);
            listOfImageData.add(_imageData);
        });
        return listOfImageData;
    }

    public  void showMatrix(List<ImageData> list, int numberOfLine){
        System.out.println(list.get(numberOfLine).getDigit());
        for(int row = 0; row < 28; row++){
            for(int col = 0; col < 28; col++){
                if(list.get(numberOfLine).getMatrix()[row][col] > 100){
                    System.out.print("xx" + " ");
                } else{
                    System.out.print(".." + " ");
                }
            }
            System.out.print("\n");
        }
    }

    public List<ImageDistribution> calculateDistribution(List<ImageData> list){
        List<ImageDistribution> _tempList = new ArrayList<>();
        Map<Double, Long> counted = list.stream()
                .collect(Collectors.groupingBy(ImageData::getDigit, Collectors.counting()));

        counted.forEach((item, item2) -> {
            ImageDistribution _imageDistribution = new ImageDistribution();
            _imageDistribution.setDigit(item);
            _imageDistribution.setDistribution(item2.intValue());
            _tempList.add(_imageDistribution);
        });

        return _tempList;
    }
}
