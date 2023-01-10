package animals.language;

public enum BinaryChoice {
    NO("No"),
    YES("Yes"),
    UNDETERMINED("Undetermined");

    private final String binaryChoice;

    BinaryChoice(String binaryChoice) { this.binaryChoice = binaryChoice; }

    @Override
    public String toString() { return binaryChoice; }
}
