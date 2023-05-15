package np.info.abhinandanaryal.datamodel;

public class Image {
    public double Label;

    public double getLabel() {
        return Label;
    }

    public void setLabel(double label) {
        Label = label;
    }

    public double[][] getDataMatrix() {
        return dataMatrix;
    }

    public void setDataMatrix(double[][] dataMatrix) {
        this.dataMatrix = dataMatrix;
    }

    public double[][] dataMatrix;
}
