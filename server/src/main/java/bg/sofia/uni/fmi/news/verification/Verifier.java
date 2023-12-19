package bg.sofia.uni.fmi.news.verification;

import java.util.Collection;

public class Verifier {
    public static boolean isEmptyOrNull(String value) {
        return value == null || value.isBlank();
    }

    public static boolean containsEmptyOrNull(Collection<String> values) {
        for (String value: values) {
            if (Verifier.isEmptyOrNull(value)) {
                return true;
            }
        }
        return false;
    }

}
