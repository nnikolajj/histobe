package histoguessr.histobe.Enum;

public enum SeedTypeEnum {
    HISTO((short) 0),
    NARA((short) 1),
    ARCH((short) 2);

    private final short type;

    SeedTypeEnum(short type) {
        this.type = type;
    }

    // Getter, um auf die Zahl zuzugreifen
    public short getType() {
        return type;
    }
}