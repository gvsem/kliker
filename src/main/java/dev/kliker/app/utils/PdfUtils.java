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


    // public static boolean hasSupportedPdfHeader(byte[] data) {

    //     if (data == null || data.length < 8) { return false; }

    //     if (!subeq(data, 0, "%PDF-")) {
    //         return false;
    //     }

    //     String ending;
    //     if (subeq(data, 5, "1.3")) {
    //         ending = "%%EOF " + (char) 0x000A;
    //     } else if (subeq(data, 5, "1.4")) {
    //         ending = "%%EOF" + (char) 0x000A;
    //     } else {
    //         return false;
    //     }

    //     return subend(data, ending);

    // }

    public static boolean hasSupportedPdfHeader(byte[] data) {
        // header
        if (data == null || data.length < 4) {
            return false;
        }
        if (
            data[0] != 0x25 ||
            data[1] != 0x50 ||
            data[2] != 0x44 ||
            data[3] != 0x46 ||
            data[4] != 0x2D
        ) {
            return false;
        }

        if(data[5]==0x31 && data[6]==0x2E && data[7]==0x33) // version is 1.3 ?
        {                  
            // file terminator
            if (
                data[data.length-7] != 0x25 ||
                data[data.length-6] != 0x25 ||
                data[data.length-5] != 0x45 ||
                data[data.length-4] != 0x4F ||
                data[data.length-3] != 0x46 ||
                data[data.length-2] != 0x20 ||
                data[data.length-1] !=0x0A
            ) {
                return false;
            }

            return true;
        }

        if(data[5]==0x31 && data[6]==0x2E && data[7]==0x34) // version is 1.4 ?
        {
            // file terminator
            if (
                data[data.length-6] != 0x25 ||
                data[data.length-5] != 0x25 ||
                data[data.length-4] != 0x45 ||
                data[data.length-3] != 0x4F ||
                data[data.length-2] != 0x46 ||
                data[data.length-1] != 0x0A 
            ) {
                return false;
            }

            return true;
        }

        return false;
    }

}
