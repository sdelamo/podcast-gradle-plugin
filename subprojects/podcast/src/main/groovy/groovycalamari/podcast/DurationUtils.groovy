package groovycalamari.podcast

final class DurationUtils {

    static String durationFromSeconds(int seconds) {
        int p1 = seconds % 60
        int p2 = (seconds / 60).toInteger()
        int p3 = p2 % 60
        p2 = (p2 / 60).toInteger();
        return "${formatTime(p2)}:${formatTime(p3)}:${formatTime(p1)}"
    }

    private static String formatTime(Integer val) {
        return (""+val).padLeft(2, '0')
    }
}
