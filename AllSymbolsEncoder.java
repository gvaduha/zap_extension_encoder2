package org.zaproxy.zap.extension.encoder2;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Convert *all* symbols to asked representation and do not follow appropriate skip rules
 */
public class AllSymbolsEncoder {

    /**
     * Convert *all* symbols to %XX representation
     */
    public static String encodeURL(String s, String enc)
        throws UnsupportedEncodingException {

        if (s == null || s.isEmpty())
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
                out.append(String.format("%%%02X", c));
            }
        }

        return out.toString();
    }

    public static String safeEncodeURL(String s, String enc)
    {
        try {
            return encodeURL(s, enc);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * Convert *all* symbols to #XXXX representation
     */
    public static String escapeHtml(String s) {

        if (s == null || s.isEmpty())
            return s;

        StringWriter writer = new StringWriter((int)((double)s.length() * 1.5D));

        int len = s.length();

        for(int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            writer.write("&#");
            writer.write(Integer.toString(c, 10));
            writer.write(59);
        }

        return writer.toString();
    }

}
