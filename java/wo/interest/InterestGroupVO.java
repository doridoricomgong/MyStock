package wo.interest;

public class InterestGroupVO {
    private String name;
    private InterestVO interestVO = new InterestVO();

    public InterestGroupVO(String name, InterestVO interestVO) {
        super();
        this.name = name;
        this.interestVO = interestVO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InterestVO getInterestVO() {
        return interestVO;
    }

    public void setInterestVO(InterestVO interestVO) {
        this.interestVO = interestVO;
    }
}