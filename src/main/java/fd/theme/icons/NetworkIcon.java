package fd.theme.icons;

public enum NetworkIcon implements Icon {

    WIRELESS_CONNECTED,
    WIRELESS_HOTSPOT,
    WIRELESS_NO_ROUTE,
    WIRELESS_OFFLINE,
    WIRELESS_SECURE_SIGNAL_EXCELLENT,
    WIRELESS_SECURE_SIGNAL_GOOD,
    WIRELESS_SECURE_SIGNAL_LOW,
    WIRELESS_SECURE_SIGNAL_NONE,
    WIRELESS_SECURE_SIGNAL_OK,
    WIRELESS_SIGNAL_EXCELLENT,
    WIRELESS_SIGNAL_GOOD,
    WIRELESS_SIGNAL_LOW,
    WIRELESS_SIGNAL_NONE,
    WIRELESS_SIGNAL_OK,
    WIRELESS_SIGNAL_WEAK;

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        return "network-" + super.toString().toLowerCase().replace('_', '-');
    }
}
