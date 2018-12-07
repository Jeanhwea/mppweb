package net.jeanhwea.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.jeanhwea.MPPParser;
import net.jeanhwea.helper.Statics;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private String uploadDir = null;
  private String psbpmTool = null;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public UploadServlet()
  {
    super();
    this.getProps();
  }

  private void getProps()
  {
    try {
      PropertiesConfiguration config = new PropertiesConfiguration("app.properties");
      uploadDir = config.getString("app.upload.dir").replace('/', File.separatorChar);
      psbpmTool = config.getString("app.psbpm").replace('/', File.separatorChar);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    //process only if its multipart content
    if(ServletFileUpload.isMultipartContent(request)){
      try {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> multiparts = upload.parseRequest(request);

        String full_filename = null;
        String name = null;
        for(FileItem item : multiparts){
          if(!item.isFormField()){
            name = new File(item.getName()).getName();
            File des_file = new File(uploadDir + File.separator + name);
            item.write(des_file);
            full_filename = des_file.getAbsolutePath();
          }
        }

        //File uploaded successfully
        request.setAttribute("message", "File Uploaded Successfully");

        MPPParser parser = new MPPParser();
        if (full_filename != null) {
          System.out.println(System.getProperty("user.dir"));
          parser.startWithCMD(full_filename, uploadDir);
          String cmd;
          cmd = psbpmTool +" \"" + full_filename + "\"";
          parser.executeSync(cmd);

          int dot_index = name.lastIndexOf('.');
          String xml_filename = name.substring(0, dot_index) + ".xml";
          Statics sta = new Statics(xml_filename);
          sta.run();
          String path_img = new String(name);
          int endIndex = path_img.lastIndexOf('.');
          path_img = path_img.substring(0, endIndex) + ".jpg";
          request.setAttribute("upload_filename", name);
          request.setAttribute("ntask", sta.getNtask());
          request.setAttribute("nreso", sta.getNreso());
          request.setAttribute("ndepd", sta.getNdepd());
          request.setAttribute("depd_img", "/upload/" + path_img);
          request.setAttribute("xml_filename", name.substring(0, dot_index)+".xml");

          sta.genAssignment(1);
        }

      } catch (Exception ex) {
        ex.printStackTrace();
        request.setAttribute("message", "File Upload Failed due to " + ex);
      }

    }else{
      request.setAttribute("message", "Sorry this Servlet only handles file upload request");
    }

    request.getRequestDispatcher("/result.jsp").forward(request, response);
  }
}
