package np.info.abhinandanaryal.datamodel;

public class ModelEvaluate {
    private Integer Label;

    private Long TPCount;

    private Long TNCount;

    private Double Sensitivity;
    private Double Specificity;
    private Double Accuracy;

    private Double Precision;
    private Double F1Score;

    public Double getPrecision() {
        return Precision;
    }

    public void setPrecision(Double precision) {
        Precision = precision;
    }

    public Double getF1Score() {
        return F1Score;
    }

    public void setF1Score(Double f1Score) {
        F1Score = f1Score;
    }

    public Double getSensitivity() {
        return Sensitivity;
    }

    public void setSensitivity(Double sensitivity) {
        Sensitivity = sensitivity;
    }

    public Double getSpecificity() {
        return Specificity;
    }

    public void setSpecificity(Double specificity) {
        Specificity = specificity;
    }

    public Double getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(Double accuracy) {
        Accuracy = accuracy;
    }

    public Long getTNCount() {
        return TNCount;
    }

    public void setTNCount(Long TNCount) {
        this.TNCount = TNCount;
    }

    public Long getFPCount() {
        return FPCount;
    }

    public void setFPCount(Long FPCount) {
        this.FPCount = FPCount;
    }

    public Long getFNCount() {
        return FNCount;
    }

    public void setFNCount(Long FNCount) {
        this.FNCount = FNCount;
    }

    private Long FPCount;
    private Long FNCount;

    public Integer getLabel() {
        return Label;
    }

    public void setLabel(Integer label) {
        Label = label;
    }

    public Long getTPCount() {
        return TPCount;
    }

    public void setTPCount(Long TPCount) {
        this.TPCount = TPCount;
    }
}
