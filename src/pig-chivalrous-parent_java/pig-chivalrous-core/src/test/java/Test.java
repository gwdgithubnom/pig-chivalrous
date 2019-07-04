import org.gjgr.pig.chivalrous.core.json.JsonCommand;

import java.io.IOException;

/**
 * @Author gwd
 * @Time 12-14-2018 Friday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class Test {
    private static final int MAX_DIGITS = 10; // log10(Integer.MAX_VALUE) ~= 9.3

    /**
     * Appends all digits to the given buffer.
     *
     * @param buffer the buffer to append to.
     * @param value  the value to append digits from.
     */
    private static void appendFullDigits(final Appendable buffer, int value, int minFieldWidth) throws IOException {
        // specialized paths for 1 to 4 digits -> avoid the memory allocation from the temporary work array
        // see LANG-1248
        if (value < 10000) {
            // less memory allocation path works for four digits or less
            int nDigits = 4;
            if (value < 1000) {
                --nDigits;
                if (value < 100) {
                    --nDigits;
                    if (value < 10) {
                        --nDigits;
                    }
                }
            }
            // left zero pad
            for (int i = minFieldWidth - nDigits; i > 0; --i) {
                buffer.append('0');
            }
            switch (nDigits) {
                case 4:
                    buffer.append((char) (value / 1000 + '0'));
                    value %= 1000;
                case 3:
                    if (value >= 100) {
                        buffer.append((char) (value / 100 + '0'));
                        value %= 100;
                    } else {
                        buffer.append('0');
                    }
                case 2:
                    if (value >= 10) {
                        buffer.append((char) (value / 10 + '0'));
                        value %= 10;
                    } else {
                        buffer.append('0');
                    }
                case 1:
                    buffer.append((char) (value + '0'));
                default:
                    break;
            }
        } else {
            // more memory allocation path works for any digits

            // build up decimal representation in reverse
            final char[] work = new char[MAX_DIGITS];
            int digit = 0;
            while (value != 0) {
                work[digit++] = (char) (value % 10 + '0');
                value = value / 10;
            }

            // pad with zeros
            while (digit < minFieldWidth) {
                buffer.append('0');
                --minFieldWidth;
            }

            // reverse
            while (--digit >= 0) {
                buffer.append(work[digit]);
            }
        }
    }

    @org.junit.Test
    public void testArray() {
        String[] strings = new String[2];
        strings[0] = "test";
        strings[1] = "debug";
        String[] abc = JsonCommand.to(JsonCommand.toJson(strings), String[].class);
        strings[0] = "test";
        strings[1] = "debug";
    }
}
