package net.jeanhwea.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import net.jeanhwea.helper.Statics;

import org.json.simple.JSONArray;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class SolutionServlet
 */
@WebServlet("/SolutionServlet")
public class SolutionServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public SolutionServlet() {
    super();
    // TODO Auto-generated constructor stub
  }


  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String filename = (String) request.getParameter("filename");
    int action = (int) Integer.valueOf(request.getParameter("action"));
    Statics sta = null;
    if (filename != null) {
      sta = new Statics(filename);
      try {
        sta.run();
      } catch (TransformerConfigurationException | ParserConfigurationException | SAXException e) {
        e.printStackTrace();
      }
    }
    switch (action) {
    case 1:
      if (sta != null) {
        JSONArray json = sta.genAerageDuration();
        response.getWriter().write(json.toJSONString());
      } else {
        System.err.println("action=1, error");
      }
      break;
    case 2:
      if (sta != null) {
        int sid = 1;
        String solution_id = request.getParameter("solution_id");
        if (solution_id != null) {
          sid = (int) Integer.valueOf(solution_id);
        }
        JSONArray json = sta.genAssignment(sid);
        response.getWriter().write(json.toJSONString());
      } else {
        System.err.println("action=2, error");
      }
      break;
    default:
      System.err.println("AsynReqServlet Error");
    }
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }

}
