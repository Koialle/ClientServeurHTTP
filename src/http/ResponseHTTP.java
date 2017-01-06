
package http;

import http.Http.ContentType;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseHTTP
{
    private String code = "";
    private String contentType = "";
    private byte[] content = new byte[]{};
    private int contentLength = 0;

    ResponseHTTP(String headerString, byte[] content) {
        // Get response as an array
        String[] responseArray = headerString.split("\r\n");

        // Get first line such as "GET resourceRelativePath HTTP/1.1"
        String[] responseLine = responseArray[0].split(" ", 2);
        this.code = responseLine[1].trim();

        // Check headers
        for (int i = 1; i < responseArray.length; i++) {
            String[] headerArray = responseArray[i].split(":");
            String headerName = headerArray[0].trim();
            String headerValue = headerArray[1].trim();

            switch (headerName) {
                case Http.CONTENT_LENGTH:
                    this.contentLength = Integer.valueOf(headerValue);
                    break;
                case Http.CONTENT_TYPE:
                    this.contentType = headerValue;
                    break;
            }
        }
        
        // Gestion du content de la request
        this.content = content;
    }

    public ResponseHTTP()
    {
        this.code = Http.CODE_OK;
        this.contentType = ContentType.TEXT_HTML.getValue();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public String toString()
    {
        String s = "";
        
        s += Http.HTTP1_1 + " " + code + "\r\n";
        s += Http.CONTENT_TYPE + ": " + contentType + "\r\n";
        s += Http.CONTENT_LENGTH + ": " + contentLength + "\r\n";

        return s;
    }
}
