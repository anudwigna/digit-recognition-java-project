package np.info.abhinandanaryal.datamodel;

public class ImagePrediction {
    private double Label;

    public double getLabel() {
        return Label;
    }

    public void setLabel(double label) {
        Label = label;
    }

    public double getPredictedLabel() {
        return PredictedLabel;
    }

    public void setPredictedLabel(double predictedLabel) {
        PredictedLabel = predictedLabel;
    }

    private double PredictedLabel;
}
