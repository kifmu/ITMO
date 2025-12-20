package actions;

public enum Sick {
    buzzingInEars("загудело в ушах"), 
    feelAWamble("засосало под ложечкой");

    private final String descriptionFeelings;

    Sick(String descriptionFeelings) {
        this.descriptionFeelings = descriptionFeelings;
    }
    
    @Override
    public String toString() {
        return descriptionFeelings;
    }
}
