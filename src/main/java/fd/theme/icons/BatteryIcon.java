package fd.theme.icons;

public enum BatteryIcon implements Icon {

    MISSING,
    EMPTY, //
    CAUTION, //
    LOW,
    MEDIUM,
    GOOD,
    FULL,
    EMPTY_CHARGING,
    CAUTION_CHARGING,
    LOW_CHARGING,
    MEDIUM_CHARGING,
    GOOD_CHARGING,
    FULL_CHARGED;

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        return "battery-" + super.toString().toLowerCase().replace('_', '-');
    }
}
