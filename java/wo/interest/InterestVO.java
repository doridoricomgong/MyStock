package wo.interest;

public class InterestVO {
    private String name;
    private String code;
    private String vPrice;
    private String pPrice;
    private String vRate;

    public String getvRate() { return vRate; }

    public void setvRate(String vRate) { this.vRate = vRate; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVariPrice() {
        return vPrice;
    }

    public void setVariPrice(String vPrice) {
        this.vPrice = vPrice;
    }

    public String getPrePrice() {
        return pPrice;
    }

    public void setPrePrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public int size() {
        return 5;
    }
}