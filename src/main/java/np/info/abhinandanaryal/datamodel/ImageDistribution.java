package np.info.abhinandanaryal.datamodel;

public class ImageDistribution {
    public double getDigit() {
        return Digit;
    }

    public void setDigit(double digit) {
        Digit = digit;
    }

    public long getDistribution() {
        return Distribution;
    }

    public void setDistribution(int distribution) {
        Distribution = distribution;
    }

    private double Digit;
    private long Distribution;
}
