
package http;

public class Http
{
    // Protocol & methods
    public static final String HTTP1_1 = "HTTP/1.1";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    
    // Headers
    public static final String HOST = "Host";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONNECTION_CLOSE = "Close";
    
    // Codes
    public static final String CODE_OK = "200 OK";
    public static final String CODE_NOT_FOUND = "404 Not Found";
    public static final String CODE_FORBIDDEN = "403 Forbidden";
    
    public enum ContentType
    {	
        TEXT_PLAIN(new String[]{"txt"}, "text/plain"),
        TEXT_HTML(new String[]{"html"}, "text/html"),
        TEXT_CSS(new String[]{"css"}, "text/css"),
        //TEXT_MARKDOWN(new String[]{"md"}, "text/markdown"),
        VIDEO_MP4(new String[]{"mp4, mpeg4"}, "video/mp4"),
        IMAGE_JPG(new String[]{"jpg", "jpeg"}, "image/jpg"),
        IMAGE_PNG(new String[]{"png"}, "image/png"),
        ZIP(new String[]{"zip"}, "application/zip");

        private final String[] extension;
        private final String value;

        private ContentType(String[] extension, String value)
        {
            this.extension = extension;
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }

        public static String getValueByExtension(String extension)
        {
            String contentType = "charset=UTF-8;";

            if (extension.isEmpty()) return TEXT_PLAIN.value + ";" + contentType;
//            if (extension == null || extension.equals("") || extension.equals("."))
//                    return "text/html";

            if (extension.getBytes()[0] == '.')
                extension = extension.substring(1);

            for (ContentType ct : ContentType.values())
                for (String ext : ct.extension)
                    if (ext.equalsIgnoreCase(extension))
                        return ct.value + ";" + contentType;

            return TEXT_HTML.value + ";" + contentType;
        }
    }
}
