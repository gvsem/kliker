package dev.kliker.app.utils;

public class PdfUtils {

    static boolean subeq(byte[] data, int i, String x) {
        int n = x.length();
        if (!(i >= 0 && i <= data.length)) { return false; }
        if (!(i + n <= data.length)) { return false; }
        for (int j = 0; j < n && i + j < data.length; j++) {
            if (data[i + j] != x.charAt(j)){
                return false;
            }
        }
        return true;
    }

    static boolean subend(byte[] data, String x) {
        return subeq(data, data.length - x.length(), x);
    }


    public static boolean hasSupportedPdfHeader(byte[] data) {

        if (data == null || data.length < 8) { return false; }

        if (!subeq(data, 0, "%PDF-")) {
            return false;
        }

        String ending;
        if (subeq(data, 5, "1.3")) {
            ending = "%%EOF " + (char) 0x000A;
        } else if (subeq(data, 5, "1.4")) {
            ending = "%%EOF" + (char) 0x000A;
        } else {
            return false;
        }

        return subend(data, ending);

    }

}
