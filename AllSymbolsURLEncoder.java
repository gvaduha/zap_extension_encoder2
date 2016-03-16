package org.zaproxy.zap.extension.encoder2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public class AllSymbolsURLEncoder {

    /**
     * Convert *all* symbols to %XX representation
     */
    public static String encode(String s, String enc)
        throws UnsupportedEncodingException {

        if (s.isEmpty())
            return s;

        String encOut = URLEncoder.encode(s, enc);
        StringBuilder out = new StringBuilder(encOut.length());

        Charset charset;
        try {
            charset = Charset.forName(enc);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(enc);
        }

        byte[] strBytes = s.getBytes(charset);

        for (int i=0; i < strBytes.length; ++i) {
            byte c = strBytes[i];

            if (c == '%') {
                out.append(String.format("%c%c%c", c, strBytes[i+1], strBytes[i+2]));
                i += 2;
            }
            else {
                out.append(String.format("%%%02x", c));
            }
        }

        return out.toString();
    }
}
