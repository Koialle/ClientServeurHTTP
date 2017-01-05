
package http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class Client : Navigateur Web.
 *
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 */
public class Client implements Closeable
{
    private RequestHTTP request = null;
    private ResponseHTTP response = null;
    private Socket socket = null;
    //private BufferedReader in = null;
    //private BufferedInputStream in = null;
    private DataInputStream in = null;
    private BufferedWriter out = null;
    
    // ERROR CODES
    private static final int ERR_SOCKET = -1;
    private static final int ERR_PACKET = -2;
    private static final int ERR_TIMEOUT = -3;
    private static final int ERR_FILEUNKNOWN = -4;
    private static final int ERR_HOST = -5;
    private static final int ERR_STREAM = -6;
    
    private int code = 0;
    private Exception exception = null;
    private String errorMsg = "";
    private boolean changed = false;
    private String hostName = "";
    private String resource = "";
    private String method = Http.METHOD_GET;
    
    public Client(String hostName) throws Exception
    {
        try {
            this.hostName = hostName;
            this.setSocket();
        } catch (IOException ex) {
            if (ex instanceof SocketException) {
                code = ERR_SOCKET;
            } else if (ex instanceof UnknownHostException) {
                code = ERR_HOST;
            } else {
                code = ERR_PACKET;
            }
            errorMsg = "Return code : " + code + " [ " + ex.getClass() + " - " + ex.getMessage() + " ] ";
            System.err.println(errorMsg);
            
            throw new Exception(errorMsg);
        }
    }
    
    private void setSocket() throws UnknownHostException, IOException
    {
        if (hostName.contains(":")) {
            String[] host = hostName.split(":", 2);
            int port = Integer.valueOf(host[1].trim());

            socket = new Socket(host[0].trim(), port);
        } else {
            socket = new Socket(hostName, 80);
        }
        //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //in = new BufferedInputStream(socket.getInputStream());        
        in = new DataInputStream(socket.getInputStream());        
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        changed = false;
    }

    public void get() throws Exception
    {
        FileOutputStream outputStream;

        try {
            this.flush();
            if (this.resource.isEmpty()) {
                code = ERR_FILEUNKNOWN;
                errorMsg = "la ressource ne peut être vide";
                throw new Exception(errorMsg);
            }
//            if (this.changed) {
//                this.setSocket();
//            }
            this.setSocket();
            this.request = new RequestHTTP(hostName, method, resource);

            out.write(request.toString());
            out.write("\r\n");
            // Write content only if POST request, 
            // but our client only send GET request.
            out.flush();

            // Read response data
            String headerString = "", contentString = "";
            int dataRead , i;
            ArrayList<Byte> datas = new ArrayList();
            while ((dataRead = in.read()) != -1) {
                byte b = (byte) dataRead;
                if (b == -1) break;
                datas.add(b);
            }
            byte[] data = new byte[datas.size()];
            for (i = 0; i < datas.size(); i++) {
                data[i] = datas.get(i);
            }
            for (i = 0; i < datas.size(); i++) {
                if (data[i] == '\n' && data[i - 1] == '\r' && data[i - 2] == '\n' && data[i - 3] == '\r') {
                    //System.out.println("break "+i);
                    break;
                }
            }

            byte[] header = Arrays.copyOf(data, i - 1);
            byte[] content = Arrays.copyOfRange(data, i + 1, data.length);
            
            headerString = new String(header);
            contentString = new String(content);
            
            System.out.println(data.length);
            System.out.println(header.length);
            System.out.println(content.length);
            
            System.out.println(headerString);
            System.out.println(contentString);

            if (data.length > 0) {
                //System.out.println("response");
                // Create response from header
                response = new ResponseHTTP(headerString, content);
                //System.out.println("write file");
                // Write file in Output directory
                String fileName = resource.substring(resource.lastIndexOf("/") + 1);
                File dir = new File(System.getProperty("user.dir") + "\\Browser\\");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(System.getProperty("user.dir") + "\\Browser\\" + fileName);
                outputStream = new FileOutputStream(file);
                outputStream.write(response.getContent());
                outputStream.close();
            }
        } catch (IOException ex) {
            this.close();
            if (ex instanceof SocketException) {
                code = ERR_SOCKET;
                changed = true;
            } else if (ex instanceof UnknownHostException) {
                code = ERR_HOST;
                changed = true;
            }
            errorMsg = "Return code : " + code + " [ " + ex.getClass() + " - " + ex.getMessage() + " ] ";
            System.err.println(errorMsg);
            
            throw new Exception(errorMsg);
            
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public RequestHTTP getRequest() {
        return request;
    }

    public void setRequest(RequestHTTP request) {
        this.request = request;
    }

    public ResponseHTTP getResponse() {
        return response;
    }

    public void setResponse(ResponseHTTP response) {
        this.response = response;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.changed = !this.request.equals(request) || this.changed;
        this.hostName = hostName;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Initalize all recurrent variables.
     */
    private void flush()
    {
        this.request = null;
        this.response = null;
        this.code = 0;
        this.exception = null;
        this.errorMsg = "";
    }
    
    @Override
    public void close()
    {
        System.out.println("closing client");
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {}
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {}
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }
}
