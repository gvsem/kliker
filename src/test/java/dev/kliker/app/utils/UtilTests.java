package dev.kliker.app.utils;


import dev.kliker.app.utils.PdfUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class UtilTests {

    @Test
    void contextLoads() {
    }

    @Test
    void pdfutils() {

        Assert.isTrue(PdfUtils.subend("aba".getBytes(), "aba"));
        Assert.isTrue(PdfUtils.subend("aba".getBytes(), "ba"));
        Assert.isTrue(PdfUtils.subend("aba".getBytes(), ""));

        final String mock = "%1\n";
        String[] correcttests = new String[] {
                "%PDF-1.3%%EOF " + (char) 0x000A,
                "%PDF-1.4%%EOF" +  (char) 0x000A,
                "%PDF-1.3" + mock + "%%EOF " + (char) 0x000A,
                "%PDF-1.4" + mock + "%%EOF" +  (char) 0x000A,
        };
        String[] wrongtests = new String[] {
                null,
                "",
                "%PD",
                "%PDF-1.9%%EOF " + (char) 0x000A,
                "%PDF-1.3%%E",
                "%PDF-",
                "%PDF-1",
        };
        for (var t : correcttests) {
            Assert.isTrue(PdfUtils.hasSupportedPdfHeader(t != null ? t.getBytes() : null), "expected correct pdf, but wrong: " + t);
        }
        for (var t : wrongtests) {
            Assert.isTrue(!PdfUtils.hasSupportedPdfHeader(t != null ? t.getBytes() : null), "expected wrong pdf, but correct: " + t);
        }
    }


}
