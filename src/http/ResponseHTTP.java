
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

    ResponseHTTP(String headerString, String bodyString) {
        // Get response as an array
        String[] responseArray = headerString.split("\r\n");

        // Get first line such as "GET resourceRelativePath HTTP/1.1"
        String[] responseLine = responseArray[0].split(" ", 2);
        this.code = responseLine[1].trim();

        // Check headers
        //for (int i = 1; i <= responseArray.length; i++) 
        //int i = 1, contentLength = 0;
        //while (i < responseArray.length - 1 && !responseArray[i].isEmpty()) {
        for (int i = 1; i < responseArray.length; i++) {
            String[] headerArray = responseArray[i].split(":");
            String headerName = headerArray[0].trim();
            String headerValue = headerArray[1].trim();

            switch (headerName) {
//                case Http.CONNECTION:
//                    this.connection = headerValue;
//                    break;
                case Http.CONTENT_LENGTH:
                    this.contentLength = Integer.valueOf(headerValue);
                    break;
                case Http.CONTENT_TYPE:
                    this.contentType = headerValue;
                    break;
            }
            //i++;
        }
        
        // Gestion du content de la request
//        //byte[] data = new byte[contentLength+1];
//        String contentString = "";
//        i++;
//        while (i < responseArray.length && !responseArray[i].isEmpty()) {
//            System.out.println(responseArray[i]);
//            contentString += responseArray[i] + "\r\n";
//            i++;
//        }
        try {
            this.content = bodyString.getBytes("UTF-8");
            // Todo set encoding
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ResponseHTTP.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @Override
    public String toString()
    {
        String s = "";
        
        s += Http.HTTP1_1 + " " + this.code + "\r\n";
        s += Http.CONTENT_TYPE + ": " + this.contentType + "\r\n";
        s += Http.CONTENT_LENGTH + ": " + this.content.length + "\r\n";

        return s;
    }
}
